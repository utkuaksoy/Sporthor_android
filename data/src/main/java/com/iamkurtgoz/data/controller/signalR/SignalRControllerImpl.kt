/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.data.controller.signalR

import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.signalrlib.HubConnection
import com.iamkurtgoz.core.signalrlib.HubEventListener
import com.iamkurtgoz.core.signalrlib.HubMessage
import com.iamkurtgoz.core.signalrlib.WebSocketHubConnectionP2
import com.iamkurtgoz.domain.controller.SignalRController
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.domain.model.enums.SocketConnectionStatus
import com.iamkurtgoz.domain.model.enums.toSignalRMessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import javax.inject.Inject

internal class SignalRControllerImpl @Inject constructor(
    private val json: Json,
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    private val appEventBus: AppEventBus,
) : SignalRController {
    // Tag
    override val logTag: String
        get() = "SignalRController"

    // Connection
    private var connection: HubConnection? = null

    // User id
    private var userId: String = ""

    // Status
    private var _connectionStatus: SocketConnectionStatus = SocketConnectionStatus.IDLE
    override val connectionStatus: SocketConnectionStatus
        get() {
            return _connectionStatus
        }

    override suspend fun connect(coroutineScope: CoroutineScope, userId: String) {
        this.userId = userId
        connection = WebSocketHubConnectionP2(
            json = json,
            hubUrl = appBuildConfigStatePack.socketUrl,
            logTag = logTag,
            coroutineScope = coroutineScope,
            authHeader = null,
            onConnected = {
                _connectionStatus = SocketConnectionStatus.CONNECTED
                connection?.subscribeToEvent(
                    eventName = "ReceiveMessage",
                    eventListener = object : HubEventListener {
                        override suspend fun onEventMessage(message: HubMessage) {
                            val messageUserId = message.arguments.getOrNull(AppDefaults.ZERO)?.jsonPrimitive?.content
                            val displayName = message.arguments.getOrNull(AppDefaults.ONE)?.jsonPrimitive?.content
                            val messageContent = message.arguments.getOrNull(AppDefaults.TWO)?.jsonPrimitive?.content
                            val type: SignalRMessageType? = message.arguments.getOrNull(AppDefaults.THREE)?.jsonPrimitive?.content?.toIntOrNull()?.toSignalRMessageType()
                            val attachment = message.arguments.getOrNull(AppDefaults.FOUR)?.jsonPrimitive?.content
                            appEventBus.receiveNewMessage(
                                messageUserId = messageUserId,
                                displayName = displayName,
                                messageContent = messageContent,
                                type = type ?: SignalRMessageType.TEXT,
                                attachment = attachment,
                            )
                        }
                    },
                )

                connection?.subscribeToEvent(
                    eventName = "UserTyping",
                    eventListener = object : HubEventListener {
                        override suspend fun onEventMessage(message: HubMessage) {
                            val channelId = message.arguments.getOrNull(AppDefaults.ZERO)?.jsonPrimitive?.content
                            appEventBus.receiveUserTyping(
                                channelId = channelId,
                            )
                        }
                    },
                )
            },
            onDisconnected = {
                _connectionStatus = SocketConnectionStatus.DISCONNECTED
            },
            onMessage = { message ->
                Timber.tag(logTag).d("onMessage: $message")
            },
            onError = { exception ->
                Timber.tag(logTag).e(exception)
            },
            tryConnectError = {
                _connectionStatus = SocketConnectionStatus.DISCONNECTED
                Timber.tag(logTag).e("TryConnectError: $_connectionStatus")
            },
        )

        Timber.tag(logTag).d("CALLED CONNECT")
        _connectionStatus = SocketConnectionStatus.CONNECTING
        connection?.connect()
    }

    override suspend fun disconnect() {
        Timber.tag(logTag).d("CALLED DISCONNECT")
        _connectionStatus = SocketConnectionStatus.DISCONNECTED
        connection?.disconnect()
    }

    override suspend fun joinGroup(group: String) {
        Timber.tag(logTag).d("CALLED JOIN GROUP TO $group")
        connection?.sendMessage("JoinGroup", group)
    }

    override suspend fun leaveGroup(group: String) {
        Timber.tag(logTag).d("CALLED LEAVE GROUP TO $group")
        connection?.sendMessage("LeaveGroup", group)
    }

    override suspend fun notifyTyping(userId: String) {
        Timber.tag(logTag).d("CALLED NOTIFY TYPING $userId")
        connection?.sendMessage("NotifyTyping", userId)
    }

    override suspend fun sendMessage(group: String, message: String, type: SignalRMessageType, extension: String) {
        connection?.sendMessage("SendMessageToGroup", group, userId, message, type.type, extension)
    }
}

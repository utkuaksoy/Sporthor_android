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
package com.iamkurtgoz.core.signalrlib

import android.net.Uri
import androidx.core.net.toUri
import com.iamkurtgoz.core.common.contract.AppDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.UnknownHostException
import java.util.Locale
import java.util.UUID
import javax.net.ssl.SSLSocketFactory

class WebSocketHubConnectionP2(
    hubUrl: String,
    private val logTag: String = "SignalRWebSocketHub",
    private val coroutineScope: CoroutineScope,
    private val json: Json,
    private val authHeader: String?,
    private val onConnected: suspend () -> Unit,
    private val onDisconnected: () -> Unit,
    private val onMessage: (message: HubMessage) -> Unit,
    private val onError: (exception: Exception) -> Unit,
    private val tryConnectError: (exception: Exception) -> Unit,
) : HubConnection {
    companion object {
        private const val SPECIAL_SYMBOL = "\u001E"
        private const val CONNECT_TIMEOUT = 15_000
        private const val READ_TIMEOUT = 15_000
        private const val REQUEST_METHOD = "POST"
        private const val HTTP_RESPONSE_CODE_SUCCESS = 200
        private const val HTTP_RESPONSE_CODE_UNAUTHORIZED = 401

        private object InputStreamConverter {
            private const val RETURN_SYMBOL = '\n'

            @Throws(IOException::class)
            fun convert(stream: InputStream?): String {
                val r = BufferedReader(InputStreamReader(stream))
                val total = StringBuilder()
                var line: String?
                while ((r.readLine().also { line = it }) != null) {
                    total.append(line)
                    total.append(RETURN_SYMBOL)
                }
                return total.toString()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val singleThread = newSingleThreadContext("web-socket-hub-connection-p2-race-condition-thread")
    private var client: WebSocketClient? = null
    private val eventListeners: MutableMap<String, MutableList<HubEventListener>> = HashMap()
    private val parsedUri: Uri = hubUrl.toUri()
    private var connectionId: String? = null

    override suspend fun connect() = withContext(singleThread) {
        if (client != null && client?.isOpen == true) return@withContext
        if (connectionId == null) {
            getConnectionId()
        } else {
            connectClient()
        }
    }

    @Suppress("TooGenericExceptionThrown", "ThrowsCount", "TooGenericExceptionCaught")
    @Throws(RuntimeException::class)
    private suspend fun getConnectionId() {
        Timber.tag(logTag).i("Requesting connection id...")
        if (!(parsedUri.scheme == "http" || parsedUri.scheme == "https")) {
            throw RuntimeException("URL must start with http or https")
        }

        withContext(Dispatchers.IO) {
            try {
                val negotiateUri = parsedUri.buildUpon().appendPath("negotiate").build().toString()
                val connection = URL(negotiateUri).openConnection() as HttpURLConnection
                if (!authHeader.isNullOrEmpty()) {
                    connection.addRequestProperty("Authorization", authHeader)
                }

                connection.connectTimeout = CONNECT_TIMEOUT
                connection.readTimeout = READ_TIMEOUT
                connection.requestMethod = REQUEST_METHOD
                val responseCode = connection.responseCode

                when (responseCode) {
                    HTTP_RESPONSE_CODE_SUCCESS -> {
                        val result = InputStreamConverter.convert(connection.inputStream)
                        val jsonElement = json.parseToJsonElement(result)
                        val connectionId = jsonElement.jsonObject["connectionId"]?.jsonPrimitive?.content
                            ?: error("connectionId not found")
                        val availableTransports = jsonElement.jsonObject["availableTransports"]?.jsonArray
                            ?.mapNotNull { it.jsonObject["transport"]?.jsonPrimitive?.content }
                            ?: emptyList()
                        if ("WebSockets" !in availableTransports) {
                            error("The server does not support WebSockets transport")
                        }
                        this@WebSocketHubConnectionP2.connectionId = connectionId
                        connectClient()
                    }
                    HTTP_RESPONSE_CODE_UNAUTHORIZED -> throw RuntimeException("Unauthorized request")
                    else -> throw RuntimeException("Server error")
                }
            } catch (e: UnknownHostException) {
                Timber.tag(logTag).e(e)
                error(e)
                tryConnectError.invoke(e)
            } catch (e: Exception) {
                Timber.tag(logTag).e(e)
                error(e)
                tryConnectError.invoke(e)
            }
        }
    }

    @Suppress("NestedBlockDepth", "TooGenericExceptionCaught")
    private fun connectClient() {
        val uriBuilder = parsedUri.buildUpon()
        uriBuilder.appendQueryParameter("id", connectionId)
        uriBuilder.scheme(parsedUri.scheme?.replace("http", "ws"))
        val uri = uriBuilder.build()
        val headers: MutableMap<String, String> = HashMap()
        if (!authHeader.isNullOrEmpty()) {
            headers["Authorization"] = authHeader
        }
        try {
            client = object : WebSocketClient(URI(uri.toString()), Draft_6455(), headers, CONNECT_TIMEOUT) {
                override fun onOpen(handshakeData: ServerHandshake) {
                    Timber.tag(logTag).i("Opened")
                    coroutineScope.launch {
                        onConnected.invoke()
                    }
                    send("{\"protocol\":\"json\",\"version\":1}$SPECIAL_SYMBOL")
                }

                override fun onMessage(message: String) {
                    Timber.tag(logTag).i("On message received: $message")
                    message.split(SPECIAL_SYMBOL)
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .forEach { part ->
                            try {
                                val element = json.decodeFromString<SignalRMessage>(part)

                                when (element.type) {
                                    AppDefaults.ONE -> {
                                        val hubMessage = HubMessage(
                                            element.invocationId ?: "",
                                            checkNotNull(element.target) { "target is null" },
                                            checkNotNull(element.arguments) { "arguments is null" },
                                        )
                                        onMessage(hubMessage)
                                        eventListeners[hubMessage.target]?.forEach {
                                            coroutineScope.launch {
                                                it.onEventMessage(hubMessage)
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Timber.tag(logTag).e(e, "Failed to parse part: $part")
                            }
                        }
                }

                override fun onClose(code: Int, reason: String, remote: Boolean) {
                    val format = String.format(Locale.getDefault(), "Closed. Code: %s, Reason: %s, Remote: %s", code, reason, remote)
                    Timber.tag(logTag).i(format)
                    onDisconnected.invoke()
                    connectionId = null
                }

                override fun onError(ex: Exception) {
                    Timber.tag(logTag).i("Error %s", ex.message)
                    error(ex)
                }
            }

            if (parsedUri.scheme == "https") {
                client?.setSocketFactory(SSLSocketFactory.getDefault())
            }
        } catch (e: Exception) {
            Timber.tag(logTag).e(e)
        }
        Timber.tag(logTag).i("Connecting...")
        client?.connect()
    }

    private fun error(ex: Exception) {
        onError.invoke(ex)
    }

    override fun disconnect() {
        client?.close()
    }

    override fun subscribeToEvent(eventName: String, eventListener: HubEventListener) {
        val eventMap: MutableList<HubEventListener>?
        if (eventListeners.containsKey(eventName)) {
            eventMap = eventListeners[eventName]
        } else {
            eventMap = ArrayList()
            eventListeners[eventName] = eventMap
        }
        eventMap?.add(eventListener)
    }

    override fun unSubscribeFromEvent(eventName: String, eventListener: HubEventListener) {
        if (eventListeners.containsKey(eventName)) {
            val eventMap = eventListeners[eventName]
            eventMap?.remove(eventListener)
            if (eventMap?.isNotEmpty() == true) {
                eventListeners.remove(eventName)
            }
        }
    }

    @Suppress("TooGenericExceptionThrown")
    @Throws(RuntimeException::class)
    override suspend fun sendMessage(event: String, vararg parameters: Any) {
        if (client == null || client?.isOpen == false) return
        val arguments: List<JsonElement> = parameters.map {
            when (it) {
                is String -> JsonPrimitive(it)
                is Number -> JsonPrimitive(it)
                is Boolean -> JsonPrimitive(it)
                else -> throw IllegalArgumentException("Unsupported argument type: ${it::class.simpleName}")
            }
        }

        val message = SignalRMessage(
            type = 1,
            invocationId = UUID.randomUUID().toString(),
            target = event,
            arguments = arguments,
            nonBlocking = false,
        )
        Timber.tag(logTag).i("Sending message: %s", message)
        client?.send(json.encodeToString(message) + SPECIAL_SYMBOL)
    }
}

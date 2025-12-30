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
package com.iamkurtgoz.domain.eventbus.impl

import com.iamkurtgoz.domain.core.CoreEventBus
import com.iamkurtgoz.domain.eventbus.impl.ChatEventBus.Event
import com.iamkurtgoz.domain.model.enums.SignalRMessageType

object ChatMessagingEventBus : CoreEventBus<ChatMessagingEventBus.Event>() {
    sealed class Event {
        data class ReceiveNewMessage(
            val messageUserId: String?,
            val displayName: String?,
            val messageContent: String?,
            val type: SignalRMessageType?,
            val attachment: String?,
        ) : Event()

        data class ReceiveUserTyping(
            val channelId: String?,
        ) : Event()
    }
}

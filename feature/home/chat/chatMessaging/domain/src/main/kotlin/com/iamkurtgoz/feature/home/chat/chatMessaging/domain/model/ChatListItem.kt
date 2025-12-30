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
package com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model

import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.domain.model.base.Listable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed class ChatListItem : Listable {
    data class DateHeader(val dateLabel: String) : ChatListItem(), Listable {
        override val uuid: String
            get() = dateLabel
    }

    data class MessageItem(
        val message: ChatMessageItemUIModel,
        val isParentMessageRow: Boolean,
    ) : ChatListItem(), Listable {
        override val uuid: String
            get() = "$isParentMessageRow${message.messageType}${message.from}${message.id}${message.fileExtension}"
    }
}

fun List<ChatMessageItemUIModel>.withDateHeaders(): List<ChatListItem> {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    val grouped: Map<LocalDate?, List<ChatMessageItemUIModel>> =
        this.groupBy { it.sendDate?.toLocalDate() }

    val sortedDates = grouped.keys
        .filterNotNull()
    grouped.keys.filter { it == null }

    val result = mutableListOf<ChatListItem>()
    for (date in sortedDates) {
        grouped.getOrDefault(date, emptyList()).let { itemList ->
            itemList.forEachIndexed { index, item ->
                val currentUser = item.from?.id
                val nextUser = itemList.getOrNull(index + AppDefaults.ONE)?.from?.id
                val isLastOfGroup = currentUser != nextUser
                result += ChatListItem.MessageItem(
                    message = item,
                    isParentMessageRow = isLastOfGroup,
                )
            }
        }

        date?.let { formatter.format(it) }?.let {
            result += ChatListItem.DateHeader(it)
        }
    }
    return result
}

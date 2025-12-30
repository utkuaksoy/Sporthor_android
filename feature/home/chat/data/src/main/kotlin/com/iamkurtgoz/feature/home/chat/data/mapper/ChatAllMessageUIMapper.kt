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
package com.iamkurtgoz.feature.home.chat.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.ChatAllMessageDomainModel
import com.iamkurtgoz.feature.home.chat.domain.model.ChatAllMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.domain.model.ChatAllMessageUIModel
import javax.inject.Inject

internal class ChatAllMessageUIMapper @Inject constructor() : IMapper<ChatAllMessageDomainModel, ChatAllMessageUIModel> {
    override fun map(response: ChatAllMessageDomainModel): ChatAllMessageUIModel {
        return with(response) {
            ChatAllMessageUIModel(
                messages = response.messages?.map { item ->
                    ChatAllMessageItemUIModel(
                        image = item.image,
                        isGroup = item.isGroup,
                        lastMessage = item.lastMessage,
                        messageDate = item.messageDate,
                        messageDateLong = item.messageDateLong,
                        messageId = item.messageId,
                        name = item.name,
                        toUserId = item.toUserId,
                        unReadMessageCount = item.unReadMessageCount,
                        userId = item.userId,
                        userName = item.userName,
                    )
                },
            )
        }
    }
}

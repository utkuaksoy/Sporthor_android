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
package com.iamkurtgoz.feature.home.chat.chatMessaging.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.enums.toSignalRMessageType
import com.iamkurtgoz.domain.model.response.ChatMessageDomainModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemFromUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageUserItemUIModel
import javax.inject.Inject

internal class ChatMessageUIMapper @Inject constructor() : IMapper<ChatMessageDomainModel, ChatMessageUIModel> {
    override fun map(response: ChatMessageDomainModel): ChatMessageUIModel {
        return with(response) {
            ChatMessageUIModel(
                messages = response.messages?.map { item ->
                    ChatMessageItemUIModel(
                        content = item?.content,
                        fileExtension = item?.fileExtension,
                        from = ChatMessageItemFromUIModel(
                            id = item?.from?.id,
                            image = item?.from?.image,
                            name = item?.from?.name,
                        ),
                        id = item?.id,
                        messageType = item?.messageType?.toSignalRMessageType(),
                        sendDate = item?.sendDate,
                    )
                },
                users = response.users?.map { user ->
                    ChatMessageUserItemUIModel(
                        id = user?.id,
                        imageUrl = user?.imageUrl,
                        isCurrentUser = user?.isCurrentUser,
                        isFollow = user?.isFollow,
                        name = user?.name,
                        summary = user?.summary,
                        username = user?.username,
                    )
                },
            )
        }
    }
}

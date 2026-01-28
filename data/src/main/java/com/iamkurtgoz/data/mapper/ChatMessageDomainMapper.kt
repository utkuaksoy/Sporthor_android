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
package com.iamkurtgoz.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.data.model.ChatMessageResponseModel
import com.iamkurtgoz.domain.model.response.ChatMessageDomainModel
import com.iamkurtgoz.domain.model.response.ChatMessageItemDomainModel
import com.iamkurtgoz.domain.model.response.ChatMessageItemFromDomainModel
import com.iamkurtgoz.domain.model.response.ChatMessageUserItemDomainModel
import javax.inject.Inject

internal class ChatMessageDomainMapper @Inject constructor() : IMapper<ChatMessageResponseModel, ChatMessageDomainModel> {
    override fun map(response: ChatMessageResponseModel): ChatMessageDomainModel {
        return with(response) {
            ChatMessageDomainModel(
                messages = response.messages?.map { item ->
                    ChatMessageItemDomainModel(
                        content = item?.content,
                        fileExtension = item?.fileExtension,
                        from = ChatMessageItemFromDomainModel(
                            id = item?.from?.id,
                            image = item?.from?.image,
                            name = item?.from?.name,
                        ),
                        id = item?.id,
                        messageType = item?.messageType,
                        sendDate = item?.sendDate,
                        shortDate = item?.shortDate,
                    )
                },
                users = response.users?.map { user ->
                    ChatMessageUserItemDomainModel(
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

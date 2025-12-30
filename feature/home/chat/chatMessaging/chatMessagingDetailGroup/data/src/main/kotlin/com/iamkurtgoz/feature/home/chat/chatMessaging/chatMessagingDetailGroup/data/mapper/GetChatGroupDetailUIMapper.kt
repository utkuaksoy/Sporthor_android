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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.GetChatGroupDetailDomainModel
import com.iamkurtgoz.domain.model.response.MemberDomainModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.model.GetChatGroupDetailUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.model.MemberUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetChatGroupDetailUIMapper @Inject constructor(
    private val memberUIMapper: MemberUIMapper,
) : IMapper<GetChatGroupDetailDomainModel, GetChatGroupDetailUIModel> {

    override fun map(response: GetChatGroupDetailDomainModel): GetChatGroupDetailUIModel {
        return with(response) {
            GetChatGroupDetailUIModel(
                groupCreatedDate = groupCreatedDate,
                groupId = groupId,
                groupImageUrl = groupImageUrl,
                groupName = groupName,
                mediaCount = mediaCount,
                members = members?.filterNotNull()?.map(memberUIMapper::map),
            )
        }
    }
}

@Singleton
internal class MemberUIMapper @Inject constructor() :
    IMapper<MemberDomainModel, MemberUIModel> {

    override fun map(response: MemberDomainModel): MemberUIModel {
        return with(response) {
            MemberUIModel(
                id = id,
                imageUrl = imageUrl,
                isCurrentUser = isCurrentUser,
                isFollow = isFollow,
                name = name,
                summary = summary,
                username = username,
            )
        }
    }
}

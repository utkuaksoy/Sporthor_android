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
import com.iamkurtgoz.data.model.GetChatGroupDetailResponseModel
import com.iamkurtgoz.data.model.MemberResponseModel
import com.iamkurtgoz.domain.model.response.GetChatGroupDetailDomainModel
import com.iamkurtgoz.domain.model.response.MemberDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetChatGroupDetailDomainMapper @Inject constructor(
    private val memberDomainMapper: MemberDomainMapper,
) : IMapper<GetChatGroupDetailResponseModel, GetChatGroupDetailDomainModel> {

    override fun map(response: GetChatGroupDetailResponseModel): GetChatGroupDetailDomainModel {
        return with(response) {
            GetChatGroupDetailDomainModel(
                groupCreatedDate = groupCreatedDate,
                groupId = groupId,
                groupImageUrl = groupImageUrl,
                groupName = groupName,
                mediaCount = mediaCount,
                members = members?.filterNotNull()?.map(memberDomainMapper::map),
            )
        }
    }
}

@Singleton
internal class MemberDomainMapper @Inject constructor() :
    IMapper<MemberResponseModel, MemberDomainModel> {

    override fun map(response: MemberResponseModel): MemberDomainModel {
        return with(response) {
            MemberDomainModel(
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

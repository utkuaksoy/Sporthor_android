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
import com.iamkurtgoz.data.model.GetChatGroupSummaryResponseModel
import com.iamkurtgoz.data.model.IconResponseModel
import com.iamkurtgoz.domain.model.response.GetChatGroupSummaryDomainModel
import com.iamkurtgoz.domain.model.response.IconDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetChatGroupSummaryDomainMapper @Inject constructor(
    private val iconDomainMapper: IconDomainMapper,
) : IMapper<GetChatGroupSummaryResponseModel, GetChatGroupSummaryDomainModel> {

    override fun map(response: GetChatGroupSummaryResponseModel): GetChatGroupSummaryDomainModel {
        return with(response) {
            GetChatGroupSummaryDomainModel(
                groupId = groupId,
                groupImageUrl = groupImageUrl,
                groupName = groupName,
                icons = icons?.filterNotNull()?.map(iconDomainMapper::map),
            )
        }
    }
}

@Singleton
internal class IconDomainMapper @Inject constructor() : IMapper<IconResponseModel, IconDomainModel> {

    override fun map(response: IconResponseModel): IconDomainModel {
        return with(response) {
            IconDomainModel(
                bgColor = bgColor,
                iconPath = iconPath,
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.GetChatGroupSummaryDomainModel
import com.iamkurtgoz.domain.model.response.IconDomainModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.model.GetChatGroupSummaryUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.model.IconUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetChatGroupSummaryUIMapper @Inject constructor(
    private val iconUIMapper: IconUIMapper,
) : IMapper<GetChatGroupSummaryDomainModel, GetChatGroupSummaryUIModel> {

    override fun map(response: GetChatGroupSummaryDomainModel): GetChatGroupSummaryUIModel {
        return with(response) {
            GetChatGroupSummaryUIModel(
                groupId = groupId,
                groupImageUrl = groupImageUrl,
                groupName = groupName,
                icons = icons?.filterNotNull()?.map(iconUIMapper::map),
            )
        }
    }
}

@Singleton
internal class IconUIMapper @Inject constructor() : IMapper<IconDomainModel, IconUIModel> {

    override fun map(response: IconDomainModel): IconUIModel {
        return with(response) {
            IconUIModel(
                bgColor = bgColor,
                iconPath = iconPath,
            )
        }
    }
}

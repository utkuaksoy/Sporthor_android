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
package com.iamkurtgoz.feature.home.inviteGroupMembers.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.SearchSocialDomainModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.toSocialSearchUIItemType
import javax.inject.Inject

internal class SocialSearchUIMapper @Inject constructor() : IMapper<SearchSocialDomainModel, SocialSearchUIModel> {
    override fun map(response: SearchSocialDomainModel): SocialSearchUIModel {
        return with(response) {
            SocialSearchUIModel(
                searchList = searchList?.map {
                    SocialSearchUIItemModel(
                        id = it?.id,
                        image = it?.image,
                        name = it?.name,
                        attribute = it?.attribute,
                        type = it?.type?.toSocialSearchUIItemType(),
                    )
                },
            )
        }
    }
}

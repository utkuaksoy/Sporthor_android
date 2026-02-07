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
package com.iamkurtgoz.feature.home.profile.userRelation.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.UserRelationDomainModel
import com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel
import com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIModel
import javax.inject.Inject

internal class UserRelationUIMapper @Inject constructor() : IMapper<UserRelationDomainModel, UserRelationUIModel> {
    override fun map(response: UserRelationDomainModel): UserRelationUIModel {
        return with(response) {
            UserRelationUIModel(
                users = users?.map {
                    UserRelationUIItemModel(
                        id = it?.id,
                        name = it?.name,
                        username = it?.username,
                        summary = it?.summary,
                        imageUrl = it?.imageUrl,
                        isFollow = it?.isFollow,
                        isFollowRequest = it?.isFollowRequest,
                        isCurrentUser = it?.isCurrentUser,
                    )
                },
            )
        }
    }
}

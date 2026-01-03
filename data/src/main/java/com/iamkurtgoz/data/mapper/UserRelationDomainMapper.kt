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
import com.iamkurtgoz.data.model.UserRelationResponseModel
import com.iamkurtgoz.domain.model.response.CoachRelationItemDomainModel
import com.iamkurtgoz.domain.model.response.UserRelationDomainModel
import com.iamkurtgoz.domain.model.response.UserRelationItemDomainModel
import javax.inject.Inject

internal class UserRelationDomainMapper @Inject constructor() : IMapper<UserRelationResponseModel, UserRelationDomainModel> {
    override fun map(response: UserRelationResponseModel): UserRelationDomainModel {
        return with(response) {
            UserRelationDomainModel(
                users = users?.map {
                    UserRelationItemDomainModel(
                        id = it?.id,
                        name = it?.name,
                        username = it?.username,
                        imageUrl = it?.imageUrl,
                        summary = it?.summary,
                        isFollow = it?.isFollow,
                        isCurrentUser = it?.isCurrentUser,
                    )
                },
                coaches = coaches?.map {
                    CoachRelationItemDomainModel(
                        id = it?.id,
                        name = it?.name,
                        username = it?.username,
                        imageUrl = it?.imageUrl,
                        summary = it?.summary,
                        isFollow = it?.isFollow,
                        isCurrentUser = it?.isCurrentUser,
                    )
                },
            )
        }
    }
}

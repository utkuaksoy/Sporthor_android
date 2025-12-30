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
package com.iamkurtgoz.feature.home.editEvent.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel
import com.iamkurtgoz.feature.home.editEvent.domain.model.GetTrainingGroupUserUIModel
import com.iamkurtgoz.feature.home.editEvent.domain.model.GetTrainingGroupUserUIModelGroup
import com.iamkurtgoz.feature.home.editEvent.domain.model.GetTrainingGroupUserUIModelTeam
import com.iamkurtgoz.feature.home.editEvent.domain.model.GetTrainingGroupUserUIModelUser
import javax.inject.Inject

class GetTrainingGroupUserUIMapper @Inject constructor() : IMapper<GetTrainingGroupUserDomainModel, GetTrainingGroupUserUIModel> {
    override fun map(response: GetTrainingGroupUserDomainModel): GetTrainingGroupUserUIModel {
        return with(response) {
            GetTrainingGroupUserUIModel(
                groups = response.groups?.map { item ->
                    GetTrainingGroupUserUIModelGroup(
                        groupId = item?.groupId,
                        groupImage = item?.groupImage,
                        groupName = item?.groupName,
                        season = item?.season,
                        team = GetTrainingGroupUserUIModelTeam(
                            detail = item?.team?.detail,
                            name = item?.team?.name,
                            value = item?.team?.value,
                        ),
                        users = item?.users?.map { user ->
                            GetTrainingGroupUserUIModelUser(
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
                },
            )
        }
    }
}

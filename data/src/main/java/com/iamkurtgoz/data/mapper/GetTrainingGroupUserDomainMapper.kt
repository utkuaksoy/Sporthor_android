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
import com.iamkurtgoz.data.model.GetTrainingGroupUserResponseModel
import com.iamkurtgoz.domain.model.response.GetTrainingGroupCoachDomainModelUser
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModelGroup
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModelTeam
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModelUser
import javax.inject.Inject

class GetTrainingGroupUserDomainMapper @Inject constructor() : IMapper<GetTrainingGroupUserResponseModel, GetTrainingGroupUserDomainModel> {
    override fun map(response: GetTrainingGroupUserResponseModel): GetTrainingGroupUserDomainModel {
        return with(response) {
            GetTrainingGroupUserDomainModel(
                groups = response.groups?.map { item ->
                    GetTrainingGroupUserDomainModelGroup(
                        groupId = item?.groupId,
                        groupImage = item?.groupImage,
                        groupName = item?.groupName,
                        season = item?.season,
                        team = GetTrainingGroupUserDomainModelTeam(
                            detail = item?.team?.detail,
                            name = item?.team?.name,
                            value = item?.team?.value,
                        ),
                        users = item?.users?.map { user ->
                            GetTrainingGroupUserDomainModelUser(
                                id = user?.id,
                                imageUrl = user?.imageUrl,
                                isCurrentUser = user?.isCurrentUser,
                                isFollow = user?.isFollow,
                                name = user?.name,
                                summary = user?.summary,
                                username = user?.username,
                            )
                        },
                        coaches = item?.coaches?.map { coach ->
                            GetTrainingGroupCoachDomainModelUser(
                                id = coach?.id,
                                imageUrl = coach?.imageUrl,
                                isCurrentUser = coach?.isCurrentUser,
                                isFollow = coach?.isFollow,
                                name = coach?.name,
                                summary = coach?.summary,
                                username = coach?.username,
                            )
                        },
                    )
                },
            )
        }
    }
}

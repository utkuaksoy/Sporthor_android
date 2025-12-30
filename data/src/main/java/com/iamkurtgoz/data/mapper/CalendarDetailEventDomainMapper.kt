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
import com.iamkurtgoz.data.model.CalendarDetailEventResponseModel
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModel
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModelLocation
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModelTask
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModelTaskType
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModelUser
import com.iamkurtgoz.domain.model.response.TeamsDomainItemModel
import javax.inject.Inject

class CalendarDetailEventDomainMapper @Inject constructor() : IMapper<CalendarDetailEventResponseModel, CalendarDetailEventDomainModel> {
    override fun map(response: CalendarDetailEventResponseModel): CalendarDetailEventDomainModel {
        return with(response) {
            CalendarDetailEventDomainModel(
                tasks = response.tasks?.map { task ->
                    CalendarDetailEventDomainModelTask(
                        allDay = task?.allDay,
                        description = task?.description,
                        endDate = task?.endDate,
                        hour = task?.hour,
                        id = task?.id,
                        isRecurring = task?.isRecurring,
                        location = CalendarDetailEventDomainModelLocation(
                            address = task?.location?.address,
                            lat = task?.location?.lat,
                            lng = task?.location?.lng,
                            title = task?.location?.title,
                        ),
                        recurrence = task?.recurrence,
                        startDate = task?.startDate,
                        taskType = CalendarDetailEventDomainModelTaskType(
                            detail = task?.taskType?.detail,
                            name = task?.taskType?.name,
                            value = task?.taskType?.value,
                        ),
                        title = task?.title,
                        trainingGroup = TeamsDomainItemModel(
                            name = task?.trainingGroup?.name,
                            value = task?.trainingGroup?.value,
                            image = task?.trainingGroup?.image,
                            detail = task?.trainingGroup?.detail,
                        ),
                        users = task?.users?.filterNotNull()?.map { user ->
                            CalendarDetailEventDomainModelUser(
                                id = user.id,
                                imageUrl = user.imageUrl,
                                isCurrentUser = user.isCurrentUser,
                                isFollow = user.isFollow,
                                name = user.name,
                                summary = user.summary,
                                username = user.username,
                            )
                        },
                        isOwn = task?.isOwn,
                        rpeRate = task?.rpeRate,
                    )
                },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.calendarDetail.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModel
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModel
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelLocation
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelTask
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelTaskType
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelUser
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.TeamsUIItemModel
import javax.inject.Inject

class CalendarDetailEventUIMapper @Inject constructor() : IMapper<CalendarDetailEventDomainModel, CalendarDetailEventUIModel> {
    override fun map(response: CalendarDetailEventDomainModel): CalendarDetailEventUIModel {
        return with(response) {
            CalendarDetailEventUIModel(
                tasks = response.tasks?.map { task ->
                    CalendarDetailEventUIModelTask(
                        allDay = task?.allDay,
                        description = task?.description,
                        endDate = task?.endDate,
                        hour = task?.hour,
                        id = task?.id,
                        isRecurring = task?.isRecurring,
                        location = CalendarDetailEventUIModelLocation(
                            address = task?.location?.address,
                            lat = task?.location?.lat,
                            lng = task?.location?.lng,
                            title = task?.location?.title,
                        ),
                        recurrence = task?.recurrence,
                        startDate = task?.startDate,
                        taskType = CalendarDetailEventUIModelTaskType(
                            detail = task?.taskType?.detail,
                            name = task?.taskType?.name,
                            value = task?.taskType?.value,
                        ),
                        title = task?.title,
                        trainingGroup = TeamsUIItemModel(
                            name = task?.trainingGroup?.name,
                            value = task?.trainingGroup?.value,
                            image = task?.trainingGroup?.image,
                            detail = task?.trainingGroup?.detail,
                        ),
                        users = task?.users?.filterNotNull()?.map { user ->
                            CalendarDetailEventUIModelUser(
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

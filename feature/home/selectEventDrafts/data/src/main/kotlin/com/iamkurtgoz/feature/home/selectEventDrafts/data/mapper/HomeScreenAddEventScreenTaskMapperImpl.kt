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
package com.iamkurtgoz.feature.home.selectEventDrafts.data.mapper

import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenLocation
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenModel
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenTask
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenTaskType
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenUser
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.mapper.HomeScreenAddEventScreenTaskMapper
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.model.CalendarDetailEventUIModelTask
import javax.inject.Inject

class HomeScreenAddEventScreenTaskMapperImpl @Inject constructor() : HomeScreenAddEventScreenTaskMapper {
    override fun map(response: CalendarDetailEventUIModelTask): HomeScreenAddEventScreenTask {
        return with(response) {
            HomeScreenAddEventScreenTask(
                allDay = allDay,
                description = description,
                endDate = endDate,
                hour = hour,
                id = id,
                isRecurring = isRecurring,
                location = HomeScreenAddEventScreenLocation(
                    address = location?.address,
                    lat = location?.lat,
                    lng = location?.lng,
                    title = location?.title,
                ),
                recurrence = recurrence,
                startDate = startDate,
                taskType = HomeScreenAddEventScreenTaskType(
                    detail = taskType?.detail,
                    name = taskType?.name,
                    value = taskType?.value,
                ),
                title = title,
                trainingGroup = HomeScreenAddEventScreenModel(
                    name = trainingGroup?.name,
                    value = trainingGroup?.value,
                    image = trainingGroup?.image,
                    detail = trainingGroup?.detail,
                ),
                users = users?.filterNotNull()?.map { user ->
                    HomeScreenAddEventScreenUser(
                        id = user.id,
                        imageUrl = user.imageUrl,
                        isCurrentUser = user.isCurrentUser,
                        isFollow = user.isFollow,
                        name = user.name,
                        summary = user.summary,
                        username = user.username,
                    )
                },
            )
        }
    }
}

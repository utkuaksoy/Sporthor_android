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
package com.iamkurtgoz.domain.model.response

import java.time.LocalDateTime

data class CalendarDetailEventDomainModel(
    val tasks: List<CalendarDetailEventDomainModelTask?>?,
)

data class CalendarDetailEventDomainModelTask(
    val allDay: Boolean?,
    val description: String?,
    val endDate: LocalDateTime?,
    val hour: String?,
    val id: String?,
    val isRecurring: Boolean?,
    val location: CalendarDetailEventDomainModelLocation?,
    val recurrence: Int?,
    val startDate: LocalDateTime?,
    val taskType: CalendarDetailEventDomainModelTaskType?,
    val title: String?,
    val trainingGroup: TeamsDomainItemModel?,
    val users: List<CalendarDetailEventDomainModelUser?>?,
    val isOwn: Boolean?,
    val rpeRate: Int?,
)

data class CalendarDetailEventDomainModelLocation(
    val address: String?,
    val lat: Double?,
    val lng: Double?,
    val title: String?,
)

data class CalendarDetailEventDomainModelTaskType(
    val detail: String?,
    val name: String?,
    val value: String?,
)

data class CalendarDetailEventDomainModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
)

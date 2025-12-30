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
package com.iamkurtgoz.data.model

import androidx.annotation.Keep
import com.iamkurtgoz.domain.serializer.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Keep
@Serializable
data class CalendarDetailEventResponseModel(
    @SerialName("tasks")
    val tasks: List<CalendarDetailEventResponseModelTask?>?,
)

@Keep
@Serializable
data class CalendarDetailEventResponseModelTask(
    @SerialName("allDay")
    val allDay: Boolean?,
    @SerialName("description")
    val description: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("endDate")
    val endDate: LocalDateTime?,
    @SerialName("hour")
    val hour: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("isRecurring")
    val isRecurring: Boolean?,
    @SerialName("location")
    val location: CalendarDetailEventResponseModelLocation?,
    @SerialName("recurrence")
    val recurrence: Int?,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("startDate")
    val startDate: LocalDateTime?,
    @SerialName("taskType")
    val taskType: CalendarDetailEventResponseModelTaskType?,
    @SerialName("title")
    val title: String?,
    @SerialName("trainingGroup")
    val trainingGroup: TeamsResponseItemModel?,
    @SerialName("users")
    val users: List<CalendarDetailEventResponseModelUser?>?,
    @SerialName("isOwn")
    val isOwn: Boolean?,
    @SerialName("rpePoint")
    val rpeRate: Int?,
)

@Keep
@Serializable
data class CalendarDetailEventResponseModelLocation(
    @SerialName("address")
    val address: String?,
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lng")
    val lng: Double?,
    @SerialName("title")
    val title: String?,
)

@Keep
@Serializable
data class CalendarDetailEventResponseModelTaskType(
    @SerialName("detail")
    val detail: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("value")
    val value: String?,
)

@Keep
@Serializable
data class CalendarDetailEventResponseModelUser(
    @SerialName("id")
    val id: String?,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("isCurrentUser")
    val isCurrentUser: Boolean?,
    @SerialName("isFollow")
    val isFollow: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("username")
    val username: String?,
)

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
package com.iamkurtgoz.domain.model.request

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AddTaskRequest(
    @SerialName("allDay")
    val allDay: Boolean?,
    @SerialName("description")
    val description: String?,
    @SerialName("endDate")
    val endDate: String?,
    @SerialName("isDraft")
    val isDraft: Boolean?,
    @SerialName("isRecurring")
    val isRecurring: Boolean?,
    @SerialName("location")
    val location: AddTaskRequestLocation?,
    @SerialName("recurrence")
    val recurrence: Int?,
    @SerialName("recurrenceEndDate")
    val recurrenceEndDate: String?,
    @SerialName("startDate")
    val startDate: String?,
    @SerialName("taskType")
    val taskType: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("trainingGroupIds")
    val trainingGroupIds: List<String?>?,
    @SerialName("userIds")
    val userIds: List<String?>?,
)

@Keep
@Serializable
data class AddTaskRequestLocation(
    @SerialName("title")
    val title: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lng")
    val lng: Double?,
)

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
package com.iamkurtgoz.domain.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.AddTaskRequest
import com.iamkurtgoz.domain.model.request.AddTaskTypeRequest
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.domain.model.request.UpdateTaskRequest
import com.iamkurtgoz.domain.model.response.AddTaskTypeDomainModel
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModel
import com.iamkurtgoz.domain.model.response.CalendarEventDomainModel
import com.iamkurtgoz.domain.model.response.GetTaskTypeDomainModel

interface CalendarRepository {
    suspend fun getCalendar(date: String?): RestResult<List<CalendarEventDomainModel>>
    suspend fun getCalendarDetail(date: String?): RestResult<CalendarDetailEventDomainModel>
    suspend fun getTaskTypes(): RestResult<GetTaskTypeDomainModel>
    suspend fun addTaskType(body: AddTaskTypeRequest?): RestResult<AddTaskTypeDomainModel>
    suspend fun addTask(body: AddTaskRequest): RestResult<Unit>
    suspend fun updateTask(body: UpdateTaskRequest): RestResult<Unit>
    suspend fun getDrafts(): RestResult<CalendarDetailEventDomainModel>
    suspend fun rpeSurvey(body: RpeSurveyRequest?): RestResult<Unit>
}

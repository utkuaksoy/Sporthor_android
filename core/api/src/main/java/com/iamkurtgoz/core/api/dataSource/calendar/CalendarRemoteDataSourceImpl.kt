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
package com.iamkurtgoz.core.api.dataSource.calendar

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.calendar.CalendarService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.CalendarRemoteDataSource
import com.iamkurtgoz.data.model.AddTaskTypeResponseModel
import com.iamkurtgoz.data.model.CalendarDetailEventResponseModel
import com.iamkurtgoz.data.model.CalendarEventResponseModel
import com.iamkurtgoz.data.model.GetTaskTypeResponseModel
import com.iamkurtgoz.domain.model.request.AddTaskRequest
import com.iamkurtgoz.domain.model.request.AddTaskTypeRequest
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.domain.model.request.UpdateTaskRequest
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class CalendarRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val calendarService: CalendarService,
) : CalendarRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getCalendar(date: String?): BaseResponse<CalendarEventResponseModel> = requestRetrofit {
        calendarService.getCalendar(date)
    }

    override suspend fun getCalendarDetail(date: String?): BaseResponse<CalendarDetailEventResponseModel> = requestRetrofit {
        calendarService.getCalendarDetail(date)
    }

    override suspend fun getTaskTypes(): BaseResponse<GetTaskTypeResponseModel> = requestRetrofit {
        calendarService.getTaskTypes()
    }

    override suspend fun addTaskType(body: AddTaskTypeRequest?): BaseResponse<AddTaskTypeResponseModel> = requestRetrofit {
        calendarService.addTaskType(body)
    }

    override suspend fun addTask(body: AddTaskRequest): BaseResponse<Unit> = requestRetrofit {
        calendarService.addTask(body)
    }

    override suspend fun updateTask(body: UpdateTaskRequest): BaseResponse<Unit> = requestRetrofit {
        calendarService.updateTask(body)
    }

    override suspend fun getDrafts(): BaseResponse<CalendarDetailEventResponseModel> = requestRetrofit {
        calendarService.getDrafts()
    }

    override suspend fun rpeSurvey(body: RpeSurveyRequest?): BaseResponse<Unit> = requestRetrofit {
        calendarService.rpeSurvey(body)
    }
}

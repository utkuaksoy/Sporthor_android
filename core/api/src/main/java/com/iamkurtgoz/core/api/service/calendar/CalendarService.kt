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
package com.iamkurtgoz.core.api.service.calendar

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.AddTaskTypeResponseModel
import com.iamkurtgoz.data.model.CalendarDetailEventResponseModel
import com.iamkurtgoz.data.model.CalendarEventResponseModel
import com.iamkurtgoz.data.model.GetTaskTypeResponseModel
import com.iamkurtgoz.domain.model.request.AddTaskRequest
import com.iamkurtgoz.domain.model.request.AddTaskTypeRequest
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.domain.model.request.UpdateTaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface CalendarService {

    @GET("Calendar/GetCalendar")
    suspend fun getCalendar(@Query("date") date: String?): Response<BaseResponse<CalendarEventResponseModel>>

    @GET("Calendar/GetCalendarDetail")
    suspend fun getCalendarDetail(@Query("date") date: String?): Response<BaseResponse<CalendarDetailEventResponseModel>>

    @GET("Calendar/GetTaskTypes")
    suspend fun getTaskTypes(): Response<BaseResponse<GetTaskTypeResponseModel>>

    @POST("Calendar/AddTaskType")
    suspend fun addTaskType(@Body body: AddTaskTypeRequest?): Response<BaseResponse<AddTaskTypeResponseModel>>

    @POST("Calendar/AddTask")
    suspend fun addTask(@Body body: AddTaskRequest): Response<BaseResponse<Unit>>

    @POST("Calendar/UpdateTask")
    suspend fun updateTask(@Body body: UpdateTaskRequest): Response<BaseResponse<Unit>>

    @GET("Calendar/GetDrafts")
    suspend fun getDrafts(): Response<BaseResponse<CalendarDetailEventResponseModel>>

    @POST("Calendar/RPESurvey")
    suspend fun rpeSurvey(@Body body: RpeSurveyRequest?): Response<BaseResponse<Unit>>
}

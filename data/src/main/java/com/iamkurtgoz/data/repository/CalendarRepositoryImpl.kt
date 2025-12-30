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
package com.iamkurtgoz.data.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.data.core.CoreRepository
import com.iamkurtgoz.data.dataSource.CalendarRemoteDataSource
import com.iamkurtgoz.data.mapper.AddTaskTypeDomainMapper
import com.iamkurtgoz.data.mapper.CalendarDetailEventDomainMapper
import com.iamkurtgoz.data.mapper.CalendarEventDomainMapper
import com.iamkurtgoz.data.mapper.GetTaskTypeDomainMapper
import com.iamkurtgoz.domain.model.request.AddTaskRequest
import com.iamkurtgoz.domain.model.request.AddTaskTypeRequest
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.domain.model.request.UpdateTaskRequest
import com.iamkurtgoz.domain.model.response.AddTaskTypeDomainModel
import com.iamkurtgoz.domain.model.response.CalendarDetailEventDomainModel
import com.iamkurtgoz.domain.model.response.CalendarEventDomainModel
import com.iamkurtgoz.domain.model.response.GetTaskTypeDomainModel
import com.iamkurtgoz.domain.repository.CalendarRepository
import javax.inject.Inject

internal class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
    private val calendarEventDomainMapper: CalendarEventDomainMapper,
    private val calendarDetailEventDomainMapper: CalendarDetailEventDomainMapper,
    private val getTaskTypeDomainMapper: GetTaskTypeDomainMapper,
    private val addTaskTypeDomainMapper: AddTaskTypeDomainMapper,
) : CalendarRepository, CoreRepository() {
    override suspend fun getCalendar(date: String?): RestResult<List<CalendarEventDomainModel>> = mapToRestResult {
        calendarRemoteDataSource.getCalendar(date)
    }.mapOnSuccess {
        it.events?.filterNotNull()?.map {
            calendarEventDomainMapper.map(it)
        } ?: listOf()
    }

    override suspend fun getCalendarDetail(date: String?): RestResult<CalendarDetailEventDomainModel> = mapToRestResult {
        calendarRemoteDataSource.getCalendarDetail(date)
    }.mapOnSuccess(calendarDetailEventDomainMapper::map)

    override suspend fun getTaskTypes(): RestResult<GetTaskTypeDomainModel> = mapToRestResult {
        calendarRemoteDataSource.getTaskTypes()
    }.mapOnSuccess(getTaskTypeDomainMapper::map)

    override suspend fun addTaskType(body: AddTaskTypeRequest?): RestResult<AddTaskTypeDomainModel> = mapToRestResult {
        calendarRemoteDataSource.addTaskType(body)
    }.mapOnSuccess(addTaskTypeDomainMapper::map)

    override suspend fun addTask(body: AddTaskRequest): RestResult<Unit> = mapToRestResult {
        calendarRemoteDataSource.addTask(body)
    }

    override suspend fun updateTask(body: UpdateTaskRequest): RestResult<Unit> = mapToRestResult {
        calendarRemoteDataSource.updateTask(body)
    }

    override suspend fun getDrafts(): RestResult<CalendarDetailEventDomainModel> = mapToRestResult {
        calendarRemoteDataSource.getDrafts()
    }.mapOnSuccess(calendarDetailEventDomainMapper::map)

    override suspend fun rpeSurvey(body: RpeSurveyRequest?): RestResult<Unit> = mapToRestResult {
        calendarRemoteDataSource.rpeSurvey(body)
    }
}

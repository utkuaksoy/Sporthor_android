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
package com.iamkurtgoz.feature.home.calendar.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.CalendarRepository
import com.iamkurtgoz.feature.home.calendar.data.mapper.CalendarEventUIMapper
import com.iamkurtgoz.feature.home.calendar.domain.model.CalendarEventUIModel
import com.iamkurtgoz.feature.home.calendar.domain.useCase.GetCalendarUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCalendarUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: CalendarRepository,
    private val mapper: CalendarEventUIMapper,
) : GetCalendarUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: String?): Flow<RestResult<List<CalendarEventUIModel>>> = prepare {
        repository.getCalendar(params)
            .mapOnSuccess {
                it.map { item -> mapper.map(item) }
            }
    }
}

package com.iamkurtgoz.feature.home.calendarDetail.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.domain.repository.CalendarRepository
import com.iamkurtgoz.feature.home.calendarDetail.domain.useCase.RpeSurveyUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RpeSurveyUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: CalendarRepository,
) : RpeSurveyUseCase, CoreUseCase(coroutineDispatcher) {
    override fun invoke(params: RpeSurveyRequest?): Flow<RestResult<Unit>> = prepare {
        repository.rpeSurvey(params)
    }
}

package com.iamkurtgoz.feature.home.calendarDetail.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest

interface RpeSurveyUseCase : IUseCase<RpeSurveyRequest?, RestResult<Unit>>

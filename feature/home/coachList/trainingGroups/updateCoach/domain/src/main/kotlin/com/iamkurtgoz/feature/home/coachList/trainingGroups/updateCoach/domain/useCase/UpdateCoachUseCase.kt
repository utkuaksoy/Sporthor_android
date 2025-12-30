package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest

interface UpdateCoachUseCase : IUseCase<UpdateCoachRequest, RestResult<Unit>>

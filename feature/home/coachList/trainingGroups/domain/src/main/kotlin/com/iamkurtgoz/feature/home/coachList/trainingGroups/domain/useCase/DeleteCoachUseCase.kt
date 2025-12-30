package com.iamkurtgoz.feature.home.coachList.trainingGroups.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest

interface DeleteCoachUseCase : IUseCase<DeleteCoachRequest, RestResult<Unit>>

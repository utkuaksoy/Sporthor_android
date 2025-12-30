package com.iamkurtgoz.feature.home.selectTrainingGroup.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest

interface RemoveTrainingGroupUseCase : IUseCase<RemoveTrainingGroupRequest, RestResult<Unit>>

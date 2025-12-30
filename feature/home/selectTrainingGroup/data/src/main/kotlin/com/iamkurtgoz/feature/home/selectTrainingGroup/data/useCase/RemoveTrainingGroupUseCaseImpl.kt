package com.iamkurtgoz.feature.home.selectTrainingGroup.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.domain.repository.CoachRepository
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.useCase.RemoveTrainingGroupUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RemoveTrainingGroupUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoachRepository,
) : RemoveTrainingGroupUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: RemoveTrainingGroupRequest): Flow<RestResult<Unit>> = prepare {
        repository.removeTrainingGroup(params)
    }
}

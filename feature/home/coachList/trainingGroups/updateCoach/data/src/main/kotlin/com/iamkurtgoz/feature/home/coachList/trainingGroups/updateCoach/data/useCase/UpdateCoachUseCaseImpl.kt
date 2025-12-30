package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.repository.ManagerRepository
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase.UpdateCoachUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UpdateCoachUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ManagerRepository,
) : UpdateCoachUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: UpdateCoachRequest): Flow<RestResult<Unit>> = prepare {
        repository.updateCoach(params)
    }
}

package com.iamkurtgoz.feature.home.coachList.trainingGroups.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.repository.ManagerRepository
import com.iamkurtgoz.feature.home.coachList.trainingGroups.domain.useCase.DeleteCoachUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DeleteCoachUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ManagerRepository,
) : DeleteCoachUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: DeleteCoachRequest): Flow<RestResult<Unit>> = prepare {
        repository.deleteCoach(params)
    }
}

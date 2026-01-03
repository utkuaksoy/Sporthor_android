package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.repository.CoachRepository
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.AddTrainingGroupUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AddTrainingGroupUserUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoachRepository,
) : AddTrainingGroupUserUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: AddTrainingGroupUserRequest?): Flow<RestResult<Unit>> = prepare {
        repository.addTrainingGroupUser(params)
    }
}

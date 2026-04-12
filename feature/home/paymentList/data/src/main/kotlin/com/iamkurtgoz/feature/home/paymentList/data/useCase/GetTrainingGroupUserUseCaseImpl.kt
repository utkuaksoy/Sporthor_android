package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel
import com.iamkurtgoz.domain.repository.CoachRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetTrainingGroupUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTrainingGroupUserUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val coachRepository: CoachRepository,
) : GetTrainingGroupUserUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<GetTrainingGroupUserDomainModel>> = prepare {
        coachRepository.getTrainingGroupUser()
    }
}

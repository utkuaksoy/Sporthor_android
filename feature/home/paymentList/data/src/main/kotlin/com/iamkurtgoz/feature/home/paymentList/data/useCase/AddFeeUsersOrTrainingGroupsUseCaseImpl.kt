package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.AddFeeUsersOrTrainingGroupsRequest
import com.iamkurtgoz.domain.model.response.AddFeeUsersOrTrainingGroupsDomainModel
import com.iamkurtgoz.domain.repository.FeeRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.AddFeeUsersOrTrainingGroupsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AddFeeUsersOrTrainingGroupsUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val feeRepository: FeeRepository,
) : AddFeeUsersOrTrainingGroupsUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: AddFeeUsersOrTrainingGroupsRequest): Flow<RestResult<AddFeeUsersOrTrainingGroupsDomainModel>> = prepare {
        feeRepository.addFeeUsersOrTrainingGroups(params)
    }
}

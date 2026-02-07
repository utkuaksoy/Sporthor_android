package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.response.GetFeeCategoryDomainModel
import com.iamkurtgoz.domain.repository.FeeRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeeCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetFeeCategoryUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val feeRepository: FeeRepository,
) : GetFeeCategoryUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<GetFeeCategoryDomainModel>> = prepare {
        feeRepository.getFeeCategory()
    }
}

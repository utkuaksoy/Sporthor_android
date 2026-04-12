package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.GetFeesDetailRequest
import com.iamkurtgoz.domain.model.response.GetFeesDetailDomainModel
import com.iamkurtgoz.domain.repository.FeeRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesDetailUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class GetFeesDetailUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val feeRepository: FeeRepository,
) : GetFeesDetailUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: GetFeesDetailRequest): Flow<RestResult<GetFeesDetailDomainModel>> = prepare {
        feeRepository.getFeesDetail(params)
    }
}

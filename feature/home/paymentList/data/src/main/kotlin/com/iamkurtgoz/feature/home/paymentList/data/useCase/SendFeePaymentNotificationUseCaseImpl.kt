package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.SendFeePaymentNotificationRequest
import com.iamkurtgoz.domain.repository.FeeRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.SendFeePaymentNotificationUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class SendFeePaymentNotificationUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val feeRepository: FeeRepository,
) : SendFeePaymentNotificationUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: SendFeePaymentNotificationRequest): Flow<RestResult<String>> = prepare {
        feeRepository.sendFeePaymentNotification(params)
    }
}

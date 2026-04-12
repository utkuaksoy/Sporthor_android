package com.iamkurtgoz.feature.home.paymentList.detail

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.model.request.GetFeesDetailRequest
import com.iamkurtgoz.domain.model.request.MarkAsPaidFeeRequest
import com.iamkurtgoz.domain.model.request.SendFeePaymentNotificationRequest
import com.iamkurtgoz.domain.model.response.GetFeesDetailDomainModel
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesDetailUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.MarkAsPaidFeeUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.SendFeePaymentNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class FeeDetailViewModel @Inject constructor(
    private val getFeesDetailUseCase: GetFeesDetailUseCase,
    private val markAsPaidFeeUseCase: MarkAsPaidFeeUseCase,
    private val sendFeePaymentNotificationUseCase: SendFeePaymentNotificationUseCase,
    private val appEventBus: AppEventBus,
) : CoreViewModel<FeeDetailViewModel.State, FeeDetailViewModel.SideEffect, FeeDetailViewModel.Event>(
    initialState = State(),
) {

    data class State(
        override val isLoading: Boolean = false,
        val userId: String = "",
        val trainingGroupId: String = "",
        val detail: GetFeesDetailDomainModel? = null,
        val markAsPaidSuccessToken: Int = 0,
    ) : com.iamkurtgoz.domain.core.CoreState.ViewState

    sealed class Event : com.iamkurtgoz.domain.core.CoreState.Event {
        data class Initialize(
            val userId: String,
            val trainingGroupId: String,
        ) : Event()

        data object NavigateUp : Event()
        data object NavigateToAddFee : Event()
        data class MarkFeeAsPaid(val feeId: String) : Event()
        data class SendFeePaymentNotification(
            val feeId: String,
            val isOverdue: Boolean,
        ) : Event()
    }

    sealed class SideEffect : com.iamkurtgoz.domain.core.CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data class NavigateToAddFee(val trainingGroupId: String) : SideEffect()
        data class ShowNotificationToast(val isOverdue: Boolean) : SideEffect()
    }

    override fun setEvent(event: Event) {
        when (event) {
            is Event.Initialize -> {
                updateState {
                    it.copy(
                        userId = event.userId,
                        trainingGroupId = event.trainingGroupId,
                    )
                }
                getDetail(
                    userId = event.userId,
                    trainingGroupId = event.trainingGroupId,
                )
            }

            Event.NavigateUp -> setSideEffect(SideEffect.NavigateUp)

            Event.NavigateToAddFee -> {
                val trainingGroupId = viewState.detail?.user?.trainingGroupId
                    ?.takeIf { it.isNotBlank() }
                    ?: viewState.trainingGroupId.takeIf { it.isNotBlank() }
                    ?: return

                setSideEffect(
                    SideEffect.NavigateToAddFee(
                        trainingGroupId = trainingGroupId,
                    ),
                )
            }

            is Event.MarkFeeAsPaid -> markFeeAsPaid(event.feeId)
            is Event.SendFeePaymentNotification -> sendFeePaymentNotification(
                feeId = event.feeId,
                isOverdue = event.isOverdue,
            )
        }
    }

    private fun getDetail(
        userId: String,
        trainingGroupId: String,
    ) {
        if (userId.isBlank() || trainingGroupId.isBlank()) return

        getFeesDetailUseCase.invoke(
            GetFeesDetailRequest(
                userId = userId,
                trainingGroupId = trainingGroupId,
            ),
        ).requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess { response ->
                updateState {
                    it.copy(
                        isLoading = false,
                        detail = response,
                    )
                }
            }
    }

    private fun markFeeAsPaid(
        feeId: String,
    ) {
        if (feeId.isBlank()) return

        val userId = viewState.userId
        val trainingGroupId = viewState.trainingGroupId
        if (userId.isBlank() || trainingGroupId.isBlank()) return

        markAsPaidFeeUseCase.invoke(
            MarkAsPaidFeeRequest(feeId = feeId),
        ).requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess {
                updateState {
                    it.copy(
                        isLoading = false,
                        markAsPaidSuccessToken = it.markAsPaidSuccessToken + 1,
                    )
                }
                viewModelScope.launch {
                    appEventBus.refreshPaymentList()
                }
                getDetail(
                    userId = userId,
                    trainingGroupId = trainingGroupId,
                )
            }
    }

    private fun sendFeePaymentNotification(
        feeId: String,
        isOverdue: Boolean,
    ) {
        if (feeId.isBlank()) return

        sendFeePaymentNotificationUseCase.invoke(
            SendFeePaymentNotificationRequest(feeId = feeId),
        ).requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess {
                updateState { it.copy(isLoading = false) }
                setSideEffect(SideEffect.ShowNotificationToast(isOverdue = isOverdue))
            }
    }
}

package com.iamkurtgoz.feature.home.paymentList

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeeCategoryUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class PaymentListViewModel @Inject constructor(
    private val getFeesUseCase: GetFeesUseCase,
    private val getFeeCategoryUseCase: GetFeeCategoryUseCase,

    ) : CoreViewModel<
        PaymentListScreenContract.State,
        PaymentListScreenContract.SideEffect,
        PaymentListScreenContract.Event,
        >(
    initialState = PaymentListScreenContract.State(),

) {

    override fun setEvent(event: PaymentListScreenContract.Event) {
        when (event) {
            PaymentListScreenContract.Event.Initialize -> {
                getFeeCategories()
                getFees(viewState.selectedFilterType)
            }

            PaymentListScreenContract.Event.RefreshData -> {
                getFees(viewState.selectedFilterType)
            }

            is PaymentListScreenContract.Event.ChangeFilter -> {
                updateState { it.copy(selectedFilterType = event.filterType) }
                getFees(event.filterType)
            }
            PaymentListScreenContract.Event.NavigateUp -> {
                setSideEffect(PaymentListScreenContract.SideEffect.NavigateUp)
            }
            is PaymentListScreenContract.Event.NavigateToAddFee -> {
                setSideEffect(
                    PaymentListScreenContract.SideEffect.NavigateToAddFee(
                        trainingGroupId = event.trainingGroupId,
                    ),
                )
            }
            is PaymentListScreenContract.Event.NavigateToFeeDetail -> {
                setSideEffect(
                    PaymentListScreenContract.SideEffect.NavigateToFeeDetail(
                        userId = event.userId,
                        trainingGroupId = event.trainingGroupId,
                    ),
                )
            }
        }
    }
    private fun getFeeCategories() {
        getFeeCategoryUseCase.invoke()
            .requester
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
                        feeCategories = response.categories.orEmpty(),
                    )
                }
            }
    }

    private fun getFees(filterType: Int) {
        getFeesUseCase.invoke(GetFeesRequest(filterType = filterType))
            .requester
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
                        feeResponse = response,
                    )
                }
            }
    }

}

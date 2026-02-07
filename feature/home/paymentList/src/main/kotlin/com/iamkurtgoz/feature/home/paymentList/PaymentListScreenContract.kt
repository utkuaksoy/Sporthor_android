package com.iamkurtgoz.feature.home.paymentList

import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.response.FeeCategoryItemDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel

internal class PaymentListScreenContract {

    data class State(
        override val isLoading: Boolean = false,
        val appBuildConfigStatePack: AppBuildConfigStatePack = AppBuildConfigStatePack(),
        val appRemoteConfigStatePack: AppRemoteConfigStatePack = AppRemoteConfigStatePack(),
        val feeCategories: List<FeeCategoryItemDomainModel> = emptyList(),
        val route: Any = Route,
        val selectedFilterType: Int = 0,
        val feeResponse: GetFeesDomainModel? = null,
    ) : CoreState.ViewState

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object RefreshData : Event()
        data class ChangeFilter(val filterType: Int) : Event()
        data object NavigateUp : Event()

        data class NavigateToAddFee(val trainingGroupId: String) : Event()
        data class NavigateToFeeDetail(
            val userId: String,
            val trainingGroupId: String,
        ) : Event()

    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()

        data class NavigateToAddFee(val trainingGroupId: String) : SideEffect()
        data class NavigateToFeeDetail(
            val userId: String,
            val trainingGroupId: String,
        ) : SideEffect()

    }

    data object Route
}

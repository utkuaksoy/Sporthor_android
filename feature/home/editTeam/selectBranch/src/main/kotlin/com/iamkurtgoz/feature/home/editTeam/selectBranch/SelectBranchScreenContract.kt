package com.iamkurtgoz.feature.home.editTeam.selectBranch

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesAttributeUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesItemUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesUIModel

internal class SelectBranchScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val branchesList: BranchesUIModel? = null,
        val branchesAttributeList: BranchesAttributeUIModel? = null,
        val selectedBranch: BranchesItemUIModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()

        data class SetSelectedBranch(val branch: BranchesItemUIModel?) : Event()
    }

    object Static
}

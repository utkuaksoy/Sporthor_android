package com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class BlockedUsersScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val alertDialogModel: AlertDialogModel? = null,
        val blockedUsers: List<BlockedUserUiModel> = emptyList(),
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
    }
}

data class BlockedUserUiModel(
    val id: String,
    val name: String,
    val username: String? = null,
    val imageUrl: String? = null,
)

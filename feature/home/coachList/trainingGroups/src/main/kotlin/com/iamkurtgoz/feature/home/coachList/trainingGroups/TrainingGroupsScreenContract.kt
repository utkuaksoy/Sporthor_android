package com.iamkurtgoz.feature.home.coachList.trainingGroups

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelCoache

internal class TrainingGroupsScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeCoachListTrainingGroupsScreenRoute.Route,
        val response: GetClubsAndDetailsDomainModel? = null,
        val isDeleteMode: Boolean = false,
        val selectedUserList: List<GetClubsAndDetailsDomainModelCoache> = listOf(),
        val deleteCoachDialogModel: AlertDialogModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToUpdateCoachScreen(val clubId: String?, val trainingGroupId: String?) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object ToggleDeleteMode : Event()
        data class SetSelectedUserList(val value: GetClubsAndDetailsDomainModelCoache) : Event()
        data object ShowDeleteCoachDialog : Event()
        data object DeleteCoach : Event()
        data class NavigateToUpdateCoachScreen(val clubId: String?, val trainingGroupId: String?) : Event()
    }

    object Static
}

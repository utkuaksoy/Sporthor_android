package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsUpdateCoachScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import com.iamkurtgoz.domain.model.response.SearchSocialDomainModel
import com.iamkurtgoz.domain.model.response.SocialDomainItemModel

internal class UpdateCoachScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeCoachListTrainingGroupsUpdateCoachScreenRoute.Route,
        val response: GetClubsAndDetailsDomainModel? = null,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val searchResultList: SearchSocialDomainModel? = null,
        val selectedUserList: List<SocialDomainItemModel?> = emptyList(),
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetSearchText(val text: String) : Event()
        data class ChangeSelectedUserState(val item: Any?) : Event()
        data object UpdateCoach : Event()
    }

    object Static {
        const val SEARCH_DEBOUNCE: Long = 500
        const val MIN_SEARCH_VALUE_LENGTH: Int = 3
    }
}

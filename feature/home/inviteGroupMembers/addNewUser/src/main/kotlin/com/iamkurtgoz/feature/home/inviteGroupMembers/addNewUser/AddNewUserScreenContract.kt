package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberAddNewUserRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIModel

internal class AddNewUserScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenInviteGroupMemberAddNewUserRoute,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val searchResultList: SocialSearchUIModel? = null,
        val selectedUserList: List<SocialSearchUIItemModel?> = emptyList(),
        val selectedUserIdList: List<String> = emptyList(),
        val followingList: UserRelationUIModel? = null,
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
        data object InviteClubMembers : Event()
        data class SetSearchText(val text: String) : Event()
        data class ChangeSelectedUserState(val item: Any?) : Event()
    }

    object Static
}

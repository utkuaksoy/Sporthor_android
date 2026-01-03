/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.InviteGroupMembersTab
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIModel

internal class InviteGroupMembersScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenInviteGroupMemberRoute,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val searchResultList: SocialSearchUIModel? = null,
        val selectedUserList: List<SocialSearchUIItemModel?> = emptyList(),
        val selectedUserIdList: List<String> = emptyList(),
        val followingList: UserRelationUIModel? = null,
        val selectedTab: InviteGroupMembersTab = InviteGroupMembersTab.PLAYERS,
        val firstCreateSheet: InviteGroupMembersTab? = null,
        val isInCreateFlowSecondStep: Boolean = false,
        val showCoachRoleSelectionBottomSheet: Boolean = false,
        val selectedCoachForRoleSelection: Any? = null,
        val selectedCoachRole: String? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
        data class NavigateToAddAddNewUserScreen(val model: HomeScreenAddNewUserScreenNavigationModel) : SideEffect()
        data object CloseBottomSheet : SideEffect()
        data object OpenBottomSheet : SideEffect() // YENİ
        data object NavigateToSelectGroup : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class ChangeSelectedUserState(val item: Any?, val tab: InviteGroupMembersTab) : Event()
        data class OnChangeTab(val tab: InviteGroupMembersTab) : Event()
        data class SetSearchText(val text: String) : Event()
        data object InviteClubMembersFromScreen : Event() // ANA EKRAN BUTON
        data object InviteClubMembersFromBottomSheet : Event()
        data class OpenCreateFlowSheet(val type: InviteGroupMembersTab) : Event()
        data object NextCreateFlowStep : Event() // sheet içindeki buton için
        data class OpenCoachRoleSelection(val coach: Any) : Event()
        data object DismissCoachRoleSelection : Event()
        data class SetCoachRole(val role: String) : Event()
        data object ConfirmCoachRoleSelection : Event()
        data object NavigateToSelectGroup : Event()
    }

    object Static {
        const val SEARCH_DEBOUNCE: Long = 500
        const val MIN_SEARCH_VALUE_LENGTH: Int = 3
    }
}

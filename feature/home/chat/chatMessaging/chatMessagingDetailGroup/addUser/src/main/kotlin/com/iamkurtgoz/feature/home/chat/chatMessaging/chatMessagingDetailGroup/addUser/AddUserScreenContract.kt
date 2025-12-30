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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.AddUserScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.model.MyFriendsFriendItemUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class AddUserScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val route: AddUserScreenRoute,
        val myFriendsList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val myFriendsFilteredList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val selectedUserList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
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
        data class UpdateChatGroup(val image: String?, val name: String?) : Event()
        data class SetTextSearch(val text: String) : Event()
        data class ChangeSelectedUserState(val item: MyFriendsFriendItemUIModel) : Event()
    }

    object Static
}

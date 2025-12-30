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
package com.iamkurtgoz.feature.home.chat.newChat.newGroupChat

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.model.MyFriendsFriendItemUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.io.File

internal class NewGroupChatScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val textGroupName: AppTextFieldValue = AppTextFieldValue(),
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val myFriendsList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val myFriendsFilteredList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val selectedUserList: ImmutableList<MyFriendsFriendItemUIModel> = persistentListOf(),
        val showPhotoPicker: Boolean = false,
        val selectedImage: File? = null,
        val onUploadProgress: Int? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToMessagingScreen(val isGroup: Boolean, val title: String, val channelId: String, val userId: String) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetTextGroupName(val value: String) : Event()
        data class SetTextSearch(val text: String) : Event()
        data class ChangeSelectedUserState(val item: MyFriendsFriendItemUIModel) : Event()
        data class SetShowStatePhotoPicker(val isShow: Boolean) : Event()
        data class SetSelectedImage(val imagePath: String?) : Event()
        data object GenerateGroupChat : Event()
    }

    object Static
}

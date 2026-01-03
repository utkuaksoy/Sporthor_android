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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.UpdateGroupScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.model.IconUIModel
import java.io.File

internal class UpdateGroupScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val route: UpdateGroupScreenRoute,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val iconList: List<IconUIModel>? = null,
        val groupImage: String? = null,
        val groupName: String? = null,
        val showPhotoPicker: Boolean = false,
        val selectedImage: File? = null,
        val onUploadProgress: Int? = null,
        val showEditNameDialog: Boolean = false,
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
        data class IconSelected(val icon: IconUIModel) : Event()
        data class SetShowStatePhotoPicker(val isShow: Boolean) : Event()
        data class SetSelectedImage(val imagePath: String?) : Event()
        data class UpdateChatGroup(val groupName: String?, val groupImage: String?) : Event()
        data class SetShowEditNameDialog(val isShow: Boolean) : Event()
        data class SetGroupName(val name: String) : Event()
    }

    data class GroupIconItem(
        val iconPath: String,
        val bgColor: String,
    )

    object Static
}

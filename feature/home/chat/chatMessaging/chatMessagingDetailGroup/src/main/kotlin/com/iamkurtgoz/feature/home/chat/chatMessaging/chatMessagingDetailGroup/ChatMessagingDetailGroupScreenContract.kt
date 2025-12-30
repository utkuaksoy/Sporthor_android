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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingDetailGroupRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.ChatDetailUserEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.model.GetChatGroupDetailUIModel

internal class ChatMessagingDetailGroupScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val route: HomeScreenChatMessagingDetailGroupRoute,
        val groupDetail: GetChatGroupDetailUIModel? = null,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToAddUser(val groupId: String?) : SideEffect()
        data class NavigateToUpdateGroup(val groupId: String?) : SideEffect()
        data class NavigateToAttachments(val userId: String?, val groupId: String?) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object LeaveChat : Event()
        data class NavigateToAddUser(val groupId: String?) : Event()
        data class NavigateToUpdateGroup(val groupId: String?) : Event()
        data class NavigateToAttachments(val groupId: String?, val userId: String?) : Event()
        data class OnClickActionButton(val isFollow: Boolean, val userId: String?) : Event()
        data class UpdateEventBusStatus(val eventBusState: ChatDetailUserEventBus.Event) : Event()
    }

    object Static
}

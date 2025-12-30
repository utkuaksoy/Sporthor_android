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
package com.iamkurtgoz.feature.home.chat.chatMessaging

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.ChatMessagingEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatListItem
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageUserItemUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class ChatMessagingScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenChatMessagingRoute,
        val textMessage: AppTextFieldValue = AppTextFieldValue(),
        var paginationPage: Int = AppDefaults.LIST_PARAM_PAGE,
        var paginationItemsPerPage: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        var paginationInitialing: Boolean = false,
        var paginationLoading: Boolean = false,
        var paginationReloading: Boolean = false,
        var paginationHasNext: Boolean = true,
        val messages: ImmutableList<ChatMessageItemUIModel> = persistentListOf(),
        val groupedMessages: ImmutableList<ChatListItem> = persistentListOf(),
        val users: ImmutableList<ChatMessageUserItemUIModel> = persistentListOf(),
        val isKeyboardShow: Boolean = false,
        val isTyping: Boolean = false,
        var isShowFileSelectDialog: Boolean = false,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object ScrollToBottom : SideEffect()
        data object ShowKeyboard : SideEffect()
        data object HideKeyboard : SideEffect()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : SideEffect()
        data class NavigateToChatMessagingDetailUser(val userId: String?, val groupId: String?) : SideEffect()
        data class NavigateToChatMessagingDetailGroup(val userId: String?, val groupId: String?) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetTextMessage(val text: String) : Event()
        data object SendMessage : Event()
        data class UpdateEventBusStatus(val eventBusState: ChatMessagingEventBus.Event) : Event()
        data class SetKeyboardShow(val isKeyboardShow: Boolean) : Event()
        data class FetchChatMessagingList(val fetchParam: FetchParam) : Event()
        data class SetShowFileSelectDialogState(val isShowFileSelectDialog: Boolean) : Event()
        data class SendSelectedFile(val path: String?, val extension: String?, val type: SignalRMessageType?) : Event()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : Event()
        data class NavigateToChatMessagingDetailUser(val userId: String?, val groupId: String?) : Event()
        data class NavigateToChatMessagingDetailGroup(val userId: String?, val groupId: String?) : Event()
    }

    object Static {
        const val NOTIFY_TYPING_DELAY = 1500L
        const val USER_TYPING_DELAY = 3000L
        const val SEND_FILE_DELAY = 2000L
    }
}

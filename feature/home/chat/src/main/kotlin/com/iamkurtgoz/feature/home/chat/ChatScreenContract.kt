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
package com.iamkurtgoz.feature.home.chat

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.base.AppChipItem
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.feature.home.chat.domain.model.ChatAllMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.domain.types.ChatFilterTypeList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal class ChatScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val selectedFilterType: AppChipItem = ChatFilterTypeList.first(),
        var paginationPage: Int = AppDefaults.LIST_PARAM_PAGE,
        var paginationItemsPerPage: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        var paginationInitialing: Boolean = false,
        var paginationLoading: Boolean = false,
        var paginationReloading: Boolean = false,
        var paginationHasNext: Boolean = true,
        val chatList: PersistentList<ChatAllMessageItemUIModel> = persistentListOf(),
        val chatFilteredList: PersistentList<ChatAllMessageItemUIModel> = persistentListOf(),
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToNewChatScreen : SideEffect()
        data class NavigateToMessagingScreen(val isGroup: Boolean, val title: String, val channelId: String, val userId: String) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object NavigateToNewChatScreen : Event()
        data class NavigateToMessagingScreen(val isGroup: Boolean, val title: String, val channelId: String, val userId: String) : Event()
        data object DismissDialogs : Event()
        data class SetSearch(val text: String) : Event()
        data class SetFilterType(val selectedFilterType: AppChipItem) : Event()
        data class FetchChatList(val fetchParam: FetchParam) : Event()
        data class HideMessage(val groupId: String?) : Event()
    }

    object Static
}

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
package com.iamkurtgoz.feature.home.search

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.search.domain.mock.MockData
import com.iamkurtgoz.feature.home.search.domain.model.SearchHistoryUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SocialSearchUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SuggestionUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SuggestionUserUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

internal class SearchScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val isSearchTextFieldFocused: Boolean = false,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val imageList: ImmutableList<SuggestionUIModel> = MockData.imageList.toPersistentList(),
        val suggestionUserList: ImmutableList<SuggestionUserUIModel> = MockData.suggestionUserList.toPersistentList(),
        val lastSearchUserList: ImmutableList<SuggestionUserUIModel> = MockData.suggestionUserList.toPersistentList(),
        val searchResultList: SocialSearchUIModel? = null,
        val searchHistoryResultList: SearchHistoryUIModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object ClearFocusAndHideKeyboard : SideEffect()
        data class NavigateToProfile(val userId: String) : SideEffect()
        data class NavigateToTeam(val teamId: String) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetSearchTextFieldFocusedStatus(val isFocused: Boolean) : Event()
        data class SetSearchText(val text: String) : Event()
        data object ClearFocusAndHideKeyboard : Event()
        data class ClickedSearchItem(
            val searchTerm: String? = null,
            val userId: String? = null,
            val teamId: String? = null,
        ) : Event()
        data class RemoveSearchHistory(val id: String?) : Event()
    }

    object Static {
        const val SEARCH_DEBOUNCE: Long = 500
        const val MIN_SEARCH_VALUE_LENGTH: Int = 4
    }
}

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

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.AddSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.RemoveSearchHistoryRequest
import com.iamkurtgoz.feature.home.search.domain.AddSearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.RemoveSearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.SearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val searchUseCase: SearchUseCase,
    private val searchHistoryUseCase: SearchHistoryUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val removeSearchHistoryUseCase: RemoveSearchHistoryUseCase,
) : CoreViewModel<SearchScreenContract.State, SearchScreenContract.SideEffect, SearchScreenContract.Event>(
    initialState = SearchScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    private var searchJob: Job? = null
    private var searchHistoryJob: Job? = null

    override fun setEvent(event: SearchScreenContract.Event) {
        when (event) {
            is SearchScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SearchScreenContract.Event.NavigateUp -> setSideEffect(SearchScreenContract.SideEffect.NavigateUp)
            is SearchScreenContract.Event.PopBackStack -> setSideEffect(SearchScreenContract.SideEffect.PopBackStack)
            is SearchScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SearchScreenContract.Event.SetSearchTextFieldFocusedStatus -> setSearchTextFieldFocusedStatus(event.isFocused)
            is SearchScreenContract.Event.SetSearchText -> setSearchText(event.text)
            is SearchScreenContract.Event.ClearFocusAndHideKeyboard -> setSideEffect(SearchScreenContract.SideEffect.ClearFocusAndHideKeyboard)
            is SearchScreenContract.Event.ClickedSearchItem -> clickedSearchItem(searchTerm = event.searchTerm, userId = event.userId, teamId = event.teamId)
            is SearchScreenContract.Event.RemoveSearchHistory -> removeSearchHistory(id = event.id)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        getSearchHistory()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setSearchTextFieldFocusedStatus(isFocused: Boolean) {
        updateState { state ->
            state.copy(
                isSearchTextFieldFocused = isFocused,
            )
        }
    }

    private fun setSearchText(text: String) {
        val textFieldValue = viewState.textSearch.copy(
            value = text,
            isError = false,
        )
        updateState { state ->
            state.copy(
                textSearch = textFieldValue,
            )
        }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .debounce(SearchScreenContract.Static.SEARCH_DEBOUNCE)
            .map { it.textSearch.value }
            .filter { it.length >= SearchScreenContract.Static.MIN_SEARCH_VALUE_LENGTH }
            .onEach(::getSearch)
            .launchIn(viewModelScope)
    }

    private fun getSearch(userName: String) {
        if (searchJob != null) {
            searchJob?.cancel()
            searchJob = null
        }

        searchJob = searchUseCase.invoke(userName)
            .requester
            .onLoading {
            }
            .onError {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        searchResultList = it,
                    )
                }
            }
    }

    private fun getSearchHistory() {
        if (searchHistoryJob != null) {
            searchHistoryJob?.cancel()
            searchHistoryJob = null
        }

        searchHistoryJob = searchHistoryUseCase.invoke(Unit)
            .requester
            .onLoading {
            }
            .onError {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        searchHistoryResultList = it,
                    )
                }
            }
    }

    private fun clickedSearchItem(searchTerm: String?, userId: String?, teamId: String?) {
        val params = AddSearchHistoryRequest(
            userId = userId,
            teamId = teamId,
            searchTerm = searchTerm,
        )

        addSearchHistoryUseCase.invoke(params)
            .requester
            .callWithSuccess {
                getSearchHistory()
            }

        userId?.let {
            setSideEffect(SearchScreenContract.SideEffect.NavigateToProfile(userId = it))
        }
        teamId?.let {
            setSideEffect(SearchScreenContract.SideEffect.NavigateToTeam(teamId = it))
        }
    }

    private fun removeSearchHistory(id: String?) {
        val params = RemoveSearchHistoryRequest(
            id = id,
        )

        removeSearchHistoryUseCase.invoke(params)
            .requester
            .callWithSuccess {
                getSearchHistory()
            }
    }
}

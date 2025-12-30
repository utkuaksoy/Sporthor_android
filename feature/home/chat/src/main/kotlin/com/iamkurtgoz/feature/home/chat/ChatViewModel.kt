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

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AppChipItem
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.request.HideMessagesRequest
import com.iamkurtgoz.feature.home.chat.domain.model.ChatAllMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.domain.useCase.GetAllMessagesUseCase
import com.iamkurtgoz.feature.home.chat.domain.useCase.HideMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val hideMessagesUseCase: HideMessagesUseCase,
) : CoreViewModel<ChatScreenContract.State, ChatScreenContract.SideEffect, ChatScreenContract.Event>(
    initialState = ChatScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: ChatScreenContract.Event) {
        when (event) {
            is ChatScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ChatScreenContract.Event.NavigateUp -> setSideEffect(ChatScreenContract.SideEffect.NavigateUp)
            is ChatScreenContract.Event.PopBackStack -> setSideEffect(ChatScreenContract.SideEffect.PopBackStack)
            is ChatScreenContract.Event.NavigateToNewChatScreen -> setSideEffect(ChatScreenContract.SideEffect.NavigateToNewChatScreen)
            is ChatScreenContract.Event.NavigateToMessagingScreen -> setSideEffect(ChatScreenContract.SideEffect.NavigateToMessagingScreen(event.isGroup, event.title, event.channelId, event.userId))
            is ChatScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ChatScreenContract.Event.SetSearch -> setSearch(event.text)
            is ChatScreenContract.Event.SetFilterType -> setFilterType(event.selectedFilterType)
            is ChatScreenContract.Event.FetchChatList -> fetchChatList(event.fetchParam)
            is ChatScreenContract.Event.HideMessage -> hideMessage(event.groupId)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        fetchChatList(fetchParam = FetchParam.INITIAL)
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setFilterType(selectedFilterType: AppChipItem) {
        updateState { state ->
            state.copy(
                selectedFilterType = selectedFilterType,
            )
        }
    }

    private fun setSearch(text: String) {
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
            .map { it.textSearch.value }
            .onEach {
                updateState { state ->
                    state.copy(
                        chatFilteredList = state.chatList.filter { item -> item.isMatch(it) }.toPersistentList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchChatList(fetchParam: FetchParam) {
        if (viewState.paginationInitialing || viewState.paginationLoading || viewState.paginationReloading) {
            return
        }

        when (fetchParam) {
            FetchParam.INITIAL -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = true,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = true,
                        chatList = persistentListOf(),
                    )
                }
            }
            FetchParam.RELOAD -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = true,
                        paginationHasNext = true,
                        chatList = persistentListOf(),
                    )
                }
            }
            FetchParam.NEXT_PAGE -> {
                if (viewState.paginationHasNext) {
                    updateState { state ->
                        state.copy(
                            paginationPage = viewState.paginationPage.plus(AppDefaults.ONE),
                            paginationInitialing = false,
                            paginationLoading = true,
                            paginationReloading = false,
                        )
                    }
                }
            }
        }

        if (!viewState.paginationHasNext) {
            return
        }

        getAllMessagesUseCase.invoke(viewState.paginationPage)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isShimmerLoading = fetchParam == FetchParam.INITIAL || fetchParam == FetchParam.RELOAD,
                        paginationInitialing = fetchParam == FetchParam.INITIAL,
                        paginationLoading = fetchParam == FetchParam.NEXT_PAGE,
                        paginationReloading = fetchParam == FetchParam.RELOAD,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                val responseMessages = response.messages ?: emptyList()
                val currentList: List<ChatAllMessageItemUIModel> = when (fetchParam) {
                    FetchParam.INITIAL -> responseMessages
                    FetchParam.RELOAD -> responseMessages
                    FetchParam.NEXT_PAGE -> {
                        val list = viewState.chatList.toMutableList()
                        list.apply {
                            addAll(responseMessages)
                        }
                    }
                }
                val paginationHasNext = responseMessages.isNotEmpty()

                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = paginationHasNext,
                        chatList = currentList.toPersistentList(),
                    )
                }
            }
    }

    private fun hideMessage(groupId: String?) {

        val request = HideMessagesRequest(
                groupId = groupId,
        )

        hideMessagesUseCase.invoke(request)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
            .callWithSuccess {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
    }
}

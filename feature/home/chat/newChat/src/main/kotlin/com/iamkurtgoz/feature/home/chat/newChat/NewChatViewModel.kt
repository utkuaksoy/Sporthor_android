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
package com.iamkurtgoz.feature.home.chat.newChat

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.GenerateChatGroupRequest
import com.iamkurtgoz.feature.home.chat.newChat.domain.model.MyFriendsFriendItemUIModel
import com.iamkurtgoz.feature.home.chat.newChat.domain.useCase.GenerateChatGroupUseCase
import com.iamkurtgoz.feature.home.chat.newChat.domain.useCase.GetMyFriendsUseCase
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
internal class NewChatViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getMyFriendsUseCase: GetMyFriendsUseCase,
    private val generateChatGroupUseCase: GenerateChatGroupUseCase,
) : CoreViewModel<NewChatScreenContract.State, NewChatScreenContract.SideEffect, NewChatScreenContract.Event>(
    initialState = NewChatScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: NewChatScreenContract.Event) {
        when (event) {
            is NewChatScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is NewChatScreenContract.Event.NavigateUp -> setSideEffect(NewChatScreenContract.SideEffect.NavigateUp)
            is NewChatScreenContract.Event.PopBackStack -> setSideEffect(NewChatScreenContract.SideEffect.PopBackStack)
            is NewChatScreenContract.Event.DismissDialogs -> dismissDialogs()
            is NewChatScreenContract.Event.SetSearch -> setSearch(event.text)
            is NewChatScreenContract.Event.CreateGroupChat -> setSideEffect(NewChatScreenContract.SideEffect.NavigateToNewGroupChat)
            is NewChatScreenContract.Event.CreateCommunity -> createCommunity()
            is NewChatScreenContract.Event.ConnectContacts -> connectContacts()
            is NewChatScreenContract.Event.GenerateChatGroup -> generateChatGroup(event.item)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        getMyFriends()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
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
                        myFriendsFilteredList = state.myFriendsList.filter { item -> item.isMatch(it) }.toPersistentList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun createCommunity() = Unit
    private fun connectContacts() = Unit

    private fun getMyFriends() {
        getMyFriendsUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        myFriendsList = it.friends?.filterNotNull()?.toPersistentList() ?: persistentListOf(),
                    )
                }
            }
    }

    private fun generateChatGroup(item: MyFriendsFriendItemUIModel) {
        val params = GenerateChatGroupRequest(
            image = null,
            isPrivate = true,
            name = item.name,
            users = listOf(
                item.id,
            ),
        )
        generateChatGroupUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                it.id?.let { channelId ->
                    val effect = NewChatScreenContract.SideEffect.NavigateToMessagingScreen(
                        isGroup = false,
                        title = it.name ?: "",
                        channelId = channelId,
                        userId = it.toUserId ?: "",
                    )
                    setSideEffect(effect)
                }
            }
    }
}

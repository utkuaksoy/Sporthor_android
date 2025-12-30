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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateChatGroupRequest
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.di.MyFriendsUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.di.UpdateChatGroupUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.model.MyFriendsFriendItemUIModel
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
internal class AddUserViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val myFriendsUseCase: MyFriendsUseCase,
    private val updateChatGroupUseCase: UpdateChatGroupUseCase,
) : CoreViewModel<AddUserScreenContract.State, AddUserScreenContract.SideEffect, AddUserScreenContract.Event>(
    initialState = AddUserScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: AddUserScreenContract.Event) {
        when (event) {
            is AddUserScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AddUserScreenContract.Event.NavigateUp -> setSideEffect(AddUserScreenContract.SideEffect.NavigateUp)
            is AddUserScreenContract.Event.PopBackStack -> setSideEffect(AddUserScreenContract.SideEffect.PopBackStack)
            is AddUserScreenContract.Event.DismissDialogs -> dismissDialogs()
            is AddUserScreenContract.Event.ChangeSelectedUserState -> changeSelectedUserState(event.item)
            is AddUserScreenContract.Event.SetTextSearch -> setTextSearch(event.text)
            is AddUserScreenContract.Event.UpdateChatGroup -> updateChatGroup(image = event.image, name = event.name)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getMyFriends()
        searchListen()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getMyFriends() {
        myFriendsUseCase.invoke()
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

    private fun changeSelectedUserState(item: MyFriendsFriendItemUIModel) {
        val selectedUserList = viewState.selectedUserList.toMutableList()
        if (selectedUserList.any { it.id == item.id }) {
            selectedUserList.removeAll { it.id == item.id }
        } else {
            selectedUserList.add(item)
        }
        updateState { state ->
            state.copy(
                selectedUserList = selectedUserList.toPersistentList(),
            )
        }
    }

    private fun setTextSearch(value: String) {
        updateState { state ->
            state.copy(
                textSearch = state.textSearch.copy(
                    value = value,
                ),
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

    private fun updateChatGroup(image: String?, name: String?) {
        val request = UpdateChatGroupRequest(
            groupId = viewState.route.groupId,
            image = image,
            name = name,
            newUsers = viewState.selectedUserList.map { it.id },
        )
        updateChatGroupUseCase.invoke(request)
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
            }
    }
}

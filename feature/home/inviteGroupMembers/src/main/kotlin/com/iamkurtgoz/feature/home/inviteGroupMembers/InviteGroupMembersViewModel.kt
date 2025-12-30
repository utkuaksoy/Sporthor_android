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
package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.toHomeScreenInviteGroupMemberRouteTypeMap
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.SocialSearchUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.UserRelationUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.AddTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.GetUserRelationUseCaseParams
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class InviteGroupMembersViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val userRelationUseCase: GetUserRelationUseCase,
    private val searchUseCase: SearchUseCase,
    private val addTrainingGroupUserUseCase: AddTrainingGroupUserUseCase,
) : CoreViewModel<InviteGroupMembersScreenContract.State, InviteGroupMembersScreenContract.SideEffect, InviteGroupMembersScreenContract.Event>(
    initialState = InviteGroupMembersScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenInviteGroupMemberRouteTypeMap(),
    ),
) {
    override fun setEvent(event: InviteGroupMembersScreenContract.Event) {
        when (event) {
            is InviteGroupMembersScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is InviteGroupMembersScreenContract.Event.NavigateUp -> setSideEffect(InviteGroupMembersScreenContract.SideEffect.NavigateUp)
            is InviteGroupMembersScreenContract.Event.PopBackStack -> setSideEffect(InviteGroupMembersScreenContract.SideEffect.PopBackStack)
            is InviteGroupMembersScreenContract.Event.DismissDialogs -> dismissDialogs()
            is InviteGroupMembersScreenContract.Event.InviteClubMembers -> inviteClubMembers()
            is InviteGroupMembersScreenContract.Event.SetSearchText -> setSearchText(event.text)
            is InviteGroupMembersScreenContract.Event.ChangeSelectedUserState -> changeSelectedUserState(event.item)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        if (viewState.route.model.users.isEmpty()) {
            appPreferences.currentPreferenceState.firstOrNull()?.userId?.let {
                getFollowing(it)
            }
        } else {
            updateState { state ->
                state.copy(
                    followingList = UserRelationUIModel(
                        users = viewState.route.model.users.map {
                            UserRelationUIItemModel(
                                id = it.id,
                                name = it.name,
                                username = it.username,
                                summary = it.summary,
                                imageUrl = it.imageUrl,
                                isFollow = it.isFollow,
                                isCurrentUser = it.isCurrentUser,
                            )
                        },
                    ),
                    selectedUserList = viewState.route.model.users.map {
                        SocialSearchUIItemModel(
                            id = it.id,
                            image = it.imageUrl,
                            name = it.name,
                            attribute = it.summary,
                            type = SocialSearchUIItemType.User,
                        )
                    },
                    selectedUserIdList = viewState.route.model.users.mapNotNull {
                        it.id
                    },
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
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
            .debounce(InviteGroupMembersScreenContract.Static.SEARCH_DEBOUNCE)
            .map { it.textSearch.value }
            .filter { it.length >= InviteGroupMembersScreenContract.Static.MIN_SEARCH_VALUE_LENGTH }
            .onEach(::getSearch)
            .launchIn(viewModelScope)
    }

    private var searchJob: Job? = null
    private fun getSearch(query: String) {
        if (searchJob != null) {
            searchJob?.cancel()
            searchJob = null
        }

        searchJob = searchUseCase.invoke(query)
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

    private fun changeSelectedUserState(item: Any?) {
        val selectedUserList = viewState.selectedUserList.toMutableList()
        val selectedUserIdList = viewState.selectedUserIdList.toMutableList()
        if (item is SocialSearchUIItemModel) {
            if (!selectedUserList.any { it?.id == item.id }) {
                selectedUserList.add(item)
            }
            if (selectedUserIdList.any { it == item.id }) {
                selectedUserIdList.removeAll { it == item.id }
            } else {
                item.id?.let { selectedUserIdList.add(it) }
            }
        } else if (item is UserRelationUIItemModel) {
            val castItem = SocialSearchUIItemModel(
                id = item.id,
                image = item.imageUrl,
                name = item.name,
                attribute = item.summary,
                type = SocialSearchUIItemType.User,
            )
            if (!selectedUserList.any { it?.id == castItem.id }) {
                selectedUserList.add(castItem)
            }
            if (selectedUserIdList.any { it == castItem.id }) {
                selectedUserIdList.removeAll { it == castItem.id }
            } else {
                castItem.id?.let { selectedUserIdList.add(it) }
            }
        }
        updateState { state ->
            state.copy(
                selectedUserList = selectedUserList.toPersistentList(),
                selectedUserIdList = selectedUserIdList.toPersistentList(),
            )
        }
    }

    private fun getFollowing(userId: String) {
        val params = GetUserRelationUseCaseParams(
            userId = userId,
            type = UserRelationUIItemType.FOLLOWING,
        )
        userRelationUseCase.invoke(params)
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
                        followingList = it,
                    )
                }
            }
    }

    private fun inviteClubMembers() {
        if (viewState.selectedUserList.isEmpty()) {
            return
        }

        val params = AddTrainingGroupUserRequest(
            groupId = viewState.route.model.groupId,
            users = viewState.selectedUserIdList,
        )

        addTrainingGroupUserUseCase.invoke(params)
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
                setSideEffect(InviteGroupMembersScreenContract.SideEffect.NavigateToHome)
            }
    }
}

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
package com.iamkurtgoz.feature.home.profile.userRelation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.impl.ProfileUserRelationEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.feature.home.profile.userRelation.domain.types.UserRelationUIItemType
import com.iamkurtgoz.feature.home.profile.userRelation.domain.useCase.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.profile.userRelation.domain.useCase.GetUserRelationUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserRelationViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val userRelationUseCase: GetUserRelationUseCase,
    private val userActionController: UserActionController,
) : CoreViewModel<UserRelationScreenContract.State, UserRelationScreenContract.SideEffect, UserRelationScreenContract.Event>(
    initialState = UserRelationScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigationRoute = savedStateHandle.toRoute(),
    ),
) {
    private var relationJob: Job? = null

    override fun setEvent(event: UserRelationScreenContract.Event) {
        when (event) {
            is UserRelationScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is UserRelationScreenContract.Event.NavigateUp -> setSideEffect(UserRelationScreenContract.SideEffect.NavigateUp)
            is UserRelationScreenContract.Event.PopBackStack -> setSideEffect(UserRelationScreenContract.SideEffect.PopBackStack)
            is UserRelationScreenContract.Event.DismissDialogs -> dismissDialogs()
            is UserRelationScreenContract.Event.SetSelectedTabIndex -> setSelectedTabIndex(event.index)
            is UserRelationScreenContract.Event.ChangeFollowStatus -> changeFollowStatus(event.userActionFollowType, event.userId)
            is UserRelationScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.eventBusState)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        fetchUserRelationByTab(viewState.selectedTabIndex)
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setSelectedTabIndex(index: Int) {
        updateState { state ->
            state.copy(
                selectedTabIndex = index,
            )
        }
        fetchUserRelationByTab(index)
    }

    private fun fetchUserRelationByTab(index: Int) {
        val userId = viewState.navigationRoute.userId
        when (index) {
            AppDefaults.ZERO -> {
                if (viewState.followersList == null) {
                    getUserRelation(userId, UserRelationUIItemType.FOLLOWERS)
                }
            }
            AppDefaults.ONE -> {
                if (viewState.followingList == null) {
                    getUserRelation(userId, UserRelationUIItemType.FOLLOWING)
                }
            }
        }
    }

    private fun getUserRelation(userId: String, type: UserRelationUIItemType) {
        if (relationJob != null) {
            relationJob?.cancel()
            relationJob = null
        }

        val params = GetUserRelationUseCaseParams(
            userId = userId,
            type = type,
        )
        relationJob = userRelationUseCase.invoke(params)
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
                    when (type) {
                        UserRelationUIItemType.FOLLOWERS -> state.copy(followersList = it)
                        UserRelationUIItemType.FOLLOWING -> state.copy(followingList = it)
                    }
                }
            }
    }

    private fun changeFollowStatus(userActionFollowType: UserActionFollowType, userId: String?) {
        updateState { state ->
            val updateUser: (com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel) -> com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel = { item ->
                if (item.id != userId) {
                    item
                } else {
                    when (userActionFollowType) {
                        UserActionFollowType.Follow -> item.copy(
                            isFollow = false,
                            isFollowRequest = true,
                        )
                        UserActionFollowType.UnFollow -> item.copy(
                            isFollow = false,
                            isFollowRequest = false,
                        )
                    }
                }
            }
            state.copy(
                followersList = state.followersList?.copy(
                    users = state.followersList?.users?.map(updateUser),
                ),
                followingList = state.followingList?.copy(
                    users = state.followingList?.users?.map(updateUser),
                ),
            )
        }

        userActionController.changeFollowStatus(
            scope = viewModelScope,
            targetUserId = userId,
            followType = userActionFollowType,
            onErrorAction = {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            },
        )
    }

    private fun updateEventBusStatus(status: ProfileUserRelationEventBus.Event) {
        when (status) {
            is ProfileUserRelationEventBus.Event.UpdateFollowingStatus -> {
                val updateUser: (com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel) -> com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel = { item ->
                    if (item.id != status.targetUserId) {
                        item
                    } else {
                        when (status.followType) {
                            UserActionFollowType.Follow -> item.copy(
                                isFollow = false,
                                isFollowRequest = true,
                            )
                            UserActionFollowType.UnFollow -> item.copy(
                                isFollow = false,
                                isFollowRequest = false,
                            )
                        }
                    }
                }
                updateState { state ->
                    state.copy(
                        followersList = state.followersList?.copy(
                            users = state.followersList?.users?.map(updateUser),
                        ),
                        followingList = state.followingList?.copy(
                            users = state.followingList?.users?.map(updateUser),
                        ),
                    )
                }
            }
        }
    }
}

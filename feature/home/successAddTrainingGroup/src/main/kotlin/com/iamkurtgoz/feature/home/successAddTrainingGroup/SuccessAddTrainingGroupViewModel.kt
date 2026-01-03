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
package com.iamkurtgoz.feature.home.successAddTrainingGroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModelUser
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.toHomeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SuccessAddTrainingGroupViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<SuccessAddTrainingGroupScreenContract.State, SuccessAddTrainingGroupScreenContract.SideEffect, SuccessAddTrainingGroupScreenContract.Event>(
    initialState = SuccessAddTrainingGroupScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenSuccessAddTrainingGroupRoute(),
    ),
) {
    override fun setEvent(event: SuccessAddTrainingGroupScreenContract.Event) {
        when (event) {
            is SuccessAddTrainingGroupScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SuccessAddTrainingGroupScreenContract.Event.NavigateUp -> setSideEffect(SuccessAddTrainingGroupScreenContract.SideEffect.NavigateUp)
            is SuccessAddTrainingGroupScreenContract.Event.PopBackStack -> setSideEffect(SuccessAddTrainingGroupScreenContract.SideEffect.PopBackStack)
            is SuccessAddTrainingGroupScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SuccessAddTrainingGroupScreenContract.Event.NavigateToInviteGroupMembersScreen -> {
                val model = HomeScreenInviteGroupMemberScreenNavigationModel(
                    isEdit = viewState.route.model.isEdit,
                    clubId = viewState.route.model.clubId,
                    clubName = viewState.route.model.clubName,
                    clubLogo = viewState.route.model.clubLogo,
                    groupId = viewState.route.model.groupId,
                    groupName = viewState.route.model.groupName,
                    users = viewState.route.model.users.map { user ->
                        HomeScreenInviteGroupMemberScreenNavigationModelUser(
                            id = user.id,
                            imageUrl = user.imageUrl,
                            isCurrentUser = user.isCurrentUser,
                            isFollow = user.isFollow,
                            name = user.name,
                            summary = user.summary,
                            username = user.username,
                        )
                    },
                    coaches = viewState.route.model.coaches.map { coach ->
                        HomeScreenInviteGroupMemberScreenNavigationModelUser(
                            id = coach.id,
                            imageUrl = coach.imageUrl,
                            isCurrentUser = coach.isCurrentUser,
                            isFollow = coach.isFollow,
                            name = coach.name,
                            summary = coach.summary,
                            username = coach.username,
                        )
                    },
                )
                setSideEffect(SuccessAddTrainingGroupScreenContract.SideEffect.NavigateToInviteGroupMembersScreen(model))
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

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
package com.iamkurtgoz.feature.home.selectTrainingGroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModelUser
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.model.GetTrainingGroupUserUIModelGroup
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.useCase.GetTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.useCase.RemoveTrainingGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectTrainingGroupViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getTrainingGroupUserUseCase: GetTrainingGroupUserUseCase,
    private val removeTrainingGroupUseCase: RemoveTrainingGroupUseCase,
) : CoreViewModel<SelectTrainingGroupScreenContract.State, SelectTrainingGroupScreenContract.SideEffect, SelectTrainingGroupScreenContract.Event>(
    initialState = SelectTrainingGroupScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: SelectTrainingGroupScreenContract.Event) {
        when (event) {
            is SelectTrainingGroupScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectTrainingGroupScreenContract.Event.NavigateUp -> setSideEffect(SelectTrainingGroupScreenContract.SideEffect.NavigateUp)
            is SelectTrainingGroupScreenContract.Event.PopBackStack -> setSideEffect(SelectTrainingGroupScreenContract.SideEffect.PopBackStack)
            is SelectTrainingGroupScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectTrainingGroupScreenContract.Event.NavigateToEditTrainingGroupScreen -> {
                val model = HomeScreenEditTrainingGroupScreenNavigationModel(
                    teamId = event.model.team?.value,
                    teamName = event.model.team?.name,
                    teamLogo = event.model.team?.detail,
                    season = event.model.season,
                    id = event.model.groupId,
                    groupName = event.model.groupName,
                    users = event.model.users?.filterNotNull()?.map { user ->
                        HomeScreenEditTrainingGroupScreenNavigationModelUser(
                            id = user.id,
                            imageUrl = user.imageUrl,
                            isCurrentUser = user.isCurrentUser,
                            isFollow = user.isFollow,
                            name = user.name,
                            summary = user.summary,
                            username = user.username,
                        )
                    } ?: listOf(),
                    coaches = event.model.coaches?.filterNotNull()?.map { user ->
                        HomeScreenEditTrainingGroupScreenNavigationModelUser(
                            id = user.id,
                            imageUrl = user.imageUrl,
                            isCurrentUser = user.isCurrentUser,
                            isFollow = user.isFollow,
                            name = user.name,
                            summary = user.summary,
                            username = user.username,
                        )
                    } ?: listOf(),
                )

                // event.model.users
                setSideEffect(SelectTrainingGroupScreenContract.SideEffect.NavigateToEditTrainingGroupScreen(model))
            }
            is SelectTrainingGroupScreenContract.Event.ToggleDeleteMode -> toggleDeleteMode()
            is SelectTrainingGroupScreenContract.Event.SetSelectedTrainingGroup -> setSelectedTrainingGroup(event.value)
            is SelectTrainingGroupScreenContract.Event.ShowDeleteTrainingGroupDialog -> showDeleteTrainingGroupDialog()
            is SelectTrainingGroupScreenContract.Event.DeleteTrainingGroup -> deleteTrainingGroup()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getTrainingGroupUser()
    }

    private fun dismissDialogs() {
        updateState {
            it.copy(
                alertDialogModel = null,
                deleteTrainingGroupDialogModel = null,
            )
        }
    }

    private fun getTrainingGroupUser() {
        getTrainingGroupUserUseCase.invoke()
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
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        groups = response.groups?.filterNotNull() ?: emptyList(),
                    )
                }
            }
    }

    private fun toggleDeleteMode() {
        updateState { state ->
            state.copy(
                isDeleteMode = !state.isDeleteMode,
            )
        }
    }

    private fun setSelectedTrainingGroup(value: GetTrainingGroupUserUIModelGroup?) {
        updateState { state ->
            state.copy(
                selectedTrainingGroup = value,
            )
        }
    }

    private fun showDeleteTrainingGroupDialog() {
        updateState { state ->
            state.copy(
                deleteTrainingGroupDialogModel = AnyAlertDialogModel(
                    title = "",
                    message = "${viewState.selectedTrainingGroup?.groupName ?: ""} isimli antrenman grubunuzu silmek istediğinize emin misiniz?",
                    confirmButton = "Evet",
                    dismissButton = "Hayır",
                ),
            )
        }
    }

    private fun deleteTrainingGroup() {
        val trainingGroupId = viewState.selectedTrainingGroup?.groupId
        val requestBody = RemoveTrainingGroupRequest(
            trainingGroupId = trainingGroupId,
        )

        removeTrainingGroupUseCase
            .invoke(requestBody)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                getTrainingGroupUser()
            }
    }
}

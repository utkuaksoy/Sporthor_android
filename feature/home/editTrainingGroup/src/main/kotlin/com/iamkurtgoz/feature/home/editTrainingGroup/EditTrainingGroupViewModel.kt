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
package com.iamkurtgoz.feature.home.editTrainingGroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.toHomeScreenEditTrainingGroupScreenRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModelUser
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.UpdateTrainingGroupRequest
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.model.GetSeasonsUIModel
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.GetRecommendedGroupNamesUseCase
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.GetSeasonsUseCase
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.UpdateTrainingGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditTrainingGroupViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val updateTrainingGroupUseCase: UpdateTrainingGroupUseCase,
    private val getRecommendedGroupNamesUseCase: GetRecommendedGroupNamesUseCase,
) : CoreViewModel<EditTrainingGroupScreenContract.State, EditTrainingGroupScreenContract.SideEffect, EditTrainingGroupScreenContract.Event>(
    initialState = EditTrainingGroupScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenEditTrainingGroupScreenRouteTypeMap(),
    ),
) {
    override fun setEvent(event: EditTrainingGroupScreenContract.Event) {
        when (event) {
            is EditTrainingGroupScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is EditTrainingGroupScreenContract.Event.NavigateUp -> setSideEffect(EditTrainingGroupScreenContract.SideEffect.NavigateUp)
            is EditTrainingGroupScreenContract.Event.PopBackStack -> setSideEffect(EditTrainingGroupScreenContract.SideEffect.PopBackStack)
            is EditTrainingGroupScreenContract.Event.DismissDialogs -> dismissDialogs()
            is EditTrainingGroupScreenContract.Event.ShowSelectSeasonDialog -> showSelectSeasonDialog()
            is EditTrainingGroupScreenContract.Event.SetSelectedSeason -> setSelectedSeason(event.selectedSeason)
            is EditTrainingGroupScreenContract.Event.SetGroupName -> setGroupName(event.value)
            is EditTrainingGroupScreenContract.Event.EditTrainingGroup -> editTrainingGroup()
            is EditTrainingGroupScreenContract.Event.NavigateToHome -> setSideEffect(EditTrainingGroupScreenContract.SideEffect.NavigateToHome)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getSeasons()
        setGroupName(viewState.route.model.groupName ?: "")
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showSelectSeasonDialog = false,
            )
        }
    }

    private fun showSelectSeasonDialog() {
        updateState { state ->
            state.copy(
                showSelectSeasonDialog = true,
            )
        }
    }

    private fun setSelectedSeason(selectedSeason: GetSeasonsUIModel?) {
        updateState { state ->
            state.copy(
                selectedSeason = selectedSeason,
            )
        }
    }

    private fun setGroupName(value: String) {
        val userName = viewState.textGroupName.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textGroupName = userName,
            )
        }
    }

    private fun getSeasons() {
        getSeasonsUseCase.invoke()
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
            .callWithSuccess { list ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        seasonList = list,
                    )
                }
                setSelectedSeason(list.firstOrNull { it.name == viewState.route.model.season })
                getRecommendedGroupNames()
            }
    }

    private fun getRecommendedGroupNames() {
        getRecommendedGroupNamesUseCase.invoke(viewState.route.model.teamId)
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
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        getRecomendedGroupNames = response,
                    )
                }
            }
    }

    private fun editTrainingGroup() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }
        if (viewState.selectedSeason == null) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = "Hata", // TODO: Localize
                        message = "Lütfen bir sezon seçiniz.", // TODO: Localize
                        confirmButton = "Tamam", // TODO: Localize
                        dismissButton = null,
                    ),
                )
            }
            return
        }

        val params = UpdateTrainingGroupRequest(
            id = viewState.route.model.id,
            groupName = viewState.textGroupName.value,
            season = viewState.selectedSeason?.value,
            teamId = viewState.route.model.teamId,
        )

        updateTrainingGroupUseCase.invoke(params)
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
            .callWithSuccess { clubUIModel ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                val model = HomeScreenSuccessAddTrainingGroupScreenNavigationModel(
                    isEdit = true,
                    clubId = clubUIModel.teamId,
                    clubName = clubUIModel.teamName,
                    clubLogo = clubUIModel.logo,
                    groupId = clubUIModel.trainingGroupId,
                    groupName = clubUIModel.groupName,
                    users = viewState.route.model.users.map { user ->
                        HomeScreenSuccessAddTrainingGroupScreenNavigationModelUser(
                            id = user.id,
                            imageUrl = user.imageUrl,
                            isCurrentUser = user.isCurrentUser,
                            isFollow = user.isFollow,
                            name = user.name,
                            summary = user.summary,
                            username = user.username,
                        )
                    },
                )
                setSideEffect(EditTrainingGroupScreenContract.SideEffect.NavigateToSuccessAddTrainingGroupScreen(model))
            }
    }
}

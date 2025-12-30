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
package com.iamkurtgoz.feature.home.trainingScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.toHomeScreenTrainingRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.AddTrainingGroupRequest
import com.iamkurtgoz.feature.home.trainingScreen.domain.model.GetSeasonsUIModel
import com.iamkurtgoz.feature.home.trainingScreen.domain.useCase.AddTrainingGroupUseCase
import com.iamkurtgoz.feature.home.trainingScreen.domain.useCase.GetRecommendedGroupNamesUseCase
import com.iamkurtgoz.feature.home.trainingScreen.domain.useCase.GetSeasonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class TrainingScreenViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val addTrainingGroupUseCase: AddTrainingGroupUseCase,
    private val getRecommendedGroupNamesUseCase: GetRecommendedGroupNamesUseCase,
) : CoreViewModel<TrainingScreenScreenContract.State, TrainingScreenScreenContract.SideEffect, TrainingScreenScreenContract.Event>(
    initialState = TrainingScreenScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenTrainingRoute(),
    ),
) {
    override fun setEvent(event: TrainingScreenScreenContract.Event) {
        when (event) {
            is TrainingScreenScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is TrainingScreenScreenContract.Event.NavigateUp -> setSideEffect(TrainingScreenScreenContract.SideEffect.NavigateUp)
            is TrainingScreenScreenContract.Event.PopBackStack -> setSideEffect(TrainingScreenScreenContract.SideEffect.PopBackStack)
            is TrainingScreenScreenContract.Event.DismissDialogs -> dismissDialogs()
            is TrainingScreenScreenContract.Event.ShowChangeClubDialog -> showChangeClubDialog()
            is TrainingScreenScreenContract.Event.SetSelectedClub -> setSelectedClub(event.selectedClub)
            is TrainingScreenScreenContract.Event.ShowSelectSeasonDialog -> showSelectSeasonDialog()
            is TrainingScreenScreenContract.Event.SetSelectedSeason -> setSelectedSeason(event.selectedSeason)
            is TrainingScreenScreenContract.Event.SetGroupName -> setGroupName(event.value)
            is TrainingScreenScreenContract.Event.CreateTrainingGroup -> createTrainingGroup()
            is TrainingScreenScreenContract.Event.NavigateToHome -> setSideEffect(TrainingScreenScreenContract.SideEffect.NavigateToHome)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        viewState.route.model.list.firstOrNull()?.let {
            setSelectedClub(it)
        }
        getSeasons()
        if (appBuildConfigStatePack.isDebug) {
            setGroupName("Grup Name ${Random.nextInt(0, 1000)}")
        }
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showChangeClubDialog = false,
                showSelectSeasonDialog = false,
            )
        }
    }

    private fun showChangeClubDialog() {
        updateState { state ->
            state.copy(
                showChangeClubDialog = true,
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

    private fun setSelectedClub(selectedClub: HomeScreenTrainingScreenNavigateModel) {
        updateState { state ->
            state.copy(
                selectedClub = selectedClub,
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
        filterSuggestions()
    }

    private fun filterSuggestions() {
        val names = viewState.getRecomendedGroupNames?.names?.filterNotNull() ?: listOf()
        val filteredNames = names.filter {
            it.contains(viewState.textGroupName.value, ignoreCase = true)
        }.take(20)
        updateState { state ->
            state.copy(
                suggestions = filteredNames,
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
                if (appBuildConfigStatePack.isDebug) {
                    setSelectedSeason(list.firstOrNull())
                }
                getRecommendedGroupNames()
            }
    }

    private fun getRecommendedGroupNames() {
        getRecommendedGroupNamesUseCase.invoke(viewState.route.model.clubId)
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

    private fun createTrainingGroup() = viewModelScope.launch {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return@launch
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
            return@launch
        }

        val params = AddTrainingGroupRequest(
            groupName = viewState.textGroupName.value,
            season = viewState.selectedSeason?.value,
            teamId = viewState.selectedClub?.clubId,
        )

        addTrainingGroupUseCase.invoke(params)
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
                    clubId = clubUIModel.teamId,
                    clubName = clubUIModel.teamName,
                    clubLogo = clubUIModel.logo,
                    groupId = clubUIModel.trainingGroupId,
                    groupName = clubUIModel.groupName,
                )
                setSideEffect(TrainingScreenScreenContract.SideEffect.NavigateToSuccessAddTrainingGroupScreen(model))
            }
    }
}

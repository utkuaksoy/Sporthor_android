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
package com.iamkurtgoz.feature.home.selectTeam

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.SaveUserTeamsRequestBody
import com.iamkurtgoz.feature.home.selectTeam.domain.model.TeamsUIItemModel
import com.iamkurtgoz.feature.home.selectTeam.domain.useCase.GetTeamsUseCase
import com.iamkurtgoz.feature.home.selectTeam.domain.useCase.SaveUserTeamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectTeamViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val saveUserTeamsUseCase: SaveUserTeamsUseCase,
) : CoreViewModel<SelectTeamScreenContract.State, SelectTeamScreenContract.SideEffect, SelectTeamScreenContract.Event>(
    initialState = SelectTeamScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: SelectTeamScreenContract.Event) {
        when (event) {
            is SelectTeamScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectTeamScreenContract.Event.NavigateUp -> setSideEffect(SelectTeamScreenContract.SideEffect.NavigateUp)
            is SelectTeamScreenContract.Event.PopBackStack -> setSideEffect(SelectTeamScreenContract.SideEffect.PopBackStack)
            is SelectTeamScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectTeamScreenContract.Event.SetTextSearch -> setTextSearch(event.text)
            is SelectTeamScreenContract.Event.SetSelectedTeam -> setSelectedTeam(event.team)
            is SelectTeamScreenContract.Event.SaveUserTeams -> saveUserTeams()
            is SelectTeamScreenContract.Event.CreateTeam -> setSideEffect(SelectTeamScreenContract.SideEffect.CreateTeam)
            is SelectTeamScreenContract.Event.NavigateToHome -> setSideEffect(SelectTeamScreenContract.SideEffect.NavigateToHome)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        getTeamsList()
        appPreferences.currentPreferenceState.firstOrNull()?.let { currentPreferenceState ->
            updateState { state ->
                state.copy(
                    customUserRole = currentPreferenceState.customUserRole,
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .map { it.textSearch.value }
            .onEach {
                updateState { state ->
                    state.copy(
                        filteredTeamsList = state.teamsList.filter { item -> item.isMatch(it) }.toPersistentList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getTeamsList() {
        getTeamsUseCase.invoke()
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
                        teamsList = it.teams?.filterNotNull()?.toPersistentList() ?: persistentListOf(),
                    )
                }
            }
    }

    private fun setTextSearch(text: String) {
        val textFieldValue = viewState.textSearch.copy(
            value = text,
        )
        updateState { state ->
            state.copy(
                textSearch = textFieldValue,
            )
        }
    }

    private fun setSelectedTeam(team: TeamsUIItemModel) {
        val selectedTeamsList = viewState.selectedTeamsList.toMutableList()
        if (viewState.route.fromTrainingGroup) {
            selectedTeamsList.clear()
            selectedTeamsList.add(team)
        } else {
            if (selectedTeamsList.contains(team)) {
                selectedTeamsList.remove(team)
            } else {
                selectedTeamsList.add(team)
            }
        }
        updateState { state ->
            state.copy(
                selectedTeamsList = selectedTeamsList,
            )
        }
    }

    private fun saveUserTeams() {
        val params = SaveUserTeamsRequestBody(
            teams = viewState.teamsList.map { it.value },
        )
        saveUserTeamsUseCase.invoke(params)
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
                appPreferences.setUserTeamsPageCompleted(isUserTeamsPageCompleted = true)
                if (viewState.route.fromGenerateClub) {
                    val club = viewState.selectedTeamsList.firstOrNull()
                    val model = HomeScreenSendClubAuthDocumentScreenNavigateModel(
                        clubId = club?.value,
                        founderUserId = null,
                        clubName = club?.name,
                        logo = club?.image,
                    )
                    setSideEffect(
                        SelectTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument(
                            model = model,
                            fromGenerateClub = viewState.route.fromGenerateClub,
                        ),
                    )
                } else {
                    val modelList = viewState.selectedTeamsList.map {
                        HomeScreenTrainingScreenNavigateModel(
                            clubId = it.value,
                            founderUserId = null,
                            clubName = it.name,
                            logo = it.image,
                        )
                    }
                    val firstModel = modelList.firstOrNull()?.copy(
                        list = modelList,
                    )
                    if (firstModel != null) {
                        setSideEffect(SelectTeamScreenContract.SideEffect.NavigateToTrainingScreen(firstModel))
                    }
                }
            }
    }
}

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
package com.iamkurtgoz.feature.home.selectSportClub

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.editTeam.HomeScreenEditTeamScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.selectSportClub.domain.useCase.GetSportClubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectSportClubViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getSportClubUseCase: GetSportClubUseCase,
) : CoreViewModel<SelectSportClubScreenContract.State, SelectSportClubScreenContract.SideEffect, SelectSportClubScreenContract.Event>(
    initialState = SelectSportClubScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: SelectSportClubScreenContract.Event) {
        when (event) {
            is SelectSportClubScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectSportClubScreenContract.Event.NavigateUp -> setSideEffect(SelectSportClubScreenContract.SideEffect.NavigateUp)
            is SelectSportClubScreenContract.Event.PopBackStack -> setSideEffect(SelectSportClubScreenContract.SideEffect.PopBackStack)
            is SelectSportClubScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectSportClubScreenContract.Event.NavigateToEditTeamScreen -> {
                val model = HomeScreenEditTeamScreenNavigationModel(
                    address = event.model.address,
                    city = event.model.city,
                    clubId = event.model.clubId,
                    clubName = event.model.clubName,
                    confirmationStatus = event.model.confirmationStatus,
                    county = event.model.county,
                    foundationYear = event.model.foundationYear,
                    logo = event.model.logo,
                    branchName = event.model.branch?.name,
                    value = event.model.branch?.value,
                    val2 = event.model.branch?.val2,
                )
                setSideEffect(SelectSportClubScreenContract.SideEffect.NavigateToEditTeamScreen(model))
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getSportClubs()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getSportClubs() {
        getSportClubUseCase.invoke()
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
                        list = response,
                    )
                }
            }
    }
}

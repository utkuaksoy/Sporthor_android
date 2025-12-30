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
package com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateUserRolesRequest
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.model.ConfigurationUserRoleUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.useCase.GetConfigurationUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.useCase.GetMyRolesUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.useCase.UpdateUserRolesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectUserRoleViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getMyRolesUseCase: GetMyRolesUseCase,
    private val appPreferences: AppPreferences,
    private val updateUserRolesUseCase: UpdateUserRolesUseCase,
) : CoreViewModel<SelectUserRoleScreenContract.State, SelectUserRoleScreenContract.SideEffect, SelectUserRoleScreenContract.Event>(
    initialState = SelectUserRoleScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: SelectUserRoleScreenContract.Event) {
        when (event) {
            is SelectUserRoleScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectUserRoleScreenContract.Event.NavigateUp -> setSideEffect(SelectUserRoleScreenContract.SideEffect.NavigateUp)
            is SelectUserRoleScreenContract.Event.PopBackStack -> setSideEffect(SelectUserRoleScreenContract.SideEffect.PopBackStack)
            is SelectUserRoleScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectUserRoleScreenContract.Event.SetSelectedUserRoleType -> setSelectedCustomizeUserRoleType(event.type)
            is SelectUserRoleScreenContract.Event.UpdateUserRole -> updateUserRole()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getConfiguration()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getConfiguration() {
        getConfigurationUseCase.invoke()
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
                        configurationUIModel = it,
                        userRoleTypeList = it.userRoles?.filterNotNull()?.toPersistentList(),
                    )
                }
                getMyRoles()
            }
    }

    private fun getMyRoles() {
        getMyRolesUseCase.invoke()
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
                        selectedUserRolesTypeList = state.userRoleTypeList?.filter { response.contains(it.value) } ?: emptyList(),
                    )
                }
            }
    }

    private fun setSelectedCustomizeUserRoleType(type: ConfigurationUserRoleUIModel) = viewModelScope.launch {
        val list = viewState.selectedUserRolesTypeList.toMutableList()
        if (list.any { it.uuid == type.uuid }) {
            list.removeAll { it.uuid == type.uuid }
        } else {
            list.add(type)
        }
        updateState { state ->
            state.copy(
                selectedUserRolesTypeList = list,
                configurationUIModel = state.configurationUIModel?.copy(
                    selectedUserRolesTypeList = list,
                ),
            )
        }

        // Save selected trainer status
        val isSelectedTrainer = viewState.selectedUserRolesTypeList.any { it.name == "Antranör" }
        val isSelectedClubOfficial = viewState.selectedUserRolesTypeList.any { it.name == "Kulüp Yetkilisi" }
        appPreferences.setSelectedTrainer(isSelectedTrainer)
        appPreferences.setSelectedClubOfficial(isSelectedClubOfficial)
    }

    private fun updateUserRole() {
        val updateUserRolesRequest = UpdateUserRolesRequest(
            roles = viewState.selectedUserRolesTypeList.map { it.value },
        )
        updateUserRolesUseCase.invoke(updateUserRolesRequest)
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
                setSideEffect(SelectUserRoleScreenContract.SideEffect.PopBackStack)
            }
    }
}

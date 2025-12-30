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
package com.iamkurtgoz.feature.home.customizeUserInfo

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.DateFormat
import com.iamkurtgoz.core.common.extensions.isDay
import com.iamkurtgoz.core.common.extensions.isMonth
import com.iamkurtgoz.core.common.extensions.isYear
import com.iamkurtgoz.core.common.extensions.toString
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.GenderType
import com.iamkurtgoz.domain.model.request.UpdateProfileRequest
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationBranchUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationCoachRoleUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationUserRoleUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.types.CustomizePageType
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.useCase.GetConfigurationUseCase
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.useCase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class CustomizeUserInfoViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : CoreViewModel<CustomizeUserInfoScreenContract.State, CustomizeUserInfoScreenContract.SideEffect, CustomizeUserInfoScreenContract.Event>(
    initialState = CustomizeUserInfoScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: CustomizeUserInfoScreenContract.Event) {
        when (event) {
            is CustomizeUserInfoScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CustomizeUserInfoScreenContract.Event.NavigateUp -> setSideEffect(CustomizeUserInfoScreenContract.SideEffect.NavigateUp)
            is CustomizeUserInfoScreenContract.Event.PopBackStack -> setSideEffect(CustomizeUserInfoScreenContract.SideEffect.PopBackStack)
            is CustomizeUserInfoScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CustomizeUserInfoScreenContract.Event.PreviousPage -> previousPage()
            is CustomizeUserInfoScreenContract.Event.NextPage -> nextPage()
            is CustomizeUserInfoScreenContract.Event.SetSelectedUserBranchType -> setSelectedCustomizeUserSportType(event.type)
            is CustomizeUserInfoScreenContract.Event.SetSelectedUserRoleType -> setSelectedCustomizeUserRoleType(event.type)
            is CustomizeUserInfoScreenContract.Event.SetSelectedCoachRoleType -> setSelectedCustomizeCoachRoleType(event.type)
            is CustomizeUserInfoScreenContract.Event.SetBirthdayDay -> setBirthdayDay(event.text)
            is CustomizeUserInfoScreenContract.Event.SetBirthdayMonth -> setBirthdayMonth(event.text)
            is CustomizeUserInfoScreenContract.Event.SetBirthdayYear -> setBirthdayYear(event.text)
            is CustomizeUserInfoScreenContract.Event.SetSelectedGenderType -> setSelectedGenderType(event.type)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
            setBirthdayDay("1")
            setBirthdayMonth("1")
            setBirthdayYear("2025")
        }
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
                        isLoading = false,
                        configurationModel = it,
                        currentPageIndex = it.pageList.first(),
                        userBranchesTypeList = it.branches?.filterNotNull()?.toPersistentList(),
                        userRoleTypeList = it.userRoles?.filterNotNull()?.toPersistentList(),
                        coachRoleTypeList = it.coachRoles?.filterNotNull()?.toPersistentList(),
                    )
                }
            }
    }

    private fun previousPage() {
        if (viewState.currentPageIndex == viewState.configurationModel?.pageList?.first()) {
            setSideEffect(CustomizeUserInfoScreenContract.SideEffect.NavigateUp)
        } else {
            val previousPage = when (viewState.currentPageIndex) {
                CustomizePageType.UserInfo -> {
                    if (viewState.selectedUserRolesTypeList.any { it.name == "Antranör" }) {
                        CustomizePageType.CoachRoleCategory
                    } else {
                        CustomizePageType.UserBranchPage
                    }
                }
                CustomizePageType.CoachRoleCategory -> CustomizePageType.UserBranchPage
                CustomizePageType.UserBranchPage -> CustomizePageType.UserRoleCategory
                CustomizePageType.UserRoleCategory -> CustomizePageType.Welcome
                else -> null
            }

            updateState { state ->
                state.copy(
                    currentPageIndex = previousPage ?: CustomizePageType.Welcome,
                )
            }
        }
    }

    private fun nextPage() {
        if (viewState.currentPageIndex == CustomizePageType.UserInfo) {
            saveCustomizeUserInfo()
        } else {
            val nextPage = when (viewState.currentPageIndex) {
                CustomizePageType.Welcome -> CustomizePageType.UserRoleCategory
                CustomizePageType.UserRoleCategory -> CustomizePageType.UserBranchPage
                CustomizePageType.UserBranchPage -> {
                    if (viewState.selectedUserRolesTypeList.any { it.name == "Antranör" }) {
                        CustomizePageType.CoachRoleCategory
                    } else {
                        CustomizePageType.UserInfo
                    }
                }
                CustomizePageType.CoachRoleCategory -> CustomizePageType.UserInfo
                else -> null
            }

            updateState { state ->
                state.copy(
                    currentPageIndex = nextPage ?: CustomizePageType.Welcome,
                )
            }
        }
    }

    private fun setSelectedCustomizeUserSportType(type: ConfigurationBranchUIModel) {
        val list = viewState.selectedUserBranchesTypeList.toMutableList()
        if (list.any { it.uuid == type.uuid }) {
            list.removeAll { it.uuid == type.uuid }
        } else {
            list.add(type)
        }
        updateState { state ->
            state.copy(
                selectedUserBranchesTypeList = list,
            )
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
                configurationModel = state.configurationModel?.copy(
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

    private fun setSelectedCustomizeCoachRoleType(type: ConfigurationCoachRoleUIModel) = viewModelScope.launch {
        val list = viewState.selectedCoachRolesTypeList.toMutableList()
        if (list.any { it.uuid == type.uuid }) {
            list.removeAll { it.uuid == type.uuid }
        } else {
            list.add(type)
        }
        updateState { state ->
            state.copy(
                selectedCoachRolesTypeList = list,
            )
        }
    }

    private fun setBirthdayDay(text: String) {
        val textFieldValue = viewState.textBirthdayDay.copy(
            value = text,
            isError = !text.isDay(),
        )
        updateState { state ->
            state.copy(
                textBirthdayDay = textFieldValue,
            )
        }
    }

    private fun setBirthdayMonth(text: String) {
        val textFieldValue = viewState.textBirthdayMonth.copy(
            value = text,
            isError = !text.isMonth(),
        )
        updateState { state ->
            state.copy(
                textBirthdayMonth = textFieldValue,
            )
        }
    }

    private fun setBirthdayYear(text: String) {
        val textFieldValue = viewState.textBirthdayYear.copy(
            value = text,
            isError = !text.isYear(),
        )
        updateState { state ->
            state.copy(
                textBirthdayYear = textFieldValue,
            )
        }
    }

    private fun setSelectedGenderType(type: GenderType) {
        updateState { state ->
            state.copy(
                selectedGenderType = type,
            )
        }
    }

    private fun saveCustomizeUserInfo() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }

        val year = viewState.textBirthdayYear.value.toIntOrNull() ?: return
        val month = viewState.textBirthdayMonth.value.toIntOrNull() ?: return
        val day = viewState.textBirthdayDay.value.toIntOrNull() ?: return
        val birthDate = LocalDate.of(year, month, day)
        val params = UpdateProfileRequest(
            birthDate = birthDate.toString(format = DateFormat.YYYY_MM_DD),
            gender = viewState.selectedGenderType.value,
            branchesOfInterests = viewState.selectedUserBranchesTypeList.map { it.value },
            userRoles = viewState.selectedUserRolesTypeList.map { it.value },
            coachRoles = viewState.selectedCoachRolesTypeList.map { it.value },
        )
        updateProfileUseCase.invoke(params)
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
                appPreferences.setUserInfoPageCompleted(isUserInfoPageCompleted = true)
                setSideEffect(CustomizeUserInfoScreenContract.SideEffect.NavigateToOnboarding)
            }
    }
}

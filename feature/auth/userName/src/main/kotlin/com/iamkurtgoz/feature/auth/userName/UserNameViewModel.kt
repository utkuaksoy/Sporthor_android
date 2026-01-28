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
package com.iamkurtgoz.feature.auth.userName

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.isUserNameValid
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.auth.userName.toAuthUserNameScreenRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.CheckUserNameRequest
import com.iamkurtgoz.domain.model.request.RegisterRequest
import com.iamkurtgoz.domain.model.request.UpdateConfigurationRequest
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import com.iamkurtgoz.domain.repository.ProfileRepository
import com.iamkurtgoz.feature.auth.userName.domain.useCase.CheckUserNameUseCase
import com.iamkurtgoz.feature.auth.userName.domain.useCase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserNameViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val notificationSettingsManager: INotificationSettingsManager,
    private val appPreferences: AppPreferences,
    private val profileRepository: ProfileRepository,
    private val checkUserNameUseCase: CheckUserNameUseCase,
    private val registerUseCase: RegisterUseCase,
) : CoreViewModel<UserNameScreenContract.State, UserNameScreenContract.SideEffect, UserNameScreenContract.Event>(
    initialState = UserNameScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toAuthUserNameScreenRoute(),
    ),
) {
    private var checkUserNameJob: Job? = null

    private fun generateRandomUserName(
        usernameLength: Int = 4,
    ): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
        val username = buildString {
            repeat(usernameLength) {
                append(chars.random())
            }
        }
        return username
    }

    init {
        if (appBuildConfigStatePack.isDebug) {
            setUserName("iamkurtgoz" + generateRandomUserName())
        }
    }

    override fun setEvent(event: UserNameScreenContract.Event) {
        when (event) {
            is UserNameScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is UserNameScreenContract.Event.NavigateUp -> setSideEffect(UserNameScreenContract.SideEffect.NavigateUp)
            is UserNameScreenContract.Event.PopBackStack -> setSideEffect(UserNameScreenContract.SideEffect.PopBackStack)
            is UserNameScreenContract.Event.DismissDialogs -> dismissDialogs()
            is UserNameScreenContract.Event.SetUserName -> setUserName(event.text)
            is UserNameScreenContract.Event.SetUserNameFromSuggestion -> setUserNameFromSuggestion(event.text)
            is UserNameScreenContract.Event.SaveUserName -> saveUserName()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setUserName(text: String) {
        val textFieldValue = viewState.textUserName.copy(
            value = text,
            isError = !text.isUserNameValid(),
        )
        updateState { state ->
            state.copy(
                isUserNameChecking = true,
                textUserName = textFieldValue,
                isFieldErrorShow = text.length >= UserNameScreenContract.Static.MIN_USER_NAME_LENGTH,
                backendErrorMessage = if (text.length >= UserNameScreenContract.Static.MIN_USER_NAME_LENGTH) state.backendErrorMessage else null,
            )
        }
    }

    private fun setUserNameFromSuggestion(text: String) {
        setSideEffect(UserNameScreenContract.SideEffect.ClearFocus)
        val textFieldValue = viewState.textUserName.copy(
            value = text,
            isError = false,
        )
        updateState { state ->
            state.copy(
                textUserName = textFieldValue,
                isFieldErrorShow = false,
                backendErrorMessage = null,
            )
        }
    }

    private fun saveUserName() = viewModelScope.launch {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return@launch
        }

        val params = RegisterRequest(
            username = viewState.textUserName.value,
            firebaseId = "",
            firebaseToken = notificationSettingsManager.getRegisterFcmToken(),
            email = viewState.navigateRoute.model.email,
            mobilePhone = viewState.navigateRoute.model.phoneNumber,
            password = viewState.navigateRoute.model.password,
            name = viewState.navigateRoute.model.firstName,
            surname = viewState.navigateRoute.model.lastName,
            socialInfo = viewState.navigateRoute.model.registerSocialInfoRequest,
        )
        registerUseCase.invoke(params)
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
                refreshFirebaseToken()
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                setSideEffect(UserNameScreenContract.SideEffect.NavigateToHome)
            }
    }

    private fun refreshFirebaseToken() {
        viewModelScope.launch {
            val token = notificationSettingsManager.getRegisterFcmToken()
            if (!token.isNullOrBlank()) {
                appPreferences.setFirebaseToken(firebaseToken = token)
                profileRepository.updateConfiguration(UpdateConfigurationRequest(firebaseToken = token))
            }
        }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textUserName.value }
            .debounce(UserNameScreenContract.Static.USER_NAME_DEBOUNCE)
            .map { it.textUserName.value }
            .filter { it.length >= UserNameScreenContract.Static.MIN_USER_NAME_LENGTH }
            .onEach(::checkUserNameRequest)
            .launchIn(viewModelScope)
    }

    private fun checkUserNameRequest(userName: String) {
        if (checkUserNameJob != null) {
            checkUserNameJob?.cancel()
            checkUserNameJob = null
        }

        val params = CheckUserNameRequest(
            username = userName,
        )
        checkUserNameJob = checkUserNameUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isUserNameChecking = true,
                    )
                }
            }
            .onError {
                val textFieldValue = viewState.textUserName.copy(
                    isError = true,
                )
                updateState { state ->
                    state.copy(
                        isUserNameChecking = false,
                        isFieldErrorShow = true,
                        textUserName = textFieldValue,
                        backendErrorMessage = it.errorMessage,
                    )
                }
            }
            .callWithSuccess {
                val textFieldValue = viewState.textUserName.copy(
                    isError = it.isUsable == false,
                )
                updateState { state ->
                    state.copy(
                        isUserNameChecking = false,
                        isFieldErrorShow = true,
                        textUserName = textFieldValue,
                        checkUserNameModel = it,
                        backendErrorMessage = null,
                    )
                }
            }
    }
}

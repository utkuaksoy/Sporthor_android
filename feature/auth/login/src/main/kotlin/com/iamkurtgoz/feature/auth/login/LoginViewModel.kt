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
package com.iamkurtgoz.feature.auth.login

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.LoginWithUserNameRequest
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import com.iamkurtgoz.feature.auth.login.domain.useCase.LoginWithUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val loginWithEmailUseCase: LoginWithUserNameUseCase,
    private val notificationSettingsManager: INotificationSettingsManager,
    private val appPreferences: AppPreferences,
) : CoreViewModel<LoginScreenContract.State, LoginScreenContract.SideEffect, LoginScreenContract.Event>(
    initialState = LoginScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: LoginScreenContract.Event) {
        when (event) {
            is LoginScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is LoginScreenContract.Event.NavigateUp -> setSideEffect(LoginScreenContract.SideEffect.NavigateUp)
            is LoginScreenContract.Event.PopBackStack -> setSideEffect(LoginScreenContract.SideEffect.PopBackStack)
            is LoginScreenContract.Event.DismissDialogs -> dismissDialogs()
            is LoginScreenContract.Event.NavigateToRegister -> setSideEffect(LoginScreenContract.SideEffect.NavigateToRegister)
            is LoginScreenContract.Event.NavigateToHome -> setSideEffect(LoginScreenContract.SideEffect.NavigateToHome)
            is LoginScreenContract.Event.NavigateToForgotPassword -> setSideEffect(LoginScreenContract.SideEffect.NavigateToForgotPassword)
            is LoginScreenContract.Event.SetUserName -> setUserName(event.text)
            is LoginScreenContract.Event.SetPassword -> setPassword(event.text)
            is LoginScreenContract.Event.ChangePasswordVisualTransformation -> changePasswordVisualTransformation()
            is LoginScreenContract.Event.Login -> login()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
//            setUserName("derturke3")
//            setPassword("123456Dt.")
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setUserName(text: String) {
        val userName = viewState.textUserName.copy(
            value = text,
        )
        updateState { state ->
            state.copy(
                textUserName = userName,
            )
        }
    }

    private fun setPassword(text: String) {
        val password = viewState.textPassword.copy(
            value = text,
        )
        updateState { state ->
            state.copy(
                textPassword = password,
            )
        }
    }

    private fun changePasswordVisualTransformation() {
        val visualTransformation = if (viewState.textPassword.visualTransformation == VisualTransformation.None) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
        val textFieldValue = viewState.textPassword.copy(
            visualTransformation = visualTransformation,
        )
        updateState { state ->
            state.copy(
                textPassword = textFieldValue,
            )
        }
    }

    private fun login() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }

        val params = LoginWithUserNameRequest(
            userName = viewState.textUserName.value,
            password = viewState.textPassword.value,
        )
        loginWithEmailUseCase.invoke(params)
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
                setSideEffect(LoginScreenContract.SideEffect.NavigateToHome)
            }
    }

    private fun refreshFirebaseToken() {
        viewModelScope.launch {
            val token = notificationSettingsManager.getRegisterFcmToken()
            if (!token.isNullOrBlank()) {
                appPreferences.setFirebaseToken(firebaseToken = token)
            }
        }
    }
}

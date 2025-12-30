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
package com.iamkurtgoz.feature.auth.loginWithEmail

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.isEmail
import com.iamkurtgoz.core.common.extensions.isPasswordValid
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.LoginWithEmailRequest
import com.iamkurtgoz.feature.auth.loginWithEmail.domain.useCase.LoginWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginWithEmailViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : CoreViewModel<LoginWithEmailScreenContract.State, LoginWithEmailScreenContract.SideEffect, LoginWithEmailScreenContract.Event>(
    initialState = LoginWithEmailScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: LoginWithEmailScreenContract.Event) {
        when (event) {
            is LoginWithEmailScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is LoginWithEmailScreenContract.Event.NavigateUp -> setSideEffect(LoginWithEmailScreenContract.SideEffect.NavigateUp)
            is LoginWithEmailScreenContract.Event.PopBackStack -> setSideEffect(LoginWithEmailScreenContract.SideEffect.PopBackStack)
            is LoginWithEmailScreenContract.Event.NavigateToForgotPassword -> setSideEffect(LoginWithEmailScreenContract.SideEffect.NavigateToForgotPassword)
            is LoginWithEmailScreenContract.Event.DismissDialogs -> dismissDialogs()
            is LoginWithEmailScreenContract.Event.SetEmailAddress -> setEmailAddress(event.text)
            is LoginWithEmailScreenContract.Event.ChangePasswordVisualTransformation -> changePasswordVisualTransformation()
            is LoginWithEmailScreenContract.Event.SetPassword -> setPassword(event.text)
            is LoginWithEmailScreenContract.Event.Login -> login()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
            setEmailAddress("kurtgozmehmet159@gmail.com")
            setPassword("AaAaAa123")
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
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

    private fun setEmailAddress(text: String) {
        val textFieldValue = viewState.textEmailAddress.copy(
            value = text,
            isError = !text.isEmail(),
        )
        updateState { state ->
            state.copy(
                textEmailAddress = textFieldValue,
            )
        }
    }

    private fun setPassword(text: String) {
        val textFieldValue = viewState.textPassword.copy(
            value = text,
            isError = !text.isPasswordValid(),
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

        val params = LoginWithEmailRequest(
            email = viewState.textEmailAddress.value,
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
                setSideEffect(LoginWithEmailScreenContract.SideEffect.NavigateToHome)
            }
    }
}

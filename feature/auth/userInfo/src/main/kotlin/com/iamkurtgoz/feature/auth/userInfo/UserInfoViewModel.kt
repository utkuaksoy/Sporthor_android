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
package com.iamkurtgoz.feature.auth.userInfo

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.isEmail
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.common.extensions.isPasswordValid
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.auth.userInfo.toAuthUserInfoScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userName.AuthUserNameScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserInfoViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<UserInfoScreenContract.State, UserInfoScreenContract.SideEffect, UserInfoScreenContract.Event>(
    initialState = UserInfoScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toAuthUserInfoScreenRoute(),
    ),
) {
    override fun setEvent(event: UserInfoScreenContract.Event) {
        when (event) {
            is UserInfoScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is UserInfoScreenContract.Event.NavigateUp -> setSideEffect(UserInfoScreenContract.SideEffect.NavigateUp)
            is UserInfoScreenContract.Event.PopBackStack -> setSideEffect(UserInfoScreenContract.SideEffect.PopBackStack)
            is UserInfoScreenContract.Event.DismissDialogs -> dismissDialogs()
            is UserInfoScreenContract.Event.SetFirstName -> setFirstName(event.text)
            is UserInfoScreenContract.Event.SetLastName -> setLastName(event.text)
            is UserInfoScreenContract.Event.SetEmailAddress -> setEmailAddress(event.text)
            is UserInfoScreenContract.Event.SetPassword -> setPassword(event.text)
            is UserInfoScreenContract.Event.ChangePasswordVisualTransformation -> changePasswordVisualTransformation()
            is UserInfoScreenContract.Event.SaveUserInfo -> saveUserInfo()
        }
    }

    private fun generateRandomEmail(
        mailLength: Int = 10,
    ): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val mail = buildString {
            repeat(mailLength) {
                append(chars.random())
            }
        }
        return "$mail@gmail.com"
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
            setFirstName("Mehmet")
            setLastName("Kurtgöz")
            setEmailAddress(generateRandomEmail())
            setPassword("AaAaAa123")
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setFirstName(text: String) {
        val textFieldValue = viewState.textFirstName.copy(
            value = text,
            isError = text.isEmpty() || text.isNumber(),
        )
        updateState { state ->
            state.copy(
                textFirstName = textFieldValue,
            )
        }
    }

    private fun setLastName(text: String) {
        val textFieldValue = viewState.textLastName.copy(
            value = text,
            isError = text.isEmpty() || text.isNumber(),
        )
        updateState { state ->
            state.copy(
                textLastName = textFieldValue,
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

    private fun saveUserInfo() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }

        val model = AuthUserNameScreenNavigateModel(
            phoneNumber = viewState.navigateRoute.model.phoneNumber,
            firstName = viewState.textFirstName.value,
            lastName = viewState.textLastName.value,
            email = viewState.textEmailAddress.value,
            password = viewState.textPassword.value,
            registerSocialInfoRequest = viewState.navigateRoute.model.registerSocialInfoRequest,
        )
        setSideEffect(UserInfoScreenContract.SideEffect.NavigateToUserName(model))
    }
}

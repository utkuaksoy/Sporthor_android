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
package com.iamkurtgoz.feature.auth.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.isTurkishMobileNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenFromPage
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.register.toAuthRegisterScreenRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RegisterViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<RegisterScreenContract.State, RegisterScreenContract.SideEffect, RegisterScreenContract.Event>(
    initialState = RegisterScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toAuthRegisterScreenRoute(),
    ),
) {
    override fun setEvent(event: RegisterScreenContract.Event) {
        when (event) {
            is RegisterScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is RegisterScreenContract.Event.NavigateUp -> setSideEffect(RegisterScreenContract.SideEffect.NavigateUp)
            is RegisterScreenContract.Event.PopBackStack -> setSideEffect(RegisterScreenContract.SideEffect.PopBackStack)
            is RegisterScreenContract.Event.DismissDialogs -> dismissDialogs()
            is RegisterScreenContract.Event.NavigateToLogin -> setSideEffect(RegisterScreenContract.SideEffect.NavigateToLogin)
            is RegisterScreenContract.Event.SetTextPhoneNumber -> setTextPhoneNumber(event.text)
            is RegisterScreenContract.Event.SendOtpCode -> sendOtpCode()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
            updateState { state ->
                state.copy(
                    textPhoneNumber = AppTextFieldValue(),
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setTextPhoneNumber(text: String) {
        val textPhoneNumber = viewState.textPhoneNumber.copy(
            value = text,
            isError = !text.isTurkishMobileNumber(),
        )
        updateState { state ->
            state.copy(
                textPhoneNumber = textPhoneNumber,
            )
        }
    }

    private fun sendOtpCode() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }

        setSideEffect(RegisterScreenContract.SideEffect.HideKeyboard)
        setSideEffect(RegisterScreenContract.SideEffect.ClearTextFieldFocus)

        val model = AuthOtpScreenNavigateModel(
            fromPage = AuthOtpScreenFromPage.Register,
            phoneNumber = viewState.textPhoneNumber.value,
            registerSocialInfoRequest = viewState.navigateRoute.model.registerSocialInfoRequest,
        )
        setSideEffect(RegisterScreenContract.SideEffect.NavigateToOtp(model))
    }
}

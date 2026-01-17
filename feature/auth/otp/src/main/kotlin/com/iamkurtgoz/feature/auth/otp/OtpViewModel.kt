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
package com.iamkurtgoz.feature.auth.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenFromPage
import com.iamkurtgoz.core.navigation.model.auth.otp.toAuthOtpScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userInfo.AuthUserInfoScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.GenerateOtpRequest
import com.iamkurtgoz.domain.model.request.LoginWithPhoneRequest
import com.iamkurtgoz.domain.model.request.ValidateOtpRequest
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import com.iamkurtgoz.feature.auth.otp.domain.useCase.GenerateOtpUseCase
import com.iamkurtgoz.feature.auth.otp.domain.useCase.LoginWithPhoneUseCase
import com.iamkurtgoz.feature.auth.otp.domain.useCase.ValidateOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OtpViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val generateOtpUseCase: GenerateOtpUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val loginWithPhoneUseCase: LoginWithPhoneUseCase,
    private val notificationSettingsManager: INotificationSettingsManager,
    private val appPreferences: AppPreferences,
) : CoreViewModel<OtpScreenContract.State, OtpScreenContract.SideEffect, OtpScreenContract.Event>(
    initialState = OtpScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toAuthOtpScreenRoute(),
    ),
) {
    private var timerJob: Job? = null

    override fun setEvent(event: OtpScreenContract.Event) {
        when (event) {
            is OtpScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is OtpScreenContract.Event.NavigateUp -> setSideEffect(OtpScreenContract.SideEffect.NavigateUp)
            is OtpScreenContract.Event.PopBackStack -> setSideEffect(OtpScreenContract.SideEffect.PopBackStack)
            is OtpScreenContract.Event.DismissDialogs -> dismissDialogs()
            is OtpScreenContract.Event.SetOtpValue -> setOtpValue(event.text)
            is OtpScreenContract.Event.ValidateNumber -> validateNumber()
            is OtpScreenContract.Event.ReSendOtpCode -> reSendOtpCode()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        generateOtpCode()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setOtpValue(value: String) {
        val textFieldValue = viewState.textOtpValue.copy(
            value = value,
            isError = !value.isNumber() || value.length != AppDefaults.SIX,
        )
        updateState { state ->
            state.copy(
                textOtpValue = textFieldValue,
            )
        }
    }

    private fun validateNumber() {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return
        }

        setSideEffect(OtpScreenContract.SideEffect.HideKeyboard)
        setSideEffect(OtpScreenContract.SideEffect.ClearTextFieldFocus)

        if (viewState.navigateRoute.model.fromPage == AuthOtpScreenFromPage.Register) {
            validateOtpCode()
        } else if (viewState.navigateRoute.model.fromPage == AuthOtpScreenFromPage.Login) {
            loginWithPhone()
        }
    }

    private fun validateOtpCode() {
        val params = ValidateOtpRequest(
            mobilePhone = OtpScreenContract.Static.DEFAULT_TR_COUNTRY_CODE + viewState.navigateRoute.model.phoneNumber,
            otpCode = viewState.textOtpValue.value,
        )
        validateOtpUseCase.invoke(params)
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
                stopTimer()
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        remainingTime = OtpScreenContract.Static.MIN_REMAINING_TIME,
                    )
                }
                val model = AuthUserInfoScreenNavigateModel(
                    phoneNumber = viewState.navigateRoute.model.phoneNumber,
                    registerSocialInfoRequest = viewState.navigateRoute.model.registerSocialInfoRequest,
                )
                setSideEffect(OtpScreenContract.SideEffect.NavigateToUserInfo(model))
            }
    }

    private fun loginWithPhone() {
        val params = LoginWithPhoneRequest(
            mobilePhone = OtpScreenContract.Static.DEFAULT_TR_COUNTRY_CODE + viewState.navigateRoute.model.phoneNumber,
            otpCode = viewState.textOtpValue.value,
        )
        loginWithPhoneUseCase.invoke(params)
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
                setSideEffect(OtpScreenContract.SideEffect.NavigateToHome)
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

    private fun reSendOtpCode() {
        generateOtpCode()
    }

    private fun generateOtpCode() {
        val body = GenerateOtpRequest(
            mobilePhone = OtpScreenContract.Static.DEFAULT_TR_COUNTRY_CODE + viewState.navigateRoute.model.phoneNumber,
        )
        generateOtpUseCase.invoke(body)
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
                        remainingTime = OtpScreenContract.Static.DEFAULT_REMAINING_TIME,
                    )
                }
                startTimer()
            }
    }

    private fun startTimer() = viewModelScope.launch {
        var remainingTime = OtpScreenContract.Static.DEFAULT_REMAINING_TIME
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerJob != null) {
                delay(OtpScreenContract.Static.DELAY_ONE_SECOND)
                remainingTime = remainingTime.minus(AppDefaults.ONE)
                if (remainingTime <= AppDefaults.ZERO) {
                    stopTimer()
                    remainingTime = AppDefaults.ZERO.toFloat()
                }
                updateState { state ->
                    state.copy(
                        remainingTime = remainingTime,
                    )
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    public override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}

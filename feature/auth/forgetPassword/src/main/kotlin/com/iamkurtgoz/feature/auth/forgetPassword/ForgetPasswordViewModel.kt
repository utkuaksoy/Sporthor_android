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
package com.iamkurtgoz.feature.auth.forgetPassword

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.isEmail
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.SuccessGenericAlertDialogModel
import com.iamkurtgoz.domain.model.request.ForgotPasswordRequest
import com.iamkurtgoz.feature.auth.forgetPassword.domain.useCase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.iamkurtgoz.core.resources.R as resourcesR

@HiltViewModel
internal class ForgetPasswordViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : CoreViewModel<ForgetPasswordScreenContract.State, ForgetPasswordScreenContract.SideEffect, ForgetPasswordScreenContract.Event>(
    initialState = ForgetPasswordScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: ForgetPasswordScreenContract.Event) {
        when (event) {
            is ForgetPasswordScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ForgetPasswordScreenContract.Event.NavigateUp -> setSideEffect(ForgetPasswordScreenContract.SideEffect.NavigateUp)
            is ForgetPasswordScreenContract.Event.PopBackStack -> setSideEffect(ForgetPasswordScreenContract.SideEffect.PopBackStack)
            is ForgetPasswordScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ForgetPasswordScreenContract.Event.SetEmailAddress -> setEmailAddress(event.text)
            is ForgetPasswordScreenContract.Event.SetForgotPasswordType -> setForgotPasswordType(event.type)
            is ForgetPasswordScreenContract.Event.SendResetPasswordMail -> sendResetPassword()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (appBuildConfigStatePack.isDebug) {
            updateState { state ->
                state.copy(
                    textUserName = AppTextFieldValue(
                        value = "cllkrc",
                    ),
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                successSentForgetPasswordAlertDialogModel = null,
            )
        }
    }

    private fun setEmailAddress(text: String) {
        val textFieldValue = viewState.textUserName.copy(
            value = text,
            isError = !text.isEmail(),
        )
        updateState { state ->
            state.copy(
                textUserName = textFieldValue,
            )
        }
    }

    private fun setForgotPasswordType(type: Int) {
        updateState { state ->
            state.copy(
                forgotPasswordType = type,
            )
        }
    }

    private fun sendResetPassword() {
        if (viewState.isFieldsAnyError && viewState.forgotPasswordType < AppDefaults.ZERO) {
            return
        }
        val params = ForgotPasswordRequest(
            username = viewState.textUserName.value,
            type = viewState.forgotPasswordType,
        )
        forgotPasswordUseCase.invoke(params)
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
                        successSentForgetPasswordAlertDialogModel = SuccessGenericAlertDialogModel(
                            title = if (params.type == AppDefaults.ZERO) resourcesR.string.alert_label_reset_password_mail_sent_title else "Sıfırlama linki sms olarak gönderildi",
                            message = if (params.type == AppDefaults.ZERO) resourcesR.string.alert_label_reset_password_mail_sent_sub_title else "Sıfırlama işlemini tamamlamak için size gönderilen e-postadaki linke tıklayın.",
                            buttonContent = resourcesR.string.button_ok_button,
                        ),
                    )
                }
            }
    }
}

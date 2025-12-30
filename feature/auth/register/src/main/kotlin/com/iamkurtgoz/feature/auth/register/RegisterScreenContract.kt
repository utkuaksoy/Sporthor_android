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

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.AuthRegisterScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class RegisterScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val navigateRoute: AuthRegisterScreenRoute,
        val textPhoneNumber: AppTextFieldValue = AppTextFieldValue(),
        val isFieldErrorShow: Boolean = false,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textPhoneNumber.isError
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToOtp(val model: AuthOtpScreenNavigateModel) : SideEffect()
        data object NavigateToLogin : SideEffect()
        data object HideKeyboard : SideEffect()
        data object ClearTextFieldFocus : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object NavigateToLogin : Event()
        data class SetTextPhoneNumber(val text: String) : Event()
        data object SendOtpCode : Event()
    }

    object Static {
        const val COUNTRY_CODE_TR: String = "+90"
        const val PHONE_NUMBER_LIMIT: Long = 10
    }
}

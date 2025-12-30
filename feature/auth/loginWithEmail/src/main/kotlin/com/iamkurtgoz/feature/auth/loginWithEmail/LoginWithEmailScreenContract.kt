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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class LoginWithEmailScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val textEmailAddress: AppTextFieldValue = AppTextFieldValue(),
        val textPassword: AppTextFieldValue = AppTextFieldValue(
            visualTransformation = PasswordVisualTransformation(),
        ),
        val isFieldErrorShow: Boolean = false,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textEmailAddress.isError || textPassword.isError

        val buttonActive: Boolean
            get() = textEmailAddress.value.isNotEmpty() && textPassword.value.isNotEmpty()
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToForgotPassword : SideEffect()
        data object NavigateToHome : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object NavigateToForgotPassword : Event()
        data object DismissDialogs : Event()
        data class SetEmailAddress(val text: String) : Event()
        data class SetPassword(val text: String) : Event()
        data object ChangePasswordVisualTransformation : Event()
        data object Login : Event()
    }

    object Static
}

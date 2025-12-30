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

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.extensions.isUserNameValid
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.AuthUserNameScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.auth.userName.domain.model.CheckUserNameUIModel

internal class UserNameScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val navigateRoute: AuthUserNameScreenRoute,
        val textUserName: AppTextFieldValue = AppTextFieldValue(),
        val isFieldErrorShow: Boolean = false,
        val checkUserNameModel: CheckUserNameUIModel? = null,
        val backendErrorMessage: String? = null,
        val isUserNameChecking: Boolean = false,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textUserName.isError

        val buttonActive: Boolean
            get() = !isUserNameChecking && textUserName.value.isUserNameValid()
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
        data object ClearFocus : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetUserName(val text: String) : Event()
        data class SetUserNameFromSuggestion(val text: String) : Event()
        data object SaveUserName : Event()
    }

    object Static {
        const val USER_NAME_DEBOUNCE: Long = 500
        const val MIN_USER_NAME_LENGTH: Int = 4
        const val MAX_USER_NAME_LENGTH: Int = 15
    }
}

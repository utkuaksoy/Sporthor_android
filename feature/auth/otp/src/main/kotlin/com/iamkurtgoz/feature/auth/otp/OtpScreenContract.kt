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

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.AuthOtpScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenFromPage
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.userInfo.AuthUserInfoScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class OtpScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val navigateRoute: AuthOtpScreenRoute,
        val textOtpValue: AppTextFieldValue = AppTextFieldValue(),
        val isFieldErrorShow: Boolean = false,
        val remainingTime: Float = Static.MIN_REMAINING_TIME,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textOtpValue.isError

        val timerIsActive: Boolean
            get() = remainingTime > 0
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToUserInfo(val model: AuthUserInfoScreenNavigateModel) : SideEffect()
        data object NavigateToHome : SideEffect()
        data object HideKeyboard : SideEffect()
        data object ClearTextFieldFocus : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetOtpValue(val text: String) : Event()
        data object ValidateNumber : Event()
        data object ReSendOtpCode : Event()
    }

    object Static {
        const val DEFAULT_TR_COUNTRY_CODE: String = "+90"
        const val MIN_REMAINING_TIME: Float = 0f
        const val DEFAULT_REMAINING_TIME: Float = 180f
        const val DEFAULT_WARNING_THRESHOLD_TIME: Float = 60f
        const val DELAY_ONE_SECOND = 1000L
    }

    object Fake {
        val fakeNavigateRoute: AuthOtpScreenRoute = AuthOtpScreenRoute(
            model = AuthOtpScreenNavigateModel(
                fromPage = AuthOtpScreenFromPage.Register,
                phoneNumber = "",
                registerSocialInfoRequest = null,
            ),
        )
    }
}

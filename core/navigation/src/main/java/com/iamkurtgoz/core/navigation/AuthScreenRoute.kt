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
package com.iamkurtgoz.core.navigation

import androidx.annotation.Keep
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.register.AuthRegisterScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.userInfo.AuthUserInfoScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.userName.AuthUserNameScreenNavigateModel
import kotlinx.serialization.Serializable

@Keep
@Serializable
data object AuthScreenRoute

@Keep
@Serializable
data object AuthWelcomeScreenRoute

@Keep
@Serializable
data class AuthRegisterScreenRoute(
    val model: AuthRegisterScreenNavigateModel,
)

@Keep
@Serializable
data object AuthLoginScreenRoute

@Keep
@Serializable
data object AuthLoginWithEmailScreenRoute

@Keep
@Serializable
data class AuthOtpScreenRoute(
    val model: AuthOtpScreenNavigateModel,
)

@Keep
@Serializable
data class AuthUserInfoScreenRoute(
    val model: AuthUserInfoScreenNavigateModel,
)

@Keep
@Serializable
data class AuthUserNameScreenRoute(
    val model: AuthUserNameScreenNavigateModel,
)

@Keep
@Serializable
data object AuthForgetPasswordScreenRoute

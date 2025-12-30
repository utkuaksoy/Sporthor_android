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
package com.iamkurtgoz.feature.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.AuthWelcomeScreenRoute
import com.iamkurtgoz.core.navigation.route.AuthNavRoute
import com.iamkurtgoz.feature.auth.forgetPassword.navigation.forgetPasswordScreenNavigation
import com.iamkurtgoz.feature.auth.forgetPassword.navigation.navigateToForgetPasswordScreen
import com.iamkurtgoz.feature.auth.login.navigation.loginScreenNavigation
import com.iamkurtgoz.feature.auth.login.navigation.navigateToLoginScreen
import com.iamkurtgoz.feature.auth.loginWithEmail.navigation.loginWithEmailScreenNavigation
import com.iamkurtgoz.feature.auth.otp.navigation.navigateToOtpScreen
import com.iamkurtgoz.feature.auth.otp.navigation.otpScreenNavigation
import com.iamkurtgoz.feature.auth.register.navigation.navigateToRegisterScreen
import com.iamkurtgoz.feature.auth.register.navigation.registerScreenNavigation
import com.iamkurtgoz.feature.auth.userInfo.navigation.navigateToUserInfoScreen
import com.iamkurtgoz.feature.auth.userInfo.navigation.userInfoScreenNavigation
import com.iamkurtgoz.feature.auth.userName.navigation.navigateToUserNameScreen
import com.iamkurtgoz.feature.auth.userName.navigation.userNameScreenNavigation
import com.iamkurtgoz.feature.auth.welcome.navigation.welcomeScreenNavigation

@Composable
internal fun AuthScreenContent(
    authNavController: NavHostController,
    setEvent: (AuthScreenContract.Event) -> Unit,
) {
    NavHost(
        navController = authNavController,
        route = AuthNavRoute::class,
        startDestination = AuthWelcomeScreenRoute::class,
        builder = {
            welcomeScreenNavigation(
                navigateToLogin = authNavController::navigateToLoginScreen,
                navigateToRegister = authNavController::navigateToRegisterScreen,
                navigateToHome = {
                    val event = AuthScreenContract.Event.NavigateToHome(
                        isNewRegisteredUser = false,
                    )
                    setEvent.invoke(event)
                },
            )

            loginScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToRegister = {
                    authNavController.currentDestination?.id?.let { destinationId ->
                        authNavController.popBackStack(
                            destinationId = destinationId,
                            inclusive = true,
                        )
                    }
                    authNavController.navigateToRegisterScreen()
                },
                navigateToHome = {
                    val event = AuthScreenContract.Event.NavigateToHome(
                        isNewRegisteredUser = false,
                    )
                    setEvent.invoke(event)
                },
                navigateToForgotPassword = authNavController::navigateToForgetPasswordScreen,
            )

            loginWithEmailScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToForgotPassword = authNavController::navigateToForgetPasswordScreen,
                navigateToHome = {
                    val event = AuthScreenContract.Event.NavigateToHome(
                        isNewRegisteredUser = false,
                    )
                    setEvent.invoke(event)
                },
            )

            registerScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToOtp = { model ->
                    authNavController.navigateToOtpScreen(
                        model = model,
                    )
                },
                navigateToLogin = {
                    authNavController.currentDestination?.id?.let { destinationId ->
                        authNavController.popBackStack(
                            destinationId = destinationId,
                            inclusive = true,
                        )
                    }
                    authNavController.navigateToLoginScreen()
                },
            )

            otpScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToUserInfo = authNavController::navigateToUserInfoScreen,
                navigateToHome = {
                    val event = AuthScreenContract.Event.NavigateToHome(
                        isNewRegisteredUser = false,
                    )
                    setEvent.invoke(event)
                },
            )

            userInfoScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToUserName = authNavController::navigateToUserNameScreen,
            )

            userNameScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
                navigateToHome = {
                    val event = AuthScreenContract.Event.NavigateToHome(
                        isNewRegisteredUser = true,
                    )
                    setEvent.invoke(event)
                },
            )

            forgetPasswordScreenNavigation(
                navigateUp = authNavController::navigateUp,
                popBackStack = authNavController::popBackStack,
            )
        },
    )
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AuthScreenContent(
                authNavController = rememberNavController(),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.auth.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.AuthRegisterScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.otp.AuthOtpScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.register.AuthRegisterScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.register.authRegisterScreenRouteTypeMap
import com.iamkurtgoz.feature.auth.register.RegisterScreen

fun NavGraphBuilder.registerScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToOtp: (AuthOtpScreenNavigateModel) -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<AuthRegisterScreenRoute>(typeMap = authRegisterScreenRouteTypeMap) {
        RegisterScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToOtp = navigateToOtp,
            navigateToLogin = navigateToLogin,
        )
    }
}

fun NavController.navigateToRegisterScreen(navOptions: NavOptions? = null) {
    val emptyModel = AuthRegisterScreenNavigateModel(
        registerSocialInfoRequest = null,
    )
    this.navigateToRegisterScreen(
        model = emptyModel,
        navOptions = navOptions,
    )
}

fun NavController.navigateToRegisterScreen(model: AuthRegisterScreenNavigateModel, navOptions: NavOptions? = null) {
    val route = AuthRegisterScreenRoute(
        model = model,
    )
    this.navigate(route, navOptions)
}

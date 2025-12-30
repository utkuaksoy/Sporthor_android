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
package com.sporthor.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.navigation.AuthScreenRoute
import com.iamkurtgoz.core.navigation.StartScreenRoute
import com.iamkurtgoz.core.navigation.ext.navigateAndClearBackStack
import com.iamkurtgoz.core.navigation.ext.navigatePopUpToInclusive
import com.iamkurtgoz.core.navigation.route.AppNavRoute
import com.iamkurtgoz.domain.state.AuthState
import com.iamkurtgoz.feature.auth.navigation.authScreenNavigation
import com.iamkurtgoz.feature.home.navigation.homeScreenNavigation
import com.iamkurtgoz.feature.home.navigation.navigateToHomeScreen

@Composable
internal fun AppScreenContent(
    authState: AuthState,
    navigationController: NavHostController,
    state: AppScreenContract.State,
) {
    authState.userEffect.observeSideEffect {
        if (it == AuthState.Effect.RouteToLoginWithClearBackStack) {
            navigationController.navigateAndClearBackStack(AuthScreenRoute)
            authState.setUserEffect(AuthState.Effect.None)
        }
    }

    if (state.startDestination != StartScreenRoute.WaitSplashScreen) {
        NavHost(
            navController = navigationController,
            route = AppNavRoute::class,
            startDestination = state.startDestination.screen,
            builder = {
                composableBuilder(
                    navigationController = navigationController,
                )
            },
        )
    }
}

private fun NavGraphBuilder.composableBuilder(
    navigationController: NavHostController,
) {
    authScreenNavigation(
        navigateUp = navigationController::navigateUp,
        navigateToHome = { isNewRegisteredUser ->
            val navOptions = navigationController
                .navigatePopUpToInclusive()
                .build()
            navigationController.navigateToHomeScreen(
                isNewRegisteredUser = isNewRegisteredUser,
                navOptions = navOptions,
            )
        },
    )

    homeScreenNavigation(
        navigateUp = navigationController::navigateUp,
    )
}

@Preview(showBackground = true)
@Composable
private fun AppScreenPreview() {
    AppScreenContent(
        authState = AuthState,
        navigationController = rememberNavController(),
        state = AppScreenContract.State(
            isLoading = false,
            appBuildConfigStatePack = AppBuildConfigStatePack(),
            appRemoteConfigStatePack = AppRemoteConfigStatePack(),
        ),
    )
}

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

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.designsystem.util.AppThemeUtil
import com.iamkurtgoz.core.navigation.AuthScreenRoute
import com.iamkurtgoz.core.navigation.AuthWelcomeScreenRoute

@Composable
internal fun AuthScreen(
    navigateUp: () -> Unit,
    navigateToHome: (isNewRegisteredUser: Boolean) -> Unit,
    darkTheme: Boolean = isSystemInDarkTheme(),
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val view = LocalView.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val authNavController = rememberNavController()
    val navBackStackEntry by authNavController.currentBackStackEntryAsState()
    val colorScheme by AppThemeUtil.getColorScheme()

    TrackedScreen(AuthScreenRoute)

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(AuthScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is AuthScreenContract.SideEffect.NavigateUp -> navigateUp()
            is AuthScreenContract.SideEffect.NavigateToHome -> navigateToHome(event.isNewRegisteredUser)
        }
    }

    LaunchedEffect(key1 = navBackStackEntry) {
        val currentDestination = navBackStackEntry?.destination
        if (currentDestination?.hierarchy?.any { 
            it.route == AuthWelcomeScreenRoute::class.qualifiedName } == true && 
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            AppThemeUtil.setAppearanceBarsColors(
                view = view,
                darkTheme = darkTheme,
                colorScheme = colorScheme,
                isAppearanceLightStatusBars = false,
                isAppearanceLightNavigationBars = false,
            )
        } else {
            AppThemeUtil.setAppearanceBarsColors(
                view = view,
                darkTheme = darkTheme,
                colorScheme = colorScheme,
                isAppearanceLightStatusBars = null,
                isAppearanceLightNavigationBars = null,
            )
        }
    }

    AuthScreenScaffold(
        authNavController = authNavController,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun AuthScreenScaffold(
    authNavController: NavHostController,
    setEvent: (AuthScreenContract.Event) -> Unit,
) {
    AppThemeScaffold {
        AuthScreenContent(
            authNavController = authNavController,
            setEvent = setEvent,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AuthScreenScaffold(
                authNavController = rememberNavController(),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppHomeSafeAreaPadding
import com.iamkurtgoz.core.designsystem.util.AppThemeUtil
import com.iamkurtgoz.core.navigation.HomeScreenCalendarDetailRoute
import com.iamkurtgoz.core.navigation.HomeScreenCalendarRoute
import com.iamkurtgoz.core.navigation.HomeScreenDashboardRoute
import com.iamkurtgoz.core.navigation.HomeScreenRoute
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.HomeScreenStoryViewerRoute
import com.iamkurtgoz.core.navigation.route.HomeNavRoute
import com.iamkurtgoz.feature.home.bottomNavigation.HomeBottomNavigationComponent
import timber.log.Timber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(
    navigateUp: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    val view = LocalView.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val colorScheme by AppThemeUtil.getColorScheme()

    TrackedScreen(HomeNavRoute)

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(HomeScreenContract.Event.Initialize)
    }

    LaunchedEffect(key1 = navBackStackEntry) {
        val destination = navBackStackEntry?.destination?.route?.split("/")?.firstOrNull()
        val isSpecialDestination = destination == HomeScreenShareRoute::class.java.name ||
            destination == HomeScreenShareCompleteRoute::class.java.name ||
            destination == HomeScreenStoryViewerRoute::class.java.name ||
            destination == HomeScreenCalendarRoute::class.java.name ||
            destination == HomeScreenCalendarDetailRoute::class.java.name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM && isSpecialDestination) {
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

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is HomeScreenContract.SideEffect.NavigateUp -> navigateUp()
        }
    }

    HomeScreenScaffold(
        state = state,
        homeNavController = homeNavController,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun HomeScreenScaffold(
    state: HomeScreenContract.State,
    homeNavController: NavHostController,
    setEvent: (HomeScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            HomeBottomNavigationComponent(
                homeNavController = homeNavController,
                scrollToTop = {
                    Timber.d("Scroll to top: ${it::class.qualifiedName}")
                },
            )
        },
        content = { paddingValues ->
            CompositionLocalProvider(LocalAppHomeSafeAreaPadding provides paddingValues) {
                HomeScreenContent(
                    modifier = Modifier,
                    state = state,
                    homeNavController = homeNavController,
                )
            }

            state.alertDialogModel?.Alert {
                setEvent.invoke(HomeScreenContract.Event.DismissDialogs)
            }

            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppLoadingDialog()
            }
        },
    )
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            HomeScreenScaffold(
                state = HomeScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    startDestination = HomeScreenDashboardRoute,
                    navigateRoute = HomeScreenRoute(
                        isNewRegisteredUser = false,
                    ),
                ),
                homeNavController = rememberNavController(),
                setEvent = { },
            )
        }
    }
}

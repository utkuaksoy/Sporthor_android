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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.common.extensions.hideSystemBars
import com.iamkurtgoz.core.common.extensions.openUrl
import com.iamkurtgoz.core.common.extensions.showSystemBarsDefault
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.state.AuthState
import com.sporthor.app.MainActivity
import com.sporthor.`as`.BuildConfig

@Composable
internal fun AppScreen(
    activity: MainActivity,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    appPreferences: AppPreferences,
    appEventBus: AppEventBus,
    authState: AuthState,
    viewModel: AppScreenViewModel = hiltViewModel(),
    onFinishApp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigationController = rememberNavController()

    TrackedScreen("AppScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(AppScreenContract.Event.Initialize)
    }

    LaunchedEffect(key1 = state.keepSplashScreenOn) {
        if (state.keepSplashScreenOn) {
            activity.window.hideSystemBars()
        } else {
            activity.window.showSystemBarsDefault()
        }
    }

    AppTheme(
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        appPreferences = appPreferences,
        appEventBus = appEventBus,
    ) {
        AppThemeSurface {
            if (!state.keepSplashScreenOn) {
                AppScreenContent(
                    authState = authState,
                    navigationController = navigationController,
                    state = state,
                )
            }

            state.alertConnectionError.Alert(
                onConfirmClick = onFinishApp,
            )

            state.alertRepairMode.Alert(
                onConfirmClick = onFinishApp,
            )

            state.alertUpdateRequired.Alert(
                onConfirmClick = {
                    activity.openUrl(BuildConfig.GOOGLE_PLAY_ADDRESS)
                },
            )
        }
    }
}

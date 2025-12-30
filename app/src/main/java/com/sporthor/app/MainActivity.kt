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
package com.sporthor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.state.AuthState
import com.sporthor.app.presentation.AppScreen
import com.sporthor.app.presentation.AppScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AppScreenViewModel>()

    @Inject
    lateinit var appBuildConfigStatePack: AppBuildConfigStatePack

    @Inject
    lateinit var appRemoteConfigStatePack: AppRemoteConfigStatePack

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var authState: AuthState

    @Inject
    lateinit var appEventBus: AppEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        processSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen(
                activity = this,
                appBuildConfigStatePack = appBuildConfigStatePack,
                appRemoteConfigStatePack = appRemoteConfigStatePack,
                appPreferences = appPreferences,
                appEventBus = appEventBus,
                authState = authState,
                viewModel = viewModel,
                onFinishApp = {
                    finish()
                },
            )
        }
    }

    private fun processSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.keepSplashScreenOn && !viewModel.state.value.isAlertShow
            }
        }
    }
}

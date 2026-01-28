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

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.app.domain.useCase.CheckLoginStatusUseCase
import com.iamkurtgoz.core.common.common.enums.ConnectivityStatus
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.StartScreenRoute
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.connectivity.ConnectivityObserver
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AppScreenViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    private val appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val connectivityObserver: ConnectivityObserver,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : CoreViewModel<AppScreenContract.State, AppScreenContract.SideEffect, AppScreenContract.Event>(
    initialState = AppScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: AppScreenContract.Event) {
        when (event) {
            AppScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            AppScreenContract.Event.RetrieveRemoteConfig -> retrieveRemoteConfig()
            AppScreenContract.Event.RouteToHomeScreen -> routeToHomeScreen()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        checkNetworkConnection()
    }

    private fun checkNetworkConnection() = viewModelScope.launch {
        val connectivityStatus = connectivityObserver.isConnected.firstOrNull()
        if (connectivityStatus == ConnectivityStatus.Connected) {
            retrieveRemoteConfig()
        } else {
            updateState {
                it.copy(
                    alertConnectionError = AnyAlertDialogModel(
                        title = resourcesR.string.general_warning,
                        message = resourcesR.string.general_warning_network_connection_check,
                        confirmButton = resourcesR.string.button_ok_button,
                        dismissButton = null,
                    ),
                )
            }
        }
    }

    private fun retrieveRemoteConfig() = viewModelScope.launch {
        val currentPreferencesState = appPreferences.currentPreferenceState.firstOrNull()
        val canShowAppOpenAd = (currentPreferencesState?.openCount ?: AppDefaults.ZERO) >= appRemoteConfigStatePack.defaultAppOpenAdShowMinOpenCount
        val isSuccess = remoteConfigRepository.getConfig(appRemoteConfigStatePack)
        if (!isSuccess) {
            checkLoginStatus()
            return@launch
        }

        if (appRemoteConfigStatePack.isRepairMode) {
            updateState {
                it.copy(
                    keepSplashScreenOn = false,
                    alertRepairMode = AnyAlertDialogModel(
                        title = resourcesR.string.general_repair_mode_title,
                        message = resourcesR.string.general_repair_mode_description,
                        confirmButton = resourcesR.string.button_ok_button,
                        dismissButton = null,
                    ),
                )
            }
            return@launch
        }

        /*if (appRemoteConfigStatePack.minVersionAndroid > appBuildConfigStatePack.versionCode) {
            updateState {
                it.copy(
                    keepSplashScreenOn = false,
                    alertUpdateRequired = AnyAlertDialogModel(
                        title = resourcesR.string.general_update_available_title,
                        message = resourcesR.string.general_update_available_message,
                        confirmButton = resourcesR.string.button_update_button,
                        dismissButton = null,
                    ),
                )
            }
            return@launch
        }*/

        if (appRemoteConfigStatePack.isStartAdActive && canShowAppOpenAd) {
            // setSideEffect(AppScreenContract.SideEffect.ShowStartAd)
            return@launch
        }

        checkLoginStatus()
    }

    private fun checkLoginStatus() = viewModelScope.launch {
        checkLoginStatusUseCase.invoke()
            .requester
            .onError {
                routeToAuthScreen()
            }.callWithSuccess {
                checkAuthState()
            }
    }

    private fun checkAuthState() = viewModelScope.launch {
        val isLoggedIn = appPreferences.currentPreferenceState.firstOrNull()?.isLogin
        if (isLoggedIn == true) {
            routeToHomeScreen()
        } else {
            routeToAuthScreen()
        }
    }

    private fun routeToAuthScreen() = viewModelScope.launch {
        appPreferences.clear()
        updateState {
            it.copy(
                keepSplashScreenOn = false,
                startDestination = StartScreenRoute.AuthScreen,
            )
        }
    }

    private fun routeToHomeScreen() = viewModelScope.launch {
        updateState {
            it.copy(
                keepSplashScreenOn = false,
                startDestination = StartScreenRoute.HomeScreen,
            )
        }
    }
}

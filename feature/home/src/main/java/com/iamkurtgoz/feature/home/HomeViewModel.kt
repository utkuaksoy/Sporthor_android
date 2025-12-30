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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenCustomizeUserInfoRoute
import com.iamkurtgoz.core.navigation.HomeScreenDashboardRoute
import com.iamkurtgoz.core.navigation.HomeScreenOnboardingRoute
import com.iamkurtgoz.core.navigation.HomeScreenRoute
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.domain.controller.SignalRController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.model.enums.SocketConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val signalRController: SignalRController,
) : CoreViewModel<HomeScreenContract.State, HomeScreenContract.SideEffect, HomeScreenContract.Event>(
    initialState = HomeScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toRoute(),
        startDestination = runBlocking {
            val navigateRoute: HomeScreenRoute = savedStateHandle.toRoute()
            val isNewRegisteredUser = navigateRoute.isNewRegisteredUser
            val currentPreference = appPreferences.currentPreferenceState.firstOrNull() ?: return@runBlocking HomeScreenCustomizeUserInfoRoute
            val isLogin = currentPreference.isLogin
            when {
                !isLogin -> HomeScreenDashboardRoute // Login değilse ana ekrana yönlendir(uygulamayı keşfediyor)
                !isNewRegisteredUser -> HomeScreenDashboardRoute // yeni kayıt değilse ana ekrana yönlendir
                !currentPreference.isUserInfoPageCompleted -> HomeScreenCustomizeUserInfoRoute
                !currentPreference.isOnboardingPageCompleted -> HomeScreenOnboardingRoute
                !currentPreference.isUserTeamsPageCompleted -> HomeScreenSelectTeamRoute
                else -> HomeScreenDashboardRoute
            }
        },
    ),
) {
    private var timerJob: Job? = null
    override fun setEvent(event: HomeScreenContract.Event) {
        when (event) {
            is HomeScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is HomeScreenContract.Event.NavigateUp -> setSideEffect(HomeScreenContract.SideEffect.NavigateUp)
            is HomeScreenContract.Event.DismissDialogs -> dismissDialogs()
        }
    }

    // Events functions
    private fun initialize() {
        startSocketStatusChecker()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun startSocketStatusChecker() {
        Timber.d("Check Status Socket")
        timerJob?.cancel()
        timerJob = viewModelScope.launch(ioDispatcher) {
            while (isActive) {
                // Timber.tag(signalRController.logTag).d("Connection Status: ${signalRController.connectionStatus}")
                if (signalRController.connectionStatus == SocketConnectionStatus.IDLE || signalRController.connectionStatus == SocketConnectionStatus.DISCONNECTED) {
                    val userId = appPreferences.currentPreferenceState.firstOrNull()?.userId.orEmpty()
                    signalRController.connect(
                        coroutineScope = viewModelScope,
                        userId = userId,
                    )
                }
                delay(HomeScreenContract.Static.DELAY_ONE_SECOND)
            }
        }
    }
}

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
package com.iamkurtgoz.feature.home.profile.settings

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val authState: AuthState,
) : CoreViewModel<SettingsScreenContract.State, SettingsScreenContract.SideEffect, SettingsScreenContract.Event>(
    initialState = SettingsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: SettingsScreenContract.Event) {
        when (event) {
            is SettingsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SettingsScreenContract.Event.NavigateUp -> setSideEffect(SettingsScreenContract.SideEffect.NavigateUp)
            is SettingsScreenContract.Event.PopBackStack -> setSideEffect(SettingsScreenContract.SideEffect.PopBackStack)
            is SettingsScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SettingsScreenContract.Event.Logout -> logout()
            is SettingsScreenContract.Event.NavigateToAboutUs -> setSideEffect(SettingsScreenContract.SideEffect.NavigateToAboutUs)
            is SettingsScreenContract.Event.NavigateToAccountSettings -> setSideEffect(SettingsScreenContract.SideEffect.NavigateToAccountSettings)
            is SettingsScreenContract.Event.NavigateToWebView -> setSideEffect(SettingsScreenContract.SideEffect.NavigateToWebView(event.routeType))
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun logout() = viewModelScope.launch {
        appPreferences.clear()
        authState.setUserEffect(AuthState.Effect.RouteToLoginWithClearBackStack)
    }
}

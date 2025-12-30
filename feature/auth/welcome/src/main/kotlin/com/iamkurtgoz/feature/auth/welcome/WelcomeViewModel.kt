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
package com.iamkurtgoz.feature.auth.welcome

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
) : CoreViewModel<WelcomeScreenContract.State, WelcomeScreenContract.SideEffect, WelcomeScreenContract.Event>(
    initialState = WelcomeScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: WelcomeScreenContract.Event) {
        when (event) {
            is WelcomeScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is WelcomeScreenContract.Event.NavigateToLogin -> setSideEffect(WelcomeScreenContract.SideEffect.NavigateToLogin)
            is WelcomeScreenContract.Event.NavigateToRegister -> setSideEffect(WelcomeScreenContract.SideEffect.NavigateToRegister)
            is WelcomeScreenContract.Event.NavigateToHome -> setSideEffect(WelcomeScreenContract.SideEffect.NavigateToHome)
            is WelcomeScreenContract.Event.DismissDialogs -> dismissDialogs()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

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
package com.iamkurtgoz.feature.home.dashboard.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.webview.toHomeScreenWebViewRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WebviewViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<WebviewScreenContract.State, WebviewScreenContract.SideEffect, WebviewScreenContract.Event>(
    initialState = WebviewScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toHomeScreenWebViewRoute(),
    ),
) {
    override fun setEvent(event: WebviewScreenContract.Event) {
        when (event) {
            is WebviewScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is WebviewScreenContract.Event.NavigateUp -> setSideEffect(WebviewScreenContract.SideEffect.NavigateUp)
            is WebviewScreenContract.Event.PopBackStack -> setSideEffect(WebviewScreenContract.SideEffect.PopBackStack)
            is WebviewScreenContract.Event.DismissDialogs -> dismissDialogs()
            is WebviewScreenContract.Event.SetLoadingStatus -> updateState { state -> state.copy(isLoading = event.isLoading) }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

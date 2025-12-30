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
package com.iamkurtgoz.feature.home.mediaViewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.toHomeScreenMediaViewerRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaViewerViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<MediaViewerScreenContract.State, MediaViewerScreenContract.SideEffect, MediaViewerScreenContract.Event>(
    initialState = MediaViewerScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toHomeScreenMediaViewerRoute(),
    ),
) {
    override fun setEvent(event: MediaViewerScreenContract.Event) {
        when (event) {
            is MediaViewerScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is MediaViewerScreenContract.Event.NavigateUp -> setSideEffect(MediaViewerScreenContract.SideEffect.NavigateUp)
            is MediaViewerScreenContract.Event.PopBackStack -> setSideEffect(MediaViewerScreenContract.SideEffect.PopBackStack)
            is MediaViewerScreenContract.Event.DismissDialogs -> dismissDialogs()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
            )
        }
    }
}

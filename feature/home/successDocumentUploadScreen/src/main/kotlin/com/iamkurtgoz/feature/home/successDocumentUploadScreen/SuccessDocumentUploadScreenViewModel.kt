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
package com.iamkurtgoz.feature.home.successDocumentUploadScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.toHomeScreenSuccessDocumentUploadRoute
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SuccessDocumentUploadScreenViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
) : CoreViewModel<SuccessDocumentUploadScreenScreenContract.State, SuccessDocumentUploadScreenScreenContract.SideEffect, SuccessDocumentUploadScreenScreenContract.Event>(
    initialState = SuccessDocumentUploadScreenScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenSuccessDocumentUploadRoute(),
    ),
) {
    override fun setEvent(event: SuccessDocumentUploadScreenScreenContract.Event) {
        when (event) {
            is SuccessDocumentUploadScreenScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SuccessDocumentUploadScreenScreenContract.Event.NavigateUp -> setSideEffect(SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateUp)
            is SuccessDocumentUploadScreenScreenContract.Event.PopBackStack -> setSideEffect(SuccessDocumentUploadScreenScreenContract.SideEffect.PopBackStack)
            is SuccessDocumentUploadScreenScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SuccessDocumentUploadScreenScreenContract.Event.NavigateToTrainingScreen -> {
                val model = HomeScreenTrainingScreenNavigateModel(
                    clubId = viewState.route.model.clubId,
                    founderUserId = viewState.route.model.founderUserId,
                    clubName = viewState.route.model.clubName,
                    logo = viewState.route.model.logo,
                )
                setSideEffect(SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateToTrainingScreen(model = model))
            }
            is SuccessDocumentUploadScreenScreenContract.Event.NavigateToHome -> setSideEffect(SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateToHome)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        appPreferences.currentPreferenceState.firstOrNull()?.let { currentPreferenceState ->
            updateState { state ->
                state.copy(
                    customUserRole = currentPreferenceState.customUserRole,
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

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
package com.iamkurtgoz.feature.home.selectEventDrafts

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.mapper.HomeScreenAddEventScreenTaskMapper
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.useCase.GetDraftsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class SelectEventDraftsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getDraftsUseCase: GetDraftsUseCase,
    private val homeScreenAddEventScreenTaskMapper: HomeScreenAddEventScreenTaskMapper,
) : CoreViewModel<SelectEventDraftsScreenContract.State, SelectEventDraftsScreenContract.SideEffect, SelectEventDraftsScreenContract.Event>(
    initialState = SelectEventDraftsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: SelectEventDraftsScreenContract.Event) {
        when (event) {
            is SelectEventDraftsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectEventDraftsScreenContract.Event.NavigateUp -> setSideEffect(SelectEventDraftsScreenContract.SideEffect.NavigateUp)
            is SelectEventDraftsScreenContract.Event.PopBackStack -> setSideEffect(SelectEventDraftsScreenContract.SideEffect.PopBackStack)
            is SelectEventDraftsScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectEventDraftsScreenContract.Event.NavigateToAddEvent -> {
                val model = HomeScreenAddEventScreenNavigationModel(
                    selectedDate = LocalDate.now(),
                    task = homeScreenAddEventScreenTaskMapper.map(event.model),
                )
                setSideEffect(SelectEventDraftsScreenContract.SideEffect.NavigateToAddEvent(model = model))
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getDrafts()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getDrafts() {
        getDraftsUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        calendarDetailEventUIModel = response,
                    )
                }
            }
    }
}

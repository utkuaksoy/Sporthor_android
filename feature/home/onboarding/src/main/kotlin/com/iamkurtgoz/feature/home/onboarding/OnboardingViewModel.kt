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
package com.iamkurtgoz.feature.home.onboarding

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.onboarding.domain.useCase.GetOnBoardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val getOnBoardingUseCase: GetOnBoardingUseCase,
) : CoreViewModel<OnboardingScreenContract.State, OnboardingScreenContract.SideEffect, OnboardingScreenContract.Event>(
    initialState = OnboardingScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: OnboardingScreenContract.Event) {
        when (event) {
            is OnboardingScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is OnboardingScreenContract.Event.NavigateUp -> setSideEffect(OnboardingScreenContract.SideEffect.NavigateUp)
            is OnboardingScreenContract.Event.PopBackStack -> setSideEffect(OnboardingScreenContract.SideEffect.PopBackStack)
            is OnboardingScreenContract.Event.DismissDialogs -> dismissDialogs()
            is OnboardingScreenContract.Event.PreviousPage -> previousPage()
            is OnboardingScreenContract.Event.NextPage -> nextPage()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getOnboarding()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getOnboarding() {
        getOnBoardingUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        pageList = it.pages?.filterNotNull().orEmpty(),
                    )
                }
            }
    }

    private fun previousPage() {
        if (viewState.currentPageIndex == AppDefaults.ZERO) {
            setSideEffect(OnboardingScreenContract.SideEffect.NavigateUp)
        } else {
            val currentIndex = viewState.currentPageIndex
            val previousIndex = currentIndex.minus(AppDefaults.ONE)

            updateState { state ->
                state.copy(
                    currentPageIndex = previousIndex,
                )
            }
        }
    }

    private fun nextPage() {
        if (viewState.currentPageIndex == viewState.pageList.lastIndex) {
            saveOnboardingData()
        } else {
            val currentIndex = viewState.currentPageIndex
            val nextIndex = currentIndex.plus(AppDefaults.ONE)

            updateState { state ->
                state.copy(
                    currentPageIndex = nextIndex,
                )
            }
        }
    }

    private fun saveOnboardingData() = viewModelScope.launch {
        val currentPreferenceState = appPreferences.currentPreferenceState.firstOrNull()
        appPreferences.setOnboardingPageCompleted(
            isOnboardingPageCompleted = true,
        )

        if (currentPreferenceState?.isSelectedTrainer == true) {
            setSideEffect(OnboardingScreenContract.SideEffect.NavigateToUserTeams)
        } else {
            setSideEffect(OnboardingScreenContract.SideEffect.NavigateToHome)
        }
    }
}

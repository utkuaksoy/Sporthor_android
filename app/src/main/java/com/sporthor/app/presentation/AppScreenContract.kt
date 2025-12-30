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

import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.StartScreenRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class AppScreenContract {
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val keepSplashScreenOn: Boolean = true,
        val alertConnectionError: AlertDialogModel? = null,
        val alertRepairMode: AlertDialogModel? = null,
        val alertUpdateRequired: AlertDialogModel? = null,
        val startDestination: StartScreenRoute = StartScreenRoute.WaitSplashScreen,
    ) : CoreState.ViewState {
        val isAlertShow: Boolean
            get() = alertConnectionError != null || alertRepairMode != null || alertUpdateRequired != null
    }

    sealed class SideEffect : CoreState.SideEffect

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object RetrieveRemoteConfig : Event()
        data object RouteToHomeScreen : Event()
    }
}

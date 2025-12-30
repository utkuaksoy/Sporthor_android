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
package com.iamkurtgoz.feature.home.share

import androidx.compose.runtime.Immutable
import androidx.paging.CombinedLoadStates
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.ShareScreenEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel

internal class ShareScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val navigateRoute: HomeScreenShareRoute,
        val selectedLocalMediaModels: List<LocalMediaUIModel> = emptyList(),
        val isSwitchedMultipleSelect: Boolean = false,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object ScrollToTop : SideEffect()
        data object NavigateToCameraXScreen : SideEffect()
        data class NavigateToShareCompleteScreen(val routeType: HomeScreenShareCompleteScreenNavigateModel) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetPagingLoadState(val loadState: CombinedLoadStates) : Event()
        data class SetSelectedLocalMediaModel(val selectedLocalMediaModel: LocalMediaUIModel?) : Event()
        data class AddSelectedLocalMediaModel(val selectedLocalMediaModel: LocalMediaUIModel?) : Event()
        data class RemoveSelectedLocalMediaModel(val selectedLocalMediaModel: LocalMediaUIModel?) : Event()
        data class SetSwitchedMultipleSelect(val isSwitchedMultipleSelect: Boolean) : Event()
        data object NavigateToCameraXScreen : Event()
        data class UpdateEventBusStatus(val eventBusState: ShareScreenEventBus.Event) : Event()
        data object NavigateToShareCompleteScreen : Event()
    }

    object Static
}

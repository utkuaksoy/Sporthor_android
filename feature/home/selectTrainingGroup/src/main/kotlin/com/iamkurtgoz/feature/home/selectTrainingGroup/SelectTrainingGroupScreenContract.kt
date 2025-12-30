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
package com.iamkurtgoz.feature.home.selectTrainingGroup

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenSelectTrainingGroupScreenRoute
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.model.GetTrainingGroupUserUIModelGroup

internal class SelectTrainingGroupScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenSelectTrainingGroupScreenRoute,
        val groups: List<GetTrainingGroupUserUIModelGroup> = emptyList(),
        val isDeleteMode: Boolean = false,
        val selectedTrainingGroup: GetTrainingGroupUserUIModelGroup? = null,
        val deleteTrainingGroupDialogModel: AlertDialogModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToEditTrainingGroupScreen(val model: HomeScreenEditTrainingGroupScreenNavigationModel) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class NavigateToEditTrainingGroupScreen(val model: GetTrainingGroupUserUIModelGroup) : Event()
        data object ToggleDeleteMode : Event()
        data class SetSelectedTrainingGroup(val value: GetTrainingGroupUserUIModelGroup?) : Event()
        data object ShowDeleteTrainingGroupDialog : Event()
        data object DeleteTrainingGroup : Event()
    }

    object Static
}

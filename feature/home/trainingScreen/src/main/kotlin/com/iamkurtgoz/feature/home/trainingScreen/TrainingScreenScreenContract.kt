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
package com.iamkurtgoz.feature.home.trainingScreen

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenTrainingRoute
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import com.iamkurtgoz.feature.home.trainingScreen.domain.model.GetSeasonsUIModel

internal class TrainingScreenScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenTrainingRoute,
        val showChangeClubDialog: Boolean = false,
        val selectedClub: HomeScreenTrainingScreenNavigateModel? = null,
        val showSelectSeasonDialog: Boolean = false,
        val seasonList: List<GetSeasonsUIModel> = listOf(),
        val selectedSeason: GetSeasonsUIModel? = null,
        val textGroupName: AppTextFieldValue = AppTextFieldValue(isError = true),
        val isFieldErrorShow: Boolean = false,
        val getRecomendedGroupNames: GetRecomendedGroupNamesDomainModel? = null,
        val suggestions: List<String> = listOf(),
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textGroupName.isError
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
        data class NavigateToSuccessAddTrainingGroupScreen(val model: HomeScreenSuccessAddTrainingGroupScreenNavigationModel) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object ShowChangeClubDialog : Event()
        data class SetSelectedClub(val selectedClub: HomeScreenTrainingScreenNavigateModel) : Event()
        data object ShowSelectSeasonDialog : Event()
        data class SetSelectedSeason(val selectedSeason: GetSeasonsUIModel?) : Event()
        data class SetGroupName(val value: String) : Event()
        data object CreateTrainingGroup : Event()
        data object NavigateToHome : Event()
    }

    object Static
}

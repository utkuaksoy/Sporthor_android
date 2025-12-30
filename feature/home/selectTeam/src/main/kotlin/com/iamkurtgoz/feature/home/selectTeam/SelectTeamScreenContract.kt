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
package com.iamkurtgoz.feature.home.selectTeam

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.selectTeam.domain.model.TeamsUIItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class SelectTeamScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenSelectTeamRoute,
        val textSearch: AppTextFieldValue = AppTextFieldValue(),
        val teamsList: ImmutableList<TeamsUIItemModel> = persistentListOf(),
        val filteredTeamsList: ImmutableList<TeamsUIItemModel> = persistentListOf(),
        val selectedTeamsList: List<TeamsUIItemModel> = listOf(),
        val customUserRole: CustomUserRole = CustomUserRole.OTHER,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
        data object CreateTeam : SideEffect()
        data class NavigateToTrainingScreen(val model: HomeScreenTrainingScreenNavigateModel) : SideEffect()
        data class NavigateToSendClubAuthDocument(
            val model: HomeScreenSendClubAuthDocumentScreenNavigateModel,
            val fromGenerateClub: Boolean = false,
        ) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetTextSearch(val text: String) : Event()
        data class SetSelectedTeam(val team: TeamsUIItemModel) : Event()
        data object SaveUserTeams : Event()
        data object CreateTeam : Event()
        data object NavigateToHome : Event()
    }

    object Static {
        const val SEARCH_DEBOUNCE: Long = 500
        const val MIN_SEARCH_LENGTH: Int = 4
    }
}

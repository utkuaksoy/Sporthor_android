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
package com.iamkurtgoz.feature.home.createTeam

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenCreateTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.domain.eventbus.impl.CreateTeamEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModelBranch
import com.iamkurtgoz.feature.home.createTeam.domain.model.ClubUIModel
import java.io.File

internal class CreateTeamScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenCreateTeamRoute,
        val showPhotoPicker: Boolean = false,
        val selectedImage: File? = null,
        val textClubName: AppTextFieldValue = AppTextFieldValue(isError = true),
        val textAddressTitle: String? = null,
        val textAddressDetailName: AppTextFieldValue = AppTextFieldValue(isError = true),
        val textCity: String? = null,
        val textCountry: String? = null,
        val textSelectedBranch: GetSportClubDomainModelBranch? = null,
        val textClubCreateYear: AppTextFieldValue = AppTextFieldValue(),
        val isFieldErrorShow: Boolean = false,
        val createdClubModel: ClubUIModel? = null,
        val customUserRole: CustomUserRole = CustomUserRole.OTHER,
        val showYearPicker: Boolean = false,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textClubName.isError || textClubCreateYear.isError || textAddressDetailName.isError
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToSelectAddress : SideEffect()
        data class NavigateToSendClubAuthDocument(val model: HomeScreenSendClubAuthDocumentScreenNavigateModel) : SideEffect()
        data object NavigateToHome : SideEffect()
        data class NavigateToTrainingScreen(val model: HomeScreenTrainingScreenNavigateModel) : SideEffect()
        data object NavigateToCreateTeamSelectBranchScreen : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetShowStatePhotoPicker(val isShow: Boolean) : Event()
        data class SetSelectedImage(val imagePath: String?) : Event()
        data class SetClubName(val value: String) : Event()
        data class SetAddressDetailName(val value: String) : Event()
        data class SetClubCreateYear(val value: String) : Event()
        data object CreateClub : Event()
        data object NavigateToSelectAddress : Event()
        data object NavigateToSendClubAuthDocument : Event()
        data class SelectedAddressChanged(
            val title: String,
            val address: String,
            val city: String?,
            val country: String?,
        ) : Event()
        data object NavigateToHome : Event()
        data object ShowYearPicker : Event()
        data object HideYearPicker : Event()
        data object NavigateToTrainingScreen : Event()
        data class UpdateEventBusStatus(val event: CreateTeamEventBus.Event) : Event()
        data object NavigateToCreateTeamSelectBranchScreen : Event()
    }

    object Static
}

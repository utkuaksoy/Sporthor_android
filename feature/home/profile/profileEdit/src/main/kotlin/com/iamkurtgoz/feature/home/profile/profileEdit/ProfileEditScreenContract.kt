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
package com.iamkurtgoz.feature.home.profile.profileEdit

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.ProfileEditEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchInfoRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.EditProfileSummaryUIModel
import java.io.File

internal class ProfileEditScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val profileSummaryModel: EditProfileSummaryUIModel? = null,
        val branchInfoRowUIModel: BranchInfoRowUIModel? = null,
        val selectedBranchId: String? = null,
        val selectedBranchIds: List<String> = emptyList(),
        val dynamicTextFieldValues: List<AppTextFieldValue> = emptyList(),
        val selectedImage: File? = null,
        val onUploadProgress: Int? = null,
        val showPhotoPicker: Boolean = false,
        val showLocationPermissionRequest: Boolean = false,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToProfileEditSelectBranch : SideEffect()
        data object NavigateToSelectUserRole : SideEffect()
        data class ShowSuccessToast(val message: String) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetSelectedBranchId(val branchId: String?) : Event()
        data class SetDynamicTextFieldValue(val appTextFieldValue: AppTextFieldValue) : Event()
        data object UpdateProfileSummary : Event()
        data object OnClickAddBranch : Event()
        data class SetShowStatePhotoPicker(val isShow: Boolean) : Event()
        data class SetSelectedImage(val imagePath: String?) : Event()
        data class UpdateEventBusStatus(val eventBusState: ProfileEditEventBus.Event) : Event()
        data object GetLocation : Event()
        data object NavigateToSelectUserRole : Event()
    }

    object Static {
        const val DELAY_100_MILLISECOND = 100L
    }
}

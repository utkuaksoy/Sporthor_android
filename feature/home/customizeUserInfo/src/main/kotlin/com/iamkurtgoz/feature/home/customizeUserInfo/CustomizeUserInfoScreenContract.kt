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
package com.iamkurtgoz.feature.home.customizeUserInfo

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.enums.GenderType
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationBranchUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationCoachRoleUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.model.ConfigurationUserRoleUIModel
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.types.CustomizePageType
import kotlinx.collections.immutable.ImmutableList

internal class CustomizeUserInfoScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val configurationModel: ConfigurationUIModel? = null,
        val currentPageIndex: CustomizePageType? = null,
        val userBranchesTypeList: ImmutableList<ConfigurationBranchUIModel>? = null,
        val selectedUserBranchesTypeList: List<ConfigurationBranchUIModel> = emptyList(),
        val userRoleTypeList: ImmutableList<ConfigurationUserRoleUIModel>? = null,
        val selectedUserRolesTypeList: List<ConfigurationUserRoleUIModel> = emptyList(),
        val coachRoleTypeList: ImmutableList<ConfigurationCoachRoleUIModel>? = null,
        val selectedCoachRolesTypeList: List<ConfigurationCoachRoleUIModel> = emptyList(),
        val textBirthdayDay: AppTextFieldValue = AppTextFieldValue(),
        val textBirthdayMonth: AppTextFieldValue = AppTextFieldValue(),
        val textBirthdayYear: AppTextFieldValue = AppTextFieldValue(),
        val selectedGenderType: GenderType = GenderType.PreferNotToSay,
        val isFieldErrorShow: Boolean = false,
    ) : CoreState.ViewState {
        val isFieldsAnyError: Boolean
            get() = textBirthdayDay.isError || textBirthdayMonth.isError || textBirthdayYear.isError

        val buttonDisabled: Boolean
            get() = when (currentPageIndex) {
                CustomizePageType.Welcome -> false
                CustomizePageType.UserRoleCategory -> selectedUserRolesTypeList.isEmpty()
                CustomizePageType.UserBranchPage -> selectedUserBranchesTypeList.isEmpty()
                CustomizePageType.CoachRoleCategory -> selectedCoachRolesTypeList.isEmpty()
                CustomizePageType.UserInfo -> (textBirthdayDay.value.isEmpty() || textBirthdayMonth.value.isEmpty() || textBirthdayYear.value.isEmpty())
                else -> false
            }
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToOnboarding : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object PreviousPage : Event()
        data object NextPage : Event()
        data class SetSelectedUserBranchType(val type: ConfigurationBranchUIModel) : Event()
        data class SetSelectedUserRoleType(val type: ConfigurationUserRoleUIModel) : Event()
        data class SetSelectedCoachRoleType(val type: ConfigurationCoachRoleUIModel) : Event()
        data class SetBirthdayDay(val text: String) : Event()
        data class SetBirthdayMonth(val text: String) : Event()
        data class SetBirthdayYear(val text: String) : Event()
        data class SetSelectedGenderType(val type: GenderType) : Event()
    }

    object Static
}

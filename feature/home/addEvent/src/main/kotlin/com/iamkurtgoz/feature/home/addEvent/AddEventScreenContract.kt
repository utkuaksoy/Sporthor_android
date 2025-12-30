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
package com.iamkurtgoz.feature.home.addEvent

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTaskTypeUIModelType
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModel
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModelTeam
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModelUser
import java.time.LocalDate
import java.time.LocalTime

internal class AddEventScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenAddEventRoute,
        val repeatOptions: Map<Int, String> = mapOf(
            1 to "Her Gün",
            2 to "Her Hafta",
            3 to "Her 2 Haftada 1",
            4 to "Her Ay",
            5 to "Her Yıl",
        ),
        val taskTypes: List<GetTaskTypeUIModelType> = listOf(),
        val getTrainingGroupUserUIModel: GetTrainingGroupUserUIModel? = null,
        val textEventName: AppTextFieldValue = AppTextFieldValue(isError = true),
        val switchEventDateAllDay: Boolean = false,
        val eventStartDate: LocalDate,
        val eventStartTime: LocalTime,
        val eventEndDate: LocalDate,
        val eventEndTime: LocalTime,
        val switchEventRepeat: Boolean = false,
        val showStartDatePicker: Boolean = false,
        val showStartTimePicker: Boolean = false,
        val showEndDatePicker: Boolean = false,
        val showEndTimePicker: Boolean = false,
        val selectedRepeatType: Int? = null,
        val selectedTaskType: GetTaskTypeUIModelType? = null,
        val showAddTaskTypeDialog: Boolean = false,
        val showAddUserBottomSheet: Boolean = false,
        val textTaskName: AppTextFieldValue = AppTextFieldValue(),
        val textSearchUser: AppTextFieldValue = AppTextFieldValue(),
        val selectedGetTrainingGroupUserUIModelTeam: GetTrainingGroupUserUIModelTeam? = null,
        val selectedGetTrainingGroupUserList: List<GetTrainingGroupUserUIModelUser> = listOf(),
        val addressTitle: String? = null,
        val addressDetail: String? = null,
        val city: String? = null,
        val country: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val textDescription: AppTextFieldValue = AppTextFieldValue(isError = true),
        val showAddDescriptionDialog: Boolean = false,
        val isDraft: Boolean = false,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToSelectAddress : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object NavigateToSelectAddress : Event()
        data class SetEventName(val value: String) : Event()
        data class SetSwitchEventDateAllDay(val value: Boolean) : Event()
        data class SetSwitchEventRepeat(val value: Boolean) : Event()
        data object ShowStartDatePicker : Event()
        data class SetEventStartDate(val value: LocalDate) : Event()
        data object ShowStartTimePicker : Event()
        data class SetEventStartTime(val value: LocalTime) : Event()
        data object ShowEndDatePicker : Event()
        data class SetEventEndDate(val value: LocalDate) : Event()
        data object ShowEndTimePicker : Event()
        data class SetEventEndTime(val value: LocalTime) : Event()
        data class SetSelectedRepeatType(val value: Int) : Event()
        data class SetSelectedTaskType(val value: GetTaskTypeUIModelType?) : Event()
        data object ShowAddTaskTypeDialog : Event()
        data class SetTaskName(val value: String) : Event()
        data object AddTaskType : Event()
        data object ShowAddUserBottomSheet : Event()
        data class SetSearchUser(val value: String) : Event()
        data class SetGetTrainingGroupUserUIModelTeam(val value: GetTrainingGroupUserUIModelTeam?) : Event()
        data class SetSelectedGetTrainingGroupUser(val value: GetTrainingGroupUserUIModelUser) : Event()
        data class SelectedAddressChanged(
            val addressTitle: String,
            val addressDetail: String,
            val city: String?,
            val country: String?,
            val latitude: Double?,
            val longitude: Double?,
        ) : Event()

        data class SetDescription(val value: String) : Event()
        data object ShowAddDescriptionDialog : Event()
        data object ChangeCheckBoxDraftState : Event()
        data object AddTask : Event()
    }

    object Static
}

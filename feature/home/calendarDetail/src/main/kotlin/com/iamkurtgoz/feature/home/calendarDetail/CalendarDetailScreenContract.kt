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
package com.iamkurtgoz.feature.home.calendarDetail

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenCalendarDetailRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editEvent.HomeScreenEditEventScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModel
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelTask
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.DayItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class CalendarDetailScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenCalendarDetailRoute,
        val selectedDate: LocalDate? = null,
        val days: List<DayItem> = listOf(),
        val calendarDetailEventUIModel: CalendarDetailEventUIModel? = null,
        val showSelectAddEventTypeDialog: Boolean = false,
    ) : CoreState.ViewState {
        val title: String
            get() = selectedDate?.format(
                DateTimeFormatter.ofPattern("MMMM", Locale.getDefault()),
            ) ?: ""
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToAddEvent(val model: HomeScreenAddEventScreenNavigationModel) : SideEffect()
        data object NavigateToSelectEventDrafts : SideEffect()
        data object SyncScrollState : SideEffect()
        data class NavigateToEditEventScreen(val model: HomeScreenEditEventScreenNavigationModel) : SideEffect()
        data class NavigateToMap(val location: com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelLocation) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object RefreshCalendarDetail : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetSelectedDate(val localDate: LocalDate) : Event()
        data class RpeSurvey(val taskId: String, val rating: Int) : Event()
        data class NavigateToAddEvent(val selectedDate: LocalDate?) : Event()
        data object SyncScrollState : Event()
        data object ShowSelectAddEventTypeDialog : Event()
        data object NavigateToSelectEventDrafts : Event()
        data class NavigateToEditEventScreen(val model: CalendarDetailEventUIModelTask) : Event()
        data class OnMapClick(val location: com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelLocation) : Event()
    }

    object Static
}

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
package com.iamkurtgoz.feature.home.calendar

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.feature.home.calendar.domain.model.CalendarEventUIModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class CalendarScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val titles: List<String> = listOf("P", "S", "Ç", "P", "C", "C", "P"),
        val currentMonth: YearMonth = YearMonth.now(),
        val events: List<CalendarEventUIModel> = listOf(),
        val showSelectAddEventTypeDialog: Boolean = false,
    ) : CoreState.ViewState {
        val title: String
            get() = currentMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()),
            )
    }

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToCalendarDetail(val model: HomeScreenCalendarDetailScreenNavigationModel) : SideEffect()
        data class NavigateToAddEvent(val model: HomeScreenAddEventScreenNavigationModel) : SideEffect()
        data object NavigateToSelectEventDrafts : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object PreviousMonth : Event()
        data object NextMonth : Event()
        data class NavigateToCalendarDetail(val selectedDate: LocalDate?) : Event()
        data class NavigateToAddEvent(val selectedDate: LocalDate?) : Event()
        data object NavigateToSelectEventDrafts : Event()
        data object ShowSelectAddEventTypeDialog : Event()
    }

    object Static
}

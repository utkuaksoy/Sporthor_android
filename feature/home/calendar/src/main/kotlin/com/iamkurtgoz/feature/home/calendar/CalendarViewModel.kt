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

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.calendar.domain.useCase.GetCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class CalendarViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getCalendarUseCase: GetCalendarUseCase,
) : CoreViewModel<CalendarScreenContract.State, CalendarScreenContract.SideEffect, CalendarScreenContract.Event>(
    initialState = CalendarScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: CalendarScreenContract.Event) {
        when (event) {
            is CalendarScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CalendarScreenContract.Event.RefreshCalendar -> getCalendar()
            is CalendarScreenContract.Event.NavigateUp -> setSideEffect(CalendarScreenContract.SideEffect.NavigateUp)
            is CalendarScreenContract.Event.PopBackStack -> setSideEffect(CalendarScreenContract.SideEffect.PopBackStack)
            is CalendarScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CalendarScreenContract.Event.PreviousMonth -> previousMonth()
            is CalendarScreenContract.Event.NextMonth -> nextMonth()
            is CalendarScreenContract.Event.NavigateToCalendarDetail -> {
                val model = HomeScreenCalendarDetailScreenNavigationModel(
                    selectedDate = event.selectedDate,
                )
                setSideEffect(CalendarScreenContract.SideEffect.NavigateToCalendarDetail(model))
            }
            is CalendarScreenContract.Event.NavigateToAddEvent -> {
                val model = HomeScreenAddEventScreenNavigationModel(
                    selectedDate = event.selectedDate,
                )
                setSideEffect(CalendarScreenContract.SideEffect.NavigateToAddEvent(model))
            }
            is CalendarScreenContract.Event.ShowSelectAddEventTypeDialog -> showSelectAddEventTypeDialog()
            is CalendarScreenContract.Event.NavigateToSelectEventDrafts -> setSideEffect(CalendarScreenContract.SideEffect.NavigateToSelectEventDrafts)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getCalendar()
    }

    private fun dismissDialogs() {
        updateState { state -> 
            state.copy(
                alertDialogModel = null,
                showSelectAddEventTypeDialog = false,
            ) 
        }
    }

    private fun previousMonth() {
        updateState { state ->
            state.copy(
                currentMonth = state.currentMonth.minusMonths(1),
            )
        }
        getCalendar()
    }

    private fun nextMonth() {
        updateState { state ->
            state.copy(
                currentMonth = state.currentMonth.plusMonths(1),
            )
        }
        getCalendar()
    }
    
    private fun showSelectAddEventTypeDialog() {
        updateState { state ->
            state.copy(
                showSelectAddEventTypeDialog = true,
            )
        }
    }

    private fun getCalendar() {
        val firstOfMonth: LocalDate = viewState.currentMonth.atDay(1)
        val dateString = firstOfMonth.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
        )
        getCalendarUseCase.invoke(dateString)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        events = response,
                    )
                }
            }
    }
}

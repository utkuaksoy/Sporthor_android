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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.toHomeScreenCalendarDetailRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.editEvent.HomeScreenEditEventScreenNavigationModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.RpeSurveyRequest
import com.iamkurtgoz.feature.home.calendarDetail.domain.mapper.HomeScreenEditEventScreenTaskMapper
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.DayItem
import com.iamkurtgoz.feature.home.calendarDetail.domain.useCase.GetCalendarDetailUseCase
import com.iamkurtgoz.feature.home.calendarDetail.domain.useCase.RpeSurveyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class CalendarDetailViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getCalendarDetailUseCase: GetCalendarDetailUseCase,
    private val rpeSurveyUseCase: RpeSurveyUseCase,
    private val homeScreenEditEventScreenTaskMapper: HomeScreenEditEventScreenTaskMapper,
) : CoreViewModel<CalendarDetailScreenContract.State, CalendarDetailScreenContract.SideEffect, CalendarDetailScreenContract.Event>(
    initialState = CalendarDetailScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenCalendarDetailRouteTypeMap(),
        selectedDate = savedStateHandle.toHomeScreenCalendarDetailRouteTypeMap().model.selectedDate,
    ),
) {
    override fun setEvent(event: CalendarDetailScreenContract.Event) {
        when (event) {
            is CalendarDetailScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CalendarDetailScreenContract.Event.NavigateUp -> setSideEffect(CalendarDetailScreenContract.SideEffect.NavigateUp)
            is CalendarDetailScreenContract.Event.PopBackStack -> setSideEffect(CalendarDetailScreenContract.SideEffect.PopBackStack)
            is CalendarDetailScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CalendarDetailScreenContract.Event.SetSelectedDate -> setSelectedDate(event.localDate)
            is CalendarDetailScreenContract.Event.NavigateToAddEvent -> {
                val model = HomeScreenAddEventScreenNavigationModel(
                    selectedDate = event.selectedDate,
                )
                setSideEffect(CalendarDetailScreenContract.SideEffect.NavigateToAddEvent(model))
            }

            is CalendarDetailScreenContract.Event.SyncScrollState -> setSideEffect(CalendarDetailScreenContract.SideEffect.SyncScrollState)
            is CalendarDetailScreenContract.Event.ShowSelectAddEventTypeDialog -> showSelectAddEventTypeDialog()
            is CalendarDetailScreenContract.Event.NavigateToSelectEventDrafts -> setSideEffect(CalendarDetailScreenContract.SideEffect.NavigateToSelectEventDrafts)
            is CalendarDetailScreenContract.Event.NavigateToEditEventScreen -> {
                val model = HomeScreenEditEventScreenNavigationModel(
                    selectedDate = LocalDate.now(),
                    task = homeScreenEditEventScreenTaskMapper.map(event.model),
                )
                setSideEffect(CalendarDetailScreenContract.SideEffect.NavigateToEditEventScreen(model))
            }
            is CalendarDetailScreenContract.Event.RpeSurvey -> rpeSurvey(taskId = event.taskId, rating = event.rating)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        updateState { state ->
            state.copy(
                days = DayCreator.days(),
            )
        }
        withContext(Dispatchers.Main) {
            setSideEffect(CalendarDetailScreenContract.SideEffect.SyncScrollState)
        }
        getCalendarDetail()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showSelectAddEventTypeDialog = false,
            )
        }
    }

    private fun setSelectedDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
        getCalendarDetail()
    }

    private fun getCalendarDetail() {
        val dateString = viewState.selectedDate?.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
        )
        getCalendarDetailUseCase.invoke(dateString)
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
                        calendarDetailEventUIModel = response,
                    )
                }
            }
    }

    private fun rpeSurvey(taskId: String, rating: Int) {
        val request = RpeSurveyRequest(
            taskId = taskId,
            rating = rating,
        )
        rpeSurveyUseCase.invoke(request)
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
                    )
                }
            }
    }

    private fun showSelectAddEventTypeDialog() {
        updateState { state ->
            state.copy(
                showSelectAddEventTypeDialog = true,
            )
        }
    }
}

internal object DayCreator {
    private var privateList: List<DayItem> = emptyList()
    fun days(): List<DayItem> {
        if (privateList.isEmpty()) {
            val today = LocalDate.now()
            val startDate = today.minusYears(1)
            val endDate = today.plusYears(1)
            privateList = getDaysInRange(startDate, endDate)
        }
        return privateList
    }

    private fun getDaysInRange(
        start: LocalDate,
        end: LocalDate,
        locale: Locale = Locale.getDefault(),
    ): List<DayItem> {
        val days = mutableListOf<DayItem>()
        var current = start
        while (!current.isAfter(end)) {
            val letter = current.dayOfWeek
                .getDisplayName(TextStyle.NARROW_STANDALONE, locale)
                .uppercase(locale)
            days += DayItem(
                localDate = current,
                dayLetter = letter,
                dayOfMonth = current.dayOfMonth,
            )
            current = current.plusDays(1)
        }
        return days
    }
}

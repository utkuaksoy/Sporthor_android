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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.extensions.toEpochMilli
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.domain.eventbus.impl.AddEventTaskEventBus
import com.iamkurtgoz.feature.home.addEvent.component.AddDescriptionBottomSheet
import com.iamkurtgoz.feature.home.addEvent.component.AddTaskTypeBottomSheet
import com.iamkurtgoz.feature.home.addEvent.component.AddUserBottomSheet
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@Composable
internal fun AddEventScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToSelectAddress: () -> Unit,
    viewModel: AddEventViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("AddEventScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(AddEventScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.addEventTaskEventBus.observeEventBus { event ->
        when (event) {
            is AddEventTaskEventBus.Event.SelectedAddressChanged -> {
                val event = AddEventScreenContract.Event.SelectedAddressChanged(
                    addressTitle = event.title,
                    addressDetail = event.address,
                    city = event.city,
                    country = event.country,
                    latitude = event.latitude,
                    longitude = event.longitude,
                )
                viewModel.setEvent(event)
            }
        }
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is AddEventScreenContract.SideEffect.NavigateUp -> navigateUp()
            is AddEventScreenContract.SideEffect.PopBackStack -> popBackStack()
            is AddEventScreenContract.SideEffect.NavigateToSelectAddress -> navigateToSelectAddress()
        }
    }

    AddEventScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEventScreenScaffold(
    state: AddEventScreenContract.State,
    setEvent: (AddEventScreenContract.Event) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(AddEventScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Yeni Etkinlik Oluştur", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            AppButton.PrimaryLarge(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingMedium),
                text = if (state.isDraft) "Etkinliği Kaydet ve Gönder" else "Gönder", // TODO: Localize
                onClick = {
                    setEvent.invoke(AddEventScreenContract.Event.AddTask)
                },
            )
        },
    ) { padding ->
        AddEventScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        if (state.showStartDatePicker) {
            DatePickerDialog(
                initialSelectedDateMillis = state.eventStartDate.toEpochMilli(),
                onDismissRequest = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                onSelectedDate = {
                    setEvent.invoke(AddEventScreenContract.Event.SetEventStartDate(it))
                },
            )
        }

        if (state.showStartTimePicker) {
            TimePickerDialog(
                localTime = state.eventStartTime,
                onDismissRequest = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                onSelectedTime = {
                    setEvent.invoke(AddEventScreenContract.Event.SetEventStartTime(it))
                },
            )
        }

        if (state.showEndDatePicker) {
            DatePickerDialog(
                initialSelectedDateMillis = state.eventEndDate.toEpochMilli(),
                onDismissRequest = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                onSelectedDate = {
                    setEvent.invoke(AddEventScreenContract.Event.SetEventEndDate(it))
                },
            )
        }

        if (state.showEndTimePicker) {
            TimePickerDialog(
                localTime = state.eventEndTime,
                onDismissRequest = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                onSelectedTime = {
                    setEvent.invoke(AddEventScreenContract.Event.SetEventEndTime(it))
                },
            )
        }

        if (state.showRecurrenceEndDatePicker) {
            DatePickerDialog(
                initialSelectedDateMillis = state.recurrenceEndDate?.toEpochMilli(),
                onDismissRequest = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                onSelectedDate = {
                    setEvent.invoke(AddEventScreenContract.Event.SetRecurrenceEndDate(it))
                },
            )
        }

        AddTaskTypeBottomSheet(
            showSheet = state.showAddTaskTypeDialog,
            state = state,
            setEvent = setEvent,
            onDismissRequest = {
                keyboardController?.hide()
                setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
            },
        )

        AddUserBottomSheet(
            showSheet = state.showAddUserBottomSheet,
            state = state,
            setEvent = setEvent,
            onDismissRequest = {
                keyboardController?.hide()
                setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
            },
        )

        AddDescriptionBottomSheet(
            showSheet = state.showAddDescriptionDialog,
            state = state,
            setEvent = setEvent,
            onDismissRequest = {
                keyboardController?.hide()
                setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialSelectedDateMillis: Long?,
    onDismissRequest: () -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        initialDisplayMode = DisplayMode.Picker,
    )
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                    datePickerState.selectedDateMillis?.let {
                        val instant = Instant.ofEpochMilli(it)
                        val localDate: LocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                        onSelectedDate.invoke(localDate)
                    }
                },
                enabled = confirmEnabled.value,
            ) {
                Text(
                    text = "Seç", // TODO: Localize
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = "İptal", // TODO: Localize
                )
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            modifier = Modifier.verticalScroll(rememberScrollState()),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    localTime: LocalTime?,
    onDismissRequest: () -> Unit,
    onSelectedTime: (LocalTime) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = localTime?.hour ?: 0,
        initialMinute = localTime?.minute ?: 0,
        is24Hour = true,
    )
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    onSelectedTime.invoke(selectedTime)
                },
            ) {
                Text(
                    text = "Seç", // TODO: Localize
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = "İptal", // TODO: Localize
                )
            }
        },
    ) {
        TimePicker(
            state = timePickerState,
            modifier = Modifier.verticalScroll(rememberScrollState()),
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AddEventScreenScaffold(
                state = AddEventScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenAddEventRoute(
                        model = HomeScreenAddEventScreenNavigationModel(
                            selectedDate = null,
                        ),
                    ),
                    eventStartDate = LocalDate.now(),
                    eventStartTime = LocalTime.now(),
                    eventEndDate = LocalDate.now(),
                    eventEndTime = LocalTime.now().plusHours(2),
                    recurrenceEndDate = LocalDate.now()
                ),
                setEvent = { },
            )
        }
    }
}

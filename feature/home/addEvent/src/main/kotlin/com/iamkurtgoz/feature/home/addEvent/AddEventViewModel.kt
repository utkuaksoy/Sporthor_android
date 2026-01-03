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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.addEvent.toHomeScreenAddEventRouteTypeMap
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.AddTaskRequest
import com.iamkurtgoz.domain.model.request.AddTaskRequestLocation
import com.iamkurtgoz.domain.model.request.AddTaskTypeRequest
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTaskTypeUIModelType
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModelTeam
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModelUser
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.AddTaskTypeUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.AddTaskUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.GetTaskTypesUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.GetTrainingGroupUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class AddEventViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getTaskTypesUseCase: GetTaskTypesUseCase,
    private val addTaskTypeUseCase: AddTaskTypeUseCase,
    private val getTrainingGroupUserUseCase: GetTrainingGroupUserUseCase,
    private val addTaskUseCase: AddTaskUseCase,
) : CoreViewModel<AddEventScreenContract.State, AddEventScreenContract.SideEffect, AddEventScreenContract.Event>(
    initialState = AddEventScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenAddEventRouteTypeMap(),
        eventStartDate = savedStateHandle.toHomeScreenAddEventRouteTypeMap().model.selectedDate ?: LocalDate.now(),
        eventStartTime = LocalTime.now(),
        eventEndDate = savedStateHandle.toHomeScreenAddEventRouteTypeMap().model.selectedDate ?: LocalDate.now(),
        eventEndTime = LocalTime.now().plusHours(2),
        recurrenceEndDate = savedStateHandle.toHomeScreenAddEventRouteTypeMap().model.selectedDate ?: LocalDate.now(),
    ),
) {
    override fun setEvent(event: AddEventScreenContract.Event) {
        when (event) {
            is AddEventScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AddEventScreenContract.Event.NavigateUp -> setSideEffect(AddEventScreenContract.SideEffect.NavigateUp)
            is AddEventScreenContract.Event.PopBackStack -> setSideEffect(AddEventScreenContract.SideEffect.PopBackStack)
            is AddEventScreenContract.Event.DismissDialogs -> dismissDialogs()
            is AddEventScreenContract.Event.NavigateToSelectAddress -> setSideEffect(AddEventScreenContract.SideEffect.NavigateToSelectAddress)
            is AddEventScreenContract.Event.SetEventName -> setEventName(event.value)
            is AddEventScreenContract.Event.SetSwitchEventDateAllDay -> setSwitchEventDateAllDay(event.value)
            is AddEventScreenContract.Event.SetSwitchEventRepeat -> setSwitchEventRepeat(event.value)
            is AddEventScreenContract.Event.ShowStartDatePicker -> showStartDatePicker()
            is AddEventScreenContract.Event.SetEventStartDate -> setEventStartDate(event.value)
            is AddEventScreenContract.Event.ShowStartTimePicker -> showStartTimePicker()
            is AddEventScreenContract.Event.SetEventStartTime -> setEventStartTime(event.value)
            is AddEventScreenContract.Event.ShowEndDatePicker -> showEndDatePicker()
            is AddEventScreenContract.Event.SetEventEndDate -> setEventEndDate(event.value)
            is AddEventScreenContract.Event.ShowEndTimePicker -> showEndTimePicker()
            is AddEventScreenContract.Event.SetEventEndTime -> setEventEndTime(event.value)
            is AddEventScreenContract.Event.SetSelectedRepeatType -> setSelectedRepeatType(event.value)
            is AddEventScreenContract.Event.SetSelectedTaskType -> setSelectedTaskType(event.value)
            is AddEventScreenContract.Event.ShowAddTaskTypeDialog -> showAddTaskTypeDialog()
            is AddEventScreenContract.Event.SetTaskName -> setTaskName(event.value)
            is AddEventScreenContract.Event.AddTaskType -> addTaskType()
            is AddEventScreenContract.Event.ShowAddUserBottomSheet -> showAddUserBottomSheet()
            is AddEventScreenContract.Event.SetSearchUser -> setSearchUser(event.value)
            is AddEventScreenContract.Event.SetGetTrainingGroupUserUIModelTeam -> setGetTrainingGroupUserUIModelTeam(event.value)
            is AddEventScreenContract.Event.SetSelectedGetTrainingGroupUser -> setSelectedGetTrainingGroupUser(event.value)
            is AddEventScreenContract.Event.SelectedAddressChanged -> setSelectedAddressChanged(
                addressTitle = event.addressTitle,
                addressDetail = event.addressDetail,
                city = event.city,
                country = event.country,
                latitude = event.latitude,
                longitude = event.longitude,
            )
            is AddEventScreenContract.Event.SetDescription -> setDescription(event.value)
            is AddEventScreenContract.Event.ShowAddDescriptionDialog -> showAddDescriptionDialog()
            is AddEventScreenContract.Event.ChangeCheckBoxDraftState -> changeCheckBoxDraftState()
            is AddEventScreenContract.Event.ShowRecurrenceEndDatePicker -> showRecurrenceEndDatePicker()
            is AddEventScreenContract.Event.SetRecurrenceEndDate -> setRecurrenceEndDate(event.value)
            is AddEventScreenContract.Event.AddTask -> addTask()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getTaskTypes()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showStartDatePicker = false,
                showStartTimePicker = false,
                showEndDatePicker = false,
                showEndTimePicker = false,
                showAddTaskTypeDialog = false,
                showAddUserBottomSheet = false,
                showAddDescriptionDialog = false,
                showRecurrenceEndDatePicker = false,
            )
        }
    }

    private fun getTaskTypes() {
        getTaskTypesUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        taskTypes = response.types?.filterNotNull() ?: emptyList(),
                    )
                }
                getTrainingGroupUser()
            }
    }

    private fun getTrainingGroupUser() {
        getTrainingGroupUserUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        getTrainingGroupUserUIModel = response,
                    )
                }
                initRoutedData()
            }
    }

    private fun initRoutedData() {
        viewState.route.model.task?.let { task ->
            task.title?.let {
                setEventName(it)
            }
            task.isRecurring?.let {
                setSwitchEventRepeat(it)
                val repeatType = viewState.repeatOptions.filter { it.key == task.recurrence }.map { it.key }.firstOrNull()
                setSelectedRepeatType(repeatType)
            }
            task.taskType?.let {
                val type = GetTaskTypeUIModelType(
                    detail = it.detail,
                    name = it.name,
                    value = it.value,
                )
                setSelectedTaskType(type)
            }
            task.trainingGroup?.let {
                val value = GetTrainingGroupUserUIModelTeam(
                    name = it.name,
                    value = it.value,
                    detail = it.detail,
                )
                setGetTrainingGroupUserUIModelTeam(value)
            }
            task.users?.let {
                it.forEach { user ->
                    val value = GetTrainingGroupUserUIModelUser(
                        id = user?.id,
                        imageUrl = user?.imageUrl,
                        isCurrentUser = user?.isCurrentUser,
                        isFollow = user?.isFollow,
                        name = user?.name,
                        summary = user?.summary,
                        username = user?.username,
                    )
                    setSelectedGetTrainingGroupUser(value)
                }
            }
            task.location?.let {
                setSelectedAddressChanged(
                    addressTitle = it.title ?: "",
                    addressDetail = it.address ?: "",
                    city = null,
                    country = null,
                    latitude = it.lat,
                    longitude = it.lng,
                )
            }
            task.description?.let {
                setDescription(it)
            }
        }
    }

    private fun addTaskType() {
        if (viewState.textTaskName.value.isEmpty()) {
            return
        }
        val typeName = viewState.textTaskName.value
        val params = AddTaskTypeRequest(
            typeName = typeName,
        )
        setTaskName("")
        dismissDialogs()
        addTaskTypeUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                val model = GetTaskTypeUIModelType(
                    detail = response.detail?.detail,
                    name = response.detail?.name,
                    value = response.detail?.value,
                )
                val taskTypes = viewState.taskTypes.toMutableList()
                taskTypes.add(model)
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        taskTypes = taskTypes,
                    )
                }
            }
    }

    private fun setEventName(value: String) {
        val userName = viewState.textEventName.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textEventName = userName,
            )
        }
    }

    private fun setSwitchEventDateAllDay(value: Boolean) {
        updateState { state ->
            state.copy(
                switchEventDateAllDay = value,
            )
        }
    }

    private fun setSwitchEventRepeat(value: Boolean) {
        updateState { state ->
            state.copy(
                switchEventRepeat = value,
            )
        }
    }

    private fun showStartDatePicker() {
        updateState { state ->
            state.copy(
                showStartDatePicker = true,
            )
        }
    }

    private fun setEventStartDate(date: LocalDate) {
        updateState { state ->
            state.copy(
                eventStartDate = date,
            )
        }
    }

    private fun showStartTimePicker() {
        updateState { state ->
            state.copy(
                showStartTimePicker = true,
            )
        }
    }

    private fun setEventStartTime(time: LocalTime) {
        updateState { state ->
            state.copy(
                eventStartTime = time,
            )
        }
    }

    private fun showEndDatePicker() {
        updateState { state ->
            state.copy(
                showEndDatePicker = true,
            )
        }
    }

    private fun setEventEndDate(date: LocalDate) {
        updateState { state ->
            state.copy(
                eventEndDate = date,
            )
        }
    }

    private fun showEndTimePicker() {
        updateState { state ->
            state.copy(
                showEndTimePicker = true,
            )
        }
    }

    private fun setEventEndTime(time: LocalTime) {
        updateState { state ->
            state.copy(
                eventEndTime = time,
            )
        }
    }

    private fun setSelectedRepeatType(value: Int?) {
        updateState { state ->
            state.copy(
                selectedRepeatType = value,
            )
        }
    }

    private fun setSelectedTaskType(value: GetTaskTypeUIModelType?) {
        updateState { state ->
            state.copy(
                selectedTaskType = value,
            )
        }
    }

    private fun showAddTaskTypeDialog() {
        setTaskName("")
        updateState { state ->
            state.copy(
                showAddTaskTypeDialog = true,
            )
        }
    }

    private fun setTaskName(value: String) {
        val value = viewState.textTaskName.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textTaskName = value,
            )
        }
    }

    private fun setSearchUser(value: String) {
        val value = viewState.textSearchUser.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textSearchUser = value,
            )
        }
    }

    private fun setGetTrainingGroupUserUIModelTeam(value: GetTrainingGroupUserUIModelTeam?) {
        updateState { state ->
            state.copy(
                selectedGetTrainingGroupUserUIModelTeam = value,
                selectedGetTrainingGroupUserList = emptyList(),
            )
        }
    }

    private fun setSelectedGetTrainingGroupUser(value: GetTrainingGroupUserUIModelUser) {
        val list = viewState.selectedGetTrainingGroupUserList.toMutableList()
        if (list.any { it.id == value.id }) {
            list.removeIf { it.id == value.id }
        } else {
            list.add(value)
        }

        updateState { state ->
            state.copy(
                selectedGetTrainingGroupUserUIModelTeam = null,
                selectedGetTrainingGroupUserList = list,
            )
        }
    }

    private fun showAddUserBottomSheet() {
        updateState { state ->
            state.copy(
                showAddUserBottomSheet = true,
            )
        }
    }

    private fun showAddDescriptionDialog() {
        updateState { state ->
            state.copy(
                showAddDescriptionDialog = true,
            )
        }
    }

    private fun changeCheckBoxDraftState() {
        updateState { state ->
            state.copy(
                isDraft = !state.isDraft,
            )
        }
    }

    private fun setSelectedAddressChanged(addressTitle: String, addressDetail: String, city: String?, country: String?, latitude: Double?, longitude: Double?) {
        updateState { state ->
            state.copy(
                addressTitle = addressTitle,
                addressDetail = addressDetail,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude,
            )
        }
    }

    private fun setDescription(value: String) {
        val value = viewState.textDescription.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textDescription = value,
            )
        }
    }

    private fun showRecurrenceEndDatePicker() {
        updateState { state ->
            state.copy(
                showRecurrenceEndDatePicker = true,
            )
        }
    }

    private fun setRecurrenceEndDate(date: LocalDate) {
        updateState { state ->
            state.copy(
                recurrenceEndDate = date,
            )
        }
    }

    private fun addTask() {
        if (viewState.textEventName.isEmpty) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = null,
                        message = "Etkinlik Başlığı boş olamaz!", // TODO: Localize,
                        confirmButton = "Tamam", // TODO: Localize,
                        dismissButton = null,
                    ),
                )
            }
            return
        }

        if (viewState.switchEventRepeat && viewState.selectedRepeatType == null) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = null,
                        message = "Etkinlik Tekrarı seçiniz!", // TODO: Localize,
                        confirmButton = "Tamam", // TODO: Localize,
                        dismissButton = null,
                    ),
                )
            }
            return
        }

        if (viewState.selectedTaskType == null) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = null,
                        message = "Etkinlik Tipi seçiniz!", // TODO: Localize,
                        confirmButton = "Tamam", // TODO: Localize,
                        dismissButton = null,
                    ),
                )
            }
            return
        }

        if (viewState.textDescription.isEmpty) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = null,
                        message = "Açıklama boş olamaz!", // TODO: Localize,
                        confirmButton = "Tamam", // TODO: Localize,
                        dismissButton = null,
                    ),
                )
            }
            return
        }

        val request = AddTaskRequest(
            title = viewState.textEventName.value,
            description = viewState.textDescription.value,
            startDate = viewState.eventStartDate.toString() + " " + viewState.eventStartTime.toString(),
            endDate = viewState.eventEndDate.toString() + " " + viewState.eventEndTime.toString(),
            allDay = viewState.switchEventDateAllDay,
            location = AddTaskRequestLocation(
                title = viewState.addressTitle,
                address = viewState.addressDetail,
                lat = viewState.latitude ?: 0.0,
                lng = viewState.longitude ?: 0.0,
            ),
            isRecurring = viewState.switchEventRepeat,
            isDraft = viewState.isDraft,
            userIds = if (viewState.selectedGetTrainingGroupUserList.isEmpty()) null else viewState.selectedGetTrainingGroupUserList.mapNotNull { it.id },
            trainingGroupIds = if (viewState.selectedGetTrainingGroupUserUIModelTeam == null) null else listOfNotNull(viewState.selectedGetTrainingGroupUserUIModelTeam?.value),
            taskType = viewState.selectedTaskType?.value,
            recurrence = if (viewState.switchEventRepeat) viewState.selectedRepeatType else null,
            recurrenceEndDate = if (viewState.switchEventRepeat) viewState.recurrenceEndDate.toString() else null,
        )

        addTaskUseCase.invoke(request)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                setSideEffect(AddEventScreenContract.SideEffect.PopBackStack)
            }
    }
}

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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.impl.CreateTeamEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModelBranch
import com.iamkurtgoz.feature.home.createTeam.domain.useCase.AddSportClubUseCase
import com.iamkurtgoz.feature.home.createTeam.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.createTeam.domain.useCase.ImageUploadUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class CreateTeamViewModel @Inject constructor(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val addSportClubUseCase: AddSportClubUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val appPreferences: AppPreferences,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<CreateTeamScreenContract.State, CreateTeamScreenContract.SideEffect, CreateTeamScreenContract.Event>(
    initialState = CreateTeamScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: CreateTeamScreenContract.Event) {
        when (event) {
            is CreateTeamScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CreateTeamScreenContract.Event.NavigateUp -> setSideEffect(CreateTeamScreenContract.SideEffect.NavigateUp)
            is CreateTeamScreenContract.Event.PopBackStack -> setSideEffect(CreateTeamScreenContract.SideEffect.PopBackStack)
            is CreateTeamScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CreateTeamScreenContract.Event.SetShowStatePhotoPicker -> setShowStatePhotoPicker(event.isShow)
            is CreateTeamScreenContract.Event.SetSelectedImage -> setSelectedImage(event.imagePath)
            is CreateTeamScreenContract.Event.SetClubName -> setClubName(event.value)
            is CreateTeamScreenContract.Event.SetAddressDetailName -> setAddressDetailName(event.value)
            is CreateTeamScreenContract.Event.SetClubCreateYear -> setClubCreateYear(event.value)
            is CreateTeamScreenContract.Event.CreateClub -> createClub()
            is CreateTeamScreenContract.Event.ShowYearPicker -> setShowYearPicker(true)
            is CreateTeamScreenContract.Event.HideYearPicker -> setShowYearPicker(false)
            is CreateTeamScreenContract.Event.NavigateToSelectAddress -> setSideEffect(CreateTeamScreenContract.SideEffect.NavigateToSelectAddress)
            is CreateTeamScreenContract.Event.SelectedAddressChanged -> selectedAddressChanged(
                title = event.title,
                address = event.address,
                city = event.city,
                country = event.country,
            )
            is CreateTeamScreenContract.Event.NavigateToSendClubAuthDocument -> {
                val model = HomeScreenSendClubAuthDocumentScreenNavigateModel(
                    clubId = viewState.createdClubModel?.id,
                    founderUserId = viewState.createdClubModel?.founderUserId,
                    clubName = viewState.createdClubModel?.clubName,
                    logo = viewState.createdClubModel?.logo,
                )
                setSideEffect(CreateTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument(model = model))
            }
            is CreateTeamScreenContract.Event.NavigateToTrainingScreen -> {
                val model = HomeScreenTrainingScreenNavigateModel(
                    clubId = viewState.createdClubModel?.id,
                    founderUserId = viewState.createdClubModel?.founderUserId,
                    clubName = viewState.createdClubModel?.clubName,
                    logo = viewState.createdClubModel?.logo,
                )
                setSideEffect(CreateTeamScreenContract.SideEffect.NavigateToTrainingScreen(model = model))
            }
            is CreateTeamScreenContract.Event.NavigateToHome -> setSideEffect(CreateTeamScreenContract.SideEffect.NavigateToHome)
            is CreateTeamScreenContract.Event.NavigateToCreateTeamSelectBranchScreen -> setSideEffect(CreateTeamScreenContract.SideEffect.NavigateToCreateTeamSelectBranchScreen)
            is CreateTeamScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.event)
        }
    }
    private fun setShowYearPicker(isShow: Boolean) {
        updateState { it.copy(showYearPicker = isShow) }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        appPreferences.currentPreferenceState.firstOrNull()?.let { currentPreferenceState ->
            updateState { state ->
                state.copy(
                    customUserRole = currentPreferenceState.customUserRole,
                )
            }
        }
        if (appBuildConfigStatePack.isDebug) {
            setClubName("Test Sport Club ${Random.nextInt(0, 10000)}")
            updateState { state ->
                state.copy(
                    textCity = "Test Sport Club ${Random.nextInt(0, 10000)}",
                    textCountry = "Test Sport Club ${Random.nextInt(0, 10000)}",
                    textAddressTitle = "Test Sport Club ${Random.nextInt(0, 10000)}",
                )
            }
            setAddressDetailName("Test Address ${Random.nextInt(0, 10000)}")
            setClubCreateYear("2023")
        }
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showPhotoPicker = false,
            )
        }
    }

    private fun setShowStatePhotoPicker(isShow: Boolean) {
        updateState { state ->
            state.copy(
                showPhotoPicker = isShow,
            )
        }
    }

    private fun setSelectedImage(imagePath: String?) {
        imagePath?.let {
            updateState { state ->
                state.copy(
                    selectedImage = File(imagePath),
                )
            }
        }
    }

    private fun setClubName(value: String) {
        val userName = viewState.textClubName.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textClubName = userName,
            )
        }
    }

    private fun setAddressDetailName(value: String) {
        val userName = viewState.textAddressDetailName.copy(
            value = value,
            isError = !value.isNotEmpty(),
        )
        updateState { state ->
            state.copy(
                textAddressDetailName = userName,
            )
        }
    }

    private fun setClubCreateYear(value: String) {
        val userName = viewState.textClubCreateYear.copy(
            value = value,
            isError = !value.isNumber(),
        )
        updateState { state ->
            state.copy(
                textClubCreateYear = userName,
            )
        }
    }

    private fun selectedAddressChanged(title: String, address: String, city: String?, country: String?) {
        updateState { state ->
            state.copy(
                textAddressTitle = title,
                textAddressDetailName = AppTextFieldValue(
                    value = address,
                ),
                textCity = city,
                textCountry = country,
            )
        }
    }

    private fun createClub() = viewModelScope.launch {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return@launch
        }
        if (viewState.selectedImage == null) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = resourcesR.string.general_any_error_message,
                        message = "Lütfen kulüp logosunu seçin", // TODO: Localize
                        confirmButton = resourcesR.string.button_ok_button,
                        dismissButton = null,
                    ),
                )
            }
            return@launch
        }

        val logo = try {
            requestImageUpload()
        } catch (_: Exception) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = resourcesR.string.general_any_error_message,
                        message = "Bir hata oluştu#img_up", // TODO: Localize
                        confirmButton = resourcesR.string.button_ok_button,
                        dismissButton = null,
                    ),
                )
            }
            return@launch
        }

        val params = AddSportClubRequest(
            address = viewState.textAddressDetailName.value,
            city = viewState.textCity,
            clubName = viewState.textClubName.value,
            county = viewState.textCountry,
            foundationYear = viewState.textClubCreateYear.value,
            logo = requestImageUpload(),
            branchId = viewState.textSelectedBranch?.value,
        )

        addSportClubUseCase.invoke(params)
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
            .callWithSuccess { clubUIModel ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        createdClubModel = clubUIModel,
                    )
                }
            }
    }

    private suspend fun requestImageUpload(): String? {
        val response: CompletableDeferred<String?> = CompletableDeferred()

        val mediaFileDataList: MutableList<File> = mutableListOf()
        viewState.selectedImage?.let {
            if (it.isFile && it.exists()) {
                mediaFileDataList.add(it)
            }
        }
        val onUploadProgress: (Int) -> Unit = {
            Timber.d(it.toString())
        }
        val body = ImageUploadUseCaseParams(
            files = mediaFileDataList,
            onUploadProgress = onUploadProgress,
        )

        imageUploadUseCase.invoke(body)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                response.completeExceptionally(error)
            }
            .callWithSuccess {
                response.complete(it.firstOrNull())
            }

        return response.await()
    }

    private fun updateEventBusStatus(status: CreateTeamEventBus.Event) {
        when (status) {
            is CreateTeamEventBus.Event.UpdateSelectedBranch -> {
                val newBranchUIModel = GetSportClubDomainModelBranch(
                    name = status.branchTitle,
                    value = status.branchId,
                    val2 = null,
                )
                updateState { state ->
                    state.copy(
                        textSelectedBranch = newBranchUIModel,
                    )
                }
            }
        }
    }
}

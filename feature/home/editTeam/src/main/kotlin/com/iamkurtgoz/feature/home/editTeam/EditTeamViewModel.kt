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
package com.iamkurtgoz.feature.home.editTeam

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.navigation.model.home.editTeam.toHomeScreenEditTeamRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.impl.EditTeamEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModelBranch
import com.iamkurtgoz.feature.home.editTeam.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.editTeam.domain.useCase.ImageUploadUseCaseParams
import com.iamkurtgoz.feature.home.editTeam.domain.useCase.UpdateSportClubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class EditTeamViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val updateSportClubUseCase: UpdateSportClubUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
) : CoreViewModel<EditTeamScreenContract.State, EditTeamScreenContract.SideEffect, EditTeamScreenContract.Event>(
    initialState = EditTeamScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenEditTeamRouteTypeMap(),
    ),
) {
    override fun setEvent(event: EditTeamScreenContract.Event) {
        when (event) {
            is EditTeamScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is EditTeamScreenContract.Event.NavigateUp -> setSideEffect(EditTeamScreenContract.SideEffect.NavigateUp)
            is EditTeamScreenContract.Event.PopBackStack -> setSideEffect(EditTeamScreenContract.SideEffect.PopBackStack)
            is EditTeamScreenContract.Event.DismissDialogs -> dismissDialogs()
            is EditTeamScreenContract.Event.SetShowStatePhotoPicker -> setShowStatePhotoPicker(event.isShow)
            is EditTeamScreenContract.Event.SetSelectedImage -> setSelectedImage(event.imagePath)
            is EditTeamScreenContract.Event.SetClubName -> setClubName(event.value)
            is EditTeamScreenContract.Event.SetAddressDetailName -> setAddressDetailName(event.value)
            is EditTeamScreenContract.Event.SetClubCreateYear -> setClubCreateYear(event.value)
            is EditTeamScreenContract.Event.NavigateToSelectAddress -> setSideEffect(EditTeamScreenContract.SideEffect.NavigateToSelectAddress)
            is EditTeamScreenContract.Event.SelectedAddressChanged -> selectedAddressChanged(
                title = event.title,
                address = event.address,
                city = event.city,
                country = event.country,
            )
            is EditTeamScreenContract.Event.NavigateToSendClubAuthDocument -> {
                val model = HomeScreenSendClubAuthDocumentScreenNavigateModel(
                    clubId = viewState.editedClubModel?.id,
                    founderUserId = viewState.editedClubModel?.founderUserId,
                    clubName = viewState.editedClubModel?.clubName,
                    logo = viewState.editedClubModel?.logo,
                )
                setSideEffect(EditTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument(model = model))
            }
            is EditTeamScreenContract.Event.NavigateToTrainingScreen -> {
                val model = HomeScreenTrainingScreenNavigateModel(
                    clubId = viewState.editedClubModel?.id,
                    founderUserId = viewState.editedClubModel?.founderUserId,
                    clubName = viewState.editedClubModel?.clubName,
                    logo = viewState.editedClubModel?.logo,
                )
                setSideEffect(EditTeamScreenContract.SideEffect.NavigateToTrainingScreen(model = model))
            }
            is EditTeamScreenContract.Event.NavigateToHome -> setSideEffect(EditTeamScreenContract.SideEffect.NavigateToHome)
            is EditTeamScreenContract.Event.EditClub -> editClub()
            is EditTeamScreenContract.Event.NavigateToEditTeamSelectBranchScreen -> setSideEffect(EditTeamScreenContract.SideEffect.NavigateToEditTeamSelectBranchScreen)
            is EditTeamScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.event)
        }
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
        with(viewState.route.model) {
            updateState { state ->
                state.copy(
                    originalImageUrl = logo,
                    textAddressTitle = county,
                    textCity = city,
                    textCountry = county,
                    textSelectedBranch = GetSportClubDomainModelBranch(
                        name = branchName,
                        value = value,
                        val2 = val2,
                    ),
                )
            }
            setClubName(clubName ?: "")
            setAddressDetailName(address ?: "")
            setClubCreateYear(foundationYear ?: "")
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

    private fun editClub() = viewModelScope.launch {
        if (viewState.isFieldsAnyError) {
            updateState { state ->
                state.copy(
                    isFieldErrorShow = true,
                )
            }
            return@launch
        }
        if (viewState.originalImageUrl == null && viewState.selectedImage == null) {
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
            if (viewState.selectedImage != null) {
                requestImageUpload()
            } else {
                viewState.originalImageUrl
            }
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

        val params = UpdateSportClubRequest(
            id = viewState.route.model.clubId,
            address = viewState.textAddressDetailName.value,
            city = viewState.textCity,
            clubName = viewState.textClubName.value,
            county = viewState.textCountry,
            foundationYear = viewState.textClubCreateYear.value,
            logo = logo,
            branchId = viewState.textSelectedBranch?.value,
        )

        updateSportClubUseCase.invoke(params)
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
                        editedClubModel = clubUIModel,
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

    private fun updateEventBusStatus(status: EditTeamEventBus.Event) {
        when (status) {
            is EditTeamEventBus.Event.UpdateSelectedBranch -> {
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

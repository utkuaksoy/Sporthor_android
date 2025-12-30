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
package com.iamkurtgoz.feature.home.sendClubAuthDocument

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.DocumentUploadSectionItem
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.toHomeScreenSendClubAuthDocumentRoute
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.feature.home.sendClubAuthDocument.SendClubAuthDocumentScreenContract.SideEffect.NavigateToTrainingScreen
import com.iamkurtgoz.feature.home.sendClubAuthDocument.domain.useCase.FileUploadUseCase
import com.iamkurtgoz.feature.home.sendClubAuthDocument.domain.useCase.FileUploadUseCaseParams
import com.iamkurtgoz.feature.home.sendClubAuthDocument.domain.useCase.UpdateSportClubFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class SendClubAuthDocumentViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val fileUploadUseCase: FileUploadUseCase,
    private val updateSportClubFilesUseCase: UpdateSportClubFilesUseCase,
    private val appPreferences: AppPreferences,
) : CoreViewModel<SendClubAuthDocumentScreenContract.State, SendClubAuthDocumentScreenContract.SideEffect, SendClubAuthDocumentScreenContract.Event>(
    initialState = SendClubAuthDocumentScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenSendClubAuthDocumentRoute(),
    ),
) {
    override fun setEvent(event: SendClubAuthDocumentScreenContract.Event) {
        when (event) {
            is SendClubAuthDocumentScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SendClubAuthDocumentScreenContract.Event.NavigateUp -> setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.NavigateUp)
            is SendClubAuthDocumentScreenContract.Event.PopBackStack -> setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.PopBackStack)
            is SendClubAuthDocumentScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SendClubAuthDocumentScreenContract.Event.AddNewDocumentClick -> addNewDocumentClick()
            is SendClubAuthDocumentScreenContract.Event.OnSelectDocumentClick -> onSelectDocumentClick(event.index)
            is SendClubAuthDocumentScreenContract.Event.OnDocumentPicked -> onDocumentPicked(event.path, event.extension)
            is SendClubAuthDocumentScreenContract.Event.NavigateToTrainingScreen -> {
                val model = HomeScreenTrainingScreenNavigateModel(
                    clubId = viewState.route.model.clubId,
                    founderUserId = viewState.route.model.founderUserId,
                    clubName = viewState.route.model.clubName,
                    logo = viewState.route.model.logo,
                )
                setSideEffect(NavigateToTrainingScreen(model = model))
            }
            is SendClubAuthDocumentScreenContract.Event.UploadRequest -> requestFileUpload()
            is SendClubAuthDocumentScreenContract.Event.NavigateToHome -> setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.NavigateToHome)
            is SendClubAuthDocumentScreenContract.Event.NavigateToWebView -> setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.NavigateToWebView(event.routeType))
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
    }

    private fun dismissDialogs() {
        updateState {
            it.copy(
                alertDialogModel = null,
                selectedDocumentIndex = null,
            )
        }
    }

    private fun addNewDocumentClick() {
        val newDocument = DocumentUploadSectionItem(
            index = viewState.documentList.size,
            file = null,
        )
        val documentList = viewState.documentList.toMutableList().apply {
            add(newDocument)
        }
        updateState { state ->
            state.copy(
                documentList = documentList,
            )
        }
    }

    private fun onSelectDocumentClick(index: Int) {
        updateState { state ->
            state.copy(
                selectedDocumentIndex = index,
            )
        }
    }

    private fun onDocumentPicked(path: String?, extension: String?) {
        updateState { state ->
            state.copy(
                documentList = state.documentList.mapIndexed { index, item ->
                    if (index == state.selectedDocumentIndex) {
                        item.copy(
                            file = path,
                            extension = extension,
                        )
                    } else {
                        item
                    }
                },
            )
        }
        dismissDialogs()
    }

    private fun requestFileUpload() = viewModelScope.launch {
        val mediaFileDataList: MutableList<File> = mutableListOf()
        viewState.documentList.mapNotNull { it.file }.map {
            val file = File(it)
            if (file.isFile && file.exists()) {
                mediaFileDataList.add(file)
            }
        }
        val onUploadProgress: (Int) -> Unit = {
            Timber.d(it.toString())
        }
        val body = FileUploadUseCaseParams(
            files = mediaFileDataList,
            onUploadProgress = onUploadProgress,
        )

        fileUploadUseCase.invoke(body)
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
            .callWithSuccess { fileList ->
                updateSportClubFiles(fileList)
            }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun updateSportClubFiles(fileList: List<String>) = viewModelScope.launch {
        try {
            fileList.fastForEach {
                val params = UpdateSportClubFilesRequest(
                    clubId = viewState.route.model.clubId,
                    file = it,
                )
                updateSportClubFilesUseCase.invoke(params)
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
                    .call()
            }

            if (viewState.route.fromGenerateClub) {
                setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.NavigateToHome)
            } else {
                val model = HomeScreenSuccessDocumentUploadScreenNavigateModel(
                    clubId = viewState.route.model.clubId,
                    founderUserId = viewState.route.model.founderUserId,
                    clubName = viewState.route.model.clubName,
                    logo = viewState.route.model.logo,
                )
                setSideEffect(SendClubAuthDocumentScreenContract.SideEffect.NavigateToSuccessDocumentUploadScreen(model))
            }
        } catch (e: Exception) {
            updateState { state ->
                state.copy(
                    isLoading = false,
                    alertDialogModel = e.toAlertDialog,
                )
            }
        }
    }
}

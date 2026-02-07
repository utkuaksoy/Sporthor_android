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
package com.iamkurtgoz.feature.home.share.complete

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.helper.FileHelper
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.share.complete.toHomeScreenShareCompleteRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.domain.model.request.CreatePostRequest
import com.iamkurtgoz.domain.model.request.CreateStoryRequest
import com.iamkurtgoz.domain.model.request.MediaRequest
import com.iamkurtgoz.feature.home.share.complete.domain.params.ImageUploadUseCaseParams
import com.iamkurtgoz.feature.home.share.complete.domain.params.VideoUploadUseCaseParams
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.CreatePostUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.CreateStoryUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.VideoUploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class CompleteViewModel @Inject constructor(
    @ApplicationContext private val application: Application,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val videoUploadUseCase: VideoUploadUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val createStoryUseCase: CreateStoryUseCase,
    private val appEventBus: AppEventBus,
) : CoreViewModel<CompleteScreenContract.State, CompleteScreenContract.SideEffect, CompleteScreenContract.Event>(
    initialState = CompleteScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toHomeScreenShareCompleteRoute(),
    ),
) {
    override fun setEvent(event: CompleteScreenContract.Event) {
        when (event) {
            is CompleteScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CompleteScreenContract.Event.NavigateUp -> setSideEffect(CompleteScreenContract.SideEffect.NavigateUp)
            is CompleteScreenContract.Event.PopBackStack -> setSideEffect(CompleteScreenContract.SideEffect.PopBackStack)
            is CompleteScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CompleteScreenContract.Event.SetLoadingStatus -> setLoadingStatus(event.isLoading)
            is CompleteScreenContract.Event.SetTextContent -> setTextContent(event.value)
            is CompleteScreenContract.Event.SetStoryLinkUrl -> setStoryLinkUrl(event.value)
            is CompleteScreenContract.Event.Share -> share()
            is CompleteScreenContract.Event.ShareStoryImage -> shareStoryImage(event.file)
            is CompleteScreenContract.Event.ShareStoryVideo -> shareStoryVideo()
            is CompleteScreenContract.Event.SetStoryOverlayInputDialog -> setStoryOverlayInputDialog(event.storyOverlayInputDialog)
            is CompleteScreenContract.Event.NavigateToSelectAddressScreen -> setSideEffect(CompleteScreenContract.SideEffect.NavigateToSelectAddressScreen)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                storyOverlayInputDialog = null,
            )
        }
    }

    private fun setLoadingStatus(isLoading: Boolean) {
        updateState { state ->
            state.copy(
                isLoading = isLoading,
            )
        }
    }

    private fun setTextContent(value: String) {
        updateState { state ->
            state.copy(
                textContent = state.textContent.copy(
                    value = value,
                ),
            )
        }
    }

    private fun setStoryLinkUrl(value: String?) {
        updateState { state ->
            state.copy(
                storyLinkUrl = value?.normalizeLink(),
            )
        }
    }

    private fun share() = viewModelScope.launch {
        if (viewState.navigateRoute.routeType.selectedMediaList.isEmpty()) {
            return@launch
        }

        updateState { state ->
            state.copy(
                isLoading = true,
            )
        }

        val mediaList: MutableList<Pair<CustomMediaType, String>> = mutableListOf()
        mediaList.addAll(uploadImages())
        mediaList.addAll(videoImages())

        createPost(mediaList)
    }

    private fun shareStoryImage(file: File) {
        val params = ImageUploadUseCaseParams(
            files = listOf(file),
            onUploadProgress = {},
        )
        imageUploadUseCase.invoke(params)
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
                response.firstOrNull()?.let {
                    createStory(CustomMediaType.IMAGE, it)
                }
            }
    }

    private fun shareStoryVideo() {
        val files = viewState.navigateRoute.routeType.selectedMediaList.filter {
            it.customMediaType == CustomMediaType.VIDEO
        }.mapNotNull {
            val uri = it.uri
            if (uri != null) {
                val address = FileHelper.getRealPathFromURI(
                    context = application,
                    uri = uri,
                )
                if (address != null) {
                    File(address)
                } else {
                    null
                }
            } else {
                null
            }
        }
        val params = VideoUploadUseCaseParams(
            files = files,
            onUploadProgress = {},
        )
        videoUploadUseCase.invoke(params)
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
                response.firstOrNull()?.let {
                    createStory(CustomMediaType.VIDEO, it)
                }
            }
    }

    private suspend fun uploadImages(): List<Pair<CustomMediaType, String>> {
        val completableDeferred: CompletableDeferred<List<Pair<CustomMediaType, String>>> = CompletableDeferred()
        val files = viewState.navigateRoute.routeType.selectedMediaList.filter {
            it.customMediaType == CustomMediaType.IMAGE
        }.mapNotNull {
            val uri = it.uri
            if (uri != null) {
                val address = FileHelper.getRealPathFromURI(
                    context = application,
                    uri = uri,
                )
                if (address != null) {
                    File(address)
                } else {
                    null
                }
            } else {
                null
            }
        }
        if (files.isEmpty()) {
            completableDeferred.complete(emptyList())
            return completableDeferred.await()
        }
        val params = ImageUploadUseCaseParams(
            files = files,
            onUploadProgress = {},
        )
        imageUploadUseCase.invoke(params)
            .requester
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
            .callWithSuccess { response ->
                val images = response.map { url ->
                    Pair(CustomMediaType.IMAGE, url)
                }
                completableDeferred.complete(images)
            }

        return completableDeferred.await()
    }

    private suspend fun videoImages(): List<Pair<CustomMediaType, String>> {
        val completableDeferred: CompletableDeferred<List<Pair<CustomMediaType, String>>> = CompletableDeferred()
        val files = viewState.navigateRoute.routeType.selectedMediaList.filter {
            it.customMediaType == CustomMediaType.VIDEO
        }.mapNotNull {
            val uri = it.uri
            if (uri != null) {
                val address = FileHelper.getRealPathFromURI(
                    context = application,
                    uri = uri,
                )
                if (address != null) {
                    File(address)
                } else {
                    null
                }
            } else {
                null
            }
        }
        if (files.isEmpty()) {
            completableDeferred.complete(emptyList())
            return completableDeferred.await()
        }
        val params = VideoUploadUseCaseParams(
            files = files,
            onUploadProgress = {},
        )
        videoUploadUseCase.invoke(params)
            .requester
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
            .callWithSuccess { response ->
                val videos = response.map { url ->
                    Pair(CustomMediaType.VIDEO, url)
                }
                completableDeferred.complete(videos)
            }

        return completableDeferred.await()
    }

    private fun createPost(mediaList: List<Pair<CustomMediaType, String>>) = viewModelScope.launch {
        val params = CreatePostRequest(
            description = viewState.textContent.value,
            media = mediaList.map {
                MediaRequest(
                    type = it.first.value,
                    url = it.second,
                )
            },
        )
        createPostUseCase.invoke(params)
            .requester
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                setSideEffect(CompleteScreenContract.SideEffect.PopBackStackToDashboard)
            }
    }

    private fun createStory(mediaType: CustomMediaType, mediaUrl: String) = viewModelScope.launch {
        val params = CreateStoryRequest(
            mediaType = mediaType.value,
            mediaUrl = mediaUrl,
            link = viewState.storyLinkUrl,
            linkDescription = viewState.storyLinkUrl?.toLinkDescription(),
        )
        createStoryUseCase.invoke(params)
            .requester
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                appEventBus.fetchStory()
                setSideEffect(CompleteScreenContract.SideEffect.PopBackStackToDashboard)
            }
    }

    private fun setStoryOverlayInputDialog(storyOverlayInputDialog: CompleteScreenContract.StoryOverlayInputDialog?) {
        updateState { state ->
            state.copy(
                storyOverlayInputDialog = storyOverlayInputDialog,
            )
        }
    }

    private fun String.normalizeLink(): String {
        val trimmedValue = trim()
        if (trimmedValue.startsWith("http://") || trimmedValue.startsWith("https://")) {
            return trimmedValue
        }
        return "https://$trimmedValue"
    }

    private fun String.toLinkDescription(): String {
        return removePrefix("https://")
            .removePrefix("http://")
            .trimEnd('/')
    }
}

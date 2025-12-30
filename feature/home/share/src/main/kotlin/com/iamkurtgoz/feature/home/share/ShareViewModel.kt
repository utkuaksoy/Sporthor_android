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
package com.iamkurtgoz.feature.home.share

import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.toHomeScreenShareRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.impl.ShareScreenEventBus
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.share.domain.mapper.LocalMediaUIMapper
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import com.iamkurtgoz.feature.home.share.domain.useCase.LocalMediaUseCase
import com.iamkurtgoz.feature.home.share.domain.useCase.LocalMediaUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShareViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    localMediaUseCase: LocalMediaUseCase,
    savedStateHandle: SavedStateHandle,
    private val localMediaUIMapper: LocalMediaUIMapper,
) : CoreViewModel<ShareScreenContract.State, ShareScreenContract.SideEffect, ShareScreenContract.Event>(
    initialState = ShareScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toHomeScreenShareRoute(),
    ),
) {
    val localMediaPagingFlow = localMediaUseCase.invoke(
        LocalMediaUseCaseParams(
            page = 0,
        ),
    ).cachedIn(viewModelScope)

    override fun setEvent(event: ShareScreenContract.Event) {
        when (event) {
            is ShareScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ShareScreenContract.Event.NavigateUp -> setSideEffect(ShareScreenContract.SideEffect.NavigateUp)
            is ShareScreenContract.Event.PopBackStack -> setSideEffect(ShareScreenContract.SideEffect.PopBackStack)
            is ShareScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ShareScreenContract.Event.SetPagingLoadState -> setPagingLoadState(event.loadState)
            is ShareScreenContract.Event.SetSelectedLocalMediaModel -> setSelectedLocalMediaModel(event.selectedLocalMediaModel)
            is ShareScreenContract.Event.AddSelectedLocalMediaModel -> addSelectedLocalMediaModel(event.selectedLocalMediaModel)
            is ShareScreenContract.Event.RemoveSelectedLocalMediaModel -> removeSelectedLocalMediaModel(event.selectedLocalMediaModel)
            is ShareScreenContract.Event.SetSwitchedMultipleSelect -> setSwitchedMultipleSelect(event.isSwitchedMultipleSelect)
            is ShareScreenContract.Event.NavigateToCameraXScreen -> setSideEffect(ShareScreenContract.SideEffect.NavigateToCameraXScreen)
            is ShareScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(status = event.eventBusState)
            is ShareScreenContract.Event.NavigateToShareCompleteScreen -> navigateToShareCompleteScreen()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setPagingLoadState(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.NotLoading -> {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
            is LoadState.Loading -> {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            else -> {}
        }
    }

    private fun setSelectedLocalMediaModel(selectedLocalMediaModel: LocalMediaUIModel?) {
        updateState { state ->
            state.copy(
                selectedLocalMediaModels = listOfNotNull(selectedLocalMediaModel),
            )
        }

        if (viewState.navigateRoute.routeType is HomeScreenShareRouteScreenNavigateModel.CreatePost) {
            setSideEffect(ShareScreenContract.SideEffect.ScrollToTop)
        } else if (viewState.navigateRoute.routeType is HomeScreenShareRouteScreenNavigateModel.CreateStory) {
            navigateToShareCompleteScreen()
        }
    }

    private fun addSelectedLocalMediaModel(selectedLocalMediaModel: LocalMediaUIModel?) {
        val selectedLocalMediaModels = viewState.selectedLocalMediaModels.toMutableSet()
        if (selectedLocalMediaModel != null && selectedLocalMediaModels.size <= AppDefaults.FOUR) {
            selectedLocalMediaModels.add(selectedLocalMediaModel)
        }
        updateState { state ->
            state.copy(
                selectedLocalMediaModels = selectedLocalMediaModels.toList(),
            )
        }
    }

    private fun removeSelectedLocalMediaModel(selectedLocalMediaModel: LocalMediaUIModel?) {
        val selectedLocalMediaModels = viewState.selectedLocalMediaModels.toMutableSet()
        if (selectedLocalMediaModel != null) {
            selectedLocalMediaModels.remove(selectedLocalMediaModel)
        }
        updateState { state ->
            state.copy(
                selectedLocalMediaModels = selectedLocalMediaModels.toList(),
            )
        }
    }

    private fun setSwitchedMultipleSelect(isSwitchedMultipleSelect: Boolean) {
        updateState { state ->
            state.copy(
                isSwitchedMultipleSelect = isSwitchedMultipleSelect,
                selectedLocalMediaModels = listOf(),
            )
        }
    }

    private fun updateEventBusStatus(status: ShareScreenEventBus.Event) {
        when (status) {
            is ShareScreenEventBus.Event.MediaFileSaved -> {
                setSelectedLocalMediaModel(localMediaUIMapper.map(status.localMediaDomainModel))
            }
        }
    }

    private fun navigateToShareCompleteScreen() = viewModelScope.launch {
        val selectedMediaList = viewState.selectedLocalMediaModels.map {
            val customMediaType = when (it.mediaType) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> CustomMediaType.IMAGE
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> CustomMediaType.VIDEO
                else -> CustomMediaType.IMAGE
            }
            val uri = it.uri ?: return@launch
            HomeScreenShareCompleteScreenNavigateModelMediaItem(
                customMediaType = customMediaType,
                uri = uri,
            )
        }
        if (viewState.selectedLocalMediaModels.isEmpty()) return@launch
        val effect = ShareScreenContract.SideEffect.NavigateToShareCompleteScreen(
            routeType = HomeScreenShareCompleteScreenNavigateModel(
                shareType = when (viewState.navigateRoute.routeType) {
                    is HomeScreenShareRouteScreenNavigateModel.CreatePost -> HomeScreenShareCompleteShareTypeScreenNavigateModel.CreatePost
                    is HomeScreenShareRouteScreenNavigateModel.CreateStory -> HomeScreenShareCompleteShareTypeScreenNavigateModel.CreateStory
                },
                selectedMediaList = selectedMediaList,
            ),
        )
        setSideEffect(effect)
    }
}

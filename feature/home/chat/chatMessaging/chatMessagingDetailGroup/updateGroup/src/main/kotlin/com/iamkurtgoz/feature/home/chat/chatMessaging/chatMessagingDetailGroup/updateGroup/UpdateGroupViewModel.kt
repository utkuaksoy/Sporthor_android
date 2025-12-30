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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateChatGroupRequest
import com.iamkurtgoz.domain.model.request.UpdateProfileImageRequest
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.model.IconUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.useCase.GetChatGroupSummaryUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.useCase.ImageUploadUseCaseParams
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.useCase.UpdateChatGroupUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain.useCase.UpdateProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class UpdateGroupViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val updateGroupUseCase: GetChatGroupSummaryUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val updateChatGroupUseCase: UpdateChatGroupUseCase,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<UpdateGroupScreenContract.State, UpdateGroupScreenContract.SideEffect, UpdateGroupScreenContract.Event>(
    initialState = UpdateGroupScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: UpdateGroupScreenContract.Event) {
        when (event) {
            is UpdateGroupScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is UpdateGroupScreenContract.Event.NavigateUp -> setSideEffect(UpdateGroupScreenContract.SideEffect.NavigateUp)
            is UpdateGroupScreenContract.Event.PopBackStack -> setSideEffect(UpdateGroupScreenContract.SideEffect.PopBackStack)
            is UpdateGroupScreenContract.Event.DismissDialogs -> dismissDialogs()
            is UpdateGroupScreenContract.Event.IconSelected -> onIconSelected(event.icon)
            is UpdateGroupScreenContract.Event.SetShowStatePhotoPicker -> setShowStatePhotoPicker(event.isShow)
            is UpdateGroupScreenContract.Event.SetSelectedImage -> setSelectedImage(event.imagePath)
            is UpdateGroupScreenContract.Event.UpdateChatGroup -> updateChatGroup(image = event.groupImage, name = event.groupName)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        updateGroup(viewState.route.groupId)
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showPhotoPicker = false,
            )
        }
    }

    private fun updateGroup(groupId: String?) {
        updateGroupUseCase.invoke(groupId)
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
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        iconList = it.icons,
                        groupName = it.groupName,
                        groupImage = it.groupImageUrl,
                    )
                }
            }
    }

    private fun onIconSelected(icon: IconUIModel) {
        updateState { state ->
            state.copy(groupImage = icon.iconPath)
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
                    groupImage = imagePath,
                )
            }
        }
        requestImageUpload()
    }

    private fun requestImageUpload() {
        val mediaFileDataList: MutableList<File> = mutableListOf()
        viewState.selectedImage?.let {
            if (it.isFile && it.exists()) {
                mediaFileDataList.add(it)
            }
        }
        val onUploadProgress: (Int) -> Unit = {
            updateState { state ->
                state.copy(
                    onUploadProgress = it,
                )
            }
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
                        onUploadProgress = null,
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                        onUploadProgress = null,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        onUploadProgress = null,
                    )
                }
                updateProfileImage(imageUrl = it.firstOrNull())
            }
    }

    private fun updateProfileImage(imageUrl: String?) {
        val params = UpdateProfileImageRequest(
            imageUrl = imageUrl,
        )

        updateProfileImageUseCase.invoke(params)
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
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }

    private fun updateChatGroup(image: String?, name: String?) {
        val request = UpdateChatGroupRequest(
            groupId = viewState.route.groupId,
            image = image,
            name = name,
            newUsers = null,
        )
        updateChatGroupUseCase.invoke(request)
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
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }
}

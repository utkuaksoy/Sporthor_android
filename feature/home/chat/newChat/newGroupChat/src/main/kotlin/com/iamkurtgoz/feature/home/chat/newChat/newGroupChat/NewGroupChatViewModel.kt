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
package com.iamkurtgoz.feature.home.chat.newChat.newGroupChat

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.GenerateChatGroupRequest
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.model.MyFriendsFriendItemUIModel
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.useCase.GenerateChatGroupUseCase
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.useCase.GetMyFriendsUseCase
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.useCase.ImageUploadUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import com.iamkurtgoz.core.resources.R as resourcesR

@HiltViewModel
internal class NewGroupChatViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getMyFriendsUseCase: GetMyFriendsUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val generateChatGroupUseCase: GenerateChatGroupUseCase,
) : CoreViewModel<NewGroupChatScreenContract.State, NewGroupChatScreenContract.SideEffect, NewGroupChatScreenContract.Event>(
    initialState = NewGroupChatScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: NewGroupChatScreenContract.Event) {
        when (event) {
            is NewGroupChatScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is NewGroupChatScreenContract.Event.NavigateUp -> setSideEffect(NewGroupChatScreenContract.SideEffect.NavigateUp)
            is NewGroupChatScreenContract.Event.PopBackStack -> setSideEffect(NewGroupChatScreenContract.SideEffect.PopBackStack)
            is NewGroupChatScreenContract.Event.DismissDialogs -> dismissDialogs()
            is NewGroupChatScreenContract.Event.SetTextGroupName -> setTextGroupName(event.value)
            is NewGroupChatScreenContract.Event.SetTextSearch -> setTextSearch(event.text)
            is NewGroupChatScreenContract.Event.ChangeSelectedUserState -> changeSelectedUserState(event.item)
            is NewGroupChatScreenContract.Event.SetShowStatePhotoPicker -> setShowStatePhotoPicker(event.isShow)
            is NewGroupChatScreenContract.Event.SetSelectedImage -> setSelectedImage(event.imagePath)
            is NewGroupChatScreenContract.Event.GenerateGroupChat -> generateGroupChat()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        getMyFriends()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showPhotoPicker = false,
            )
        }
    }

    private fun setTextGroupName(value: String) {
        updateState { state ->
            state.copy(
                textGroupName = state.textGroupName.copy(
                    value = value,
                ),
            )
        }
    }

    private fun setTextSearch(value: String) {
        updateState { state ->
            state.copy(
                textSearch = state.textSearch.copy(
                    value = value,
                ),
            )
        }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .map { it.textSearch.value }
            .onEach {
                updateState { state ->
                    state.copy(
                        myFriendsFilteredList = state.myFriendsList.filter { item -> item.isMatch(it) }.toPersistentList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getMyFriends() {
        getMyFriendsUseCase.invoke()
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
                        myFriendsList = it.friends?.filterNotNull()?.toPersistentList() ?: persistentListOf(),
                    )
                }
            }
    }

    private fun changeSelectedUserState(item: MyFriendsFriendItemUIModel) {
        val selectedUserList = viewState.selectedUserList.toMutableList()
        if (selectedUserList.any { it.id == item.id }) {
            selectedUserList.removeAll { it.id == item.id }
        } else {
            selectedUserList.add(item)
        }
        updateState { state ->
            state.copy(
                selectedUserList = selectedUserList.toPersistentList(),
            )
        }
    }

    private fun generateGroupChat() {
        if (viewState.textGroupName.isEmpty) {
            updateState { state ->
                state.copy(
                    alertDialogModel = AnyAlertDialogModel(
                        title = resourcesR.string.general_warning,
                        message = "Lütfen grup adı giriniz",
                        confirmButton = resourcesR.string.button_ok_button,
                        dismissButton = null,
                    ),
                )
            }
            return
        }
        if (viewState.selectedImage == null) {
            generateChatGroup(image = null)
        } else {
            requestImageUpload()
        }
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
                generateChatGroup(image = it.firstOrNull())
            }
    }

    private fun generateChatGroup(image: String?) {
        val params = GenerateChatGroupRequest(
            image = image,
            isPrivate = false,
            name = viewState.textGroupName.value,
            users = viewState.selectedUserList.map { it.id },
        )
        generateChatGroupUseCase.invoke(params)
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
                it.id?.let { channelId ->
                    val effect = NewGroupChatScreenContract.SideEffect.NavigateToMessagingScreen(
                        isGroup = true,
                        title = it.name ?: "",
                        channelId = channelId,
                        userId = it.toUserId ?: "",
                    )
                    setSideEffect(effect)
                }
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
}

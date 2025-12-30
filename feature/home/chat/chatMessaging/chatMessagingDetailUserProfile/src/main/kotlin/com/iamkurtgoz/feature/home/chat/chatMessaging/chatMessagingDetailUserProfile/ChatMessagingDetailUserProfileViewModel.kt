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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.impl.ChatDetailUserEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.domain.useCase.GetChatUserProfileUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.domain.useCase.GetChatUserProfileUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ChatMessagingDetailUserProfileViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getChatUserProfileUseCase: GetChatUserProfileUseCase,
    private val userActionController: UserActionController,
) : CoreViewModel<ChatMessagingDetailUserProfileScreenContract.State, ChatMessagingDetailUserProfileScreenContract.SideEffect, ChatMessagingDetailUserProfileScreenContract.Event>(
    initialState = ChatMessagingDetailUserProfileScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: ChatMessagingDetailUserProfileScreenContract.Event) {
        when (event) {
            is ChatMessagingDetailUserProfileScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ChatMessagingDetailUserProfileScreenContract.Event.NavigateUp -> setSideEffect(ChatMessagingDetailUserProfileScreenContract.SideEffect.NavigateUp)
            is ChatMessagingDetailUserProfileScreenContract.Event.PopBackStack -> setSideEffect(ChatMessagingDetailUserProfileScreenContract.SideEffect.PopBackStack)
            is ChatMessagingDetailUserProfileScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ChatMessagingDetailUserProfileScreenContract.Event.OnClickActionButton -> onClickActionButton(isFollow = event.isFollow, userId = event.userId)
            is ChatMessagingDetailUserProfileScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.eventBusState)
            is ChatMessagingDetailUserProfileScreenContract.Event.NavigateToAttachments -> setSideEffect(ChatMessagingDetailUserProfileScreenContract.SideEffect.NavigateToAttachments(userId = event.userId, groupId = event.groupId))
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getChatUserProfile()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getChatUserProfile() {

        val params = GetChatUserProfileUseCaseParams(
            userId = viewState.route.userId,
            groupId = viewState.route.groupId,
        )

        getChatUserProfileUseCase.invoke(params)
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
                        chatUserProfile = it,
                    )
                }
            }
    }

    private fun onClickActionButton(isFollow: Boolean, userId: String?) {
        userActionController.changeFollowStatus(
            scope = viewModelScope,
            targetUserId = userId,
            followType = if (!isFollow) UserActionFollowType.Follow else UserActionFollowType.UnFollow,
            onErrorAction = {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            },
        )
        changeFollowStatus(
            targetUserId = userId,
            followType = if (!isFollow) UserActionFollowType.Follow else UserActionFollowType.UnFollow,
        )
    }

    private fun updateEventBusStatus(status: ChatDetailUserEventBus.Event) {
        when (status) {
            is ChatDetailUserEventBus.Event.UpdateFollowingStatus -> {
                changeFollowStatus(
                    targetUserId = status.targetUserId,
                    followType = status.followType,
                )
            }
        }
    }

    private fun changeFollowStatus(targetUserId: String?, followType: UserActionFollowType) {
        if (viewState.chatUserProfile?.userId == targetUserId) {
            updateState { state ->
                state.copy(
                    chatUserProfile = state.chatUserProfile?.copy(
                        isFollow = followType == UserActionFollowType.Follow,
                    ),
                )
            }

            Timber.d("isFollow: ${viewState.chatUserProfile?.isFollow}")
            println(viewState.chatUserProfile)
        }
    }
}

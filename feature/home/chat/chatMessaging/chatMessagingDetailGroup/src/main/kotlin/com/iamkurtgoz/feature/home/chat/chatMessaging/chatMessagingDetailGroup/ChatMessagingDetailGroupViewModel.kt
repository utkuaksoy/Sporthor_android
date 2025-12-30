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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup

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
import com.iamkurtgoz.domain.model.request.LeaveChatRequest
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.useCase.GetChatGroupDetailUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.useCase.LeaveChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ChatMessagingDetailGroupViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getChatGroupDetailUseCase: GetChatGroupDetailUseCase,
    private val userActionController: UserActionController,
    private val leaveChatUseCase: LeaveChatUseCase,
) : CoreViewModel<ChatMessagingDetailGroupScreenContract.State, ChatMessagingDetailGroupScreenContract.SideEffect, ChatMessagingDetailGroupScreenContract.Event>(
    initialState = ChatMessagingDetailGroupScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: ChatMessagingDetailGroupScreenContract.Event) {
        when (event) {
            is ChatMessagingDetailGroupScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ChatMessagingDetailGroupScreenContract.Event.NavigateUp -> setSideEffect(ChatMessagingDetailGroupScreenContract.SideEffect.NavigateUp)
            is ChatMessagingDetailGroupScreenContract.Event.PopBackStack -> setSideEffect(ChatMessagingDetailGroupScreenContract.SideEffect.PopBackStack)
            is ChatMessagingDetailGroupScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ChatMessagingDetailGroupScreenContract.Event.OnClickActionButton -> onClickActionButton(isFollow = event.isFollow, userId = event.userId)
            is ChatMessagingDetailGroupScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.eventBusState)
            is ChatMessagingDetailGroupScreenContract.Event.NavigateToAttachments -> setSideEffect(ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToAttachments(userId = event.userId, groupId = event.groupId))
            is ChatMessagingDetailGroupScreenContract.Event.LeaveChat -> leaveChat()
            is ChatMessagingDetailGroupScreenContract.Event.NavigateToAddUser -> setSideEffect(ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToAddUser(groupId = event.groupId))
            is ChatMessagingDetailGroupScreenContract.Event.NavigateToUpdateGroup -> setSideEffect(ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToUpdateGroup(groupId = event.groupId))
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        groupDetail(viewState.route.userId)
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun groupDetail(userId: String?) {
        getChatGroupDetailUseCase.invoke(userId)
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
                        groupDetail = it,
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

    private fun changeFollowStatus(targetUserId: String?, followType: UserActionFollowType) {
        val updatedMembers = viewState.groupDetail?.members
            ?.map { member ->
                if (member?.id == targetUserId) {
                    member?.copy(isFollow = followType == UserActionFollowType.Follow)
                } else {
                    member
                }
            }

        updateState { state ->
            state.copy(
                groupDetail = state.groupDetail?.copy(
                    members = updatedMembers,
                ),
            )
        }

        Timber.d("Updated member follow: ${updatedMembers?.find { it?.id == targetUserId }?.isFollow}")
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

    private fun leaveChat() {
        val request = LeaveChatRequest(
            groupId = viewState.route.groupId,
        )
        leaveChatUseCase.invoke(request)
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

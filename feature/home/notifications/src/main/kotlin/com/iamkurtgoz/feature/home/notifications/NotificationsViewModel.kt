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
package com.iamkurtgoz.feature.home.notifications

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.ConfirmationFollowRequest
import com.iamkurtgoz.domain.model.request.ConfirmationTrainingGroupUserRequest
import com.iamkurtgoz.feature.home.notifications.domain.useCase.ConfirmationFollowUseCase
import com.iamkurtgoz.feature.home.notifications.domain.useCase.ConfirmationTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.notifications.domain.useCase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val confirmationTrainingGroupUserUseCase: ConfirmationTrainingGroupUserUseCase,
    private val confirmationFollowUseCase: ConfirmationFollowUseCase,
) : CoreViewModel<NotificationsScreenContract.State, NotificationsScreenContract.SideEffect, NotificationsScreenContract.Event>(
    initialState = NotificationsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: NotificationsScreenContract.Event) {
        when (event) {
            is NotificationsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is NotificationsScreenContract.Event.NavigateUp -> setSideEffect(NotificationsScreenContract.SideEffect.NavigateUp)
            is NotificationsScreenContract.Event.PopBackStack -> setSideEffect(NotificationsScreenContract.SideEffect.PopBackStack)
            is NotificationsScreenContract.Event.DismissDialogs -> dismissDialogs()
            is NotificationsScreenContract.Event.SetFilterType -> setFilterType(event.filterType)
            is NotificationsScreenContract.Event.SendConfirmationTrainingGroupUser -> sendConfirmationTrainingGroupUser(
                notificationId = event.notificationId,
                groupId = event.groupId,
                isAccepted = event.isAccepted,
            )
            is NotificationsScreenContract.Event.SendConfirmationFollow -> sendConfirmationFollow(
                notificationId = event.notificationId,
                targetUserId = event.targetUserId,
                isAccepted = event.isAccepted,
            )
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getNotifications()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun setFilterType(filterType: NotifListFilterType) {
        updateState { it.copy(filterType = filterType) }
    }
    private fun getNotifications() {
        getNotificationsUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        notificationList = emptyList(),
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        notificationList = emptyList(),
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        notificationList = it,
                    )
                }
            }
    }

    private fun sendConfirmationTrainingGroupUser(notificationId: String?, groupId: String?, isAccepted: Boolean) {
        val params = ConfirmationTrainingGroupUserRequest(
            notificationId = notificationId,
            groupId = groupId,
            isAccepted = isAccepted,
        )
        confirmationTrainingGroupUserUseCase.invoke(params)
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
                getNotifications()
            }
    }

    private fun sendConfirmationFollow(notificationId: String?, targetUserId: String?, isAccepted: Boolean) {
        if (!notificationId.isNullOrBlank()) {
            updateState { state ->
                state.copy(
                    followRequestDecisionMap = state.followRequestDecisionMap + (notificationId to isAccepted),
                )
            }
        }
        val params = ConfirmationFollowRequest(
            notificationId = notificationId,
            targetUserId = targetUserId,
            isAccepted = isAccepted,
        )
        confirmationFollowUseCase.invoke(params)
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
                getNotifications()
            }
    }
}

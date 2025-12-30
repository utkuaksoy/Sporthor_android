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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.useCase.GetAttachmentsUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.useCase.GetAttachmentsUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AttachmentsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val attachmentsUseCase: GetAttachmentsUseCase,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<AttachmentsScreenContract.State, AttachmentsScreenContract.SideEffect, AttachmentsScreenContract.Event>(
    initialState = AttachmentsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: AttachmentsScreenContract.Event) {
        when (event) {
            is AttachmentsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AttachmentsScreenContract.Event.NavigateUp -> setSideEffect(AttachmentsScreenContract.SideEffect.NavigateUp)
            is AttachmentsScreenContract.Event.PopBackStack -> setSideEffect(AttachmentsScreenContract.SideEffect.PopBackStack)
            is AttachmentsScreenContract.Event.DismissDialogs -> dismissDialogs()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getAttachments(userId = viewState.route.userId, groupId = viewState.route.groupId)
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getAttachments(userId: String?, groupId: String?) {
        val params = GetAttachmentsUseCaseParams(
            userId = userId,
            groupId = groupId,
        )

        attachmentsUseCase.invoke(params)
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
                        mediaList = it.medias,
                        documentList = it.files,
                    )
                }
            }
    }
}

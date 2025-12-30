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
package com.iamkurtgoz.feature.home.dashboard.component.comment

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.extensions.DateFormat
import com.iamkurtgoz.core.common.extensions.toString
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.AddCommentRequest
import com.iamkurtgoz.feature.home.dashboard.domain.model.CommentUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.AddCommentUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.GetCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
internal class CommentDialogViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
) : CoreViewModel<CommentDialogScreenContract.State, CommentDialogScreenContract.SideEffect, CommentDialogScreenContract.Event>(
    initialState = CommentDialogScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: CommentDialogScreenContract.Event) {
        when (event) {
            is CommentDialogScreenContract.Event.SetPostId -> setPostId(event.postId)
            is CommentDialogScreenContract.Event.SetComment -> setComment(event.text)
            is CommentDialogScreenContract.Event.SendComment -> sendComment()
        }
    }

    // Events functions
    private fun setPostId(postId: String?) = viewModelScope.launch {
        updateState { state ->
            state.copy(
                postId = postId,
                comments = persistentListOf(),
                textComment = AppTextFieldValue(),
            )
        }
        postId?.let {
            getComments()
        }
    }

    private fun getComments() {
        getCommentsUseCase.invoke(viewState.postId)
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
                        comments = it.toPersistentList(),
                    )
                }
            }
    }

    private fun setComment(text: String) {
        val textComment = viewState.textComment.copy(
            value = text,
        )
        updateState { state ->
            state.copy(
                textComment = textComment,
            )
        }
    }

    private fun sendComment() {
        val comment = viewState.textComment.value
        if (comment.isEmpty()) {
            return
        }

        updateState { state ->
            state.copy(
                textComment = AppTextFieldValue(),
            )
        }

        val params = AddCommentRequest(
            postId = viewState.postId,
            comment = comment,
        )
        addCommentUseCase.invoke(params)
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
                val currentPreferenceState = appPreferences.currentPreferenceState.firstOrNull()
                val commentList = viewState.comments.toMutableList()
                commentList.add(
                    CommentUIModel(
                        createdAt = LocalDateTime.now().toString(format = DateFormat.D_MMMM_EEEEE_YYYY),
                        deletedAt = null,
                        id = UUID.randomUUID().toString(),
                        isDeleted = false,
                        postId = viewState.postId,
                        status = true,
                        text = comment,
                        updatedAt = LocalDateTime.now().toString(format = DateFormat.D_MMMM_EEEEE_YYYY),
                        userId = currentPreferenceState?.userId,
                    ),
                )
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        comments = commentList.toPersistentList(),
                    )
                }
            }
    }
}

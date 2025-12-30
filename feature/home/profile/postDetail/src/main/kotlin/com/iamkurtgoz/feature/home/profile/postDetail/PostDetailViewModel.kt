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
package com.iamkurtgoz.feature.home.profile.postDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.domain.model.request.HidePostRequest
import com.iamkurtgoz.feature.home.profile.postDetail.domain.model.UserPostsItemUIModel
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.HidePostUseCase
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.UserPostsUseCase
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.UserPostsUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PostDetailViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val userPostsUseCase: UserPostsUseCase,
    private val userActionController: UserActionController,
    private val hidePostUseCase: HidePostUseCase,
) : CoreViewModel<PostDetailScreenContract.State, PostDetailScreenContract.SideEffect, PostDetailScreenContract.Event>(
    initialState = PostDetailScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: PostDetailScreenContract.Event) {
        when (event) {
            is PostDetailScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is PostDetailScreenContract.Event.NavigateUp -> setSideEffect(PostDetailScreenContract.SideEffect.NavigateUp)
            is PostDetailScreenContract.Event.PopBackStack -> setSideEffect(PostDetailScreenContract.SideEffect.PopBackStack)
            is PostDetailScreenContract.Event.DismissDialogs -> dismissDialogs()
            is PostDetailScreenContract.Event.SetPlayingVideoUrl -> setPlayingVideoUrl(event.playingVideoUrl)
            is PostDetailScreenContract.Event.DashboardFeed -> dashboardFeed(event.fetchParam)
            is PostDetailScreenContract.Event.SetLikeStatus -> setLikeStatus(postId = event.postId, actionType = event.actionType)
            is PostDetailScreenContract.Event.NavigateToMediaViewer -> setSideEffect(PostDetailScreenContract.SideEffect.NavigateToMediaViewer(event.routeType))
            is PostDetailScreenContract.Event.SetCommentDialogShowPostId -> setCommentDialogShowPostId(event.commentDialogShowPostId)
            is PostDetailScreenContract.Event.ShowReportDialog -> updateState {
                it.copy(showReportDialogForPostId = event.postId)
            }
            is PostDetailScreenContract.Event.SetHide -> hidePost(event.postId)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        dashboardFeed(fetchParam = FetchParam.INITIAL)
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                commentDialogShowPostId = null,
            )
        }
    }

    private fun setPlayingVideoUrl(playingVideoUrl: String?) {
        updateState { state ->
            state.copy(
                playingVideoUrl = playingVideoUrl,
            )
        }
    }

    private fun dashboardFeed(fetchParam: FetchParam) {
        if (viewState.paginationInitialing || viewState.paginationLoading || viewState.paginationReloading) {
            return
        }

        when (fetchParam) {
            FetchParam.INITIAL -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = true,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = true,
                        userPostFeedList = persistentListOf(),
                    )
                }
            }
            FetchParam.RELOAD -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = true,
                        paginationHasNext = true,
                        userPostFeedList = persistentListOf(),
                    )
                }
            }
            FetchParam.NEXT_PAGE -> {
                if (viewState.paginationHasNext) {
                    updateState { state ->
                        state.copy(
                            paginationPage = viewState.paginationPage.plus(AppDefaults.ONE),
                            paginationInitialing = false,
                            paginationLoading = true,
                            paginationReloading = false,
                        )
                    }
                }
            }
        }

        if (!viewState.paginationHasNext) {
            return
        }

        val params = UserPostsUseCaseParams(
            userId = viewState.userId,
            page = viewState.paginationPage,
            pageSize = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        )

        userPostsUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = fetchParam == FetchParam.INITIAL || fetchParam == FetchParam.RELOAD,
                        paginationInitialing = fetchParam == FetchParam.INITIAL,
                        paginationLoading = fetchParam == FetchParam.NEXT_PAGE,
                        paginationReloading = fetchParam == FetchParam.RELOAD,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                val responseList = response.posts ?: emptyList()
                val currentList: List<UserPostsItemUIModel> = when (fetchParam) {
                    FetchParam.INITIAL -> responseList
                    FetchParam.RELOAD -> responseList
                    FetchParam.NEXT_PAGE -> {
                        val list = viewState.userPostFeedList.toMutableList()
                        list.apply {
                            addAll(responseList)
                        }
                    }
                }
                val paginationHasNext = responseList.isNotEmpty()

                updateState {
                    it.copy(
                        isLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = paginationHasNext,
                        userPostFeedList = currentList.toPersistentList(),
                    )
                }
            }
    }

    private fun setLikeStatus(postId: String?, actionType: UserActionPostLikeType) {
        userActionController.changePostLikeStatus(
            scope = viewModelScope,
            postId = postId,
            actionType = actionType,
            onErrorAction = {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            },
        )
        val currentLikeCount = viewState.userPostFeedList.firstOrNull { it.id == postId }?.likeCount
        val newLikeCount = when (actionType) {
            UserActionPostLikeType.Like -> currentLikeCount?.plus(AppDefaults.ONE)
            UserActionPostLikeType.UnLike -> currentLikeCount?.minus(AppDefaults.ONE)
        }
        updateLikeStatus(
            postId = postId,
            actionType = actionType,
            newLikeCount = newLikeCount ?: AppDefaults.ZERO,
        )
    }

    private fun updateLikeStatus(postId: String?, actionType: UserActionPostLikeType, newLikeCount: Int) {
        val newList = viewState.userPostFeedList.map {
            if (it.id == postId) {
                when (actionType) {
                    UserActionPostLikeType.Like -> it.copy(
                        isLiked = true,
                        likeCount = newLikeCount,
                    )

                    UserActionPostLikeType.UnLike -> it.copy(
                        isLiked = false,
                        likeCount = newLikeCount,
                    )
                }
            } else {
                it
            }
        }
        updateState { state ->
            state.copy(
                userPostFeedList = newList.toPersistentList(),
            )
        }
    }

    private fun setCommentDialogShowPostId(commentDialogShowPostId: String?) {
        updateState { state ->
            state.copy(
                commentDialogShowPostId = commentDialogShowPostId,
            )
        }
    }

    private fun hidePost(
        postId: String?,
    ) {

        val request = HidePostRequest(
            postId = postId,
        )

        hidePostUseCase.invoke(request)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        userPostFeedList = state.userPostFeedList.filter {
                            it.id != postId
                        }.toPersistentList(),
                    )
                }
            }
    }
}

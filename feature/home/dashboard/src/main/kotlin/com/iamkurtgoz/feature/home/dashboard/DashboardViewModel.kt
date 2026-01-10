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
package com.iamkurtgoz.feature.home.dashboard

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.impl.DashboardEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.domain.model.request.DeletePostRequest
import com.iamkurtgoz.domain.model.request.HidePostRequest
import com.iamkurtgoz.domain.model.request.ReportPostRequest
import com.iamkurtgoz.feature.home.dashboard.DashboardScreenContract.SideEffect.NavigateToMediaViewer
import com.iamkurtgoz.feature.home.dashboard.DashboardScreenContract.SideEffect.NavigateToSelectTeamScreen
import com.iamkurtgoz.feature.home.dashboard.DashboardScreenContract.SideEffect.NavigateToSelectTrainingGroupScreen
import com.iamkurtgoz.feature.home.dashboard.DashboardScreenContract.SideEffect.NavigateToShare
import com.iamkurtgoz.feature.home.dashboard.DashboardScreenContract.SideEffect.NavigateToWebView
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.toMenuUIModelItemList
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.DashboardFeedAsyncUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.DashboardFeedAsyncUseCaseParams
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.DeletePostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.HidePostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.MenuUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.ReportPostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.StoryFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appPreferences: AppPreferences,
    private val storyFeedUseCase: StoryFeedUseCase,
    private val dashboardFeedAsyncUseCase: DashboardFeedAsyncUseCase,
    private val menuUseCase: MenuUseCase,
    private val userActionController: UserActionController,
    private val hidePostUseCase: HidePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : CoreViewModel<DashboardScreenContract.State, DashboardScreenContract.SideEffect, DashboardScreenContract.Event>(
    initialState = DashboardScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: DashboardScreenContract.Event) {
        when (event) {
            is DashboardScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is DashboardScreenContract.Event.NavigateUp -> setSideEffect(DashboardScreenContract.SideEffect.NavigateUp)
            is DashboardScreenContract.Event.PopBackStack -> setSideEffect(DashboardScreenContract.SideEffect.PopBackStack)
            is DashboardScreenContract.Event.DismissDialogs -> dismissDialogs()
            is DashboardScreenContract.Event.DashboardFeed -> dashboardFeed(event.fetchParam)
            is DashboardScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(status = event.eventBusState)
            is DashboardScreenContract.Event.SetLikeStatus -> setLikeStatus(postId = event.postId, actionType = event.actionType)
            is DashboardScreenContract.Event.ClickedStoryItem -> clickedStoryItem(userId = event.userId)
            is DashboardScreenContract.Event.NavigateToMediaViewer -> setSideEffect(NavigateToMediaViewer(event.routeType))
            is DashboardScreenContract.Event.NavigateToShare -> setSideEffect(NavigateToShare(event.routeType))
            is DashboardScreenContract.Event.SetAskedNotificationPermission -> setAskedNotificationPermission()
            is DashboardScreenContract.Event.SetCommentDialogShowPostId -> setCommentDialogShowPostId(event.commentDialogShowPostId)
            is DashboardScreenContract.Event.SetComplain -> setComplain(event.postId)
            is DashboardScreenContract.Event.SetHide -> hidePost(event.postId)
            is DashboardScreenContract.Event.SetDelete -> deletePost(event.postId)
            is DashboardScreenContract.Event.ReportPost -> reportPost(event.postId, event.reason)
            is DashboardScreenContract.Event.ShowReportDialog -> updateState {
                it.copy(showReportDialogForPostId = event.postId)
            }

            is DashboardScreenContract.Event.DismissReportDialog -> updateState {
                it.copy(
                    showReportDialogForPostId = null,
                    reportText = "",
                )
            }

            is DashboardScreenContract.Event.UpdateReportText -> updateState {
                it.copy(reportText = event.text ?: "")
            }
            is DashboardScreenContract.Event.SetPlayingVideoUrl -> setPlayingVideoUrl(event.playingVideoUrl)
            is DashboardScreenContract.Event.GetMenu -> getMenu()
            is DashboardScreenContract.Event.NavigateToWebView -> setSideEffect(NavigateToWebView(event.routeType))
            is DashboardScreenContract.Event.NavigateToCalendar -> setSideEffect(DashboardScreenContract.SideEffect.NavigateToCalendar)
            is DashboardScreenContract.Event.NavigateToNotifications -> setSideEffect(DashboardScreenContract.SideEffect.NavigateToNotifications)
            is DashboardScreenContract.Event.NavigateToCreateTeamScreen -> setSideEffect(DashboardScreenContract.SideEffect.NavigateToCreateTeamScreen)
            is DashboardScreenContract.Event.NavigateToSelectSportClubScreen -> setSideEffect(DashboardScreenContract.SideEffect.NavigateToSelectSportClubScreen)
            is DashboardScreenContract.Event.NavigateToSelectTeamScreen -> setSideEffect(NavigateToSelectTeamScreen(event.fromGenerateClub, event.fromTrainingGroup))
            is DashboardScreenContract.Event.NavigateToSelectTrainingGroupScreen -> setSideEffect(NavigateToSelectTrainingGroupScreen(event.fromTrainingGroup))
            is DashboardScreenContract.Event.NavigateToCoachListScreen -> setSideEffect(DashboardScreenContract.SideEffect.NavigateToCoachListScreen)
            is DashboardScreenContract.Event.OnMainMenuClick -> {
                updateState { state ->
                    state.copy(
                        menuTitle = "${event.mainMenuItem.name} Menüsü",
                        menuList = state.menuList?.copy(
                            menu = event.mainMenuItem.subMenus
                                ?.toMenuUIModelItemList(event.mainMenuItem.menuUserType)
                                .orEmpty(),
                        ),
                    )
                }
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        val userId = appPreferences.currentPreferenceState.firstOrNull()?.userId
        updateState {
            it.copy(
                userId = userId,
            )
        }
        fetchStoryFeed(fetchAlsoDashboardFeed = true)
        getMenu()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                commentDialogShowPostId = null,
            )
        }
    }

    private fun fetchStoryFeed(fetchAlsoDashboardFeed: Boolean) {
        storyFeedUseCase.invoke()
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
                if (fetchAlsoDashboardFeed) {
                    dashboardFeed(fetchParam = FetchParam.INITIAL)
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        storyFeedList = it,
                    )
                }
                if (fetchAlsoDashboardFeed) {
                    dashboardFeed(fetchParam = FetchParam.INITIAL)
                }
            }
    }

    private fun getMenu() {
        menuUseCase.invoke()
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
                        menuList = it,
                        menuTitle = "Hızlı Menü",
                    )
                }
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
                        dashboardFeedList = persistentListOf(),
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
                        dashboardFeedList = persistentListOf(),
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

        val params = DashboardFeedAsyncUseCaseParams(
            page = viewState.paginationPage,
            pageSize = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        )

        dashboardFeedAsyncUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isShimmerLoading = fetchParam == FetchParam.INITIAL || fetchParam == FetchParam.RELOAD,
                        paginationInitialing = fetchParam == FetchParam.INITIAL,
                        paginationLoading = fetchParam == FetchParam.NEXT_PAGE,
                        paginationReloading = fetchParam == FetchParam.RELOAD,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                val responseList = response.posts ?: emptyList()
                val currentList: List<DashboardPostUIModel> = when (fetchParam) {
                    FetchParam.INITIAL -> responseList
                    FetchParam.RELOAD -> responseList
                    FetchParam.NEXT_PAGE -> {
                        val list = viewState.dashboardFeedList.toMutableList()
                        list.apply {
                            addAll(responseList)
                        }
                    }
                }
                val paginationHasNext = responseList.isNotEmpty()

                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = paginationHasNext,
                        dashboardFeedList = currentList.toPersistentList(),
                    )
                }
            }
    }

    private fun updateEventBusStatus(status: DashboardEventBus.Event) {
        when (status) {
            is DashboardEventBus.Event.UpdateLikeStatus -> {
                updateLikeStatus(
                    postId = status.postId,
                    actionType = status.actionType,
                    newLikeCount = status.likeCount,
                )
            }
            is DashboardEventBus.Event.FetchStoryFeed -> {
                fetchStoryFeed(fetchAlsoDashboardFeed = false)
            }
            is DashboardEventBus.Event.RefreshHome -> {
                dashboardFeed(fetchParam = FetchParam.INITIAL)
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
        val currentLikeCount = viewState.dashboardFeedList.firstOrNull { it.id == postId }?.likeCount
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
        val newList = viewState.dashboardFeedList.map {
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
                dashboardFeedList = newList.toPersistentList(),
            )
        }
    }

    private fun clickedStoryItem(userId: String?) {
        userId?.let {
            setSideEffect(DashboardScreenContract.SideEffect.NavigateToStory(it))
        }
    }

    private fun setAskedNotificationPermission() = viewModelScope.launch {
        appPreferences.setIsAskedNotificationPermission(true)
    }

    private fun setCommentDialogShowPostId(commentDialogShowPostId: String?) {
        updateState { state ->
            state.copy(
                commentDialogShowPostId = commentDialogShowPostId,
            )
        }
    }

    private fun setComplain(postId: String?) {
        updateState { state ->
            state.copy(
                dashboardFeedList = state.dashboardFeedList.filter {
                    it.id != postId
                }.toPersistentList(),
            )
        }
    }

    private fun setHide(postId: String?) {
        updateState { state ->
            state.copy(
                dashboardFeedList = state.dashboardFeedList.filter {
                    it.id != postId
                }.toPersistentList(),
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
                        dashboardFeedList = state.dashboardFeedList.filter {
                            it.id != postId
                        }.toPersistentList(),
                    )
                }
            }
    }

    private fun reportPost(
        postId: String?,
        reason: String?,
    ) {
        val request = ReportPostRequest(
            postId = postId,
            reason = reason,
        )

        reportPostUseCase.invoke(request)
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
                        dashboardFeedList = state.dashboardFeedList.filter {
                            it.id != postId
                        }.toPersistentList(),
                    )
                }
            }
    }

    private fun deletePost(id: String?) {
        val request = DeletePostRequest(
            id = id,
        )

        deletePostUseCase.invoke(request)
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
                        dashboardFeedList = state.dashboardFeedList.filter {
                            it.id != id
                        }.toPersistentList(),
                    )
                }
            }
    }
}

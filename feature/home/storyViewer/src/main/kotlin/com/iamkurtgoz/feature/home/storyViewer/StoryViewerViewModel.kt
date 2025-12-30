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
package com.iamkurtgoz.feature.home.storyViewer

import android.media.MediaMetadataRetriever
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.domain.model.request.DeleteStoryRequest
import com.iamkurtgoz.domain.model.request.WatchedStoryRequest
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryDetailUIModel
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryUIModel
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.DeleteStoryUseCase
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.StoryFeedUseCase
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.WatchedStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class StoryViewerViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val storyFeedUseCase: StoryFeedUseCase,
    private val watchedStoryUseCase: WatchedStoryUseCase,
    private val deleteStoryUseCase: DeleteStoryUseCase,
    private val appEventBus: AppEventBus,
) : CoreViewModel<StoryViewerScreenContract.State, StoryViewerScreenContract.SideEffect, StoryViewerScreenContract.Event>(
    initialState = StoryViewerScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigationRoute = savedStateHandle.toRoute(),
    ),
) {
    private var storyTimerJob: Job? = null
    private val isPaused = MutableStateFlow(false)

    override fun setEvent(event: StoryViewerScreenContract.Event) {
        when (event) {
            is StoryViewerScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is StoryViewerScreenContract.Event.NavigateUp -> setSideEffect(StoryViewerScreenContract.SideEffect.NavigateUp)
            is StoryViewerScreenContract.Event.PopBackStack -> setSideEffect(StoryViewerScreenContract.SideEffect.PopBackStack)
            is StoryViewerScreenContract.Event.DismissDialogs -> dismissDialogs()
            is StoryViewerScreenContract.Event.NavigateToBackStory -> navigateToBackStory()
            is StoryViewerScreenContract.Event.NavigateToNextStory -> navigateToNextStory()
            is StoryViewerScreenContract.Event.DeleteStory -> deleteStory(event.storyId)
            is StoryViewerScreenContract.Event.CloseBottomSheet -> {
                isPaused.value = false
                updateState { it.copy(showBottomSheet = false) }
            }
            is StoryViewerScreenContract.Event.OpenBottomSheet -> {
                isPaused.value = true
                updateState { it.copy(showBottomSheet = true) }
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        storyFeed()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun storyFeed() {
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
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        stories = it.stories?.toPersistentList() ?: persistentListOf(),
                        currentStoryUserId = state.navigationRoute.userId,
                    )
                }
                startStoryDestroy()
            }
    }

    private val currentStory: StoryUIModel?
        get() = viewState.stories.firstOrNull { it.userId == viewState.currentStoryUserId }

    private fun startStoryDestroy() = viewModelScope.launch {
        storyTimerJob?.cancel()
        storyTimerJob = null

        val currentStoryDetail = currentStory?.details?.firstOrNull { storyDetailUIModel ->
            storyDetailUIModel.isWatched == false
        } ?: currentStory?.details?.getOrNull(viewState.currentStoryDetailIndex)

        val currentStoryTotalTime = if (currentStoryDetail?.media?.type == CustomMediaType.IMAGE) {
            StoryViewerScreenContract.Static.DEFAULT_STORY_TOTAL_TIME
        } else {
            getVideoDurationMillis(currentStoryDetail?.media?.url)
        }

        updateState { state ->
            state.copy(
                currentStoryTotalTime = currentStoryTotalTime,
                currentStoryRemainingTime = currentStoryTotalTime,
            )
        }

        if (currentStoryDetail?.isWatched == false) {
            watchedStory(currentStoryDetail)
        }

        Timber.d("currentStoryTotalTime: $currentStoryTotalTime")

        storyTimerJob = viewModelScope.launch {
            while (isActive && viewState.currentStoryRemainingTime > AppDefaults.ZERO) {
                if (isPaused.value) {
                    delay(StoryViewerScreenContract.Static.WHILE_PLAYING_STORY_INTERVAL)
                    continue
                }
                delay(StoryViewerScreenContract.Static.WHILE_PLAYING_STORY_INTERVAL)
                updateState { state ->
                    state.copy(
                        currentStoryRemainingTime = state.currentStoryRemainingTime - StoryViewerScreenContract.Static.WHILE_PLAYING_STORY_INTERVAL,
                    )
                }

                if (viewState.currentStoryRemainingTime <= AppDefaults.ZERO) {
                    Timber.d("Time's up, going to next story")
                    goToNextStory()
                    break // Exit the loop instead of canceling the job from inside
                }
            }
        }
    }

    private fun watchedStory(currentStoryDetail: StoryDetailUIModel?) {
        watchedStoryUseCase.invoke(WatchedStoryRequest(currentStoryDetail?.storyId))
            .requester
            .callWithSuccess {
                Timber.d("Story marked as watched: ${currentStoryDetail?.storyId}")
                appEventBus.fetchStory()
            }
    }

    private suspend fun getVideoDurationMillis(videoUrl: String?): Double {
        if (videoUrl.isNullOrEmpty()) return StoryViewerScreenContract.Static.DEFAULT_STORY_TOTAL_TIME

        return withContext(ioDispatcher) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(videoUrl, HashMap())
                val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                retriever.release()
                durationStr?.toDoubleOrNull() ?: StoryViewerScreenContract.Static.DEFAULT_STORY_TOTAL_TIME
            } catch (_: Exception) {
                StoryViewerScreenContract.Static.DEFAULT_STORY_TOTAL_TIME
            }
        }
    }

    private fun goToNextStory() {
        if (viewState.currentStoryDetailIndex < (currentStory?.details?.lastIndex ?: 0)) {
            updateState { state ->
                state.copy(
                    currentStoryDetailIndex = state.currentStoryDetailIndex + AppDefaults.ONE,
                    currentStoryTotalTime = AppDefaults.ZERO.toDouble(),
                    currentStoryRemainingTime = AppDefaults.ZERO.toDouble(),
                )
            }
            startStoryDestroy()
        } else {
            val currentUserIndex = viewState.stories.indexOfFirst { it.userId == viewState.currentStoryUserId }
            if (currentUserIndex < viewState.stories.size - AppDefaults.ONE) {
                val nextUserIndex = currentUserIndex + AppDefaults.ONE
                updateState { state ->
                    state.copy(
                        currentStoryDetailIndex = AppDefaults.ZERO,
                        currentStoryTotalTime = AppDefaults.ZERO.toDouble(),
                        currentStoryRemainingTime = AppDefaults.ZERO.toDouble(),
                        currentStoryUserId = viewState.stories.getOrNull(nextUserIndex)?.userId,
                    )
                }
                startStoryDestroy()
            } else {
                setSideEffect(StoryViewerScreenContract.SideEffect.NavigateUp)
            }
        }
    }

    private fun goToPreviousStory() {
        if (viewState.currentStoryDetailIndex > AppDefaults.ZERO) {
            updateState { state ->
                state.copy(
                    currentStoryDetailIndex = state.currentStoryDetailIndex - AppDefaults.ONE,
                    currentStoryTotalTime = AppDefaults.ZERO.toDouble(),
                    currentStoryRemainingTime = AppDefaults.ZERO.toDouble(),
                )
            }
            startStoryDestroy()
        } else {
            val currentUserIndex = viewState.stories.indexOfFirst { it.userId == viewState.currentStoryUserId }
            if (currentUserIndex > AppDefaults.ZERO) {
                val previousUserIndex = currentUserIndex - AppDefaults.ONE
                updateState { state ->
                    state.copy(
                        currentStoryDetailIndex = viewState.stories.getOrNull(previousUserIndex)?.details?.lastIndex ?: AppDefaults.ZERO,
                        currentStoryTotalTime = AppDefaults.ZERO.toDouble(),
                        currentStoryRemainingTime = AppDefaults.ZERO.toDouble(),
                        currentStoryUserId = viewState.stories.getOrNull(previousUserIndex)?.userId,
                    )
                }
                startStoryDestroy()
            } else {
                setSideEffect(StoryViewerScreenContract.SideEffect.NavigateUp)
            }
        }
    }

    private fun navigateToBackStory() {
        goToPreviousStory()
    }

    private fun navigateToNextStory() {
        goToNextStory()
    }

    private fun deleteStory(id: String?) {
        deleteStoryUseCase.invoke(DeleteStoryRequest(id))
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
                val updatedStories = viewState.stories.map { story ->
                    if (story.isOwn == true) {
                        story.copy(
                            details = story.details?.filterNot { it.storyId == id }
                        )
                    } else {
                        story
                    }
                }
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        stories = updatedStories.toPersistentList(),
                    )
                }
                appEventBus.fetchStory()
                val shouldGoNextStory = viewState.stories.firstOrNull { it.isOwn == true }?.details?.isEmpty()
                if (shouldGoNextStory == true) {
                    goToNextStory()
                } else {
                    startStoryDestroy()
                }
            }
    }
}

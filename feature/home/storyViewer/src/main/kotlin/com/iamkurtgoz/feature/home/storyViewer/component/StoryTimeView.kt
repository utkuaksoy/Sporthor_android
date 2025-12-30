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
package com.iamkurtgoz.feature.home.storyViewer.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenStoryViewerRoute
import com.iamkurtgoz.feature.home.storyViewer.StoryViewerScreenContract
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryDetailUIModel
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryUIModel
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

const val ANIMATION_DURATION_MILLIS: Int = 100

@Composable
internal fun StoryTimeView(
    state: StoryViewerScreenContract.State,
    modifier: Modifier = Modifier,
) {
    val density: Density = LocalDensity.current
    var dynamicWidth by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    val timeViewItemCount = remember(state.currentStoryUserId, state.stories) {
        state.stories.firstOrNull { it.userId == state.currentStoryUserId }?.details?.size ?: AppDefaults.ZERO
    }

    val eachItemWidth = remember(dynamicWidth, timeViewItemCount) {
        if (timeViewItemCount == AppDefaults.ZERO) AppDefaults.ZERO.dp else
            (dynamicWidth / timeViewItemCount) - if (timeViewItemCount > AppDefaults.ONE) AppDefaults.FOUR.dp else AppDefaults.ZERO.dp
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = AppTheme.spacing.spacingSmallest,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                dynamicWidth = with(density) { it.size.width.toDp() }
            },
    ) {
        repeat(timeViewItemCount) { index ->
            Box(
                modifier = Modifier
                    .height(AppTheme.dimens.dp4)
                    .width(eachItemWidth)
                    .background(
                        color = AppTheme.colors.generalColors.foregroundWhite.copy(alpha = AppDefaults.COMPOSE_COLORS_HALF_ALPHA),
                        shape = AppTheme.shapes.radiusDoubleExtraLarge,
                    ),
            ) {
                if (index < state.currentStoryDetailIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(
                                color = AppTheme.colors.generalColors.foregroundWhite,
                                shape = AppTheme.shapes.radiusDoubleExtraLarge,
                            ),
                    )
                } else if (index == state.currentStoryDetailIndex) {
                    val currentProgressWidth by animateDpAsState(
                        targetValue = eachItemWidth * (AppDefaults.ONE.toDouble() - (state.currentStoryRemainingTime.toFloat() / state.currentStoryTotalTime.coerceAtLeast(AppDefaults.ONE.toDouble()))).toFloat(),
                        animationSpec = tween(
                            durationMillis = ANIMATION_DURATION_MILLIS,
                        ),
                        label = "storyProgressAnim",
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(currentProgressWidth)
                            .background(
                                color = AppTheme.colors.generalColors.foregroundWhite,
                                shape = AppTheme.shapes.radiusDoubleExtraLarge,
                            ),
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            StoryTimeView(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
                state = StoryViewerScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigationRoute = HomeScreenStoryViewerRoute(
                        userId = "test-user",
                    ),
                    currentStoryUserId = "test-user",
                    stories = persistentListOf(
                        StoryUIModel(
                            userId = "test-user",
                            username = UUID.randomUUID().toString(),
                            name = UUID.randomUUID().toString(),
                            lastName = UUID.randomUUID().toString(),
                            isOwn = true,
                            profileImageUrl = UUID.randomUUID().toString(),
                            details = listOf(
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),

                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),

                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                            ),
                        ),
                    ),
                ),
            )
        }
    }
}

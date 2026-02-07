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

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.browser.customtabs.CustomTabsIntent
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenStoryViewerRoute
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.storyViewer.component.StoryTimeView
import com.iamkurtgoz.feature.home.storyViewer.component.StoryVideoPlayer
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryDetailUIModel
import com.iamkurtgoz.feature.home.storyViewer.domain.model.StoryUIModel
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoryViewerScreenContent(
    state: StoryViewerScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (StoryViewerScreenContract.Event) -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.generalColors.foregroundBlack),
        contentAlignment = Alignment.Center,
    ) {
        state.stories.firstOrNull { it.userId == state.currentStoryUserId }?.let { story ->
            story.details?.getOrNull(state.currentStoryDetailIndex)?.let { storyDetail ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(AppDefaults.ASPECT_RATIO_0_56)
                        .align(Alignment.Center),
                ) {
                    if (storyDetail.media?.type == CustomMediaType.IMAGE) {
                        AppAsyncImageLoader.Load(
                            data = storyDetail.media?.url,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                    } else if (storyDetail.media?.type == CustomMediaType.VIDEO) {
                        StoryVideoPlayer(
                            url = storyDetail.media?.url,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 140.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(AppDefaults.WEIGHT_FULL)
                                .clickable {
                                    setEvent.invoke(StoryViewerScreenContract.Event.NavigateToBackStory)
                                },
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(AppDefaults.WEIGHT_FULL)
                                .clickable {
                                    setEvent.invoke(StoryViewerScreenContract.Event.NavigateToNextStory)
                                },
                        )
                    }

                    storyDetail.link?.takeIf { it.isNotBlank() }?.let { link ->
                        val normalizedUrl = link.normalizeLink()
                        Button(
                            onClick = {
                                CustomTabsIntent.Builder()
                                    .setShowTitle(true)
                                    .build()
                                    .launchUrl(context, Uri.parse(normalizedUrl))
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.generalColors.foregroundWhite,
                                contentColor = AppTheme.colors.generalColors.foregroundBlack,
                            ),
                            shape = RoundedCornerShape(22.dp),
                        ) {
                            Image(
                                painter = painterResource(resourcesR.drawable.img_link),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color(0xFF2196F3)),
                            )
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = normalizedUrl,
                                color = AppTheme.colors.generalColors.foregroundBlack,
                                style = AppTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = AppTheme.configuration.getSafeContentPaddingValues().calculateTopPadding())
                        .align(Alignment.TopCenter),
                ) {
                    StoryTimeView(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        state = state,
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = AppTheme.spacing.spacingSmall)
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UserImageView(
                            data = story.profileImageUrl,
                            size = AppTheme.dimens.dp32,
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingSmallest),
                            text = story.username ?: "",
                            color = AppTheme.colors.generalColors.foregroundWhite,
                            style = AppTheme.typography.labelSmall,
                        )

                        Spacer(
                            modifier = Modifier
                                .weight(AppDefaults.WEIGHT_FULL),
                        )
                        if (story.isOwn == true) {
                            IconButton(
                                modifier = Modifier
                                    .padding(end = AppTheme.spacing.spacingMedium)
                                    .size(AppTheme.dimens.dp32),
                                onClick = {
                                    setEvent.invoke(StoryViewerScreenContract.Event.OpenBottomSheet)
                                },
                                content = {
                                    Box(
                                        modifier = Modifier
                                            .size(AppTheme.dimens.dp32)
                                            .background(
                                                color = AppTheme.colors.generalColors.foregroundWhite,
                                                shape = AppTheme.shapes.radiusCircle,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Image(
                                            painter = painterResource(resourcesR.drawable.img_horizontal_dot),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(AppTheme.dimens.dp24),
                                            colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundBlack),
                                        )
                                    }
                                },
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .size(AppTheme.dimens.dp32),
                            onClick = {
                                setEvent.invoke(StoryViewerScreenContract.Event.NavigateUp)
                            },
                            content = {
                                Image(
                                    painter = painterResource(resourcesR.drawable.img_close),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(AppTheme.dimens.dp32),
                                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                                )
                            },
                        )
                    }
                }

                val bottomSheetState = rememberModalBottomSheetState()

                if (state.showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            setEvent.invoke(StoryViewerScreenContract.Event.CloseBottomSheet)
                        },
                        sheetState = bottomSheetState,
                    ) {
                        AppButton.PrimaryLarge(
                            text =  "Hikayeyi Sil", // TODO: Localize
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppTheme.spacing.spacingMedium)
                                .padding(horizontal = AppTheme.spacing.spacingMedium),
                            onClick = {
                                setEvent.invoke(StoryViewerScreenContract.Event.CloseBottomSheet)
                                setEvent.invoke(StoryViewerScreenContract.Event.DeleteStory(storyId = storyDetail.storyId ?: ""))
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun String.normalizeLink(): String {
    val trimmedValue = trim()
    if (trimmedValue.startsWith("http://") || trimmedValue.startsWith("https://")) {
        return trimmedValue
    }
    return "https://$trimmedValue"
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            StoryViewerScreenContent(
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
                            username = "iamkurtgoz",
                            name = "Mehmet",
                            lastName = "Kurtgöz",
                            isOwn = true,
                            profileImageUrl = UUID.randomUUID().toString(),
                            details = listOf(
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = "https://sporthor.com",
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),

                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),

                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                                StoryDetailUIModel(
                                    storyId = UUID.randomUUID().toString(),
                                    link = null,
                                    media = null,
                                    publishDate = null,
                                    isWatched = false,
                                ),
                            ),
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

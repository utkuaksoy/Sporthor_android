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
package com.iamkurtgoz.feature.home.dashboard.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourceR
import com.iamkurtgoz.feature.home.dashboard.domain.model.StoryUIModel
import java.util.UUID

@Composable
fun StoriesRow(
    stories: List<StoryUIModel>,
    navigateToShareStory: () -> Unit,
    onStoryClick: (userId: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
    ) {
        itemsIndexed(
            items = stories,
            key = { index, item ->
                "$index-$item"
            },
            itemContent = { _, item ->
                if (item.isOwn == true) {
                    Column(
                        modifier = Modifier
                            .padding(end = AppTheme.spacing.spacingSmall)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        navigateToShareStory.invoke()
                                    },
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        UserImageView(
                            modifier = Modifier
                                .padding(bottom = AppTheme.spacing.spacingSmall),
                            data = item.profileImageUrl,
                            hasBorder = item.details?.isNotEmpty() == true,
                            borderIsGray = item.isWatched == true || item.isWatched == null,
                            badgeData = resourceR.drawable.img_add_circle,
                            size = AppTheme.dimens.dp72,
                            onClickAction = {
                                if (item.details?.isNotEmpty() == true) {
                                    onStoryClick.invoke(item.userId)
                                } else {
                                    navigateToShareStory.invoke()
                                }
                            },
                            onClickBadgeAction = {
                                navigateToShareStory.invoke()
                            },
                        )

                        Text(
                            text = "Hikayen", // TODO: Localize
                            style = AppTheme.typography.labelSmall,
                            color = AppTheme.colors.generalColors.textPrimary,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(end = AppTheme.spacing.spacingSmall),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        UserImageView(
                            modifier = Modifier
                                .padding(bottom = AppTheme.spacing.spacingSmall),
                            data = item.profileImageUrl,
                            hasBorder = true,
                            borderIsGray = item.isWatched == true || item.isWatched == null,
                            onClickAction = {
                                onStoryClick(item.userId)
                            },
                            size = AppTheme.dimens.dp72,
                        )
                        Text(
                            text = item.username.orEmpty(),
                            style = AppTheme.typography.labelSmall,
                        )
                    }
                }
            },
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            StoriesRow(
                stories = listOf(
                    StoryUIModel(
                        userId = UUID.randomUUID().toString(),
                        username = "Mehmet Kurtgöz",
                        name = "Mehmet",
                        lastName = "Kurtgöz",
                        isOwn = true,
                        isWatched = false,
                        profileImageUrl = null,
                        details = null,
                    ),
                    StoryUIModel(
                        userId = UUID.randomUUID().toString(),
                        username = "Mehmet Kurtgöz",
                        name = "Mehmet",
                        lastName = "Kurtgöz",
                        isOwn = false,
                        isWatched = false,
                        profileImageUrl = null,
                        details = null,
                    ),
                    StoryUIModel(
                        userId = UUID.randomUUID().toString(),
                        username = "Mehmet Kurtgöz",
                        name = "Mehmet",
                        lastName = "Kurtgöz",
                        isOwn = false,
                        isWatched = false,
                        profileImageUrl = null,
                        details = null,
                    ),
                    StoryUIModel(
                        userId = UUID.randomUUID().toString(),
                        username = "Mehmet Kurtgöz",
                        name = "Mehmet",
                        lastName = "Kurtgöz",
                        isOwn = false,
                        isWatched = false,
                        profileImageUrl = null,
                        details = null,
                    ),
                ),
                navigateToShareStory = {},
                onStoryClick = {},
            )
        }
    }
}

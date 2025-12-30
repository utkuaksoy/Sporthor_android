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
package com.iamkurtgoz.feature.home.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.ProfileScreenContract
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataSegmentUIModel

@Composable
internal fun ProfileSegments(
    selectedSegmentState: ProfileComponentDataSegmentUIModel?,
    segments: List<ProfileComponentDataSegmentUIModel>,
    setEvent: (ProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingMedium),
    ) {
        HorizontalDivider(
            modifier = Modifier,
            thickness = AppTheme.dimens.dp1,
            color = AppTheme.colors.generalColors.borderSoft200,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            itemsIndexed(
                items = segments,
                key = { index, item ->
                    "$index-${item.id}-${item.type}-${item.image}-${item.title}"
                },
                itemContent = { _, item ->
                    SegmentButton(
                        item = item,
                        selectedSegmentState = selectedSegmentState,
                        modifier = Modifier
                            .width(AppTheme.configuration.getScreenWidthDp() / segments.size)
                            .clickable {
                                setEvent.invoke(ProfileScreenContract.Event.SetSelectedSegmentState(item))
                            },
                    )
                },
            )
        }
    }
}

@Composable
private fun SegmentButton(
    item: ProfileComponentDataSegmentUIModel,
    selectedSegmentState: ProfileComponentDataSegmentUIModel?,
    modifier: Modifier = Modifier,
) {
    val isSelected = remember(selectedSegmentState) { item == selectedSegmentState }
    Row(
        modifier = modifier
            .height(AppTheme.dimens.dp40),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        AppAsyncImageLoader.Load(
            data = item.image,
            contentDescription = item.title,
            modifier = Modifier
                .size(AppTheme.dimens.dp24),
            colorFilter = ColorFilter.tint(if (isSelected) AppTheme.colors.generalColors.textPrimary else AppTheme.colors.generalColors.contentSoft600),
        )

        Text(
            text = item.title ?: "",
            style = AppTheme.typography.labelRegular.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingSmallest),
            color = if (isSelected) AppTheme.colors.generalColors.textPrimary else AppTheme.colors.generalColors.contentSoft600,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileSegments(
                selectedSegmentState = null,
                segments = listOf(
                    ProfileComponentDataSegmentUIModel(
                        id = "1",
                        title = "Gönderiler",
                        image = "https://cdn-icons-png.flaticon.com/512/2089/2089181.png",
                        type = "Posts",
                    ),
                    ProfileComponentDataSegmentUIModel(
                        id = "2",
                        title = "Kişisel Bilgi",
                        image = "https://cdn-icons-png.flaticon.com/512/471/471662.png",
                        type = "PersonalInfo",
                    ),
                    ProfileComponentDataSegmentUIModel(
                        id = "3",
                        title = "Takım Kadrosu",
                        image = "https://cdn-icons-png.flaticon.com/512/1705/1705771.png",
                        type = "TeamSquad",
                    ),
                ),
                setEvent = {},
            )
        }
    }
}

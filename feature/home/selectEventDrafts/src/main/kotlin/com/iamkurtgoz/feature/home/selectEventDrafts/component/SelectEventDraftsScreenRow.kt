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
package com.iamkurtgoz.feature.home.selectEventDrafts.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.model.CalendarDetailEventUIModelTask
import com.iamkurtgoz.feature.home.selectEventDrafts.domain.model.mockCalendarDetailEvent

@Composable
internal fun SelectEventDraftsScreenRow(
    item: CalendarDetailEventUIModelTask,
    onItemClick: (CalendarDetailEventUIModelTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onItemClick(item)
            },
        shape = AppTheme.shapes.radiusMedium,
        color = AppTheme.colors.generalColors.backgroundWeak100,
        shadowElevation = AppTheme.dimens.dp0dot5,
        border = BorderStroke(
            width = AppTheme.dimens.dp1,
            color = AppTheme.colors.generalColors.borderSoft200,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.spacing.spacingMedium),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.title ?: "-",
                    style = AppTheme.typography.heading06,
                )

                Spacer(modifier = Modifier.weight(1f))

                item.taskType?.let { taskType ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(AppTheme.shapes.radiusCircle)
                            .background(Color(taskType.color)),
                    )

                    Text(
                        text = taskType.name ?: "-",
                        style = AppTheme.typography.subtitleSmall,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmallest),
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium),
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_time),
                    contentDescription = null,
                )

                Text(
                    text = item.hour ?: "-",
                    style = AppTheme.typography.subtitleSmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest),
                )
            }

            item.description?.let { description ->
                Text(
                    text = description,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                )
            }

            item.trainingGroup?.let { trainingGroup ->
                Row(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppAsyncImageLoader.Load(
                        data = trainingGroup.detail,
                        modifier = Modifier
                            .size(AppTheme.dimens.dp24)
                            .background(
                                color = AppTheme.colors.generalColors.backgroundSoft200,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle),
                    )

                    Text(
                        text = trainingGroup.name ?: "",
                        style = AppTheme.typography.subtitleSmall,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingRegular),
                    )
                }
            }

            item.users?.let { users ->
                Box(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    users.fastForEachIndexed { index, item ->
                        key(item) {
                            Box(
                                modifier = Modifier
                                    .padding(start = (index * 12).dp)
                                    .size(if (index == AppDefaults.ZERO) AppTheme.dimens.dp18 else AppTheme.dimens.dp24)
                                    .background(
                                        color = AppTheme.colors.generalColors.backgroundWeak100,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                    .clip(AppTheme.shapes.radiusCircle),
                                contentAlignment = Alignment.Center,
                            ) {
                                AppAsyncImageLoader.Load(
                                    data = item?.imageUrl ?: item?.name.getUserNameFirstChar(),
                                    modifier = Modifier
                                        .size(AppTheme.dimens.dp18)
                                        .background(
                                            color = AppTheme.colors.generalColors.backgroundSoft200,
                                            shape = AppTheme.shapes.radiusCircle,
                                        )
                                        .clip(AppTheme.shapes.radiusCircle),
                                )
                            }
                        }
                    }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                itemsIndexed(
                    items = mockCalendarDetailEvent.tasks?.filterNotNull() ?: listOf(),
                    key = { index, item ->
                        "$index${item.id}"
                    },
                    itemContent = { index, item ->
                        SelectEventDraftsScreenRow(
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .padding(horizontal = AppTheme.spacing.spacingMedium),
                            item = item,
                            onItemClick = {},
                        )
                    },
                )
            }
        }
    }
}

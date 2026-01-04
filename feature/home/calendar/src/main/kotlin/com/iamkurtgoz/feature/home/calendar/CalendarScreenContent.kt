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
package com.iamkurtgoz.feature.home.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
internal fun CalendarScreenContent(
    state: CalendarScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CalendarScreenContract.Event) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val cells = remember(state.currentMonth) {
            generateCalendarCells(state.currentMonth)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(AppDefaults.SEVEN),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.spacing.spacingMedium),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(cells) { dateOrNull ->
                if (dateOrNull == null) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = AppTheme.spacing.spacingTiny)
                                .size(38.dp)
                                .ifTrue(dateOrNull == LocalDate.now()) {
                                    background(
                                        color = AppTheme.colors.generalColors.foregroundBlack,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                }.clip(
                                    shape = AppTheme.shapes.radiusCircle,
                                ).clickable {
                                    setEvent.invoke(CalendarScreenContract.Event.NavigateToCalendarDetail(dateOrNull))
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = dateOrNull.dayOfMonth.toString(),
                                style = AppTheme.typography.labelMedium,
                                color = if (dateOrNull == LocalDate.now()) AppTheme.colors.generalColors.foregroundWhite else AppTheme.colors.generalColors.textPrimary,
                            )
                        }

                        Row {
                            state.events.firstOrNull { it.date == dateOrNull.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }?.let { tasks ->
                                tasks.taks?.mapNotNull { it }?.forEach { item ->
                                    key(item) {
                                        val colorString = if (item.length == 4 && item.startsWith("#")) {
                                            "#${item[1]}${item[1]}${item[2]}${item[2]}${item[3]}${item[3]}"
                                        } else {
                                            item
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(AppTheme.shapes.radiusCircle)
                                                .background(Color(colorString.toColorInt())),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun generateCalendarCells(month: YearMonth): List<LocalDate?> {
    val first = month.atDay(1)
    val leadingBlanks = (first.dayOfWeek.value - 1)
    val total = leadingBlanks + month.lengthOfMonth()
    val rows = ((total + AppDefaults.SIX) / AppDefaults.SEVEN)
    val cellCount = rows * AppDefaults.SEVEN
    return List(cellCount) { idx ->
        val dayIndex = idx - leadingBlanks + 1
        if (idx < leadingBlanks || dayIndex > month.lengthOfMonth()) null
        else month.atDay(dayIndex)
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CalendarScreenContent(
                state = CalendarScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

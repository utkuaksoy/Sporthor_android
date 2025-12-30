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
package com.iamkurtgoz.core.commonui.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

object IndicatorView {
    @Composable
    fun Primary(
        totalPages: Int,
        currentPage: Int,
        modifier: Modifier = Modifier,
        colors: IndicatorColors = IndicatorViewColors.primaryColors(),
        sizes: IndicatorSizes = IndicatorViewSizes.primarySizes(),
        shapes: IndicatorShapes = IndicatorViewShapes.primaryShapes(),
    ) = IndicatorViewImpl(
        totalPages = totalPages,
        currentPage = currentPage,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        shapes = shapes,
    )
}

@Composable
private fun IndicatorViewImpl(
    totalPages: Int,
    currentPage: Int,
    colors: IndicatorColors,
    sizes: IndicatorSizes,
    shapes: IndicatorShapes,
    modifier: Modifier = Modifier,
) {
    val density: Density = LocalDensity.current
    var dynamicWidth by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(sizes.height)
            .onGloballyPositioned {
                dynamicWidth = with(density) {
                    it.size.width.toDp()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .height(sizes.height)
                        .width((dynamicWidth / totalPages) - sizes.containerPadding)
                        .background(
                            color = if (index <= currentPage) colors.selectedContainerColor else colors.unselectedContainerColor,
                            shape = shapes.roundedCornerShape,
                        ),
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    var currentPage by remember { mutableIntStateOf(2) }
    val totalPages = AppDefaults.FOUR

    AppTheme {
        AppThemeScaffold {
            IndicatorView.Primary(
                totalPages = totalPages,
                currentPage = currentPage,
                sizes = IndicatorViewSizes.primarySizes(
                    height = AppTheme.dimens.dp10,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.dp10),
            )
        }
    }
}

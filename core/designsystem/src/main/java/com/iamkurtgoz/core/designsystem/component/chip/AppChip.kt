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
package com.iamkurtgoz.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.AppChipItem
import com.iamkurtgoz.domain.model.base.BasicAppChipItem

object AppChip {
    @Composable
    fun Primary(
        item: AppChipItem,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        colors: ChipColors = AppChipColors.primaryColors(),
        sizes: ChipSizes = AppChipSizes.primarySizes(),
        styles: ChipStyles = AppChipStyles.primaryStyles(),
        shapes: ChipShapes = AppChipShapes.primaryShapes(),
        onClick: (AppChipItem) -> Unit = {},
    ) = AppChipImpl(
        leftContent = leftContent,
        item = item,
        isSelected = isSelected,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        modifier = modifier,
        onClick = onClick,
    )

    @Composable
    fun Secondary(
        item: AppChipItem,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        colors: ChipColors = AppChipColors.secondaryColors(),
        sizes: ChipSizes = AppChipSizes.secondarySizes(),
        styles: ChipStyles = AppChipStyles.secondaryStyles(),
        shapes: ChipShapes = AppChipShapes.secondaryShapes(),
        onClick: (AppChipItem) -> Unit = {},
    ) = AppChipImpl(
        leftContent = leftContent,
        item = item,
        isSelected = isSelected,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        modifier = modifier,
        onClick = onClick,
    )

    @Composable
    fun Tertiary(
        item: AppChipItem,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        colors: ChipColors = AppChipColors.tertiaryColors(),
        sizes: ChipSizes = AppChipSizes.tertiarySizes(),
        styles: ChipStyles = AppChipStyles.tertiaryStyles(),
        shapes: ChipShapes = AppChipShapes.tertiaryShapes(),
        onClick: (AppChipItem) -> Unit = {},
    ) = AppChipImpl(
        leftContent = leftContent,
        item = item,
        isSelected = isSelected,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        modifier = modifier,
        onClick = onClick,
    )

    @Composable
    fun ProfileGray(
        item: AppChipItem,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        colors: ChipColors = AppChipColors.profileGrayColors(),
        sizes: ChipSizes = AppChipSizes.profileGraySizes(),
        styles: ChipStyles = AppChipStyles.profileGrayStyles(),
        shapes: ChipShapes = AppChipShapes.profileGrayShapes(),
        onClick: (AppChipItem) -> Unit = {},
    ) = AppChipImpl(
        leftContent = leftContent,
        item = item,
        isSelected = isSelected,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        modifier = modifier,
        onClick = onClick,
    )

    @Composable
    fun ProfileBranch(
        item: AppChipItem,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        colors: ChipColors = AppChipColors.profileBranchColors(),
        sizes: ChipSizes = AppChipSizes.profileBranchSizes(),
        styles: ChipStyles = AppChipStyles.profileBranchStyles(),
        shapes: ChipShapes = AppChipShapes.profileBranchShapes(),
        onClick: (AppChipItem) -> Unit = {},
    ) = AppChipImpl(
        leftContent = leftContent,
        item = item,
        isSelected = isSelected,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        modifier = modifier,
        onClick = onClick,
    )
}

@Composable
private fun AppChipImpl(
    leftContent: (@Composable RowScope.() -> Unit)?,
    item: AppChipItem,
    isSelected: Boolean,
    colors: ChipColors,
    sizes: ChipSizes,
    styles: ChipStyles,
    shapes: ChipShapes,
    modifier: Modifier = Modifier,
    onClick: (AppChipItem) -> Unit = {},
) {
    val containerColor by colors.containerColor(isSelected = isSelected)
    val contentColor by colors.contentColor(isSelected = isSelected)

    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shapes.containerShape,
            )
            .clip(
                shape = shapes.containerShape,
            )
            .clickable {
                onClick.invoke(item)
            }
            .padding(sizes.contentPadding),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            leftContent?.invoke(this)

            Text(
                text = item.title ?: "",
                color = contentColor,
                style = styles.textStyle,
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Row {
                AppChip.Primary(
                    item = BasicAppChipItem("A"),
                    isSelected = true,
                )

                AppChip.Secondary(
                    item = BasicAppChipItem("B"),
                    isSelected = false,
                )

                AppChip.Tertiary(
                    item = BasicAppChipItem("C"),
                    isSelected = true,
                )

                AppChip.ProfileGray(
                    item = BasicAppChipItem("Eczacıbaşı U17"),
                    isSelected = true,
                    leftContent = {
                        AppAsyncImageLoader.Load(
                            data = resourcesR.drawable.temp_team_image_ezcacibasi,
                            modifier = Modifier
                                .padding(end = AppTheme.dimens.dp8)
                                .size(AppTheme.dimens.dp24),
                        )
                    },
                )

                AppChip.ProfileBranch(
                    item = BasicAppChipItem("Eczacıbaşı U17"),
                    isSelected = false,
                    leftContent = {
                        AppAsyncImageLoader.Load(
                            data = resourcesR.drawable.temp_team_image_ezcacibasi,
                            modifier = Modifier
                                .padding(end = AppTheme.dimens.dp8)
                                .size(AppTheme.dimens.dp24),
                        )
                    },
                )
            }
        }
    }
}

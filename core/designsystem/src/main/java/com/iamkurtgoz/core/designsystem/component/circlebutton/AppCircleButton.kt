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
package com.iamkurtgoz.core.designsystem.component.circlebutton

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

object AppCircleButton {
    @Composable
    fun PrimaryLarge(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.primaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.primaryLargeSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.primaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.primaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun PrimaryMedium(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.primaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.primaryMediumSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.primaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.primaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun PrimarySmall(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.primaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.primarySmallSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.primaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.primaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondaryLarge(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondaryLargeSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondaryMedium(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondaryMediumSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondarySmall(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondarySmallSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondaryGrayLarge(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryGrayColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondaryLargeSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondaryGrayMedium(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryGrayColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondaryMediumSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun SecondaryGraySmall(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.secondaryGrayColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.secondarySmallSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.secondaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.secondaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun TertiaryLarge(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.tertiaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.tertiaryLargeSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.tertiaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.tertiaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun TertiaryMedium(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.tertiaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.tertiaryMediumSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.tertiaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.tertiaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun TertiarySmall(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.tertiaryColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.tertiarySmallSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.tertiaryBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.tertiaryShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun OutlineLarge(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.outlineColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.outlineLargeSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.outlineBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.outlineShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun OutlineMedium(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.outlineColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.outlineMediumSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.outlineBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.outlineShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )

    @Composable
    fun OutlineSmall(
        @DrawableRes icon: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: CircleButtonColors = AppCircleButtonColors.outlineColors(),
        sizes: CircleButtonSizes = AppCircleButtonSizes.outlineSmallSizes(),
        borders: CircleButtonBorders = AppCircleButtonBorders.outlineBorders(),
        shapes: CircleButtonShapes = AppCircleButtonShapes.outlineShapes(),
    ) = AppCircleButtonImpl(
        icon = icon,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
    )
}

@Composable
private fun AppCircleButtonImpl(
    @DrawableRes icon: Int,
    enabled: Boolean,
    colors: CircleButtonColors,
    sizes: CircleButtonSizes,
    borders: CircleButtonBorders,
    shapes: CircleButtonShapes,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (enabled) colors.enabledContentColor else colors.disabledContentColor
    val containerColor = if (enabled) colors.enabledContainerColor else colors.disabledContainerColor
    val border = if (enabled) borders.stroke else borders.disabled

    Box(
        modifier = modifier
            .graphicsLayer(shape = shapes.roundedCornerShape, clip = false)
            .then(
                if (border != null) {
                    Modifier.border(
                        border = border,
                        shape = shapes.roundedCornerShape,
                    )
                } else {
                    Modifier
                },
            )
            .clip(shape = shapes.roundedCornerShape)
            .background(color = containerColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(sizes.contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(sizes.iconSize),
                painter = painterResource(id = icon),
                contentDescription = "icon",
                colorFilter = ColorFilter.tint(contentColor),
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun PreviewPrimary() {
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.generalColors.backgroundPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text("Button Primary Large")
            }

            item {
                AppCircleButton.PrimaryLarge(
                    icon = resourcesR.drawable.img_plus,
                    enabled = true,
                    onClick = {},
                )

                AppCircleButton.PrimaryLarge(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    icon = resourcesR.drawable.img_plus,
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Medium")
            }

            item {
                AppCircleButton.PrimaryMedium(
                    icon = resourcesR.drawable.img_plus,
                    enabled = true,
                    onClick = {},
                )

                AppCircleButton.PrimaryMedium(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    icon = resourcesR.drawable.img_plus,
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Small")
            }

            item {
                AppCircleButton.PrimarySmall(
                    icon = resourcesR.drawable.img_plus,
                    enabled = true,
                    onClick = {},
                )

                AppCircleButton.PrimarySmall(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    icon = resourcesR.drawable.img_plus,
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
}

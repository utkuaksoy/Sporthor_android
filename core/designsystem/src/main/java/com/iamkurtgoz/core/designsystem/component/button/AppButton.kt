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
package com.iamkurtgoz.core.designsystem.component.button

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

object AppButton {
    @Composable
    fun PrimaryLarge(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.primaryColors(),
        sizes: ButtonSizes = AppButtonSizes.primaryLargeSizes(),
        borders: ButtonBorders = AppButtonBorders.primaryBorders(),
        shapes: ButtonShape = AppButtonShapes.primaryShapes(),
        styles: ButtonStyles = AppButtonStyles.primaryLargeStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun PrimaryMedium(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.primaryColors(),
        sizes: ButtonSizes = AppButtonSizes.primaryMediumSizes(),
        borders: ButtonBorders = AppButtonBorders.primaryBorders(),
        shapes: ButtonShape = AppButtonShapes.primaryShapes(),
        styles: ButtonStyles = AppButtonStyles.primaryMediumStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun PrimarySmall(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.primaryColors(),
        sizes: ButtonSizes = AppButtonSizes.primarySmallSizes(),
        borders: ButtonBorders = AppButtonBorders.primaryBorders(),
        shapes: ButtonShape = AppButtonShapes.primaryShapes(),
        styles: ButtonStyles = AppButtonStyles.primarySmallStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondaryLarge(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryColors(),
        sizes: ButtonSizes = AppButtonSizes.secondaryLargeSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondaryLargeStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondaryMedium(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryColors(),
        sizes: ButtonSizes = AppButtonSizes.secondaryMediumSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondaryMediumStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondarySmall(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryColors(),
        sizes: ButtonSizes = AppButtonSizes.secondarySmallSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondarySmallStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondaryWhiteLarge(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryWhiteColors(),
        sizes: ButtonSizes = AppButtonSizes.secondaryLargeSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondaryLargeStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondaryWhiteMedium(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryWhiteColors(),
        sizes: ButtonSizes = AppButtonSizes.secondaryMediumSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondaryMediumStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun SecondaryWhiteSmall(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.secondaryWhiteColors(),
        sizes: ButtonSizes = AppButtonSizes.secondarySmallSizes(),
        borders: ButtonBorders = AppButtonBorders.secondaryBorders(),
        shapes: ButtonShape = AppButtonShapes.secondaryShapes(),
        styles: ButtonStyles = AppButtonStyles.secondarySmallStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun TertiaryLarge(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.tertiaryColors(),
        sizes: ButtonSizes = AppButtonSizes.tertiaryLargeSizes(),
        borders: ButtonBorders = AppButtonBorders.tertiaryBorders(),
        shapes: ButtonShape = AppButtonShapes.tertiaryShapes(),
        styles: ButtonStyles = AppButtonStyles.tertiaryLargeStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun TertiaryMedium(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.tertiaryColors(),
        sizes: ButtonSizes = AppButtonSizes.tertiaryMediumSizes(),
        borders: ButtonBorders = AppButtonBorders.tertiaryBorders(),
        shapes: ButtonShape = AppButtonShapes.tertiaryShapes(),
        styles: ButtonStyles = AppButtonStyles.tertiaryMediumStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun TertiarySmall(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.tertiaryColors(),
        sizes: ButtonSizes = AppButtonSizes.tertiarySmallSizes(),
        borders: ButtonBorders = AppButtonBorders.tertiaryBorders(),
        shapes: ButtonShape = AppButtonShapes.tertiaryShapes(),
        styles: ButtonStyles = AppButtonStyles.tertiarySmallStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun OutlineLarge(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.outlineColors(),
        sizes: ButtonSizes = AppButtonSizes.outlineLargeSizes(),
        borders: ButtonBorders = AppButtonBorders.outlineBorders(),
        shapes: ButtonShape = AppButtonShapes.outlineShapes(),
        styles: ButtonStyles = AppButtonStyles.outlineLargeStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun OutlineMedium(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.outlineColors(),
        sizes: ButtonSizes = AppButtonSizes.outlineMediumSizes(),
        borders: ButtonBorders = AppButtonBorders.outlineBorders(),
        shapes: ButtonShape = AppButtonShapes.outlineShapes(),
        styles: ButtonStyles = AppButtonStyles.outlineMediumStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )

    @Composable
    fun OutlineSmall(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: ButtonColors = AppButtonColors.outlineColors(),
        sizes: ButtonSizes = AppButtonSizes.outlineSmallSizes(),
        borders: ButtonBorders = AppButtonBorders.outlineBorders(),
        shapes: ButtonShape = AppButtonShapes.outlineShapes(),
        styles: ButtonStyles = AppButtonStyles.outlineSmallStyles(),
        @DrawableRes leftIcon: Int? = null,
        @DrawableRes rightIcon: Int? = null,
    ) = AppButtonImpl(
        text = text,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        onClick = onClick,
        modifier = modifier,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
    )
}

@Composable
private fun AppButtonImpl(
    text: String,
    enabled: Boolean,
    colors: ButtonColors,
    sizes: ButtonSizes,
    borders: ButtonBorders,
    shapes: ButtonShape,
    styles: ButtonStyles,
    onClick: () -> Unit,
    @DrawableRes leftIcon: Int?,
    @DrawableRes rightIcon: Int?,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (enabled) colors.enabledContentColor else colors.disabledContentColor
    val containerColor = if (enabled) colors.enabledContainerColor else colors.disabledContainerColor
    val border = if (enabled) borders.stroke else borders.disabled
    val leftIconColorFilter by colors.leftIconColorFilter(enabled = enabled)
    val rightIconColorFilter by colors.rightIconColorFilter(enabled = enabled)

    val mergedStyle = styles.textStyle.merge(
        other = TextStyle(color = contentColor),
    )
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
            Modifier
                .padding(sizes.contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leftIcon != null) {
                Image(
                    modifier = Modifier
                        .padding(end = sizes.iconTrailingPadding)
                        .size(sizes.iconSize),
                    painter = painterResource(id = leftIcon),
                    contentDescription = "left icon",
                    colorFilter = leftIconColorFilter,
                )
            }

            BasicText(
                modifier = Modifier,
                text = text,
                style = mergedStyle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            if (rightIcon != null) {
                Image(
                    modifier = Modifier
                        .padding(start = sizes.iconLeadingPadding)
                        .size(sizes.iconSize),
                    painter = painterResource(id = rightIcon),
                    contentDescription = "right icon",
                    colorFilter = rightIconColorFilter,
                )
            }
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
                AppButton.PrimaryLarge(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Medium")
            }

            item {
                AppButton.PrimaryMedium(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Small")
            }

            item {
                AppButton.PrimarySmall(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
}

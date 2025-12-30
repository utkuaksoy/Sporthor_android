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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class CircleButtonColors(
    val enabledContainerColor: Color,
    val enabledContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)

object AppCircleButtonColors {
    @Composable
    fun primaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonPrimaryColors.circleButtonPrimaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonPrimaryColors.circleButtonPrimaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonPrimaryColors.circleButtonPrimaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonPrimaryColors.circleButtonPrimaryDisabledForeground,
    ): CircleButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        CircleButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }

    @Composable
    fun secondaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryColors.circleButtonSecondaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryColors.circleButtonSecondaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryColors.circleButtonSecondaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryColors.circleButtonSecondaryDisabledForeground,
    ): CircleButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        CircleButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }

    @Composable
    fun secondaryGrayColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryGrayColors.circleButtonSecondaryGrayEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryGrayColors.circleButtonSecondaryGrayEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryGrayColors.circleButtonSecondaryGrayDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonSecondaryGrayColors.circleButtonSecondaryGrayDisabledForeground,
    ): CircleButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        CircleButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }

    @Composable
    fun tertiaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonTertiaryColors.circleButtonTertiaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonTertiaryColors.circleButtonTertiaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonTertiaryColors.circleButtonTertiaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonTertiaryColors.circleButtonTertiaryDisabledForeground,
    ): CircleButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        CircleButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }

    @Composable
    fun outlineColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineDisabledForeground,
    ): CircleButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        CircleButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }
}

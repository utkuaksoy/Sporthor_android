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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class ButtonColors(
    val enabledContainerColor: Color,
    val enabledContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val leftIconEnabledColor: Color?,
    val leftIconDisabledColor: Color?,
    val rightIconEnabledColor: Color?,
    val rightIconDisabledColor: Color?,
) {
    @Composable
    fun leftIconColorFilter(enabled: Boolean): State<ColorFilter?> {
        val targetValue: ColorFilter? = if (enabled) {
            when {
                leftIconEnabledColor != null -> ColorFilter.tint(leftIconEnabledColor)
                else -> null
            }
        } else {
            when {
                leftIconDisabledColor != null -> ColorFilter.tint(leftIconDisabledColor)
                else -> null
            }
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    fun rightIconColorFilter(enabled: Boolean): State<ColorFilter?> {
        val targetValue: ColorFilter? = if (enabled) {
            when {
                rightIconEnabledColor != null -> ColorFilter.tint(rightIconEnabledColor)
                else -> null
            }
        } else {
            when {
                rightIconDisabledColor != null -> ColorFilter.tint(rightIconDisabledColor)
                else -> null
            }
        }
        return rememberUpdatedState(targetValue)
    }
}

object AppButtonColors {
    @Composable
    fun primaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryDisabledForeground,
        leftIconEnabledColor: Color? = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryEnabledForeground,
        leftIconDisabledColor: Color? = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryDisabledForeground,
        rightIconEnabledColor: Color? = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryEnabledForeground,
        rightIconDisabledColor: Color? = AppTheme.colors.buttonColors.primaryColors.buttonPrimaryDisabledForeground,
    ): ButtonColors = remember(
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        leftIconEnabledColor,
        leftIconDisabledColor,
        rightIconEnabledColor,
        rightIconDisabledColor,
    ) {
        ButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            leftIconEnabledColor = leftIconEnabledColor,
            leftIconDisabledColor = leftIconDisabledColor,
            rightIconEnabledColor = rightIconEnabledColor,
            rightIconDisabledColor = rightIconDisabledColor,
        )
    }

    @Composable
    fun secondaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryDisabledForeground,
        leftIconEnabledColor: Color? = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryEnabledForeground,
        leftIconDisabledColor: Color? = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryDisabledForeground,
        rightIconEnabledColor: Color? = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryEnabledForeground,
        rightIconDisabledColor: Color? = AppTheme.colors.buttonColors.secondaryColors.buttonSecondaryDisabledForeground,
    ): ButtonColors = remember(
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        leftIconEnabledColor,
        leftIconDisabledColor,
        rightIconEnabledColor,
        rightIconDisabledColor,
    ) {
        ButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            leftIconEnabledColor = leftIconEnabledColor,
            leftIconDisabledColor = leftIconDisabledColor,
            rightIconEnabledColor = rightIconEnabledColor,
            rightIconDisabledColor = rightIconDisabledColor,
        )
    }

    @Composable
    fun secondaryWhiteColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteDisabledForeground,
        leftIconEnabledColor: Color? = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteEnabledForeground,
        leftIconDisabledColor: Color? = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteDisabledForeground,
        rightIconEnabledColor: Color? = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteEnabledForeground,
        rightIconDisabledColor: Color? = AppTheme.colors.buttonColors.secondaryWhiteColors.buttonSecondaryWhiteDisabledForeground,
    ): ButtonColors = remember(
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        leftIconEnabledColor,
        leftIconDisabledColor,
        rightIconEnabledColor,
        rightIconDisabledColor,
    ) {
        ButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            leftIconEnabledColor = leftIconEnabledColor,
            leftIconDisabledColor = leftIconDisabledColor,
            rightIconEnabledColor = rightIconEnabledColor,
            rightIconDisabledColor = rightIconDisabledColor,
        )
    }

    @Composable
    fun tertiaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryDisabledForeground,
        leftIconEnabledColor: Color? = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryEnabledForeground,
        leftIconDisabledColor: Color? = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryDisabledForeground,
        rightIconEnabledColor: Color? = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryEnabledForeground,
        rightIconDisabledColor: Color? = AppTheme.colors.buttonColors.tertiaryColors.buttonTertiaryDisabledForeground,
    ): ButtonColors = remember(
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        leftIconEnabledColor,
        leftIconDisabledColor,
        rightIconEnabledColor,
        rightIconDisabledColor,
    ) {
        ButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            leftIconEnabledColor = leftIconEnabledColor,
            leftIconDisabledColor = leftIconDisabledColor,
            rightIconEnabledColor = rightIconEnabledColor,
            rightIconDisabledColor = rightIconDisabledColor,
        )
    }

    @Composable
    fun outlineColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.outlineColors.buttonOutlineEnabledBackground,
        enabledContentColor: Color = AppTheme.colors.buttonColors.outlineColors.buttonOutlineEnabledForeground,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.outlineColors.buttonOutlineDisabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttonColors.outlineColors.buttonOutlineDisabledForeground,
        leftIconEnabledColor: Color? = AppTheme.colors.buttonColors.outlineColors.buttonOutlineEnabledForeground,
        leftIconDisabledColor: Color? = AppTheme.colors.buttonColors.outlineColors.buttonOutlineDisabledForeground,
        rightIconEnabledColor: Color? = AppTheme.colors.buttonColors.outlineColors.buttonOutlineEnabledForeground,
        rightIconDisabledColor: Color? = AppTheme.colors.buttonColors.outlineColors.buttonOutlineDisabledForeground,
    ): ButtonColors = remember(
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        leftIconEnabledColor,
        leftIconDisabledColor,
        rightIconEnabledColor,
        rightIconDisabledColor,
    ) {
        ButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            leftIconEnabledColor = leftIconEnabledColor,
            leftIconDisabledColor = leftIconDisabledColor,
            rightIconEnabledColor = rightIconEnabledColor,
            rightIconDisabledColor = rightIconDisabledColor,
        )
    }
}

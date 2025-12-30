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
package com.iamkurtgoz.core.commonui.component.otpfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

data class OtpFieldColors(
    val enabledContainerColor: Color,
    val enabledContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val cursorColor: Color,
    val enabledHintContentColor: Color,
    val disabledHintContentColor: Color,
    val errorHintContentColor: Color,
) {
    @Composable
    internal fun containerColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()
        val containerColor = when {
            !enabled -> disabledContainerColor
            isError -> enabledContainerColor
            focused -> enabledContainerColor
            else -> enabledContainerColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun contentColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()
        val containerColor = when {
            !enabled -> disabledContentColor
            isError -> enabledContentColor
            focused -> enabledContentColor
            else -> enabledContentColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun hintColor(
        enabled: Boolean,
        isError: Boolean,
    ): State<Color> {
        val containerColor = when {
            !enabled -> disabledHintContentColor
            isError -> errorHintContentColor
            else -> enabledHintContentColor
        }

        return rememberUpdatedState(containerColor)
    }
}

object AppOtpFieldColors {
    @Composable
    fun primaryColors(
        enabledContainerColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryEnabledContainerColor,
        enabledContentColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryEnabledContentColor,
        disabledContainerColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryDisabledContainerColor,
        disabledContentColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryDisabledContentColor,
        cursorColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryCursorColor,
        enabledHintContentColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryEnabledHintContentColor,
        disabledHintContentColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryDisabledHintContentColor,
        errorHintContentColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryErrorHintContentColor,
    ): OtpFieldColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor, cursorColor, enabledHintContentColor, disabledHintContentColor, errorHintContentColor) {
        OtpFieldColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            cursorColor = cursorColor,
            enabledHintContentColor = enabledHintContentColor,
            disabledHintContentColor = disabledHintContentColor,
            errorHintContentColor = errorHintContentColor,
        )
    }
}

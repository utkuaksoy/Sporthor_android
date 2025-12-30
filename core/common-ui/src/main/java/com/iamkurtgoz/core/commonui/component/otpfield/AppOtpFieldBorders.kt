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

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

data class OtpFieldBorders(
    val enabledBorderColor: Color,
    val disabledBorderColor: Color,
    val focusedBorderColor: Color,
    val errorBorderColor: Color,
    val enabledBorderWidth: Dp,
    val disabledBorderWidth: Dp,
    val focusedBorderWidth: Dp,
    val errorBorderWidth: Dp,
    val shape: RoundedCornerShape,
) {
    @Composable
    internal fun borderModifier(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Modifier> {
        val focused by interactionSource.collectIsFocusedAsState()

        val borderColor = when {
            !enabled -> disabledBorderColor
            isError -> errorBorderColor
            focused -> focusedBorderColor
            else -> enabledBorderColor
        }

        val borderWidth = when {
            !enabled -> disabledBorderWidth
            isError -> errorBorderWidth
            focused -> focusedBorderWidth
            else -> enabledBorderWidth
        }

        val modifier = Modifier
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )

        return rememberUpdatedState(modifier)
    }
}

object AppOtpFieldBorders {
    @Composable
    fun primaryBorders(
        enabledBorderColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryEnabledBorderColor,
        disabledBorderColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryDisabledBorderColor,
        focusedBorderColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryFocusedBorderColor,
        errorBorderColor: Color = AppTheme.colors.textFieldColors.otpFieldPrimaryColors.otpFieldPrimaryErrorBorderColor,
        enabledBorderWidth: Dp = AppTheme.dimens.dp0,
        disabledBorderWidth: Dp = AppTheme.dimens.dp0,
        focusedBorderWidth: Dp = AppTheme.dimens.dp1dot5,
        errorBorderWidth: Dp = AppTheme.dimens.dp1dot5,
        shape: RoundedCornerShape = AppTheme.shapes.radiusMedium,
    ): OtpFieldBorders = remember(enabledBorderColor, disabledBorderColor, focusedBorderColor, errorBorderColor, enabledBorderWidth, disabledBorderWidth, focusedBorderWidth, errorBorderWidth, shape) {
        OtpFieldBorders(
            enabledBorderColor = enabledBorderColor,
            disabledBorderColor = disabledBorderColor,
            focusedBorderColor = focusedBorderColor,
            errorBorderColor = errorBorderColor,
            enabledBorderWidth = enabledBorderWidth,
            disabledBorderWidth = disabledBorderWidth,
            focusedBorderWidth = focusedBorderWidth,
            errorBorderWidth = errorBorderWidth,
            shape = shape,
        )
    }
}

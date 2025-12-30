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

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class CircleButtonBorders(
    val stroke: BorderStroke? = null,
    val disabled: BorderStroke? = stroke,
)

object AppCircleButtonBorders {
    @Composable
    fun primaryBorders(
        width: Dp = AppTheme.dimens.dp0,
        strokeColor: Color = AppTheme.colors.generalColors.transparent,
        disabledColor: Color = AppTheme.colors.generalColors.transparent,
    ) = remember(width, strokeColor, disabledColor) {
        CircleButtonBorders(
            stroke = BorderStroke(width, strokeColor),
            disabled = BorderStroke(width, disabledColor),
        )
    }

    @Composable
    fun secondaryBorders(
        width: Dp = AppTheme.dimens.dp0,
        strokeColor: Color = AppTheme.colors.generalColors.transparent,
        disabledColor: Color = AppTheme.colors.generalColors.transparent,
    ) = remember(width, strokeColor, disabledColor) {
        CircleButtonBorders(
            stroke = BorderStroke(width, strokeColor),
            disabled = BorderStroke(width, disabledColor),
        )
    }

    @Composable
    fun tertiaryBorders(
        width: Dp = AppTheme.dimens.dp0,
        strokeColor: Color = AppTheme.colors.generalColors.transparent,
        disabledColor: Color = AppTheme.colors.generalColors.transparent,
    ) = remember(width, strokeColor, disabledColor) {
        CircleButtonBorders(
            stroke = BorderStroke(width, strokeColor),
            disabled = BorderStroke(width, disabledColor),
        )
    }

    @Composable
    fun outlineBorders(
        width: Dp = AppTheme.dimens.dp1,
        strokeColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineEnabledBorder,
        disabledColor: Color = AppTheme.colors.buttonColors.circleButtonOutlineColors.circleButtonOutlineDisabledBorder,
    ) = remember(width, strokeColor, disabledColor) {
        CircleButtonBorders(
            stroke = BorderStroke(width, strokeColor),
            disabled = BorderStroke(width, disabledColor),
        )
    }
}

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
package com.iamkurtgoz.core.commonui.component.selectableCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class SelectableCardBorder(
    val selected: BorderStroke,
    val unSelected: BorderStroke,
) {
    @Composable
    fun border(isSelected: Boolean): State<BorderStroke> {
        val targetValue: BorderStroke = when {
            !isSelected -> unSelected
            else -> selected
        }
        return rememberUpdatedState(targetValue)
    }
}

object SelectableCardBorders {
    @Composable
    fun primaryBorders(
        selectedWidth: Dp = AppTheme.dimens.dp2,
        unSelectedWidth: Dp = AppTheme.dimens.dp0,
        selectedStrokeColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardSelectedBorderColor,
        unSelectedStrokeColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardUnSelectedBorderColor,
    ): SelectableCardBorder = remember(selectedWidth, unSelectedWidth, selectedStrokeColor, unSelectedStrokeColor) {
        SelectableCardBorder(
            selected = BorderStroke(
                width = selectedWidth,
                color = selectedStrokeColor,
            ),
            unSelected = BorderStroke(
                width = unSelectedWidth,
                color = unSelectedStrokeColor,
            ),
        )
    }
}

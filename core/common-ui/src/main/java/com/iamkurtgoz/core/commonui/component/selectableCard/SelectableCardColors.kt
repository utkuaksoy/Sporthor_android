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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class SelectableCardColor(
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val selectedIconBackgroundColor: Color,
    val unSelectedContainerColor: Color,
    val unSelectedContentColor: Color,
    val unSelectedIconBackgroundColor: Color,
) {
    @Composable
    fun containerColor(isSelected: Boolean): State<Color> {
        val targetValue: Color = when {
            !isSelected -> unSelectedContainerColor
            else -> selectedContainerColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    fun contentColor(isSelected: Boolean): State<Color> {
        val targetValue: Color = when {
            !isSelected -> unSelectedContentColor
            else -> selectedContentColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    fun iconBackgroundColor(isSelected: Boolean): State<Color> {
        val targetValue: Color = when {
            !isSelected -> unSelectedIconBackgroundColor
            else -> selectedIconBackgroundColor
        }
        return rememberUpdatedState(targetValue)
    }
}

object SelectableCardColors {
    @Composable
    fun primaryColors(
        selectedContainerColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardSelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardSelectedContentColor,
        selectedIconBackgroundColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardSelectedIconBackgroundColor,
        unSelectedContainerColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardUnSelectedContentColor,
        unSelectedIconBackgroundColor: Color = AppTheme.colors.cardColors.selectableCardPrimaryColors.selectableCardUnSelectedIconBackgroundColor,
    ): SelectableCardColor = remember(
        selectedContainerColor,
        selectedContentColor,
        selectedIconBackgroundColor,
        unSelectedContainerColor,
        unSelectedContentColor,
        unSelectedIconBackgroundColor,
    ) {
        SelectableCardColor(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedIconBackgroundColor = selectedIconBackgroundColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedIconBackgroundColor = unSelectedIconBackgroundColor,
        )
    }
}

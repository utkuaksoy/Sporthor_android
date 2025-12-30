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
package com.iamkurtgoz.core.designsystem.component.horizontalListButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class HorizontalListButtonColors(
    val enabledContainerColor: Color,
    val enabledContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)

object AppHorizontalListButtonColors {
    @Composable
    fun primaryColors(
        enabledContainerColor: Color = AppTheme.colors.buttonColors.horizontalListButtonColors.horizontalListButtonPrimaryEnabledContainerColor,
        enabledContentColor: Color = AppTheme.colors.buttonColors.horizontalListButtonColors.horizontalListButtonPrimaryEnabledContentColor,
        disabledContainerColor: Color = AppTheme.colors.buttonColors.horizontalListButtonColors.horizontalListButtonPrimaryDisabledContainerColor,
        disabledContentColor: Color = AppTheme.colors.buttonColors.horizontalListButtonColors.horizontalListButtonPrimaryDisabledContentColor,
    ): HorizontalListButtonColors = remember(enabledContainerColor, enabledContentColor, disabledContainerColor, disabledContentColor) {
        HorizontalListButtonColors(
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    }
}

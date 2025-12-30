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
package com.iamkurtgoz.core.commonui.component.suggestionUserCard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

data class SuggestionUserCardColor(
    val suggestionUserCardContainerColor: Color,
    val suggestionUserCardContentColor: Color,
    val suggestionUserCardBorderColor: Color,
)

object SuggestionUserCardColors {
    @Composable
    fun primaryColors(
        suggestionUserCardContainerColor: Color = AppTheme.colors.cardColors.suggestionUserCardPrimaryColors.suggestionUserCardContainerColor,
        suggestionUserCardContentColor: Color = AppTheme.colors.cardColors.suggestionUserCardPrimaryColors.suggestionUserCardContentColor,
        suggestionUserCardBorderColor: Color = AppTheme.colors.cardColors.suggestionUserCardPrimaryColors.suggestionUserCardBorderColor,
    ): SuggestionUserCardColor = remember(
        suggestionUserCardContainerColor,
        suggestionUserCardContentColor,
        suggestionUserCardBorderColor,
    ) {
        SuggestionUserCardColor(
            suggestionUserCardContainerColor = suggestionUserCardContainerColor,
            suggestionUserCardContentColor = suggestionUserCardContentColor,
            suggestionUserCardBorderColor = suggestionUserCardBorderColor,
        )
    }
}

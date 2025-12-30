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
package com.iamkurtgoz.core.designsystem.theme.configuration.color.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
data class CardColors(
    val selectableCardPrimaryColors: SelectableCardPrimaryColors,
    val suggestionUserCardPrimaryColors: SuggestionUserCardPrimaryColors,
) {
    fun update(other: CardColors) {
        selectableCardPrimaryColors.update(other.selectableCardPrimaryColors)
        suggestionUserCardPrimaryColors.update(other.suggestionUserCardPrimaryColors)
    }
}

@Stable
class SelectableCardPrimaryColors(
    selectableCardSelectedContainerColor: Color,
    selectableCardSelectedContentColor: Color,
    selectableCardSelectedBorderColor: Color,
    selectableCardSelectedIconBackgroundColor: Color,
    selectableCardUnSelectedContainerColor: Color,
    selectableCardUnSelectedContentColor: Color,
    selectableCardUnSelectedBorderColor: Color,
    selectableCardUnSelectedIconBackgroundColor: Color,
) {
    var selectableCardSelectedContainerColor by mutableStateOf(selectableCardSelectedContainerColor)
        private set
    var selectableCardSelectedContentColor by mutableStateOf(selectableCardSelectedContentColor)
        private set
    var selectableCardSelectedBorderColor by mutableStateOf(selectableCardSelectedBorderColor)
        private set
    var selectableCardSelectedIconBackgroundColor by mutableStateOf(selectableCardSelectedIconBackgroundColor)
        private set
    var selectableCardUnSelectedContainerColor by mutableStateOf(selectableCardUnSelectedContainerColor)
        private set
    var selectableCardUnSelectedContentColor by mutableStateOf(selectableCardUnSelectedContentColor)
        private set
    var selectableCardUnSelectedBorderColor by mutableStateOf(selectableCardUnSelectedBorderColor)
        private set
    var selectableCardUnSelectedIconBackgroundColor by mutableStateOf(selectableCardUnSelectedIconBackgroundColor)
        private set

    fun update(other: SelectableCardPrimaryColors) {
        selectableCardSelectedContainerColor = other.selectableCardSelectedContainerColor
        selectableCardSelectedContentColor = other.selectableCardSelectedContentColor
        selectableCardSelectedBorderColor = other.selectableCardSelectedBorderColor
        selectableCardSelectedIconBackgroundColor = other.selectableCardSelectedIconBackgroundColor
        selectableCardUnSelectedContainerColor = other.selectableCardUnSelectedContainerColor
        selectableCardUnSelectedContentColor = other.selectableCardUnSelectedContentColor
        selectableCardUnSelectedBorderColor = other.selectableCardUnSelectedBorderColor
        selectableCardUnSelectedIconBackgroundColor = other.selectableCardUnSelectedIconBackgroundColor
    }
}

@Stable
class SuggestionUserCardPrimaryColors(
    suggestionUserCardContainerColor: Color,
    suggestionUserCardContentColor: Color,
    suggestionUserCardBorderColor: Color,
) {
    var suggestionUserCardContainerColor by mutableStateOf(suggestionUserCardContainerColor)
        private set
    var suggestionUserCardContentColor by mutableStateOf(suggestionUserCardContentColor)
        private set
    var suggestionUserCardBorderColor by mutableStateOf(suggestionUserCardBorderColor)
        private set

    fun update(other: SuggestionUserCardPrimaryColors) {
        suggestionUserCardContainerColor = other.suggestionUserCardContainerColor
        suggestionUserCardContentColor = other.suggestionUserCardContentColor
        suggestionUserCardBorderColor = other.suggestionUserCardBorderColor
    }
}

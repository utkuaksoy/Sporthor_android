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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class SuggestionUserCardShape(
    val suggestionUserCardContainerShape: RoundedCornerShape,
    val imageBackgroundShape: RoundedCornerShape,
)

object SuggestionUserCardShapes {
    @Composable
    fun primaryShapes(
        suggestionUserCardContainerShape: RoundedCornerShape = AppTheme.shapes.radiusMedium,
        imageBackgroundShape: RoundedCornerShape = AppTheme.shapes.radiusCircle,
    ): SuggestionUserCardShape = remember(suggestionUserCardContainerShape, imageBackgroundShape) {
        SuggestionUserCardShape(
            suggestionUserCardContainerShape = suggestionUserCardContainerShape,
            imageBackgroundShape = imageBackgroundShape,
        )
    }
}

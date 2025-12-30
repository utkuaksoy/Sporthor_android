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

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Shape
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class SelectableCardShape(
    val selectedContainerShape: CutCornerShape,
    val unSelectedContainerShape: RoundedCornerShape,
    val iconBackgroundShape: RoundedCornerShape,
) {
    @Composable
    fun containerShape(isSelected: Boolean): State<Shape> {
        val targetValue = when {
            !isSelected -> unSelectedContainerShape
            else -> selectedContainerShape
        }

        return rememberUpdatedState(targetValue)
    }
}

object SelectableCardShapes {
    @Composable
    fun primaryShapes(
        selectedContainerShape: CutCornerShape = CutCornerShape(AppTheme.dimens.dp24),
        unSelectedContainerShape: RoundedCornerShape = AppTheme.shapes.radiusMedium,
        iconBackgroundShape: RoundedCornerShape = AppTheme.shapes.radiusCircle,
    ): SelectableCardShape = remember(selectedContainerShape, unSelectedContainerShape, iconBackgroundShape) {
        SelectableCardShape(
            selectedContainerShape = selectedContainerShape,
            unSelectedContainerShape = unSelectedContainerShape,
            iconBackgroundShape = iconBackgroundShape,
        )
    }
}

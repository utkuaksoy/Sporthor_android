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
package com.iamkurtgoz.core.commonui.shape

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class TicketShapeVertically(
    private val circleRadius: Dp,
    private val cornerSize: CornerSize,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(path = getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val roundedRect = RoundRect(size.toRect(), CornerRadius(cornerSize.toPx(size, density)))
        val roundedRectPath = Path().apply { addRoundRect(roundedRect) }
        return Path.combine(
            operation = PathOperation.Intersect,
            path1 = roundedRectPath,
            path2 = getTicketPath(size, density),
        )
    }

    private fun getTicketPath(size: Size, density: Density): Path {
        val middleY = size.height.div(other = 1.5f)
        val circleRadiusInPx = with(density) { circleRadius.toPx() }

        return Path().apply {
            reset()

            lineTo(x = 0F, y = 0F)

            lineTo(x = size.width, y = 0F)

            lineTo(x = size.width, y = middleY)

            arcTo(
                rect = Rect(
                    left = size.width - circleRadiusInPx,
                    top = middleY - circleRadiusInPx,
                    right = size.width + circleRadiusInPx,
                    bottom = middleY + circleRadiusInPx,
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -180F,
                forceMoveTo = false,
            )

            lineTo(x = size.width, y = size.height)

            lineTo(0F, size.height)

            lineTo(x = 0F, y = size.height)
            arcTo(
                rect = Rect(
                    left = 0F - circleRadiusInPx,
                    top = middleY - circleRadiusInPx,
                    right = circleRadiusInPx,
                    bottom = middleY + circleRadiusInPx,
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -180F,
                forceMoveTo = false,
            )

            lineTo(x = 0F, y = 0F)
        }
    }
}

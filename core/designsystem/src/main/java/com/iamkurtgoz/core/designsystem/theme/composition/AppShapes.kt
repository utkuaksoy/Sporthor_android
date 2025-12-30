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
package com.iamkurtgoz.core.designsystem.theme.composition

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

internal data object AppShapesStatic {
    const val RADIUS_CIRCLE_PERCENT = 50
}

@Immutable
data class AppShapes(
    val radiusNone: RoundedCornerShape,
    val radiusMicro: RoundedCornerShape,
    val radiusTiny: RoundedCornerShape,
    val radiusSmall: RoundedCornerShape,
    val radiusMedium: RoundedCornerShape,
    val radiusLarge: RoundedCornerShape,
    val radiusExtraLarge: RoundedCornerShape,
    val radiusDoubleExtraLarge: RoundedCornerShape,
    val radiusTripleExtraLarge: RoundedCornerShape,
    val radiusQuadExtraLarge: RoundedCornerShape,
    val radiusPentaExtraLarge: RoundedCornerShape,
    val radiusCircle: RoundedCornerShape,
    val radiusFull: RoundedCornerShape,
)

internal val appShapes = AppShapes(
    radiusNone = RoundedCornerShape(0.dp),
    radiusMicro = RoundedCornerShape(2.dp),
    radiusTiny = RoundedCornerShape(4.dp),
    radiusSmall = RoundedCornerShape(6.dp),
    radiusMedium = RoundedCornerShape(8.dp),
    radiusLarge = RoundedCornerShape(10.dp),
    radiusExtraLarge = RoundedCornerShape(12.dp),
    radiusDoubleExtraLarge = RoundedCornerShape(16.dp),
    radiusTripleExtraLarge = RoundedCornerShape(20.dp),
    radiusQuadExtraLarge = RoundedCornerShape(24.dp),
    radiusPentaExtraLarge = RoundedCornerShape(28.dp),
    radiusCircle = RoundedCornerShape(AppShapesStatic.RADIUS_CIRCLE_PERCENT),
    radiusFull = RoundedCornerShape(100.dp),
)

internal val LocalAppShapes = compositionLocalOf<AppShapes> { error("No Shapes provided") }

internal val mediumShapes = Shapes(
    extraSmall = appShapes.radiusTiny,
    small = appShapes.radiusSmall,
    medium = appShapes.radiusMedium,
    large = appShapes.radiusLarge,
    extraLarge = appShapes.radiusExtraLarge,
)

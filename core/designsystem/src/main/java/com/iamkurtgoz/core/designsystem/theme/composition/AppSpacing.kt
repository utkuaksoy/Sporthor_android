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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

@Immutable
data class AppSpacing(
    val spacingNone: Dp,
    val spacingTiniest: Dp,
    val spacingTiny: Dp,
    val spacingSmallest: Dp,
    val spacingSmall: Dp,
    val spacingRegular: Dp,
    val spacingMedium: Dp,
    val spacingHuge: Dp,
    val spacingLarge: Dp,
)

internal val appSpacing = AppSpacing(
    spacingNone = appDimens.dp0,
    spacingTiniest = appDimens.dp1,
    spacingTiny = appDimens.dp2,
    spacingSmallest = appDimens.dp4,
    spacingSmall = appDimens.dp8,
    spacingRegular = appDimens.dp12,
    spacingMedium = appDimens.dp16,
    spacingHuge = appDimens.dp24,
    spacingLarge = appDimens.dp32,
)

internal val LocalAppSpacing = staticCompositionLocalOf<AppSpacing> { error("No spacing provided") }

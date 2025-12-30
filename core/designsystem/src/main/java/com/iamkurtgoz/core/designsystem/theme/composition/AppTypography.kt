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

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle

data class AppTypography(
    val heading01: TextStyle,
    val heading02: TextStyle,
    val heading03: TextStyle,
    /**
     * fontWeight = FontWeight.SemiBold - W600
     * fontSize = 24
     * lineHeight = 32
     */
    val heading04: TextStyle,
    val heading05: TextStyle,
    val heading06: TextStyle,
    val heading07: TextStyle,
    val subtitleLarge: TextStyle,
    val subtitleSmall: TextStyle,
    val bodyXlarge: TextStyle,
    val bodyLarge: TextStyle,
    /**
     * fontWeight = FontWeight.Normal - W400
     * fontSize = 16
     * lineHeight = 22
     */
    val bodyLargeCompact: TextStyle,
    val bodyMedium: TextStyle,
    val bodyMediumCompact: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
    val labelRegular: TextStyle,
    val helperText: TextStyle,
    val link: TextStyle,
)

internal val LocalAppTypographies = compositionLocalOf<AppTypography> { error("No typography provided!") }

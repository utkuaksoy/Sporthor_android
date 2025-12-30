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
class GeneralColors(
    primary: Color,
    transparent: Color,
    textPrimary: Color,
    textSecondary: Color,
    textTertiary: Color,
    textDisabled: Color,
    textWhite: Color,
    textWhiteSecondary: Color,
    textSuccess800: Color,
    textError800: Color,
    backgroundPrimary: Color,
    backgroundSecondary: Color,
    backgroundDisabled: Color,
    foregroundPrimary: Color,
    foregroundSecondary: Color,
    foregroundDisabled: Color,
    foregroundWhite: Color,
    foregroundBlack: Color,
    borderSoft200: Color,
    borderSub300: Color,
    borderStrong900: Color,
    backgroundWeak100: Color,
    backgroundSoft200: Color,
    contentSoft600: Color,
    primitivesBlue600: Color,
    green100: Color,
    green200: Color,
    green500: Color,
    primaryGreen: Color,
    primitivesRed900: Color,
    primitivesRed500: Color,
    backgroundSurface800: Color,
    transparentWhite: Color,
    primitivesPink500: Color,
) {
    // General
    var primary by mutableStateOf(primary)
        private set
    var transparent by mutableStateOf(transparent)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textTertiary by mutableStateOf(textTertiary)
        private set
    var textDisabled by mutableStateOf(textDisabled)
        private set
    var textWhite by mutableStateOf(textWhite)
        private set
    var textWhiteSecondary by mutableStateOf(textWhiteSecondary)
        private set
    var textSuccess800 by mutableStateOf(textSuccess800)
        private set
    var textError800 by mutableStateOf(textError800)
        private set
    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        private set
    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set
    var backgroundDisabled by mutableStateOf(backgroundDisabled)
        private set
    var foregroundPrimary by mutableStateOf(foregroundPrimary)
        private set
    var foregroundSecondary by mutableStateOf(foregroundSecondary)
        private set
    var foregroundDisabled by mutableStateOf(foregroundDisabled)
        private set
    var foregroundWhite by mutableStateOf(foregroundWhite)
        private set
    var foregroundBlack by mutableStateOf(foregroundBlack)
        private set
    var borderSoft200 by mutableStateOf(borderSoft200)
        private set
    var borderSub300 by mutableStateOf(borderSub300)
        private set
    var borderStrong900 by mutableStateOf(borderStrong900)
        private set
    var backgroundWeak100 by mutableStateOf(backgroundWeak100)
        private set
    var backgroundSoft200 by mutableStateOf(backgroundSoft200)
        private set
    var contentSoft600 by mutableStateOf(contentSoft600)
        private set
    var primitivesBlue600 by mutableStateOf(primitivesBlue600)
        private set
    var green100 by mutableStateOf(green100)
        private set
    var green200 by mutableStateOf(green200)
        private set
    var green500 by mutableStateOf(green500)
        private set
    var primaryGreen by mutableStateOf(primaryGreen)
        private set
    var primitivesRed900 by mutableStateOf(primitivesRed900)
        private set
    var primitivesRed500 by mutableStateOf(primitivesRed500)
        private set
    var backgroundSurface800 by mutableStateOf(primitivesRed500)
        private set
    var transparentWhite by mutableStateOf(transparentWhite)
        private set
    var primitivesPink500 by mutableStateOf(primitivesPink500)
        private set

    fun update(other: GeneralColors) {
        primary = other.primary
        transparent = other.transparent
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textTertiary = other.textTertiary
        textDisabled = other.textDisabled
        textWhite = other.textWhite
        textWhiteSecondary = other.textWhiteSecondary
        textSuccess800 = other.textSuccess800
        textError800 = other.textError800
        backgroundPrimary = other.backgroundPrimary
        backgroundSecondary = other.backgroundSecondary
        backgroundDisabled = other.backgroundDisabled
        foregroundPrimary = other.foregroundPrimary
        foregroundSecondary = other.foregroundSecondary
        foregroundDisabled = other.foregroundDisabled
        foregroundWhite = other.foregroundWhite
        foregroundBlack = other.foregroundBlack
        borderSoft200 = other.borderSoft200
        borderSub300 = other.borderSub300
        borderStrong900 = other.borderStrong900
        backgroundWeak100 = other.backgroundWeak100
        backgroundSoft200 = other.backgroundSoft200
        contentSoft600 = other.contentSoft600
        primitivesBlue600 = other.primitivesBlue600
        green100 = other.green100
        green200 = other.green200
        green500 = other.green500
        primaryGreen = other.primaryGreen
        primitivesRed900 = other.primitivesRed900
        primitivesRed500 = other.primitivesRed500
        backgroundSurface800 = other.backgroundSurface800
        transparentWhite = other.transparentWhite
        primitivesPink500 = other.primitivesPink500
    }
}

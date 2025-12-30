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
package com.iamkurtgoz.core.designsystem.util

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.iamkurtgoz.core.designsystem.theme.composition.AppColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.AppColorPalette
import com.iamkurtgoz.core.designsystem.theme.configuration.color.AppThemeColorSchemeType
import com.iamkurtgoz.core.designsystem.theme.configuration.color.darkColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.lightColors

object AppThemeUtil {

    private val colorSchemeType: AppThemeColorSchemeType = AppThemeColorSchemeType.FORCE_LIGHT

    @Composable
    fun getColorScheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
    ): State<AppColors> {
        val colorScheme = when (colorSchemeType) {
            AppThemeColorSchemeType.FORCE_DARK -> AppColorPalette.darkColors
            AppThemeColorSchemeType.FORCE_LIGHT -> AppColorPalette.lightColors
            AppThemeColorSchemeType.SYSTEM -> if (darkTheme) {
                AppColorPalette.darkColors
            } else {
                AppColorPalette.lightColors
            }
        }
        return rememberUpdatedState(colorScheme)
    }

    fun setAppearanceBarsColors(
        view: View,
        darkTheme: Boolean,
        colorScheme: AppColors,
        isAppearanceLightStatusBars: Boolean? = null,
        isAppearanceLightNavigationBars: Boolean? = null,
    ) {
        val safeAppearanceLightStatusBars: Boolean = isAppearanceLightStatusBars ?: when (colorSchemeType) {
            AppThemeColorSchemeType.FORCE_DARK -> false
            AppThemeColorSchemeType.FORCE_LIGHT -> true
            AppThemeColorSchemeType.SYSTEM -> !darkTheme
        }

        val safeAppearanceLightNavigationBars: Boolean = isAppearanceLightNavigationBars ?: when (colorSchemeType) {
            AppThemeColorSchemeType.FORCE_DARK -> false
            AppThemeColorSchemeType.FORCE_LIGHT -> true
            AppThemeColorSchemeType.SYSTEM -> !darkTheme
        }

        if (!view.isInEditMode) {
            val window = (view.context as Activity).window

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                window.decorView.setBackgroundColor(colorScheme.generalColors.backgroundPrimary.toArgb())
            } else {
                @Suppress("DEPRECATION")
                window.statusBarColor = colorScheme.generalColors.backgroundPrimary.toArgb()
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.navigationBarColor = colorScheme.generalColors.backgroundPrimary.toArgb()
                }
            }

            // Set Appearance Light Bars Status
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = safeAppearanceLightStatusBars
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = safeAppearanceLightNavigationBars
            }
        }
    }
}

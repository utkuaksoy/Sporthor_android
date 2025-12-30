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
package com.iamkurtgoz.core.designsystem.theme.configuration.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.iamkurtgoz.core.designsystem.theme.composition.AppTypography
import com.iamkurtgoz.core.designsystem.theme.composition.appDimens

internal val appDefaultTypography = AppTypography(
    heading01 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp48,
        lineHeight = appDimens.sp58,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading02 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp40,
        lineHeight = appDimens.sp48,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading03 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp32,
        lineHeight = appDimens.sp40,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading04 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp24,
        lineHeight = appDimens.sp32,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading05 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp20,
        lineHeight = appDimens.sp28,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading06 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp18,
        lineHeight = appDimens.sp24,
        letterSpacing = TextUnit.Unspecified,
    ),
    heading07 = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = appDimens.sp16,
        lineHeight = appDimens.sp24,
        letterSpacing = TextUnit.Unspecified,
    ),
    subtitleLarge = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp16,
        lineHeight = appDimens.sp22,
        letterSpacing = TextUnit.Unspecified,
    ),
    subtitleSmall = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp18,
        letterSpacing = TextUnit.Unspecified,
    ),
    bodyXlarge = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp20,
        lineHeight = appDimens.sp28,
        letterSpacing = TextUnit.Unspecified,
    ),
    bodyLarge = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp16,
        lineHeight = appDimens.sp24,
        letterSpacing = TextUnit.Unspecified,
    ),
    bodyLargeCompact = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp16,
        lineHeight = appDimens.sp22,
        letterSpacing = TextUnit.Unspecified,
    ),
    bodyMedium = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp20,
        letterSpacing = TextUnit.Unspecified,
    ),
    bodyMediumCompact = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp18,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelLarge = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp16,
        lineHeight = appDimens.sp22,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelMedium = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp18,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelSmall = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp12,
        lineHeight = appDimens.sp14,
        letterSpacing = TextUnit.Unspecified,
    ),
    labelRegular = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp18,
        letterSpacing = TextUnit.Unspecified,
    ),
    helperText = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp12,
        lineHeight = appDimens.sp16,
        letterSpacing = TextUnit.Unspecified,
    ),
    link = TextStyle(
        fontFamily = AppTypographyDefaults.AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = appDimens.sp14,
        lineHeight = appDimens.sp18,
        letterSpacing = TextUnit.Unspecified,
    ),
)

val materialAppTypography = Typography(
    displayLarge = appDefaultTypography.heading01,
    displayMedium = appDefaultTypography.heading02,
    displaySmall = appDefaultTypography.heading03,
    headlineLarge = appDefaultTypography.heading03,
    headlineMedium = appDefaultTypography.heading04,
    headlineSmall = appDefaultTypography.heading05,
    titleLarge = appDefaultTypography.heading06,
    titleMedium = appDefaultTypography.heading07,
    titleSmall = appDefaultTypography.subtitleLarge,
    bodyLarge = appDefaultTypography.bodyLarge,
    bodyMedium = appDefaultTypography.bodyMedium,
    bodySmall = appDefaultTypography.bodyMediumCompact,
    labelLarge = appDefaultTypography.labelLarge,
    labelMedium = appDefaultTypography.labelMedium,
    labelSmall = appDefaultTypography.labelSmall,
)

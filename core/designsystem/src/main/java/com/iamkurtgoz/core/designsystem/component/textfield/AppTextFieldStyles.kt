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
package com.iamkurtgoz.core.designsystem.component.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class TextFieldStyles(
    val titleTextStyle: TextStyle,
    val contentTextStyle: TextStyle,
    val placeholderTextStyle: TextStyle,
    val hintTextStyle: TextStyle,
)

object AppTextFieldStyles {
    @Composable
    fun primaryStyles(
        titleTextStyle: TextStyle = AppTheme.typography.labelMedium,
        contentTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        placeholderTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        hintTextStyle: TextStyle = AppTheme.typography.labelSmall,
    ): TextFieldStyles = remember(titleTextStyle, contentTextStyle, placeholderTextStyle, hintTextStyle) {
        TextFieldStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
            placeholderTextStyle = placeholderTextStyle,
            hintTextStyle = hintTextStyle,
        )
    }

    @Composable
    fun searchFieldStyles(
        titleTextStyle: TextStyle = AppTheme.typography.labelMedium,
        contentTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        placeholderTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        hintTextStyle: TextStyle = AppTheme.typography.labelSmall,
    ): TextFieldStyles = remember(titleTextStyle, contentTextStyle, placeholderTextStyle, hintTextStyle) {
        TextFieldStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
            placeholderTextStyle = placeholderTextStyle,
            hintTextStyle = hintTextStyle,
        )
    }

    @Composable
    fun messageFieldStyles(
        titleTextStyle: TextStyle = AppTheme.typography.labelMedium,
        contentTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        placeholderTextStyle: TextStyle = AppTheme.typography.bodyLargeCompact,
        hintTextStyle: TextStyle = AppTheme.typography.labelSmall,
    ): TextFieldStyles = remember(titleTextStyle, contentTextStyle, placeholderTextStyle, hintTextStyle) {
        TextFieldStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
            placeholderTextStyle = placeholderTextStyle,
            hintTextStyle = hintTextStyle,
        )
    }
}

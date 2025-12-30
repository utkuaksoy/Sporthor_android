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
package com.iamkurtgoz.core.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class ButtonStyles(
    val textStyle: TextStyle,
)

object AppButtonStyles {
    @Composable
    fun primaryLargeStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun primaryMediumStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun primarySmallStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun secondaryLargeStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun secondaryMediumStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun secondarySmallStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun tertiaryLargeStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun tertiaryMediumStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun tertiarySmallStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun outlineLargeStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun outlineMediumStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }

    @Composable
    fun outlineSmallStyles(
        textStyle: TextStyle = AppTheme.typography.labelLarge,
    ) = remember(textStyle) {
        ButtonStyles(
            textStyle = textStyle,
        )
    }
}

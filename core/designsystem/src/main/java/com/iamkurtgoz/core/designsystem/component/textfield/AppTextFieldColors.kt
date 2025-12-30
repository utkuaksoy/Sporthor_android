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

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

data class TextFieldColors(
    val enabledTitleColor: Color,
    val disabledTitleColor: Color,
    val enabledContainerColor: Color,
    val enabledContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val placeholderColor: Color,
    val cursorColor: Color,
    val enabledLeadingIconColor: Color,
    val disabledLeadingIconColor: Color,
    val enabledTrailingIconColor: Color,
    val disabledTrailingIconColor: Color,
    val enabledHintContentColor: Color,
    val disabledHintContentColor: Color,
    val errorHintContentColor: Color,
) {
    @Composable
    internal fun containerColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()
        val containerColor = when {
            !enabled -> disabledContainerColor
            isError -> enabledContainerColor
            focused -> enabledContainerColor
            else -> enabledContainerColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun contentColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()
        val containerColor = when {
            !enabled -> disabledContentColor
            isError -> enabledContentColor
            focused -> enabledContentColor
            else -> enabledContentColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun titleColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()
        val containerColor = when {
            !enabled -> disabledTitleColor
            isError -> enabledTitleColor
            focused -> enabledTitleColor
            else -> enabledTitleColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun leadingIconColor(
        enabled: Boolean,
    ): State<Color> {
        val containerColor = when {
            enabled -> enabledLeadingIconColor
            else -> disabledLeadingIconColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun trailingIconColor(
        enabled: Boolean,
    ): State<Color> {
        val containerColor = when {
            enabled -> enabledTrailingIconColor
            else -> disabledTrailingIconColor
        }

        return rememberUpdatedState(containerColor)
    }

    @Composable
    internal fun hintColor(
        enabled: Boolean,
        isError: Boolean,
    ): State<Color> {
        val containerColor = when {
            !enabled -> disabledHintContentColor
            isError -> errorHintContentColor
            else -> enabledHintContentColor
        }

        return rememberUpdatedState(containerColor)
    }
}

object AppTextFieldColors {
    @Composable
    fun primaryColors(
        enabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledTitleColor,
        disabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledTitleColor,
        enabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledContainerColor,
        enabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledContentColor,
        disabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledContainerColor,
        disabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledContentColor,
        placeholderColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryPlaceholderColor,
        cursorColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryCursorColor,
        enabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledLeadingIconColor,
        disabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledLeadingIconColor,
        enabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledTrailingIconColor,
        disabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledTrailingIconColor,
        enabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledHintContentColor,
        disabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryDisabledHintContentColor,
        errorHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryErrorHintContentColor,
    ): TextFieldColors = remember(
        enabledTitleColor,
        disabledTitleColor,
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        placeholderColor,
        cursorColor,
        enabledLeadingIconColor,
        disabledLeadingIconColor,
        enabledTrailingIconColor,
        disabledTrailingIconColor,
        enabledHintContentColor,
        disabledHintContentColor,
        errorHintContentColor,
    ) {
        TextFieldColors(
            enabledTitleColor = enabledTitleColor,
            disabledTitleColor = disabledTitleColor,
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            placeholderColor = placeholderColor,
            cursorColor = cursorColor,
            enabledLeadingIconColor = enabledLeadingIconColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            enabledTrailingIconColor = enabledTrailingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
            enabledHintContentColor = enabledHintContentColor,
            disabledHintContentColor = disabledHintContentColor,
            errorHintContentColor = errorHintContentColor,
        )
    }

    @Composable
    fun searchFieldColors(
        enabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledTitleColor,
        disabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledTitleColor,
        enabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledContainerColor,
        enabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledContentColor,
        disabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledContainerColor,
        disabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledContentColor,
        placeholderColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchPlaceholderColor,
        cursorColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchCursorColor,
        enabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledLeadingIconColor,
        disabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledLeadingIconColor,
        enabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledTrailingIconColor,
        disabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledTrailingIconColor,
        enabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchEnabledHintContentColor,
        disabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchDisabledHintContentColor,
        errorHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldSearchColors.textFieldSearchErrorHintContentColor,
    ): TextFieldColors = remember(
        enabledTitleColor,
        disabledTitleColor,
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        placeholderColor,
        cursorColor,
        enabledLeadingIconColor,
        disabledLeadingIconColor,
        enabledTrailingIconColor,
        disabledTrailingIconColor,
        enabledHintContentColor,
        disabledHintContentColor,
        errorHintContentColor,
    ) {
        TextFieldColors(
            enabledTitleColor = enabledTitleColor,
            disabledTitleColor = disabledTitleColor,
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            placeholderColor = placeholderColor,
            cursorColor = cursorColor,
            enabledLeadingIconColor = enabledLeadingIconColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            enabledTrailingIconColor = enabledTrailingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
            enabledHintContentColor = enabledHintContentColor,
            disabledHintContentColor = disabledHintContentColor,
            errorHintContentColor = errorHintContentColor,
        )
    }

    @Composable
    fun messageFieldColors(
        enabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledTitleColor,
        disabledTitleColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledTitleColor,
        enabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledContainerColor,
        enabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledContentColor,
        disabledContainerColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledContainerColor,
        disabledContentColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledContentColor,
        placeholderColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessagePlaceholderColor,
        cursorColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageCursorColor,
        enabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledLeadingIconColor,
        disabledLeadingIconColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledLeadingIconColor,
        enabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledTrailingIconColor,
        disabledTrailingIconColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledTrailingIconColor,
        enabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageEnabledHintContentColor,
        disabledHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageDisabledHintContentColor,
        errorHintContentColor: Color = AppTheme.colors.textFieldColors.textFieldMessageColors.textFieldMessageErrorHintContentColor,
    ): TextFieldColors = remember(
        enabledTitleColor,
        disabledTitleColor,
        enabledContainerColor,
        enabledContentColor,
        disabledContainerColor,
        disabledContentColor,
        placeholderColor,
        cursorColor,
        enabledLeadingIconColor,
        disabledLeadingIconColor,
        enabledTrailingIconColor,
        disabledTrailingIconColor,
        enabledHintContentColor,
        disabledHintContentColor,
        errorHintContentColor,
    ) {
        TextFieldColors(
            enabledTitleColor = enabledTitleColor,
            disabledTitleColor = disabledTitleColor,
            enabledContainerColor = enabledContainerColor,
            enabledContentColor = enabledContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            placeholderColor = placeholderColor,
            cursorColor = cursorColor,
            enabledLeadingIconColor = enabledLeadingIconColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            enabledTrailingIconColor = enabledTrailingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
            enabledHintContentColor = enabledHintContentColor,
            disabledHintContentColor = disabledHintContentColor,
            errorHintContentColor = errorHintContentColor,
        )
    }
}

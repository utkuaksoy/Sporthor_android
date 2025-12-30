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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class TextFieldSizes(
    val height: Dp,
    val contentPadding: PaddingValues,
    val trailingIconSize: Dp,
)

object AppTextFieldSizes {
    @Composable
    fun primarySizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp12,
            horizontal = AppTheme.dimens.dp16,
        ),
        trailingIconSize: Dp = AppTheme.dimens.dp18,
    ): TextFieldSizes = remember(height, contentPadding, trailingIconSize) {
        TextFieldSizes(
            height = height,
            contentPadding = contentPadding,
            trailingIconSize = trailingIconSize,
        )
    }

    @Composable
    fun searchFieldSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp12,
            horizontal = AppTheme.dimens.dp16,
        ),
        trailingIconSize: Dp = AppTheme.dimens.dp18,
    ): TextFieldSizes = remember(height, contentPadding, trailingIconSize) {
        TextFieldSizes(
            height = height,
            contentPadding = contentPadding,
            trailingIconSize = trailingIconSize,
        )
    }

    @Composable
    fun messageFieldSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            start = AppTheme.dimens.dp16,
            top = AppTheme.dimens.dp4,
            end = AppTheme.dimens.dp4,
            bottom = AppTheme.dimens.dp4,
        ),
        trailingIconSize: Dp = AppTheme.dimens.dp38,
    ): TextFieldSizes = remember(height, contentPadding, trailingIconSize) {
        TextFieldSizes(
            height = height,
            contentPadding = contentPadding,
            trailingIconSize = trailingIconSize,
        )
    }
}

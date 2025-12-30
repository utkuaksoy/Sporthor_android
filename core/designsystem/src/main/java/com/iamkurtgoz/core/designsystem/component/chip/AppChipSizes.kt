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
package com.iamkurtgoz.core.designsystem.component.chip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class ChipSizes(
    val height: Dp,
    val contentPadding: PaddingValues,
    val borderWidth: Dp,
)

object AppChipSizes {
    @Composable
    fun primarySizes(
        height: Dp = AppTheme.dimens.dp34,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp8,
            horizontal = AppTheme.dimens.dp16,
        ),
        borderWidth: Dp = AppTheme.dimens.dp0,
    ): ChipSizes = remember(height, contentPadding, borderWidth) {
        ChipSizes(
            height = height,
            contentPadding = contentPadding,
            borderWidth = borderWidth,
        )
    }

    @Composable
    fun secondarySizes(
        height: Dp = AppTheme.dimens.dp22,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp4,
            horizontal = AppTheme.dimens.dp10,
        ),
        borderWidth: Dp = AppTheme.dimens.dp0,
    ): ChipSizes = remember(height, contentPadding, borderWidth) {
        ChipSizes(
            height = height,
            contentPadding = contentPadding,
            borderWidth = borderWidth,
        )
    }

    @Composable
    fun tertiarySizes(
        height: Dp = AppTheme.dimens.dp24,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp5,
            horizontal = AppTheme.dimens.dp12,
        ),
        borderWidth: Dp = AppTheme.dimens.dp0,
    ): ChipSizes = remember(height, contentPadding, borderWidth) {
        ChipSizes(
            height = height,
            contentPadding = contentPadding,
            borderWidth = borderWidth,
        )
    }

    @Composable
    fun profileGraySizes(
        height: Dp = AppTheme.dimens.dp40,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp8,
            horizontal = AppTheme.dimens.dp8,
        ),
        borderWidth: Dp = AppTheme.dimens.dp1,
    ): ChipSizes = remember(height, contentPadding, borderWidth) {
        ChipSizes(
            height = height,
            contentPadding = contentPadding,
            borderWidth = borderWidth,
        )
    }

    @Composable
    fun profileBranchSizes(
        height: Dp = AppTheme.dimens.dp40,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp8,
            horizontal = AppTheme.dimens.dp8,
        ),
        borderWidth: Dp = AppTheme.dimens.dp1,
    ): ChipSizes = remember(height, contentPadding, borderWidth) {
        ChipSizes(
            height = height,
            contentPadding = contentPadding,
            borderWidth = borderWidth,
        )
    }
}

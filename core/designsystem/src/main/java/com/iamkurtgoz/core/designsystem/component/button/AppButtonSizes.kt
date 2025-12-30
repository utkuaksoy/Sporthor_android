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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class ButtonSizes(
    val height: Dp,
    val contentPadding: PaddingValues,
    val iconSize: Dp,
    val iconLeadingPadding: Dp,
    val iconTrailingPadding: Dp,
)

object AppButtonSizes {
    @Composable
    fun primaryLargeSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp14,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp22,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun primaryMediumSizes(
        height: Dp = AppTheme.dimens.dp38,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp10,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun primarySmallSizes(
        height: Dp = AppTheme.dimens.dp30,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp6,
            horizontal = AppTheme.dimens.dp6,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun secondaryLargeSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp14,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp22,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun secondaryMediumSizes(
        height: Dp = AppTheme.dimens.dp38,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp10,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun secondarySmallSizes(
        height: Dp = AppTheme.dimens.dp30,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp6,
            horizontal = AppTheme.dimens.dp6,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun tertiaryLargeSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp14,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp22,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun tertiaryMediumSizes(
        height: Dp = AppTheme.dimens.dp38,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp10,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun tertiarySmallSizes(
        height: Dp = AppTheme.dimens.dp30,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp6,
            horizontal = AppTheme.dimens.dp6,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun outlineLargeSizes(
        height: Dp = AppTheme.dimens.dp48,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp14,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp22,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun outlineMediumSizes(
        height: Dp = AppTheme.dimens.dp38,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp10,
            horizontal = AppTheme.dimens.dp14,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }

    @Composable
    fun outlineSmallSizes(
        height: Dp = AppTheme.dimens.dp30,
        contentPadding: PaddingValues = PaddingValues(
            vertical = AppTheme.dimens.dp6,
            horizontal = AppTheme.dimens.dp6,
        ),
        iconSize: Dp = AppTheme.dimens.dp18,
        iconLeadingPadding: Dp = AppTheme.dimens.dp8,
        iconTrailingPadding: Dp = AppTheme.dimens.dp8,
    ) = remember(height, contentPadding) {
        ButtonSizes(
            height = height,
            contentPadding = contentPadding,
            iconSize = iconSize,
            iconLeadingPadding = iconLeadingPadding,
            iconTrailingPadding = iconTrailingPadding,
        )
    }
}

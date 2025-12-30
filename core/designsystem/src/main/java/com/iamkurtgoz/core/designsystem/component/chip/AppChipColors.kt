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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Immutable
data class ChipColors(
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val selectedBorderColor: Color,
    val unSelectedContainerColor: Color,
    val unSelectedContentColor: Color,
    val unSelectedBorderColor: Color,
) {
    @Composable
    fun containerColor(isSelected: Boolean): State<Color> {
        val targetValue: Color = when {
            !isSelected -> unSelectedContainerColor
            else -> selectedContainerColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    fun contentColor(isSelected: Boolean): State<Color> {
        val targetValue: Color = when {
            !isSelected -> unSelectedContentColor
            else -> selectedContentColor
        }
        return rememberUpdatedState(targetValue)
    }
}

object AppChipColors {
    @Composable
    fun primaryColors(
        selectedContainerColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimarySelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimarySelectedContentColor,
        selectedBorderColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimarySelectedBorderColor,
        unSelectedContainerColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimaryUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimaryUnSelectedContentColor,
        unSelectedBorderColor: Color = AppTheme.colors.chipColors.primaryChipColors.appChipPrimaryUnSelectedBorderColor,
    ): ChipColors = remember(selectedContainerColor, selectedContentColor, unSelectedContainerColor, unSelectedContentColor) {
        ChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedBorderColor = selectedBorderColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedBorderColor = unSelectedBorderColor,
        )
    }

    @Composable
    fun secondaryColors(
        selectedContainerColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondarySelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondarySelectedContentColor,
        selectedBorderColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondarySelectedBorderColor,
        unSelectedContainerColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondaryUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondaryUnSelectedContentColor,
        unSelectedBorderColor: Color = AppTheme.colors.chipColors.secondaryChipColors.appChipSecondaryUnSelectedBorderColor,
    ): ChipColors = remember(selectedContainerColor, selectedContentColor, unSelectedContainerColor, unSelectedContentColor) {
        ChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedBorderColor = selectedBorderColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedBorderColor = unSelectedBorderColor,
        )
    }

    @Composable
    fun tertiaryColors(
        selectedContainerColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiarySelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiarySelectedContentColor,
        selectedBorderColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiarySelectedBorderColor,
        unSelectedContainerColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiaryUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiaryUnSelectedContentColor,
        unSelectedBorderColor: Color = AppTheme.colors.chipColors.tertiaryChipColors.appChipTertiaryUnSelectedBorderColor,
    ): ChipColors = remember(selectedContainerColor, selectedContentColor, unSelectedContainerColor, unSelectedContentColor) {
        ChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedBorderColor = selectedBorderColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedBorderColor = unSelectedBorderColor,
        )
    }

    @Composable
    fun profileGrayColors(
        selectedContainerColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGraySelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGraySelectedContentColor,
        selectedBorderColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGraySelectedBorderColor,
        unSelectedContainerColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGrayUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGrayUnSelectedContentColor,
        unSelectedBorderColor: Color = AppTheme.colors.chipColors.profileGrayColors.appChipProfileGrayUnSelectedBorderColor,
    ): ChipColors = remember(selectedContainerColor, selectedContentColor, unSelectedContainerColor, unSelectedContentColor) {
        ChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedBorderColor = selectedBorderColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedBorderColor = unSelectedBorderColor,
        )
    }

    @Composable
    fun profileBranchColors(
        selectedContainerColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchSelectedContainerColor,
        selectedContentColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchSelectedContentColor,
        selectedBorderColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchSelectedBorderColor,
        unSelectedContainerColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchUnSelectedContainerColor,
        unSelectedContentColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchUnSelectedContentColor,
        unSelectedBorderColor: Color = AppTheme.colors.chipColors.profileBranchColors.appChipProfileBranchUnSelectedBorderColor,
    ): ChipColors = remember(selectedContainerColor, selectedContentColor, unSelectedContainerColor, unSelectedContentColor) {
        ChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedContentColor = selectedContentColor,
            selectedBorderColor = selectedBorderColor,
            unSelectedContainerColor = unSelectedContainerColor,
            unSelectedContentColor = unSelectedContentColor,
            unSelectedBorderColor = unSelectedBorderColor,
        )
    }
}

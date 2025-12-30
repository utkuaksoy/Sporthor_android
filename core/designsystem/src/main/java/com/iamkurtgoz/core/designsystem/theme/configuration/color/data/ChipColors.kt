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
data class ChipColors(
    val primaryChipColors: ChipPrimaryColors,
    val secondaryChipColors: ChipSecondaryColors,
    val tertiaryChipColors: ChipTertiaryColors,
    val profileGrayColors: ChipProfileGrayColors,
    val profileBranchColors: ChipProfileBranchColors,
) {
    fun update(other: ChipColors) {
        primaryChipColors.update(other.primaryChipColors)
        secondaryChipColors.update(other.secondaryChipColors)
        tertiaryChipColors.update(other.tertiaryChipColors)
        profileGrayColors.update(other.profileGrayColors)
        profileBranchColors.update(other.profileBranchColors)
    }
}

@Stable
class ChipPrimaryColors(
    appChipPrimarySelectedContainerColor: Color,
    appChipPrimarySelectedContentColor: Color,
    appChipPrimarySelectedBorderColor: Color,
    appChipPrimaryUnSelectedContainerColor: Color,
    appChipPrimaryUnSelectedContentColor: Color,
    appChipPrimaryUnSelectedBorderColor: Color,
) {
    var appChipPrimarySelectedContainerColor: Color by mutableStateOf(appChipPrimarySelectedContainerColor)
        private set
    var appChipPrimarySelectedContentColor: Color by mutableStateOf(appChipPrimarySelectedContentColor)
        private set
    var appChipPrimarySelectedBorderColor: Color by mutableStateOf(appChipPrimarySelectedBorderColor)
        private set
    var appChipPrimaryUnSelectedContainerColor: Color by mutableStateOf(appChipPrimaryUnSelectedContainerColor)
        private set
    var appChipPrimaryUnSelectedContentColor: Color by mutableStateOf(appChipPrimaryUnSelectedContentColor)
        private set
    var appChipPrimaryUnSelectedBorderColor: Color by mutableStateOf(appChipPrimaryUnSelectedBorderColor)
        private set

    fun update(other: ChipPrimaryColors) {
        appChipPrimarySelectedContainerColor = other.appChipPrimarySelectedContainerColor
        appChipPrimarySelectedContentColor = other.appChipPrimarySelectedContentColor
        appChipPrimarySelectedBorderColor = other.appChipPrimarySelectedBorderColor
        appChipPrimaryUnSelectedContainerColor = other.appChipPrimaryUnSelectedContainerColor
        appChipPrimaryUnSelectedContentColor = other.appChipPrimaryUnSelectedContentColor
        appChipPrimaryUnSelectedBorderColor = other.appChipPrimaryUnSelectedBorderColor
    }
}

@Stable
class ChipSecondaryColors(
    appChipSecondarySelectedContainerColor: Color,
    appChipSecondarySelectedContentColor: Color,
    appChipSecondarySelectedBorderColor: Color,
    appChipSecondaryUnSelectedContainerColor: Color,
    appChipSecondaryUnSelectedContentColor: Color,
    appChipSecondaryUnSelectedBorderColor: Color,
) {
    var appChipSecondarySelectedContainerColor: Color by mutableStateOf(appChipSecondarySelectedContainerColor)
        private set
    var appChipSecondarySelectedContentColor: Color by mutableStateOf(appChipSecondarySelectedContentColor)
        private set
    var appChipSecondarySelectedBorderColor: Color by mutableStateOf(appChipSecondarySelectedBorderColor)
        private set
    var appChipSecondaryUnSelectedContainerColor: Color by mutableStateOf(appChipSecondaryUnSelectedContainerColor)
        private set
    var appChipSecondaryUnSelectedContentColor: Color by mutableStateOf(appChipSecondaryUnSelectedContentColor)
        private set
    var appChipSecondaryUnSelectedBorderColor: Color by mutableStateOf(appChipSecondaryUnSelectedBorderColor)
        private set

    fun update(other: ChipSecondaryColors) {
        appChipSecondarySelectedContainerColor = other.appChipSecondarySelectedContainerColor
        appChipSecondarySelectedContentColor = other.appChipSecondarySelectedContentColor
        appChipSecondarySelectedBorderColor = other.appChipSecondarySelectedBorderColor
        appChipSecondaryUnSelectedContainerColor = other.appChipSecondaryUnSelectedContainerColor
        appChipSecondaryUnSelectedContentColor = other.appChipSecondaryUnSelectedContentColor
        appChipSecondaryUnSelectedBorderColor = other.appChipSecondaryUnSelectedBorderColor
    }
}

@Stable
class ChipTertiaryColors(
    appChipTertiarySelectedContainerColor: Color,
    appChipTertiarySelectedContentColor: Color,
    appChipTertiarySelectedBorderColor: Color,
    appChipTertiaryUnSelectedContainerColor: Color,
    appChipTertiaryUnSelectedContentColor: Color,
    appChipTertiaryUnSelectedBorderColor: Color,
) {
    var appChipTertiarySelectedContainerColor: Color by mutableStateOf(appChipTertiarySelectedContainerColor)
        private set
    var appChipTertiarySelectedContentColor: Color by mutableStateOf(appChipTertiarySelectedContentColor)
        private set
    var appChipTertiarySelectedBorderColor: Color by mutableStateOf(appChipTertiarySelectedBorderColor)
        private set
    var appChipTertiaryUnSelectedContainerColor: Color by mutableStateOf(appChipTertiaryUnSelectedContainerColor)
        private set
    var appChipTertiaryUnSelectedContentColor: Color by mutableStateOf(appChipTertiaryUnSelectedContentColor)
        private set
    var appChipTertiaryUnSelectedBorderColor: Color by mutableStateOf(appChipTertiaryUnSelectedBorderColor)
        private set

    fun update(other: ChipTertiaryColors) {
        appChipTertiarySelectedContainerColor = other.appChipTertiarySelectedContainerColor
        appChipTertiarySelectedContentColor = other.appChipTertiarySelectedContentColor
        appChipTertiarySelectedBorderColor = other.appChipTertiarySelectedBorderColor
        appChipTertiaryUnSelectedContainerColor = other.appChipTertiaryUnSelectedContainerColor
        appChipTertiaryUnSelectedContentColor = other.appChipTertiaryUnSelectedContentColor
        appChipTertiaryUnSelectedBorderColor = other.appChipTertiaryUnSelectedBorderColor
    }
}

@Stable
class ChipProfileGrayColors(
    appChipProfileGraySelectedContainerColor: Color,
    appChipProfileGraySelectedContentColor: Color,
    appChipProfileGraySelectedBorderColor: Color,
    appChipProfileGrayUnSelectedContainerColor: Color,
    appChipProfileGrayUnSelectedContentColor: Color,
    appChipProfileGrayUnSelectedBorderColor: Color,
) {
    var appChipProfileGraySelectedContainerColor: Color by mutableStateOf(appChipProfileGraySelectedContainerColor)
        private set
    var appChipProfileGraySelectedContentColor: Color by mutableStateOf(appChipProfileGraySelectedContentColor)
        private set
    var appChipProfileGraySelectedBorderColor: Color by mutableStateOf(appChipProfileGraySelectedBorderColor)
        private set
    var appChipProfileGrayUnSelectedContainerColor: Color by mutableStateOf(appChipProfileGrayUnSelectedContainerColor)
        private set
    var appChipProfileGrayUnSelectedContentColor: Color by mutableStateOf(appChipProfileGrayUnSelectedContentColor)
        private set
    var appChipProfileGrayUnSelectedBorderColor: Color by mutableStateOf(appChipProfileGrayUnSelectedBorderColor)
        private set

    fun update(other: ChipProfileGrayColors) {
        appChipProfileGraySelectedContainerColor = other.appChipProfileGraySelectedContainerColor
        appChipProfileGraySelectedContentColor = other.appChipProfileGraySelectedContentColor
        appChipProfileGraySelectedBorderColor = other.appChipProfileGraySelectedBorderColor
        appChipProfileGrayUnSelectedContainerColor = other.appChipProfileGrayUnSelectedContainerColor
        appChipProfileGrayUnSelectedContentColor = other.appChipProfileGrayUnSelectedContentColor
        appChipProfileGrayUnSelectedBorderColor = other.appChipProfileGrayUnSelectedBorderColor
    }
}

@Stable
class ChipProfileBranchColors(
    appChipProfileBranchSelectedContainerColor: Color,
    appChipProfileBranchSelectedContentColor: Color,
    appChipProfileBranchSelectedBorderColor: Color,
    appChipProfileBranchUnSelectedContainerColor: Color,
    appChipProfileBranchUnSelectedContentColor: Color,
    appChipProfileBranchUnSelectedBorderColor: Color,
) {
    var appChipProfileBranchSelectedContainerColor: Color by mutableStateOf(appChipProfileBranchSelectedContainerColor)
        private set
    var appChipProfileBranchSelectedContentColor: Color by mutableStateOf(appChipProfileBranchSelectedContentColor)
        private set
    var appChipProfileBranchSelectedBorderColor: Color by mutableStateOf(appChipProfileBranchSelectedBorderColor)
        private set
    var appChipProfileBranchUnSelectedContainerColor: Color by mutableStateOf(appChipProfileBranchUnSelectedContainerColor)
        private set
    var appChipProfileBranchUnSelectedContentColor: Color by mutableStateOf(appChipProfileBranchUnSelectedContentColor)
        private set
    var appChipProfileBranchUnSelectedBorderColor: Color by mutableStateOf(appChipProfileBranchUnSelectedBorderColor)
        private set

    fun update(other: ChipProfileBranchColors) {
        appChipProfileBranchSelectedContainerColor = other.appChipProfileBranchSelectedContainerColor
        appChipProfileBranchSelectedContentColor = other.appChipProfileBranchSelectedContentColor
        appChipProfileBranchSelectedBorderColor = other.appChipProfileBranchSelectedBorderColor
        appChipProfileBranchUnSelectedContainerColor = other.appChipProfileBranchUnSelectedContainerColor
        appChipProfileBranchUnSelectedContentColor = other.appChipProfileBranchUnSelectedContentColor
        appChipProfileBranchUnSelectedBorderColor = other.appChipProfileBranchUnSelectedBorderColor
    }
}

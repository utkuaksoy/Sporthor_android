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
data class BorderColors(
    val userImageViewBorderColors: UserImageViewBorderColors,
) {
    fun update(other: BorderColors) {
        userImageViewBorderColors.update(other.userImageViewBorderColors)
    }
}

@Stable
class UserImageViewBorderColors(
    borderColorFirst: Color,
    borderColorSecond: Color,
    borderColorThird: Color,
) {
    var borderColorFirst by mutableStateOf(borderColorFirst)
        private set
    var borderColorSecond by mutableStateOf(borderColorSecond)
        private set
    var borderColorThird by mutableStateOf(borderColorThird)
        private set

    fun update(other: UserImageViewBorderColors) {
        borderColorFirst = other.borderColorFirst
        borderColorSecond = other.borderColorSecond
        borderColorThird = other.borderColorThird
    }
}

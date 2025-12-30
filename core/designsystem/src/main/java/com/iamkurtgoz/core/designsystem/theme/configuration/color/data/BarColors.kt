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
data class BarColors(
    val navigationBarColors: NavigationBarColors,
) {
    fun update(other: BarColors) {
        navigationBarColors.update(other.navigationBarColors)
    }
}

@Stable
class NavigationBarColors(
    navigationBarContainerColor: Color,
    navigationBarIconSelectedColor: Color,
    navigationBarIconUnSelectedColor: Color,
    navigationBarTextSelectedColor: Color,
    navigationBarTextUnSelectedColor: Color,
) {
    var navigationBarContainerColor by mutableStateOf(navigationBarContainerColor)
        private set
    var navigationBarIconSelectedColor by mutableStateOf(navigationBarIconSelectedColor)
        private set
    var navigationBarIconUnSelectedColor by mutableStateOf(navigationBarIconUnSelectedColor)
        private set
    var navigationBarTextSelectedColor by mutableStateOf(navigationBarTextSelectedColor)
        private set
    var navigationBarTextUnSelectedColor by mutableStateOf(navigationBarTextUnSelectedColor)
        private set

    fun update(other: NavigationBarColors) {
        navigationBarContainerColor = other.navigationBarContainerColor
        navigationBarIconSelectedColor = other.navigationBarIconSelectedColor
        navigationBarIconUnSelectedColor = other.navigationBarIconUnSelectedColor
        navigationBarTextSelectedColor = other.navigationBarTextSelectedColor
        navigationBarTextUnSelectedColor = other.navigationBarTextUnSelectedColor
    }
}

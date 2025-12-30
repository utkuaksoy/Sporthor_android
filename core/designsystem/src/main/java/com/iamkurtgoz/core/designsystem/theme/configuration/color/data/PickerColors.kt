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
data class PickerColors(
    val countryCodePickerPrimaryColors: CountryCodePickerPrimaryColors,
) {
    fun update(other: PickerColors) {
        countryCodePickerPrimaryColors.update(other.countryCodePickerPrimaryColors)
    }
}

@Stable
class CountryCodePickerPrimaryColors(
    countryCodePickerPrimaryEnabledContainerColor: Color,
    countryCodePickerPrimaryDisabledContainerColor: Color,
    countryCodePickerPrimaryEnabledContentColor: Color,
    countryCodePickerPrimaryDisabledContentColor: Color,
) {
    var countryCodePickerPrimaryEnabledContainerColor: Color by mutableStateOf(countryCodePickerPrimaryEnabledContainerColor)
        private set
    var countryCodePickerPrimaryEnabledContentColor: Color by mutableStateOf(countryCodePickerPrimaryEnabledContentColor)
        private set
    var countryCodePickerPrimaryDisabledContainerColor: Color by mutableStateOf(countryCodePickerPrimaryDisabledContainerColor)
        private set
    var countryCodePickerPrimaryDisabledContentColor: Color by mutableStateOf(countryCodePickerPrimaryDisabledContentColor)
        private set

    fun update(other: CountryCodePickerPrimaryColors) {
        countryCodePickerPrimaryEnabledContainerColor = other.countryCodePickerPrimaryEnabledContainerColor
        countryCodePickerPrimaryEnabledContentColor = other.countryCodePickerPrimaryEnabledContentColor
        countryCodePickerPrimaryDisabledContainerColor = other.countryCodePickerPrimaryDisabledContainerColor
        countryCodePickerPrimaryDisabledContentColor = other.countryCodePickerPrimaryDisabledContentColor
    }
}

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
package com.iamkurtgoz.core.designsystem.theme.composition

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.BarColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.BorderColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CardColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.DialogColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.GeneralColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.PickerColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TextFieldColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TimerColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ViewColors

@Stable
class AppColors(
    val generalColors: GeneralColors,
    val buttonColors: ButtonColors,
    val textFieldColors: TextFieldColors,
    val chipColors: ChipColors,
    val pickerColors: PickerColors,
    val timerColors: TimerColors,
    val viewColors: ViewColors,
    val cardColors: CardColors,
    var barColors: BarColors,
    val borderColors: BorderColors,
    val dialogColors: DialogColors,
) {
    fun update(other: AppColors) {
        generalColors.update(other.generalColors)
        buttonColors.update(other.buttonColors)
        textFieldColors.update(other.textFieldColors)
        chipColors.update(other.chipColors)
        pickerColors.update(other.pickerColors)
        timerColors.update(other.timerColors)
        viewColors.update(other.viewColors)
        cardColors.update(other.cardColors)
        barColors.update(other.barColors)
        borderColors.update(other.borderColors)
        dialogColors.update(other.dialogColors)
    }
}

@Composable
fun AppColors.asMaterialColors() = MaterialTheme.colorScheme.copy(
    surface = generalColors.backgroundPrimary,
)

internal val LocalAppColors = compositionLocalOf<AppColors> { error("No Color provided!") }

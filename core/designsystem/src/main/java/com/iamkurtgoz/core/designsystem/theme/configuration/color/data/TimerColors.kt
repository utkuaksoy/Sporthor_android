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
data class TimerColors(
    val countDownTimerPrimaryColors: CountDownTimerPrimaryColors,
) {
    fun update(other: TimerColors) {
        countDownTimerPrimaryColors.update(other.countDownTimerPrimaryColors)
    }
}

@Stable
class CountDownTimerPrimaryColors(
    countDownTimerPrimaryTimeElapsed: Color,
    countDownTimerPrimaryRemainingTime: Color,
    countDownTimerPrimaryWarningTime: Color,
    countDownTimerPrimaryEnabledContentColor: Color,
) {
    var countDownTimerPrimaryTimeElapsed: Color by mutableStateOf(countDownTimerPrimaryTimeElapsed)
        private set
    var countDownTimerPrimaryRemainingTime: Color by mutableStateOf(countDownTimerPrimaryRemainingTime)
        private set
    var countDownTimerPrimaryWarningTime: Color by mutableStateOf(countDownTimerPrimaryWarningTime)
        private set
    var countDownTimerPrimaryEnabledContentColor: Color by mutableStateOf(countDownTimerPrimaryEnabledContentColor)
        private set

    fun update(other: CountDownTimerPrimaryColors) {
        countDownTimerPrimaryTimeElapsed = other.countDownTimerPrimaryTimeElapsed
        countDownTimerPrimaryRemainingTime = other.countDownTimerPrimaryRemainingTime
        countDownTimerPrimaryWarningTime = other.countDownTimerPrimaryWarningTime
        countDownTimerPrimaryEnabledContentColor = other.countDownTimerPrimaryEnabledContentColor
    }
}

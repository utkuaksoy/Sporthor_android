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
data class DialogColors(
    val alertDialogColors: AlertDialogColors,
) {
    fun update(other: DialogColors) {
        alertDialogColors.update(other.alertDialogColors)
    }
}

@Stable
class AlertDialogColors(
    containerColor: Color,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
    confirmButtonContentColor: Color,
    dismissButtonContentColor: Color,
) {
    var containerColor by mutableStateOf(containerColor)
        private set
    var iconContentColor by mutableStateOf(iconContentColor)
        private set
    var titleContentColor by mutableStateOf(titleContentColor)
        private set
    var textContentColor by mutableStateOf(textContentColor)
        private set
    var confirmButtonContentColor by mutableStateOf(confirmButtonContentColor)
        private set
    var dismissButtonContentColor by mutableStateOf(dismissButtonContentColor)
        private set

    fun update(other: AlertDialogColors) {
        containerColor = other.containerColor
        iconContentColor = other.iconContentColor
        titleContentColor = other.titleContentColor
        textContentColor = other.textContentColor
        confirmButtonContentColor = other.confirmButtonContentColor
        dismissButtonContentColor = other.dismissButtonContentColor
    }
}

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
data class TextFieldColors(
    val textFieldPrimaryColors: TextFieldPrimaryColors,
    val otpFieldPrimaryColors: OtpFieldPrimaryColors,
    val textFieldSearchColors: TextFieldSearchColors,
    val textFieldMessageColors: TextFieldMessageColors,
) {
    fun update(other: TextFieldColors) {
        textFieldPrimaryColors.update(other.textFieldPrimaryColors)
        otpFieldPrimaryColors.update(other.otpFieldPrimaryColors)
        textFieldSearchColors.update(other.textFieldSearchColors)
        textFieldMessageColors.update(other.textFieldMessageColors)
    }
}

@Stable
class TextFieldPrimaryColors(
    textFieldPrimaryEnabledTitleColor: Color,
    textFieldPrimaryDisabledTitleColor: Color,
    textFieldPrimaryEnabledBorderColor: Color,
    textFieldPrimaryDisabledBorderColor: Color,
    textFieldPrimaryFocusedBorderColor: Color,
    textFieldPrimaryErrorBorderColor: Color,
    textFieldPrimaryEnabledContainerColor: Color,
    textFieldPrimaryEnabledContentColor: Color,
    textFieldPrimaryDisabledContainerColor: Color,
    textFieldPrimaryDisabledContentColor: Color,
    textFieldPrimaryPlaceholderColor: Color,
    textFieldPrimaryCursorColor: Color,
    textFieldPrimaryEnabledLeadingIconColor: Color,
    textFieldPrimaryDisabledLeadingIconColor: Color,
    textFieldPrimaryEnabledTrailingIconColor: Color,
    textFieldPrimaryDisabledTrailingIconColor: Color,
    textFieldPrimaryEnabledHintContentColor: Color,
    textFieldPrimaryDisabledHintContentColor: Color,
    textFieldPrimaryErrorHintContentColor: Color,
) {
    var textFieldPrimaryEnabledTitleColor: Color by mutableStateOf(textFieldPrimaryEnabledTitleColor)
        private set
    var textFieldPrimaryDisabledTitleColor: Color by mutableStateOf(textFieldPrimaryDisabledTitleColor)
        private set
    var textFieldPrimaryEnabledBorderColor: Color by mutableStateOf(textFieldPrimaryEnabledBorderColor)
        private set
    var textFieldPrimaryDisabledBorderColor: Color by mutableStateOf(textFieldPrimaryDisabledBorderColor)
        private set
    var textFieldPrimaryFocusedBorderColor: Color by mutableStateOf(textFieldPrimaryFocusedBorderColor)
        private set
    var textFieldPrimaryErrorBorderColor: Color by mutableStateOf(textFieldPrimaryErrorBorderColor)
        private set
    var textFieldPrimaryEnabledContainerColor: Color by mutableStateOf(textFieldPrimaryEnabledContainerColor)
        private set
    var textFieldPrimaryEnabledContentColor: Color by mutableStateOf(textFieldPrimaryEnabledContentColor)
        private set
    var textFieldPrimaryDisabledContainerColor: Color by mutableStateOf(textFieldPrimaryDisabledContainerColor)
        private set
    var textFieldPrimaryDisabledContentColor: Color by mutableStateOf(textFieldPrimaryDisabledContentColor)
        private set
    var textFieldPrimaryPlaceholderColor: Color by mutableStateOf(textFieldPrimaryPlaceholderColor)
        private set
    var textFieldPrimaryCursorColor: Color by mutableStateOf(textFieldPrimaryCursorColor)
        private set
    var textFieldPrimaryEnabledLeadingIconColor: Color by mutableStateOf(textFieldPrimaryEnabledLeadingIconColor)
        private set
    var textFieldPrimaryDisabledLeadingIconColor: Color by mutableStateOf(textFieldPrimaryDisabledLeadingIconColor)
        private set
    var textFieldPrimaryEnabledTrailingIconColor: Color by mutableStateOf(textFieldPrimaryEnabledTrailingIconColor)
        private set
    var textFieldPrimaryDisabledTrailingIconColor: Color by mutableStateOf(textFieldPrimaryDisabledTrailingIconColor)
        private set
    var textFieldPrimaryEnabledHintContentColor: Color by mutableStateOf(textFieldPrimaryEnabledHintContentColor)
        private set
    var textFieldPrimaryDisabledHintContentColor: Color by mutableStateOf(textFieldPrimaryDisabledHintContentColor)
        private set
    var textFieldPrimaryErrorHintContentColor: Color by mutableStateOf(textFieldPrimaryErrorHintContentColor)
        private set

    fun update(other: TextFieldPrimaryColors) {
        textFieldPrimaryEnabledTitleColor = other.textFieldPrimaryEnabledTitleColor
        textFieldPrimaryDisabledTitleColor = other.textFieldPrimaryDisabledTitleColor
        textFieldPrimaryEnabledBorderColor = other.textFieldPrimaryEnabledBorderColor
        textFieldPrimaryDisabledBorderColor = other.textFieldPrimaryDisabledBorderColor
        textFieldPrimaryFocusedBorderColor = other.textFieldPrimaryFocusedBorderColor
        textFieldPrimaryErrorBorderColor = other.textFieldPrimaryErrorBorderColor
        textFieldPrimaryEnabledContainerColor = other.textFieldPrimaryEnabledContainerColor
        textFieldPrimaryEnabledContentColor = other.textFieldPrimaryEnabledContentColor
        textFieldPrimaryDisabledContainerColor = other.textFieldPrimaryDisabledContainerColor
        textFieldPrimaryDisabledContentColor = other.textFieldPrimaryDisabledContentColor
        textFieldPrimaryPlaceholderColor = other.textFieldPrimaryPlaceholderColor
        textFieldPrimaryCursorColor = other.textFieldPrimaryCursorColor
        textFieldPrimaryEnabledLeadingIconColor = other.textFieldPrimaryEnabledLeadingIconColor
        textFieldPrimaryDisabledLeadingIconColor = other.textFieldPrimaryDisabledLeadingIconColor
        textFieldPrimaryEnabledTrailingIconColor = other.textFieldPrimaryEnabledTrailingIconColor
        textFieldPrimaryDisabledTrailingIconColor = other.textFieldPrimaryDisabledTrailingIconColor
        textFieldPrimaryEnabledHintContentColor = other.textFieldPrimaryEnabledHintContentColor
        textFieldPrimaryDisabledHintContentColor = other.textFieldPrimaryDisabledHintContentColor
        textFieldPrimaryErrorHintContentColor = other.textFieldPrimaryErrorHintContentColor
    }
}

@Stable
class OtpFieldPrimaryColors(
    otpFieldPrimaryEnabledBorderColor: Color,
    otpFieldPrimaryDisabledBorderColor: Color,
    otpFieldPrimaryFocusedBorderColor: Color,
    otpFieldPrimaryErrorBorderColor: Color,
    otpFieldPrimaryEnabledContainerColor: Color,
    otpFieldPrimaryEnabledContentColor: Color,
    otpFieldPrimaryDisabledContainerColor: Color,
    otpFieldPrimaryDisabledContentColor: Color,
    otpFieldPrimaryCursorColor: Color,
    otpFieldPrimaryEnabledHintContentColor: Color,
    otpFieldPrimaryDisabledHintContentColor: Color,
    otpFieldPrimaryErrorHintContentColor: Color,
) {
    var otpFieldPrimaryEnabledBorderColor: Color by mutableStateOf(otpFieldPrimaryEnabledBorderColor)
        private set
    var otpFieldPrimaryDisabledBorderColor: Color by mutableStateOf(otpFieldPrimaryDisabledBorderColor)
        private set
    var otpFieldPrimaryFocusedBorderColor: Color by mutableStateOf(otpFieldPrimaryFocusedBorderColor)
        private set
    var otpFieldPrimaryErrorBorderColor: Color by mutableStateOf(otpFieldPrimaryErrorBorderColor)
        private set
    var otpFieldPrimaryEnabledContainerColor: Color by mutableStateOf(otpFieldPrimaryEnabledContainerColor)
        private set
    var otpFieldPrimaryEnabledContentColor: Color by mutableStateOf(otpFieldPrimaryEnabledContentColor)
        private set
    var otpFieldPrimaryDisabledContainerColor: Color by mutableStateOf(otpFieldPrimaryDisabledContainerColor)
        private set
    var otpFieldPrimaryDisabledContentColor: Color by mutableStateOf(otpFieldPrimaryDisabledContentColor)
        private set
    var otpFieldPrimaryCursorColor: Color by mutableStateOf(otpFieldPrimaryCursorColor)
        private set
    var otpFieldPrimaryEnabledHintContentColor: Color by mutableStateOf(otpFieldPrimaryEnabledHintContentColor)
        private set
    var otpFieldPrimaryDisabledHintContentColor: Color by mutableStateOf(otpFieldPrimaryDisabledHintContentColor)
        private set
    var otpFieldPrimaryErrorHintContentColor: Color by mutableStateOf(otpFieldPrimaryErrorHintContentColor)
        private set

    fun update(other: OtpFieldPrimaryColors) {
        otpFieldPrimaryEnabledBorderColor = other.otpFieldPrimaryEnabledBorderColor
        otpFieldPrimaryDisabledBorderColor = other.otpFieldPrimaryDisabledBorderColor
        otpFieldPrimaryFocusedBorderColor = other.otpFieldPrimaryFocusedBorderColor
        otpFieldPrimaryErrorBorderColor = other.otpFieldPrimaryErrorBorderColor
        otpFieldPrimaryEnabledContainerColor = other.otpFieldPrimaryEnabledContainerColor
        otpFieldPrimaryEnabledContentColor = other.otpFieldPrimaryEnabledContentColor
        otpFieldPrimaryDisabledContainerColor = other.otpFieldPrimaryDisabledContainerColor
        otpFieldPrimaryDisabledContentColor = other.otpFieldPrimaryDisabledContentColor
        otpFieldPrimaryCursorColor = other.otpFieldPrimaryCursorColor
        otpFieldPrimaryEnabledHintContentColor = other.otpFieldPrimaryEnabledHintContentColor
        otpFieldPrimaryDisabledHintContentColor = other.otpFieldPrimaryDisabledHintContentColor
        otpFieldPrimaryErrorHintContentColor = other.otpFieldPrimaryErrorHintContentColor
    }
}

@Stable
class TextFieldSearchColors(
    textFieldSearchEnabledTitleColor: Color,
    textFieldSearchDisabledTitleColor: Color,
    textFieldSearchEnabledBorderColor: Color,
    textFieldSearchDisabledBorderColor: Color,
    textFieldSearchFocusedBorderColor: Color,
    textFieldSearchErrorBorderColor: Color,
    textFieldSearchEnabledContainerColor: Color,
    textFieldSearchEnabledContentColor: Color,
    textFieldSearchDisabledContainerColor: Color,
    textFieldSearchDisabledContentColor: Color,
    textFieldSearchPlaceholderColor: Color,
    textFieldSearchCursorColor: Color,
    textFieldSearchEnabledLeadingIconColor: Color,
    textFieldSearchDisabledLeadingIconColor: Color,
    textFieldSearchEnabledTrailingIconColor: Color,
    textFieldSearchDisabledTrailingIconColor: Color,
    textFieldSearchEnabledHintContentColor: Color,
    textFieldSearchDisabledHintContentColor: Color,
    textFieldSearchErrorHintContentColor: Color,
) {
    var textFieldSearchEnabledTitleColor: Color by mutableStateOf(textFieldSearchEnabledTitleColor)
        private set
    var textFieldSearchDisabledTitleColor: Color by mutableStateOf(textFieldSearchDisabledTitleColor)
        private set
    var textFieldSearchEnabledBorderColor: Color by mutableStateOf(textFieldSearchEnabledBorderColor)
        private set
    var textFieldSearchDisabledBorderColor: Color by mutableStateOf(textFieldSearchDisabledBorderColor)
        private set
    var textFieldSearchFocusedBorderColor: Color by mutableStateOf(textFieldSearchFocusedBorderColor)
        private set
    var textFieldSearchErrorBorderColor: Color by mutableStateOf(textFieldSearchErrorBorderColor)
        private set
    var textFieldSearchEnabledContainerColor: Color by mutableStateOf(textFieldSearchEnabledContainerColor)
        private set
    var textFieldSearchEnabledContentColor: Color by mutableStateOf(textFieldSearchEnabledContentColor)
        private set
    var textFieldSearchDisabledContainerColor: Color by mutableStateOf(textFieldSearchDisabledContainerColor)
        private set
    var textFieldSearchDisabledContentColor: Color by mutableStateOf(textFieldSearchDisabledContentColor)
        private set
    var textFieldSearchPlaceholderColor: Color by mutableStateOf(textFieldSearchPlaceholderColor)
        private set
    var textFieldSearchCursorColor: Color by mutableStateOf(textFieldSearchCursorColor)
        private set
    var textFieldSearchEnabledLeadingIconColor: Color by mutableStateOf(textFieldSearchEnabledLeadingIconColor)
        private set
    var textFieldSearchDisabledLeadingIconColor: Color by mutableStateOf(textFieldSearchDisabledLeadingIconColor)
        private set
    var textFieldSearchEnabledTrailingIconColor: Color by mutableStateOf(textFieldSearchEnabledTrailingIconColor)
        private set
    var textFieldSearchDisabledTrailingIconColor: Color by mutableStateOf(textFieldSearchDisabledTrailingIconColor)
        private set
    var textFieldSearchEnabledHintContentColor: Color by mutableStateOf(textFieldSearchEnabledHintContentColor)
        private set
    var textFieldSearchDisabledHintContentColor: Color by mutableStateOf(textFieldSearchDisabledHintContentColor)
        private set
    var textFieldSearchErrorHintContentColor: Color by mutableStateOf(textFieldSearchErrorHintContentColor)
        private set

    fun update(other: TextFieldSearchColors) {
        textFieldSearchEnabledTitleColor = other.textFieldSearchEnabledTitleColor
        textFieldSearchDisabledTitleColor = other.textFieldSearchDisabledTitleColor
        textFieldSearchEnabledBorderColor = other.textFieldSearchEnabledBorderColor
        textFieldSearchDisabledBorderColor = other.textFieldSearchDisabledBorderColor
        textFieldSearchFocusedBorderColor = other.textFieldSearchFocusedBorderColor
        textFieldSearchErrorBorderColor = other.textFieldSearchErrorBorderColor
        textFieldSearchEnabledContainerColor = other.textFieldSearchEnabledContainerColor
        textFieldSearchEnabledContentColor = other.textFieldSearchEnabledContentColor
        textFieldSearchDisabledContainerColor = other.textFieldSearchDisabledContainerColor
        textFieldSearchDisabledContentColor = other.textFieldSearchDisabledContentColor
        textFieldSearchPlaceholderColor = other.textFieldSearchPlaceholderColor
        textFieldSearchCursorColor = other.textFieldSearchCursorColor
        textFieldSearchEnabledLeadingIconColor = other.textFieldSearchEnabledLeadingIconColor
        textFieldSearchDisabledLeadingIconColor = other.textFieldSearchDisabledLeadingIconColor
        textFieldSearchEnabledTrailingIconColor = other.textFieldSearchEnabledTrailingIconColor
        textFieldSearchDisabledTrailingIconColor = other.textFieldSearchDisabledTrailingIconColor
        textFieldSearchEnabledHintContentColor = other.textFieldSearchEnabledHintContentColor
        textFieldSearchDisabledHintContentColor = other.textFieldSearchDisabledHintContentColor
        textFieldSearchErrorHintContentColor = other.textFieldSearchErrorHintContentColor
    }
}

@Stable
class TextFieldMessageColors(
    textFieldMessageEnabledTitleColor: Color,
    textFieldMessageDisabledTitleColor: Color,
    textFieldMessageEnabledBorderColor: Color,
    textFieldMessageDisabledBorderColor: Color,
    textFieldMessageFocusedBorderColor: Color,
    textFieldMessageErrorBorderColor: Color,
    textFieldMessageEnabledContainerColor: Color,
    textFieldMessageEnabledContentColor: Color,
    textFieldMessageDisabledContainerColor: Color,
    textFieldMessageDisabledContentColor: Color,
    textFieldMessagePlaceholderColor: Color,
    textFieldMessageCursorColor: Color,
    textFieldMessageEnabledLeadingIconColor: Color,
    textFieldMessageDisabledLeadingIconColor: Color,
    textFieldMessageEnabledTrailingIconColor: Color,
    textFieldMessageDisabledTrailingIconColor: Color,
    textFieldMessageEnabledHintContentColor: Color,
    textFieldMessageDisabledHintContentColor: Color,
    textFieldMessageErrorHintContentColor: Color,
) {
    var textFieldMessageEnabledTitleColor: Color by mutableStateOf(textFieldMessageEnabledTitleColor)
        private set
    var textFieldMessageDisabledTitleColor: Color by mutableStateOf(textFieldMessageDisabledTitleColor)
        private set
    var textFieldMessageEnabledBorderColor: Color by mutableStateOf(textFieldMessageEnabledBorderColor)
        private set
    var textFieldMessageDisabledBorderColor: Color by mutableStateOf(textFieldMessageDisabledBorderColor)
        private set
    var textFieldMessageFocusedBorderColor: Color by mutableStateOf(textFieldMessageFocusedBorderColor)
        private set
    var textFieldMessageErrorBorderColor: Color by mutableStateOf(textFieldMessageErrorBorderColor)
        private set
    var textFieldMessageEnabledContainerColor: Color by mutableStateOf(textFieldMessageEnabledContainerColor)
        private set
    var textFieldMessageEnabledContentColor: Color by mutableStateOf(textFieldMessageEnabledContentColor)
        private set
    var textFieldMessageDisabledContainerColor: Color by mutableStateOf(textFieldMessageDisabledContainerColor)
        private set
    var textFieldMessageDisabledContentColor: Color by mutableStateOf(textFieldMessageDisabledContentColor)
        private set
    var textFieldMessagePlaceholderColor: Color by mutableStateOf(textFieldMessagePlaceholderColor)
        private set
    var textFieldMessageCursorColor: Color by mutableStateOf(textFieldMessageCursorColor)
        private set
    var textFieldMessageEnabledLeadingIconColor: Color by mutableStateOf(textFieldMessageEnabledLeadingIconColor)
        private set
    var textFieldMessageDisabledLeadingIconColor: Color by mutableStateOf(textFieldMessageDisabledLeadingIconColor)
        private set
    var textFieldMessageEnabledTrailingIconColor: Color by mutableStateOf(textFieldMessageEnabledTrailingIconColor)
        private set
    var textFieldMessageDisabledTrailingIconColor: Color by mutableStateOf(textFieldMessageDisabledTrailingIconColor)
        private set
    var textFieldMessageEnabledHintContentColor: Color by mutableStateOf(textFieldMessageEnabledHintContentColor)
        private set
    var textFieldMessageDisabledHintContentColor: Color by mutableStateOf(textFieldMessageDisabledHintContentColor)
        private set
    var textFieldMessageErrorHintContentColor: Color by mutableStateOf(textFieldMessageErrorHintContentColor)
        private set

    fun update(other: TextFieldMessageColors) {
        textFieldMessageEnabledTitleColor = other.textFieldMessageEnabledTitleColor
        textFieldMessageDisabledTitleColor = other.textFieldMessageDisabledTitleColor
        textFieldMessageEnabledBorderColor = other.textFieldMessageEnabledBorderColor
        textFieldMessageDisabledBorderColor = other.textFieldMessageDisabledBorderColor
        textFieldMessageFocusedBorderColor = other.textFieldMessageFocusedBorderColor
        textFieldMessageErrorBorderColor = other.textFieldMessageErrorBorderColor
        textFieldMessageEnabledContainerColor = other.textFieldMessageEnabledContainerColor
        textFieldMessageEnabledContentColor = other.textFieldMessageEnabledContentColor
        textFieldMessageDisabledContainerColor = other.textFieldMessageDisabledContainerColor
        textFieldMessageDisabledContentColor = other.textFieldMessageDisabledContentColor
        textFieldMessagePlaceholderColor = other.textFieldMessagePlaceholderColor
        textFieldMessageCursorColor = other.textFieldMessageCursorColor
        textFieldMessageEnabledLeadingIconColor = other.textFieldMessageEnabledLeadingIconColor
        textFieldMessageDisabledLeadingIconColor = other.textFieldMessageDisabledLeadingIconColor
        textFieldMessageEnabledTrailingIconColor = other.textFieldMessageEnabledTrailingIconColor
        textFieldMessageDisabledTrailingIconColor = other.textFieldMessageDisabledTrailingIconColor
        textFieldMessageEnabledHintContentColor = other.textFieldMessageEnabledHintContentColor
        textFieldMessageDisabledHintContentColor = other.textFieldMessageDisabledHintContentColor
        textFieldMessageErrorHintContentColor = other.textFieldMessageErrorHintContentColor
    }
}

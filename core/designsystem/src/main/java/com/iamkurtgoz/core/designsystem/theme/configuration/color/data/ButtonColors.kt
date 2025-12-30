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
data class ButtonColors(
    val primaryColors: ButtonPrimaryColors,
    val secondaryColors: ButtonSecondaryColors,
    val secondaryWhiteColors: ButtonSecondaryWhiteColors,
    val tertiaryColors: ButtonTertiaryColors,
    val outlineColors: ButtonOutlineColors,
    val circleButtonPrimaryColors: CircleButtonPrimaryColors,
    val circleButtonSecondaryColors: CircleButtonSecondaryColors,
    val circleButtonSecondaryGrayColors: CircleButtonSecondaryGrayColors,
    val circleButtonTertiaryColors: CircleButtonTertiaryColors,
    val circleButtonOutlineColors: CircleButtonOutlineColors,
    val radioButtonPrimaryColors: RadioButtonPrimaryColors,
    val radioButtonSecondaryColors: RadioButtonSecondaryColors,
    val horizontalListButtonColors: HorizontalListButtonColors,
) {
    fun update(other: ButtonColors) {
        primaryColors.update(other.primaryColors)
        secondaryColors.update(other.secondaryColors)
        secondaryWhiteColors.update(other.secondaryWhiteColors)
        tertiaryColors.update(other.tertiaryColors)
        outlineColors.update(other.outlineColors)
        circleButtonPrimaryColors.update(other.circleButtonPrimaryColors)
        circleButtonSecondaryColors.update(other.circleButtonSecondaryColors)
        circleButtonSecondaryGrayColors.update(other.circleButtonSecondaryGrayColors)
        circleButtonTertiaryColors.update(other.circleButtonTertiaryColors)
        circleButtonOutlineColors.update(other.circleButtonOutlineColors)
        radioButtonPrimaryColors.update(other.radioButtonPrimaryColors)
        radioButtonSecondaryColors.update(other.radioButtonSecondaryColors)
        horizontalListButtonColors.update(other.horizontalListButtonColors)
    }
}

@Stable
class ButtonPrimaryColors(
    buttonPrimaryEnabledBackground: Color,
    buttonPrimaryEnabledForeground: Color,
    buttonPrimaryDisabledBackground: Color,
    buttonPrimaryDisabledForeground: Color,
) {
    var buttonPrimaryEnabledBackground by mutableStateOf(buttonPrimaryEnabledBackground)
        private set
    var buttonPrimaryEnabledForeground by mutableStateOf(buttonPrimaryEnabledForeground)
        private set
    var buttonPrimaryDisabledBackground by mutableStateOf(buttonPrimaryDisabledBackground)
        private set
    var buttonPrimaryDisabledForeground by mutableStateOf(buttonPrimaryDisabledForeground)
        private set

    fun update(other: ButtonPrimaryColors) {
        buttonPrimaryEnabledBackground = other.buttonPrimaryEnabledBackground
        buttonPrimaryEnabledForeground = other.buttonPrimaryEnabledForeground
        buttonPrimaryDisabledBackground = other.buttonPrimaryDisabledBackground
        buttonPrimaryDisabledForeground = other.buttonPrimaryDisabledForeground
    }
}

@Stable
class ButtonSecondaryColors(
    buttonSecondaryEnabledBackground: Color,
    buttonSecondaryEnabledForeground: Color,
    buttonSecondaryDisabledBackground: Color,
    buttonSecondaryDisabledForeground: Color,
) {
    var buttonSecondaryEnabledBackground by mutableStateOf(buttonSecondaryEnabledBackground)
        private set
    var buttonSecondaryEnabledForeground by mutableStateOf(buttonSecondaryEnabledForeground)
        private set
    var buttonSecondaryDisabledBackground by mutableStateOf(buttonSecondaryDisabledBackground)
        private set
    var buttonSecondaryDisabledForeground by mutableStateOf(buttonSecondaryDisabledForeground)
        private set

    fun update(other: ButtonSecondaryColors) {
        buttonSecondaryEnabledBackground = other.buttonSecondaryEnabledBackground
        buttonSecondaryEnabledForeground = other.buttonSecondaryEnabledForeground
        buttonSecondaryDisabledBackground = other.buttonSecondaryDisabledBackground
        buttonSecondaryDisabledForeground = other.buttonSecondaryDisabledForeground
    }
}

@Stable
class ButtonSecondaryWhiteColors(
    buttonSecondaryWhiteEnabledBackground: Color,
    buttonSecondaryWhiteEnabledForeground: Color,
    buttonSecondaryWhiteDisabledBackground: Color,
    buttonSecondaryWhiteDisabledForeground: Color,
) {
    var buttonSecondaryWhiteEnabledBackground by mutableStateOf(buttonSecondaryWhiteEnabledBackground)
        private set
    var buttonSecondaryWhiteEnabledForeground by mutableStateOf(buttonSecondaryWhiteEnabledForeground)
        private set
    var buttonSecondaryWhiteDisabledBackground by mutableStateOf(buttonSecondaryWhiteDisabledBackground)
        private set
    var buttonSecondaryWhiteDisabledForeground by mutableStateOf(buttonSecondaryWhiteDisabledForeground)
        private set

    fun update(other: ButtonSecondaryWhiteColors) {
        buttonSecondaryWhiteEnabledBackground = other.buttonSecondaryWhiteEnabledBackground
        buttonSecondaryWhiteEnabledForeground = other.buttonSecondaryWhiteEnabledForeground
        buttonSecondaryWhiteDisabledBackground = other.buttonSecondaryWhiteDisabledBackground
        buttonSecondaryWhiteDisabledForeground = other.buttonSecondaryWhiteDisabledForeground
    }
}

@Stable
class ButtonTertiaryColors(
    buttonTertiaryEnabledBackground: Color,
    buttonTertiaryEnabledForeground: Color,
    buttonTertiaryDisabledBackground: Color,
    buttonTertiaryDisabledForeground: Color,
) {
    var buttonTertiaryEnabledBackground by mutableStateOf(buttonTertiaryEnabledBackground)
        private set
    var buttonTertiaryEnabledForeground by mutableStateOf(buttonTertiaryEnabledForeground)
        private set
    var buttonTertiaryDisabledBackground by mutableStateOf(buttonTertiaryDisabledBackground)
        private set
    var buttonTertiaryDisabledForeground by mutableStateOf(buttonTertiaryDisabledForeground)
        private set

    fun update(other: ButtonTertiaryColors) {
        buttonTertiaryEnabledBackground = other.buttonTertiaryEnabledBackground
        buttonTertiaryEnabledForeground = other.buttonTertiaryEnabledForeground
        buttonTertiaryDisabledBackground = other.buttonTertiaryDisabledBackground
        buttonTertiaryDisabledForeground = other.buttonTertiaryDisabledForeground
    }
}

@Stable
class ButtonOutlineColors(
    buttonOutlineEnabledBackground: Color,
    buttonOutlineEnabledForeground: Color,
    buttonOutlineDisabledBackground: Color,
    buttonOutlineDisabledForeground: Color,
    buttonOutlineEnabledBorder: Color,
    buttonOutlineDisabledBorder: Color,
) {
    var buttonOutlineEnabledBackground by mutableStateOf(buttonOutlineEnabledBackground)
        private set
    var buttonOutlineEnabledForeground by mutableStateOf(buttonOutlineEnabledForeground)
        private set
    var buttonOutlineDisabledBackground by mutableStateOf(buttonOutlineDisabledBackground)
        private set
    var buttonOutlineDisabledForeground by mutableStateOf(buttonOutlineDisabledForeground)
        private set
    var buttonOutlineEnabledBorder by mutableStateOf(buttonOutlineEnabledBorder)
        private set
    var buttonOutlineDisabledBorder by mutableStateOf(buttonOutlineDisabledBorder)
        private set

    fun update(other: ButtonOutlineColors) {
        buttonOutlineEnabledBackground = other.buttonOutlineEnabledBackground
        buttonOutlineEnabledForeground = other.buttonOutlineEnabledForeground
        buttonOutlineDisabledBackground = other.buttonOutlineDisabledBackground
        buttonOutlineDisabledForeground = other.buttonOutlineDisabledForeground
        buttonOutlineEnabledBorder = other.buttonOutlineEnabledBorder
        buttonOutlineDisabledBorder = other.buttonOutlineDisabledBorder
    }
}

@Stable
class CircleButtonPrimaryColors(
    circleButtonPrimaryEnabledBackground: Color,
    circleButtonPrimaryEnabledForeground: Color,
    circleButtonPrimaryDisabledBackground: Color,
    circleButtonPrimaryDisabledForeground: Color,
) {
    var circleButtonPrimaryEnabledBackground by mutableStateOf(circleButtonPrimaryEnabledBackground)
        private set
    var circleButtonPrimaryEnabledForeground by mutableStateOf(circleButtonPrimaryEnabledForeground)
        private set
    var circleButtonPrimaryDisabledBackground by mutableStateOf(circleButtonPrimaryDisabledBackground)
        private set
    var circleButtonPrimaryDisabledForeground by mutableStateOf(circleButtonPrimaryDisabledForeground)
        private set

    fun update(other: CircleButtonPrimaryColors) {
        circleButtonPrimaryEnabledBackground = other.circleButtonPrimaryEnabledBackground
        circleButtonPrimaryEnabledForeground = other.circleButtonPrimaryEnabledForeground
        circleButtonPrimaryDisabledBackground = other.circleButtonPrimaryDisabledBackground
        circleButtonPrimaryDisabledForeground = other.circleButtonPrimaryDisabledForeground
    }
}

@Stable
class CircleButtonSecondaryColors(
    circleButtonSecondaryEnabledBackground: Color,
    circleButtonSecondaryEnabledForeground: Color,
    circleButtonSecondaryDisabledBackground: Color,
    circleButtonSecondaryDisabledForeground: Color,
) {
    var circleButtonSecondaryEnabledBackground by mutableStateOf(circleButtonSecondaryEnabledBackground)
        private set
    var circleButtonSecondaryEnabledForeground by mutableStateOf(circleButtonSecondaryEnabledForeground)
        private set
    var circleButtonSecondaryDisabledBackground by mutableStateOf(circleButtonSecondaryDisabledBackground)
        private set
    var circleButtonSecondaryDisabledForeground by mutableStateOf(circleButtonSecondaryDisabledForeground)
        private set

    fun update(other: CircleButtonSecondaryColors) {
        circleButtonSecondaryEnabledBackground = other.circleButtonSecondaryEnabledBackground
        circleButtonSecondaryEnabledForeground = other.circleButtonSecondaryEnabledForeground
        circleButtonSecondaryDisabledBackground = other.circleButtonSecondaryDisabledBackground
        circleButtonSecondaryDisabledForeground = other.circleButtonSecondaryDisabledForeground
    }
}

@Stable
class CircleButtonSecondaryGrayColors(
    circleButtonSecondaryGrayEnabledBackground: Color,
    circleButtonSecondaryGrayEnabledForeground: Color,
    circleButtonSecondaryGrayDisabledBackground: Color,
    circleButtonSecondaryGrayDisabledForeground: Color,
) {
    var circleButtonSecondaryGrayEnabledBackground by mutableStateOf(circleButtonSecondaryGrayEnabledBackground)
        private set
    var circleButtonSecondaryGrayEnabledForeground by mutableStateOf(circleButtonSecondaryGrayEnabledForeground)
        private set
    var circleButtonSecondaryGrayDisabledBackground by mutableStateOf(circleButtonSecondaryGrayDisabledBackground)
        private set
    var circleButtonSecondaryGrayDisabledForeground by mutableStateOf(circleButtonSecondaryGrayDisabledForeground)
        private set

    fun update(other: CircleButtonSecondaryGrayColors) {
        circleButtonSecondaryGrayEnabledBackground = other.circleButtonSecondaryGrayEnabledBackground
        circleButtonSecondaryGrayEnabledForeground = other.circleButtonSecondaryGrayEnabledForeground
        circleButtonSecondaryGrayDisabledBackground = other.circleButtonSecondaryGrayDisabledBackground
        circleButtonSecondaryGrayDisabledForeground = other.circleButtonSecondaryGrayDisabledForeground
    }
}

@Stable
class CircleButtonTertiaryColors(
    circleButtonTertiaryEnabledBackground: Color,
    circleButtonTertiaryEnabledForeground: Color,
    circleButtonTertiaryDisabledBackground: Color,
    circleButtonTertiaryDisabledForeground: Color,
) {
    var circleButtonTertiaryEnabledBackground by mutableStateOf(circleButtonTertiaryEnabledBackground)
        private set
    var circleButtonTertiaryEnabledForeground by mutableStateOf(circleButtonTertiaryEnabledForeground)
        private set
    var circleButtonTertiaryDisabledBackground by mutableStateOf(circleButtonTertiaryDisabledBackground)
        private set
    var circleButtonTertiaryDisabledForeground by mutableStateOf(circleButtonTertiaryDisabledForeground)
        private set

    fun update(other: CircleButtonTertiaryColors) {
        circleButtonTertiaryEnabledBackground = other.circleButtonTertiaryEnabledBackground
        circleButtonTertiaryEnabledForeground = other.circleButtonTertiaryEnabledForeground
        circleButtonTertiaryDisabledBackground = other.circleButtonTertiaryDisabledBackground
        circleButtonTertiaryDisabledForeground = other.circleButtonTertiaryDisabledForeground
    }
}

@Stable
class CircleButtonOutlineColors(
    circleButtonOutlineEnabledBackground: Color,
    circleButtonOutlineEnabledForeground: Color,
    circleButtonOutlineDisabledBackground: Color,
    circleButtonOutlineDisabledForeground: Color,
    circleButtonOutlineEnabledBorder: Color,
    circleButtonOutlineDisabledBorder: Color,
) {
    var circleButtonOutlineEnabledBackground by mutableStateOf(circleButtonOutlineEnabledBackground)
        private set
    var circleButtonOutlineEnabledForeground by mutableStateOf(circleButtonOutlineEnabledForeground)
        private set
    var circleButtonOutlineDisabledBackground by mutableStateOf(circleButtonOutlineDisabledBackground)
        private set
    var circleButtonOutlineDisabledForeground by mutableStateOf(circleButtonOutlineDisabledForeground)
        private set
    var circleButtonOutlineEnabledBorder by mutableStateOf(circleButtonOutlineEnabledBorder)
        private set
    var circleButtonOutlineDisabledBorder by mutableStateOf(circleButtonOutlineDisabledBorder)
        private set

    fun update(other: CircleButtonOutlineColors) {
        circleButtonOutlineEnabledBackground = other.circleButtonOutlineEnabledBackground
        circleButtonOutlineEnabledForeground = other.circleButtonOutlineEnabledForeground
        circleButtonOutlineDisabledBackground = other.circleButtonOutlineDisabledBackground
        circleButtonOutlineDisabledForeground = other.circleButtonOutlineDisabledForeground
        circleButtonOutlineEnabledBorder = other.circleButtonOutlineEnabledBorder
        circleButtonOutlineDisabledBorder = other.circleButtonOutlineDisabledBorder
    }
}

@Stable
class RadioButtonPrimaryColors(
    radioButtonPrimarySelectedColor: Color,
    radioButtonPrimaryUnSelectedColor: Color,
) {
    var radioButtonPrimarySelectedColor: Color by mutableStateOf(radioButtonPrimarySelectedColor)
        private set
    var radioButtonPrimaryUnSelectedColor: Color by mutableStateOf(radioButtonPrimaryUnSelectedColor)
        private set

    fun update(other: RadioButtonPrimaryColors) {
        radioButtonPrimarySelectedColor = other.radioButtonPrimarySelectedColor
        radioButtonPrimaryUnSelectedColor = other.radioButtonPrimaryUnSelectedColor
    }
}

@Stable
class RadioButtonSecondaryColors(
    radioButtonSecondarySelectedColor: Color,
    radioButtonSecondaryUnSelectedColor: Color,
) {
    var radioButtonSecondarySelectedColor: Color by mutableStateOf(radioButtonSecondarySelectedColor)
        private set
    var radioButtonSecondaryUnSelectedColor: Color by mutableStateOf(radioButtonSecondaryUnSelectedColor)
        private set

    fun update(other: RadioButtonSecondaryColors) {
        radioButtonSecondarySelectedColor = other.radioButtonSecondarySelectedColor
        radioButtonSecondaryUnSelectedColor = other.radioButtonSecondaryUnSelectedColor
    }
}

@Stable
class HorizontalListButtonColors(
    horizontalListButtonPrimaryEnabledContainerColor: Color,
    horizontalListButtonPrimaryDisabledContainerColor: Color,
    horizontalListButtonPrimaryEnabledContentColor: Color,
    horizontalListButtonPrimaryDisabledContentColor: Color,
) {
    var horizontalListButtonPrimaryEnabledContainerColor: Color by mutableStateOf(horizontalListButtonPrimaryEnabledContainerColor)
        private set
    var horizontalListButtonPrimaryDisabledContainerColor: Color by mutableStateOf(horizontalListButtonPrimaryDisabledContainerColor)
        private set
    var horizontalListButtonPrimaryEnabledContentColor: Color by mutableStateOf(horizontalListButtonPrimaryEnabledContentColor)
        private set
    var horizontalListButtonPrimaryDisabledContentColor: Color by mutableStateOf(horizontalListButtonPrimaryDisabledContentColor)
        private set

    fun update(other: HorizontalListButtonColors) {
        horizontalListButtonPrimaryEnabledContainerColor = other.horizontalListButtonPrimaryEnabledContainerColor
        horizontalListButtonPrimaryDisabledContainerColor = other.horizontalListButtonPrimaryDisabledContainerColor
        horizontalListButtonPrimaryEnabledContentColor = other.horizontalListButtonPrimaryEnabledContentColor
        horizontalListButtonPrimaryDisabledContentColor = other.horizontalListButtonPrimaryDisabledContentColor
    }
}

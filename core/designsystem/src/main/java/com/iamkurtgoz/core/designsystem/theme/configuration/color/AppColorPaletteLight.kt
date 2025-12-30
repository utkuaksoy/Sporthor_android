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
package com.iamkurtgoz.core.designsystem.theme.configuration.color

import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.designsystem.theme.composition.AppColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.AlertDialogColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.BarColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.BorderColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonOutlineColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonSecondaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonSecondaryWhiteColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ButtonTertiaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CardColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipProfileBranchColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipProfileGrayColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipSecondaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ChipTertiaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CircleButtonOutlineColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CircleButtonPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CircleButtonSecondaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CircleButtonSecondaryGrayColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CircleButtonTertiaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CountDownTimerPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.CountryCodePickerPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.DialogColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.GeneralColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.HorizontalListButtonColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.IndicatorViewPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.NavigationBarColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.OtpFieldPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.PickerColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.PostViewPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.RadioButtonPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.RadioButtonSecondaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.SelectableCardPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.SuggestionUserCardPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TextFieldColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TextFieldMessageColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TextFieldPrimaryColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TextFieldSearchColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.TimerColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.UserImageViewBorderColors
import com.iamkurtgoz.core.designsystem.theme.configuration.color.data.ViewColors

internal val AppColorPalette.lightColors: AppColors
    get() = AppColors(
        generalColors = GeneralColors(
            primary = primitivesGreen500,
            transparent = transparent,
            textPrimary = primitivesNeutral900,
            textSecondary = primitivesNeutral800,
            textTertiary = semanticContentSub800,
            textDisabled = semanticContentSoft600,
            textWhite = semanticContentWhite0,
            textWhiteSecondary = semanticContentWeak200,
            textSuccess800 = semanticSuccessDark800,
            textError800 = semanticErrorDark800,
            backgroundPrimary = white,
            backgroundSecondary = white,
            backgroundDisabled = white,
            foregroundPrimary = primitivesNeutral900,
            foregroundSecondary = primitivesNeutral800,
            foregroundDisabled = primitivesNeutral300,
            foregroundWhite = white,
            foregroundBlack = black,
            borderSoft200 = semanticBorderSoft200,
            borderSub300 = semanticBorderSub300,
            borderStrong900 = semanticBorderStrong900,
            backgroundWeak100 = semanticBackgroundWeak100,
            backgroundSoft200 = semanticBackgroundSoft200,
            contentSoft600 = semanticContentSoft600,
            primitivesBlue600 = primitivesBlue600,
            green100 = primitivesGreen100,
            green200 = primitivesGreen200,
            green500 = primitivesGreen500,
            primaryGreen = semanticPrimaryGreen,
            primitivesRed900 = primitivesRed900,
            primitivesRed500 = primitivesRed500,
            backgroundSurface800 = semanticBackgroundSurface800,
            transparentWhite = transparentWhite,
            primitivesPink500 = primitivesPink500,
        ),
        buttonColors = ButtonColors(
            primaryColors = ButtonPrimaryColors(
                buttonPrimaryEnabledBackground = semanticPrimaryGreen,
                buttonPrimaryEnabledForeground = semanticContentStrong900,
                buttonPrimaryDisabledBackground = semanticBackgroundSub300,
                buttonPrimaryDisabledForeground = semanticContentSoft600,
            ),
            secondaryColors = ButtonSecondaryColors(
                buttonSecondaryEnabledBackground = primitivesNeutral900,
                buttonSecondaryEnabledForeground = semanticContentWhite0,
                buttonSecondaryDisabledBackground = semanticBackgroundSub300,
                buttonSecondaryDisabledForeground = semanticContentSoft600,
            ),
            secondaryWhiteColors = ButtonSecondaryWhiteColors(
                buttonSecondaryWhiteEnabledBackground = semanticContentWhite0,
                buttonSecondaryWhiteEnabledForeground = primitivesNeutral900,
                buttonSecondaryWhiteDisabledBackground = semanticBackgroundSub300,
                buttonSecondaryWhiteDisabledForeground = semanticContentSoft600,
            ),
            tertiaryColors = ButtonTertiaryColors(
                buttonTertiaryEnabledBackground = primitivesRed500,
                buttonTertiaryEnabledForeground = semanticContentWhite0,
                buttonTertiaryDisabledBackground = semanticBackgroundSub300,
                buttonTertiaryDisabledForeground = semanticContentSoft600,
            ),
            outlineColors = ButtonOutlineColors(
                buttonOutlineEnabledBackground = semanticBackgroundWeak0,
                buttonOutlineEnabledForeground = semanticContentStrong900,
                buttonOutlineDisabledBackground = semanticBackgroundSub300,
                buttonOutlineDisabledForeground = semanticContentSoft600,
                buttonOutlineEnabledBorder = semanticContentStrong900,
                buttonOutlineDisabledBorder = semanticBackgroundSub300,
            ),
            circleButtonPrimaryColors = CircleButtonPrimaryColors(
                circleButtonPrimaryEnabledBackground = semanticPrimaryGreen,
                circleButtonPrimaryEnabledForeground = semanticContentStrong900,
                circleButtonPrimaryDisabledBackground = semanticBackgroundSub300,
                circleButtonPrimaryDisabledForeground = semanticContentSoft600,
            ),
            circleButtonSecondaryColors = CircleButtonSecondaryColors(
                circleButtonSecondaryEnabledBackground = primitivesNeutral900,
                circleButtonSecondaryEnabledForeground = semanticContentWhite0,
                circleButtonSecondaryDisabledBackground = semanticBackgroundSub300,
                circleButtonSecondaryDisabledForeground = semanticContentSoft600,
            ),
            circleButtonSecondaryGrayColors = CircleButtonSecondaryGrayColors(
                circleButtonSecondaryGrayEnabledBackground = semanticBackgroundSoft200,
                circleButtonSecondaryGrayEnabledForeground = primitivesNeutral900,
                circleButtonSecondaryGrayDisabledBackground = semanticBackgroundSub300,
                circleButtonSecondaryGrayDisabledForeground = semanticContentSoft600,
            ),
            circleButtonTertiaryColors = CircleButtonTertiaryColors(
                circleButtonTertiaryEnabledBackground = primitivesRed500,
                circleButtonTertiaryEnabledForeground = semanticContentWhite0,
                circleButtonTertiaryDisabledBackground = semanticBackgroundSub300,
                circleButtonTertiaryDisabledForeground = semanticContentSoft600,
            ),
            circleButtonOutlineColors = CircleButtonOutlineColors(
                circleButtonOutlineEnabledBackground = semanticBackgroundWeak0,
                circleButtonOutlineEnabledForeground = semanticContentStrong900,
                circleButtonOutlineDisabledBackground = semanticBackgroundSub300,
                circleButtonOutlineDisabledForeground = semanticContentSoft600,
                circleButtonOutlineEnabledBorder = semanticContentStrong900,
                circleButtonOutlineDisabledBorder = semanticBackgroundSub300,
            ),
            radioButtonPrimaryColors = RadioButtonPrimaryColors(
                radioButtonPrimarySelectedColor = semanticPrimaryBlack,
                radioButtonPrimaryUnSelectedColor = semanticBorderSub300,
            ),
            radioButtonSecondaryColors = RadioButtonSecondaryColors(
                radioButtonSecondarySelectedColor = semanticSuccessBase500,
                radioButtonSecondaryUnSelectedColor = semanticBorderSub300,
            ),
            horizontalListButtonColors = HorizontalListButtonColors(
                horizontalListButtonPrimaryEnabledContainerColor = semanticBackgroundWeak100,
                horizontalListButtonPrimaryDisabledContainerColor = semanticBackgroundSurface800,
                horizontalListButtonPrimaryEnabledContentColor = semanticContentSub800,
                horizontalListButtonPrimaryDisabledContentColor = semanticContentWhite0,
            ),
        ),
        textFieldColors = TextFieldColors(
            textFieldPrimaryColors = TextFieldPrimaryColors(
                textFieldPrimaryEnabledTitleColor = semanticContentSub800,
                textFieldPrimaryDisabledTitleColor = semanticContentSub800,
                textFieldPrimaryEnabledBorderColor = transparent,
                textFieldPrimaryDisabledBorderColor = transparent,
                textFieldPrimaryFocusedBorderColor = semanticBorderStrong900,
                textFieldPrimaryErrorBorderColor = semanticErrorBase500,
                textFieldPrimaryEnabledContainerColor = semanticBackgroundWeak100,
                textFieldPrimaryEnabledContentColor = semanticContentStrong900,
                textFieldPrimaryDisabledContainerColor = semanticBackgroundSub300,
                textFieldPrimaryDisabledContentColor = semanticContentSoft600,
                textFieldPrimaryPlaceholderColor = semanticContentSoft600,
                textFieldPrimaryCursorColor = primitivesNeutral900,
                textFieldPrimaryEnabledLeadingIconColor = semanticContentStrong900,
                textFieldPrimaryDisabledLeadingIconColor = semanticBackgroundSub300,
                textFieldPrimaryEnabledTrailingIconColor = semanticContentStrong900,
                textFieldPrimaryDisabledTrailingIconColor = semanticBackgroundSub300,
                textFieldPrimaryEnabledHintContentColor = semanticContentSub800,
                textFieldPrimaryDisabledHintContentColor = semanticContentSub800,
                textFieldPrimaryErrorHintContentColor = semanticErrorBase500,
            ),
            otpFieldPrimaryColors = OtpFieldPrimaryColors(
                otpFieldPrimaryEnabledBorderColor = transparent,
                otpFieldPrimaryDisabledBorderColor = transparent,
                otpFieldPrimaryFocusedBorderColor = semanticBorderStrong900,
                otpFieldPrimaryErrorBorderColor = semanticErrorBase500,
                otpFieldPrimaryEnabledContainerColor = semanticBackgroundWeak100,
                otpFieldPrimaryEnabledContentColor = semanticContentStrong900,
                otpFieldPrimaryDisabledContainerColor = semanticBackgroundSub300,
                otpFieldPrimaryDisabledContentColor = semanticContentSoft600,
                otpFieldPrimaryCursorColor = primitivesNeutral900,
                otpFieldPrimaryEnabledHintContentColor = semanticContentSub800,
                otpFieldPrimaryDisabledHintContentColor = semanticContentSub800,
                otpFieldPrimaryErrorHintContentColor = semanticErrorBase500,
            ),
            textFieldSearchColors = TextFieldSearchColors(
                textFieldSearchEnabledTitleColor = semanticContentSub800,
                textFieldSearchDisabledTitleColor = semanticContentSub800,
                textFieldSearchEnabledBorderColor = transparent,
                textFieldSearchDisabledBorderColor = transparent,
                textFieldSearchFocusedBorderColor = semanticBorderStrong900,
                textFieldSearchErrorBorderColor = semanticErrorBase500,
                textFieldSearchEnabledContainerColor = semanticBackgroundWeak100,
                textFieldSearchEnabledContentColor = semanticContentStrong900,
                textFieldSearchDisabledContainerColor = semanticBackgroundSub300,
                textFieldSearchDisabledContentColor = semanticContentSoft600,
                textFieldSearchPlaceholderColor = semanticContentSoft600,
                textFieldSearchCursorColor = primitivesNeutral900,
                textFieldSearchEnabledLeadingIconColor = semanticContentStrong900,
                textFieldSearchDisabledLeadingIconColor = semanticBackgroundSub300,
                textFieldSearchEnabledTrailingIconColor = semanticContentStrong900,
                textFieldSearchDisabledTrailingIconColor = semanticBackgroundSub300,
                textFieldSearchEnabledHintContentColor = semanticContentSub800,
                textFieldSearchDisabledHintContentColor = semanticContentSub800,
                textFieldSearchErrorHintContentColor = semanticErrorBase500,
            ),
            textFieldMessageColors = TextFieldMessageColors(
                textFieldMessageEnabledTitleColor = semanticContentSub800,
                textFieldMessageDisabledTitleColor = semanticContentSub800,
                textFieldMessageEnabledBorderColor = transparent,
                textFieldMessageDisabledBorderColor = transparent,
                textFieldMessageFocusedBorderColor = semanticBorderStrong900,
                textFieldMessageErrorBorderColor = semanticErrorBase500,
                textFieldMessageEnabledContainerColor = semanticBackgroundWeak100,
                textFieldMessageEnabledContentColor = semanticContentStrong900,
                textFieldMessageDisabledContainerColor = semanticBackgroundSub300,
                textFieldMessageDisabledContentColor = semanticContentSoft600,
                textFieldMessagePlaceholderColor = semanticContentSoft600,
                textFieldMessageCursorColor = primitivesNeutral900,
                textFieldMessageEnabledLeadingIconColor = Color.Unspecified,
                textFieldMessageDisabledLeadingIconColor = Color.Unspecified,
                textFieldMessageEnabledTrailingIconColor = Color.Unspecified,
                textFieldMessageDisabledTrailingIconColor = Color.Unspecified,
                textFieldMessageEnabledHintContentColor = semanticContentSub800,
                textFieldMessageDisabledHintContentColor = semanticContentSub800,
                textFieldMessageErrorHintContentColor = semanticErrorBase500,
            ),
        ),
        chipColors = ChipColors(
            primaryChipColors = ChipPrimaryColors(
                appChipPrimarySelectedContainerColor = semanticPrimaryGreen,
                appChipPrimarySelectedContentColor = primitivesNeutral900,
                appChipPrimarySelectedBorderColor = transparent,
                appChipPrimaryUnSelectedContainerColor = semanticBackgroundWeak100,
                appChipPrimaryUnSelectedContentColor = semanticContentSoft600,
                appChipPrimaryUnSelectedBorderColor = transparent,
            ),
            secondaryChipColors = ChipSecondaryColors(
                appChipSecondarySelectedContainerColor = semanticPrimaryGreen,
                appChipSecondarySelectedContentColor = primitivesNeutral900,
                appChipSecondarySelectedBorderColor = transparent,
                appChipSecondaryUnSelectedContainerColor = semanticBackgroundWeak100,
                appChipSecondaryUnSelectedContentColor = semanticContentSoft600,
                appChipSecondaryUnSelectedBorderColor = transparent,
            ),
            tertiaryChipColors = ChipTertiaryColors(
                appChipTertiarySelectedContainerColor = primitivesPink100,
                appChipTertiarySelectedContentColor = primitivesPink800,
                appChipTertiarySelectedBorderColor = transparent,
                appChipTertiaryUnSelectedContainerColor = semanticBackgroundWeak100,
                appChipTertiaryUnSelectedContentColor = semanticContentSoft600,
                appChipTertiaryUnSelectedBorderColor = transparent,
            ),
            profileGrayColors = ChipProfileGrayColors(
                appChipProfileGraySelectedContainerColor = semanticBackgroundWeak100,
                appChipProfileGraySelectedContentColor = semanticContentStrong900,
                appChipProfileGraySelectedBorderColor = semanticBorderSoft200,
                appChipProfileGrayUnSelectedContainerColor = semanticBackgroundWeak100,
                appChipProfileGrayUnSelectedContentColor = semanticContentStrong900,
                appChipProfileGrayUnSelectedBorderColor = semanticBorderSoft200,
            ),
            profileBranchColors = ChipProfileBranchColors(
                appChipProfileBranchSelectedContainerColor = semanticPrimaryGreen,
                appChipProfileBranchSelectedContentColor = semanticContentStrong900,
                appChipProfileBranchSelectedBorderColor = transparent,
                appChipProfileBranchUnSelectedContainerColor = semanticBackgroundWeak100,
                appChipProfileBranchUnSelectedContentColor = semanticContentSoft600,
                appChipProfileBranchUnSelectedBorderColor = transparent,
            ),
        ),
        pickerColors = PickerColors(
            countryCodePickerPrimaryColors = CountryCodePickerPrimaryColors(
                countryCodePickerPrimaryEnabledContainerColor = semanticBackgroundWeak100,
                countryCodePickerPrimaryDisabledContainerColor = semanticBackgroundSurface800,
                countryCodePickerPrimaryEnabledContentColor = primitivesNeutral900,
                countryCodePickerPrimaryDisabledContentColor = semanticContentWhite0,
            ),
        ),
        timerColors = TimerColors(
            countDownTimerPrimaryColors = CountDownTimerPrimaryColors(
                countDownTimerPrimaryTimeElapsed = transparent,
                countDownTimerPrimaryRemainingTime = semanticPrimaryGreen,
                countDownTimerPrimaryWarningTime = primitivesRed500,
                countDownTimerPrimaryEnabledContentColor = semanticContentStrong900,
            ),
        ),
        viewColors = ViewColors(
            indicatorViewPrimaryColors = IndicatorViewPrimaryColors(
                indicatorViewPrimarySelectedColor = primitivesGreen500,
                indicatorViewPrimaryUnselectedColor = primitivesNeutral300,
            ),
            postViewPrimaryColors = PostViewPrimaryColors(
                userNameColor = semanticContentStrong900,
                likedPreviewUserNameColor = semanticContentSub800,
                defaultTextColor = semanticContentSub800,
            ),
        ),
        cardColors = CardColors(
            selectableCardPrimaryColors = SelectableCardPrimaryColors(
                selectableCardSelectedContainerColor = semanticSuccessLighter100,
                selectableCardSelectedContentColor = primitivesNeutral900,
                selectableCardSelectedBorderColor = semanticSuccessBase500,
                selectableCardSelectedIconBackgroundColor = primitivesGreen500,
                selectableCardUnSelectedContainerColor = semanticBackgroundWeak100,
                selectableCardUnSelectedContentColor = primitivesNeutral900,
                selectableCardUnSelectedBorderColor = transparent,
                selectableCardUnSelectedIconBackgroundColor = semanticContentWhite0,
            ),
            suggestionUserCardPrimaryColors = SuggestionUserCardPrimaryColors(
                suggestionUserCardContainerColor = white,
                suggestionUserCardContentColor = primitivesNeutral900,
                suggestionUserCardBorderColor = semanticBorderSoft200,
            ),
        ),
        barColors = BarColors(
            navigationBarColors = NavigationBarColors(
                navigationBarContainerColor = white,
                navigationBarIconSelectedColor = primitivesNeutral900,
                navigationBarIconUnSelectedColor = primitivesNeutral800,
                navigationBarTextSelectedColor = primitivesNeutral900,
                navigationBarTextUnSelectedColor = primitivesNeutral800,
            ),
        ),
        borderColors = BorderColors(
            userImageViewBorderColors = UserImageViewBorderColors(
                borderColorFirst = semanticPrimaryGreen,
                borderColorSecond = semanticNoticeBase500,
                borderColorThird = primitivesPink500,
            ),
        ),
        dialogColors = DialogColors(
            alertDialogColors = AlertDialogColors(
                containerColor = white,
                iconContentColor = primitivesNeutral900,
                titleContentColor = primitivesNeutral900,
                textContentColor = primitivesNeutral800,
                confirmButtonContentColor = blue,
                dismissButtonContentColor = blue,
            ),
        ),
    )

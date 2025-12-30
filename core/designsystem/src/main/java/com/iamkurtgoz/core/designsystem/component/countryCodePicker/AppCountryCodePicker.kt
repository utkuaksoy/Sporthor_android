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
package com.iamkurtgoz.core.designsystem.component.countryCodePicker

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

object AppCountryCodePicker {
    @Composable
    fun Primary(
        @DrawableRes icon: Int,
        countryCode: String,
        modifier: Modifier = Modifier,
        colors: CountryCodePickerColors = AppCountryCodePickerColors.primaryColors(),
        sizes: CountryCodePickerSizes = AppCountryCodePickerSizes.primarySizes(),
        styles: CountryCodePickerStyles = AppCountryCodePickerStyles.primaryStyles(),
        shapes: CountryCodePickerShapes = AppCountryCodePickerShapes.primaryShapes(),
    ) = CountryCodeSelectorImpl(
        icon = icon,
        countryCode = countryCode,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
    )
}

@Composable
private fun CountryCodeSelectorImpl(
    @DrawableRes icon: Int,
    countryCode: String,
    colors: CountryCodePickerColors,
    sizes: CountryCodePickerSizes,
    styles: CountryCodePickerStyles,
    shapes: CountryCodePickerShapes,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = colors.enabledContainerColor, shape = shapes.containerShape)
            .padding(sizes.contentPadding),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Country Flag",
                modifier = Modifier
                    .size(sizes.iconSize)
                    .clip(shape = shapes.imageShape),
            )

            Spacer(modifier = Modifier.width(sizes.paddingEnd))

            Text(
                text = countryCode,
                color = colors.enabledContentColor,
                style = styles.textStyle,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            AppCountryCodePicker.Primary(
                countryCode = "+90",
                icon = resourcesR.drawable.img_tr_flag,
            )
        }
    }
}

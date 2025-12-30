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
package com.iamkurtgoz.feature.home.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourceR

@Composable
internal fun ProfileDetailProfileCard(
    imageUrl: String?,
    name: String?,
    nationalityName: String?,
    flagIcon: String?,
    birthDate: String?,
    height: String?,
    weight: String?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(AppDefaults.ASPECT_RATIO_2),
        shape = AppTheme.shapes.radiusMedium,
        shadowElevation = AppTheme.dimens.dp1,
    ) {
        Image(
            painter = painterResource(id = resourceR.drawable.img_user_card_background),
            contentDescription = "user_card_background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = AppTheme.spacing.spacingHuge)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                UserImageView(
                    data = imageUrl,
                    hasBorder = false,
                )

                Text(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingSmall),
                    text = name ?: "",
                    color = Color.White,
                    style = AppTheme.typography.subtitleLarge,
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingSmall),
            ) {
                UserInfoItemWithIcon(
                    flagIcon = flagIcon,
                    label = "Uyruk", // TODO: Localize
                    value = nationalityName ?: "",
                )

                UserInfoItem(
                    label = "Doğum Tarihi", // TODO: Localize
                    value = birthDate ?: "",
                )

                UserInfoItem(
                    label = "Boy", // TODO: Localize
                    value = height ?: "",
                )

                UserInfoItem(
                    label = "Kilo", // TODO: Localize
                    value = weight ?: "",
                )
            }
        }
    }
}

@Composable
private fun UserInfoItemWithIcon(
    flagIcon: Any?,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            color = AppTheme.colors.generalColors.foregroundDisabled,
            style = AppTheme.typography.helperText,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppAsyncImageLoader.Load(
                data = flagIcon,
                contentDescription = null,
                modifier = Modifier
                    .clip(AppTheme.shapes.radiusCircle)
                    .size(AppTheme.dimens.dp24)
                    .padding(end = AppTheme.spacing.spacingSmallest),
            )

            Text(
                text = value,
                color = Color.White,
                style = AppTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun UserInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            color = AppTheme.colors.generalColors.foregroundDisabled,
            style = AppTheme.typography.helperText,
        )
        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingSmallest),
            text = value,
            color = Color.White,
            style = AppTheme.typography.labelMedium,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileDetailProfileCard(
                imageUrl = "https://randomuser.me/api/portraits/men/32.jpg",
                name = "Mesut Canbaz",
                nationalityName = "Türkiye",
                flagIcon = "https://flagcdn.com/w80/tr.png",
                birthDate = "18.05.2008",
                height = "174 cm",
                weight = "68 kg",
            )
        }
    }
}

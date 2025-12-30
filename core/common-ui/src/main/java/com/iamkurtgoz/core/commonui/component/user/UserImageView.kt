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
package com.iamkurtgoz.core.commonui.component.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.request.error
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserImageView(
    modifier: Modifier = Modifier,
    data: Any? = null,
    backgroundColor: Color = AppTheme.colors.generalColors.backgroundSoft200,
    textStyle: TextStyle = AppTheme.typography.labelLarge,
    badgeData: Any? = null,
    size: Dp = AppTheme.dimens.dp48,
    badgeSize: Dp = AppTheme.dimens.dp24,
    shape: RoundedCornerShape = AppTheme.shapes.radiusCircle,
    badgeShape: RoundedCornerShape = AppTheme.shapes.radiusCircle,
    badgeBackgroundColor: Color = AppTheme.colors.generalColors.foregroundWhite,
    hasBorder: Boolean = false,
    borderIsGray: Boolean = true,
    onClickAction: () -> Unit = {},
    onClickBadgeAction: () -> Unit = {},
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(size + AppTheme.dimens.dp8)
                .ifTrue(hasBorder && borderIsGray) {
                    this.border(
                        border = BorderStroke(
                            width = AppTheme.dimens.dp2,
                            color = AppTheme.colors.generalColors.borderSoft200,
                        ),
                        shape = shape,
                    )
                }
                .ifTrue(hasBorder && !borderIsGray) {
                    this.border(
                        border = BorderStroke(
                            width = AppTheme.dimens.dp2,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppTheme.colors.borderColors.userImageViewBorderColors.borderColorFirst,
                                    AppTheme.colors.borderColors.userImageViewBorderColors.borderColorSecond,
                                    AppTheme.colors.borderColors.userImageViewBorderColors.borderColorThird,
                                ),
                            ),
                        ),
                        shape = shape,
                    )
                }
                .clip(shape)
                .clickable(onClick = onClickAction),
            contentAlignment = Alignment.Center,
        ) {
            if (data != null && data is String && !data.contains("http") && !data.contains("https")) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(
                            color = backgroundColor,
                            shape = shape,
                        )
                        .clip(shape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = data,
                        style = textStyle,
                        color = AppTheme.colors.generalColors.textDisabled,
                    )
                }
            } else {
                AppAsyncImageLoader.Load(
                    modifier = Modifier
                        .clip(shape)
                        .size(size),
                    data = data,
                    imageRequest = {
                        it.placeholder(ColorImage(color = backgroundColor.toArgb()))
                        it.error(resourcesR.drawable.img_error_user_image)
                        it.build()
                    },
                    contentScale = ContentScale.Crop,
                )
            }
        }

        badgeData?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(badgeSize)
                    .background(
                        color = badgeBackgroundColor,
                        shape = badgeShape,
                    )
                    .clip(badgeShape)
                    .clickable(onClick = onClickBadgeAction),
                contentAlignment = Alignment.Center,
            ) {
                AppAsyncImageLoader.Load(
                    modifier = Modifier
                        .clip(badgeShape)
                        .size(badgeSize - AppTheme.dimens.dp4),
                    data = badgeData,
                )
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                UserImageView(
                    data = resourcesR.drawable.temp_img_profile_women,
                    badgeData = resourcesR.drawable.temp_team_image_ezcacibasi,
                )

                UserImageView(
                    data = "MK",
                    badgeData = resourcesR.drawable.temp_team_image_ezcacibasi,
                )

                UserImageView(
                    data = "https://randomuser.me/api/portraits/women/25.jpg",
                    hasBorder = true,
                    borderIsGray = true,
                    badgeData = resourcesR.drawable.temp_team_image_ezcacibasi,
                )

                UserImageView(
                    data = "https://randomuser.me/api/portraits/women/25.jpg",
                    hasBorder = true,
                    borderIsGray = false,
                    badgeData = resourcesR.drawable.temp_team_image_ezcacibasi,
                )
            }
        }
    }
}

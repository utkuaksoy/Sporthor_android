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
package com.iamkurtgoz.core.commonui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
fun SelectedClubSection(
    clubName: String,
    clubLogo: Any,
    onChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    showChangeButton: Boolean = false,
    showTitle: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if (showTitle) {
            Text(
                text = "Seçilen Kulüp", // TODO: Localize
                style = AppTheme.typography.labelMedium,
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.spacingSmall))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.spacing.spacingSmall),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppAsyncImageLoader.Load(
                    data = clubLogo,
                    contentDescription = "$clubName logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(AppTheme.spacing.spacingMedium))

                Column {
                    Text(
                        text = clubName,
                        style = AppTheme.typography.labelMedium,
                    )

                    subTitle?.let {
                        Text(
                            text = subTitle,
                            style = AppTheme.typography.bodyMediumCompact,
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingSmallest)
                        )
                    }
                }
            }
            
            if (showChangeButton) {
                Text(
                    text = "Değiştir", // TODO: Localize
                    style = AppTheme.typography.labelRegular,
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .clickable { onChangeClick() }
                        .padding(AppTheme.spacing.spacingSmall),
                )
            }
        }

        HorizontalDivider()
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectedClubSection(
                clubName = "Esen Spor Kulübü",
                clubLogo = resourcesR.drawable.temp_img_profile_women,
                subTitle = "asdasd",
                onChangeClick = {},
            )
        }
    }
}

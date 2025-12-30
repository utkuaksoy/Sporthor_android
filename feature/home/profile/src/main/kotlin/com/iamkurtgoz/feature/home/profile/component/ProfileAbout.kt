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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.util.fastForEach
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataExtraInfoUIModel

@Composable
internal fun ProfileAbout(
    title: String,
    description: String,
    extraInfo: List<ProfileComponentDataExtraInfoUIModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingMedium)
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(bottom = AppTheme.spacing.spacingSmall),
            text = title,
            style = AppTheme.typography.labelLarge,
        )

        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(bottom = AppTheme.spacing.spacingMedium),
            text = description,
            style = AppTheme.typography.bodyMedium,
        )

        extraInfo.fastForEach {
            key(it.key) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingSmallest),
                ) {
                    AppAsyncImageLoader.Load(
                        data = it.icon,
                        modifier = Modifier
                            .clip(AppTheme.shapes.radiusCircle)
                            .size(AppTheme.dimens.dp24),
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmall),
                        text = it.value ?: "",
                        style = AppTheme.typography.bodyMediumCompact,
                    )
                }
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileAbout(
                title = "Hakkımda",
                description = "Ben Bade Belgin, 2008 doğumluyum ve Eczacıbaşı Spor Kulübü U17 takımında voleybol oynuyorum. Güçlü servislerim ve bloklarım en büyük avantajlarım.",
                extraInfo = listOf(
                    ProfileComponentDataExtraInfoUIModel(
                        key = "Adres",
                        value = "İstanbul, Türkiye",
                        icon = "location.fill",
                    ),
                    ProfileComponentDataExtraInfoUIModel(
                        key = "Doğum Tarihi",
                        value = "26.09.1990",
                        icon = "calendar",
                    ),
                ),
            )
        }
    }
}

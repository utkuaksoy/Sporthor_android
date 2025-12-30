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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataItemUIModel

internal fun LazyListScope.profileDetailCareerHistory(
    title: String?,
    items: List<ProfileDetailComponentDataItemUIModel>?,
) {
    item {
        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(bottom = AppTheme.spacing.spacingHuge),
            text = title ?: "",
            style = AppTheme.typography.subtitleLarge,
        )
    }

    itemsIndexed(
        items = items ?: listOf(),
        key = { index, item ->
            "$index - $item"
        },
    ) { _, item ->
        CareerItemRow(
            careerItem = item,
        )
    }
}

@Composable
private fun CareerItemRow(
    careerItem: ProfileDetailComponentDataItemUIModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.spacing.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp56)
                .border(
                    width = AppTheme.dimens.dp1,
                    color = AppTheme.colors.generalColors.textWhiteSecondary,
                    shape = AppTheme.shapes.radiusCircle,
                ),
            contentAlignment = Alignment.Center,
        ) {
            AppAsyncImageLoader.Load(
                data = resourcesR.drawable.temp_team_image_ezcacibasi,
                modifier = Modifier
                    .size(AppTheme.dimens.dp48)
                    .clip(AppTheme.shapes.radiusCircle),
            )
        }

        Column(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingSmall),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.spacingSmallest),
                text = careerItem.teamName ?: "",
                style = AppTheme.typography.labelMedium,
            )

            Row {
                Text(
                    text = careerItem.startDate ?: "",
                    style = AppTheme.typography.bodyMediumCompact,
                    color = AppTheme.colors.generalColors.contentSoft600,
                )

                Text(
                    text = "-",
                    style = AppTheme.typography.bodyMediumCompact,
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingSmallest),
                )

                Text(
                    text = careerItem.endDate ?: "",
                    style = AppTheme.typography.bodyMediumCompact,
                    color = AppTheme.colors.generalColors.contentSoft600,
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
            LazyColumn {
                profileDetailCareerHistory(
                    title = "Kariyer Geçmişi",
                    items = listOf(
                        ProfileDetailComponentDataItemUIModel(
                            teamName = "Real Madrid FC",
                            teamLogoURL = "https://tmssl.akamaized.net/images/wappen/head/418.png",
                            startDate = "12 Ekim 2023",
                            endDate = "Devam Ediyor",
                        ),
                        ProfileDetailComponentDataItemUIModel(
                            teamName = "Barcelona FC",
                            teamLogoURL = "https://tmssl.akamaized.net/images/wappen/head/131.png",
                            startDate = "12 Ekim 2022",
                            endDate = "12 Ekim 2023",
                        ),
                        ProfileDetailComponentDataItemUIModel(
                            teamName = "Manchester United FC",
                            teamLogoURL = "https://tmssl.akamaized.net/images/wappen/head/985.png",
                            startDate = "12 Ekim 2021",
                            endDate = "12 Ekim 2022",
                        ),
                        ProfileDetailComponentDataItemUIModel(
                            teamName = "Bayern Munich FC",
                            teamLogoURL = "https://tmssl.akamaized.net/images/wappen/head/27.png",
                            startDate = "12 Ekim 2021",
                            endDate = "12 Ekim 2022",
                        ),
                        ProfileDetailComponentDataItemUIModel(
                            teamName = "Liverpool FC",
                            teamLogoURL = "https://tmssl.akamaized.net/images/wappen/head/31.png",
                            startDate = "12 Ekim 2020",
                            endDate = "12 Ekim 2021",
                        ),
                    ),
                )
            }
        }
    }
}

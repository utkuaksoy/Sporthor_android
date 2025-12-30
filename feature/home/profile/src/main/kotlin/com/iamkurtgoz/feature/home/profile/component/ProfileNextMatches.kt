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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataMatchUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataTeamUIModel

@Composable
internal fun ProfileNextMatches(
    title: String,
    matches: List<ProfileComponentDataMatchUIModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingHuge)
                .padding(bottom = AppTheme.spacing.spacingMedium)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            text = title,
            style = AppTheme.typography.subtitleLarge,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = AppTheme.spacing.spacingMedium,
            ),
        ) {
            itemsIndexed(
                items = matches,
                key = { index, item ->
                    "$index - $item"
                },
            ) { index, item ->
                MatchCard(
                    index = index,
                    match = item,
                )
            }
        }
    }
}

@Composable
private fun MatchCard(
    index: Int,
    match: ProfileComponentDataMatchUIModel,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .width((AppTheme.configuration.getScreenWidth() * AppDefaults.ASPECT_RATIO_0_75).dp)
            .ifTrue(index != AppDefaults.ZERO) {
                this.padding(start = AppTheme.spacing.spacingMedium)
            },
        shape = AppTheme.shapes.radiusMedium,
        color = AppTheme.colors.generalColors.backgroundWeak100,
        border = BorderStroke(
            width = AppTheme.dimens.dp1,
            color = AppTheme.colors.generalColors.borderSoft200,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(vertical = AppTheme.spacing.spacingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(AppDefaults.WEIGHT_FULL),
                horizontalAlignment = Alignment.Start,
            ) {
                match.teams?.fastForEach {
                    key(it) {
                        TeamItem(
                            teamLogoData = it.teamLogoImageUrl,
                            name = it.teamName,
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = resourcesR.drawable.img_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.dp24),
            )
        }
    }
}

@Composable
private fun TeamItem(
    teamLogoData: Any?,
    name: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = AppTheme.spacing.spacingSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.dimens.dp32)
                .clip(CircleShape)
                .background(
                    color = AppTheme.colors.generalColors.foregroundWhite,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            AppAsyncImageLoader.Load(
                data = teamLogoData,
                contentDescription = "Team Logo",
                modifier = Modifier
                    .size(AppTheme.dimens.dp24)
                    .clip(CircleShape),
            )
        }

        Text(
            text = name ?: "",
            style = AppTheme.typography.labelRegular,
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingSmall),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileNextMatches(
                title = "Yaklaşan Maçlar",
                matches = listOf(
                    ProfileComponentDataMatchUIModel(
                        matchId = 1,
                        teams = listOf(
                            ProfileComponentDataTeamUIModel(
                                teamId = "1",
                                teamName = "Galatasaray",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/141.png",
                            ),
                            ProfileComponentDataTeamUIModel(
                                teamId = "2",
                                teamName = "Real Madrid",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/418.png",
                            ),
                        ),
                    ),
                    ProfileComponentDataMatchUIModel(
                        matchId = 2,
                        teams = listOf(
                            ProfileComponentDataTeamUIModel(
                                teamId = "1",
                                teamName = "Galatasaray",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/141.png",
                            ),
                            ProfileComponentDataTeamUIModel(
                                teamId = "2",
                                teamName = "Real Madrid",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/418.png",
                            ),
                        ),
                    ),
                    ProfileComponentDataMatchUIModel(
                        matchId = 2,
                        teams = listOf(
                            ProfileComponentDataTeamUIModel(
                                teamId = "1",
                                teamName = "Galatasaray",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/141.png",
                            ),
                            ProfileComponentDataTeamUIModel(
                                teamId = "2",
                                teamName = "Real Madrid",
                                teamLogoImageUrl = "https://tmssl.akamaized.net/images/wappen/head/418.png",
                            ),
                        ),
                    ),
                ),
            )
        }
    }
}

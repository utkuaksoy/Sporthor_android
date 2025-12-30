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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataTournamentUIModel

internal fun LazyListScope.profileDetailTournaments(
    title: String?,
    tournaments: List<ProfileDetailComponentDataTournamentUIModel>?,
) {
    item {
        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(top = AppTheme.spacing.spacingHuge)
                .padding(bottom = AppTheme.spacing.spacingMedium),
            text = title ?: "", // TODO: Localize
            style = AppTheme.typography.subtitleLarge,
        )
    }

    itemsIndexed(
        items = tournaments ?: listOf(),
        key = { index, item ->
            "$index - $item"
        },
    ) { _, item ->
        TournamentRow(
            tournament = item,
        )
    }
}

@Composable
private fun TournamentRow(
    tournament: ProfileDetailComponentDataTournamentUIModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.spacing.spacingMedium)
            .padding(horizontal = AppTheme.spacing.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppAsyncImageLoader.Load(
            data = tournament.icon,
            contentDescription = null,
            modifier = Modifier
                .size(AppTheme.dimens.dp56)
                .clip(AppTheme.shapes.radiusMedium)
                .border(
                    width = AppTheme.dimens.dp1,
                    color = AppTheme.colors.generalColors.textWhiteSecondary,
                    shape = AppTheme.shapes.radiusMedium,
                )
                .padding(all = AppTheme.spacing.spacingSmall),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AppTheme.spacing.spacingSmall),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.spacingSmallest),
                text = tournament.title ?: "",
                style = AppTheme.typography.subtitleSmall,
            )

            tournament.stage?.let {
                Row(
                    modifier = Modifier
                        .background(
                            color = AppTheme.colors.generalColors.green100,
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .border(
                            width = AppTheme.dimens.dp1,
                            color = AppTheme.colors.generalColors.green500,
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .clip(AppTheme.shapes.radiusCircle)
                        .padding(
                            horizontal = AppTheme.spacing.spacingSmall,
                            vertical = AppTheme.spacing.spacingSmallest,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppAsyncImageLoader.Load(
                        data = tournament.stageIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(AppTheme.dimens.dp18),
                    )

                    Text(
                        text = it,
                        style = AppTheme.typography.bodyMediumCompact,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmallest),
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
            LazyColumn {
                profileDetailTournaments(
                    title = "Turnuvalar",
                    tournaments = listOf(
                        ProfileDetailComponentDataTournamentUIModel(
                            title = "UEFA Avrupa Şampiyonası",
                            stage = "Yarı Final",
                            icon = "https://tmssl.akamaized.net/images/wappen/head/418.png",
                            stageIcon = "https://cdn-icons-png.flaticon.com/512/3113/3113028.png",
                        ),
                        ProfileDetailComponentDataTournamentUIModel(
                            title = "Dünya Kupası",
                            stage = null,
                            icon = "https://tmssl.akamaized.net/images/wappen/head/131.png",
                            stageIcon = null,
                        ),
                    ),
                )
            }
        }
    }
}

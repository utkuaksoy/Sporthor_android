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
package com.iamkurtgoz.feature.home.selectSportClub

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.selectSportClub.domain.model.mockSportClubList

@Composable
internal fun SelectSportClubScreenContent(
    state: SelectSportClubScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectSportClubScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        itemsIndexed(
            items = state.list,
            key = { index, item ->
                "$index ${item.clubId} ${item.clubName}"
            },
            itemContent = { index, item ->
                Surface(
                    modifier = Modifier
                        .padding(all = AppTheme.spacing.spacingMedium)
                        .clip(AppTheme.shapes.radiusMedium)
                        .clickable {
                            setEvent.invoke(SelectSportClubScreenContract.Event.NavigateToEditTeamScreen(item))
                        },
                    shape = AppTheme.shapes.radiusMedium,
                    color = AppTheme.colors.generalColors.backgroundWeak100,
                    shadowElevation = AppTheme.dimens.dp0dot5,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppTheme.spacing.spacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UserImageView(
                            data = item.logo ?: item.clubName.getUserNameFirstChar(),
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(all = AppTheme.spacing.spacingMedium),
                        ) {
                            item.clubName?.let {
                                Text(
                                    text = it,
                                    style = AppTheme.typography.subtitleLarge,
                                )
                            }

                            if (!item.foundationYear.isNullOrBlank()) {
                                Text(
                                    text = item.foundationYear ?: "-",
                                    style = AppTheme.typography.subtitleLarge,
                                    modifier = Modifier
                                        .padding(top = AppTheme.spacing.spacingTiny),
                                )
                            }

                            item.city?.let {
                                Text(
                                    text = it,
                                    style = AppTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(top = AppTheme.spacing.spacingTiny),
                                )
                            }

                            item.address?.let {
                                Text(
                                    text = it,
                                    style = AppTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(top = AppTheme.spacing.spacingTiny),
                                )
                            }
                        }

                        Image(
                            painter = painterResource(resourcesR.drawable.img_arrow_right),
                            contentDescription = null,
                        )
                    }
                }
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectSportClubScreenContent(
                state = SelectSportClubScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    list = mockSportClubList,
                ),
                setEvent = { },
            )
        }
    }
}

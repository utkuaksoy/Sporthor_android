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
package com.iamkurtgoz.feature.home.selectTrainingGroup

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
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSelectTrainingGroupScreenRoute
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.model.mockTrainingGroupUserUIModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SelectTrainingGroupScreenContent(
    state: SelectTrainingGroupScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectTrainingGroupScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        itemsIndexed(
            items = state.groups,
            key = { index, item ->
                "$index ${item.groupId} ${item.groupName}"
            },
            itemContent = { index, item ->
                Surface(
                    modifier = Modifier
                        .padding(all = AppTheme.spacing.spacingTiny)
                        .clip(AppTheme.shapes.radiusMedium)
                        .clickable {
                            if (state.isDeleteMode) {
                                setEvent.invoke(SelectTrainingGroupScreenContract.Event.SetSelectedTrainingGroup(item))
                            } else {
                                setEvent.invoke(SelectTrainingGroupScreenContract.Event.NavigateToEditTrainingGroupScreen(item))
                            }
                        },
                    shape = AppTheme.shapes.radiusMedium,
                    color = AppTheme.colors.generalColors.backgroundSoft200,
                    shadowElevation = AppTheme.dimens.dp0dot5,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppTheme.spacing.spacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UserImageView(
                            data = item.groupImage ?: item.groupName.getUserNameFirstChar(),
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(all = AppTheme.spacing.spacingMedium),
                        ) {
                            item.groupName?.let {
                                Text(
                                    text = it,
                                    style = AppTheme.typography.subtitleLarge,
                                )
                            }

                            item.team?.name?.let {
                                Text(
                                    text = it,
                                    style = AppTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(top = AppTheme.spacing.spacingTiny),
                                )
                            }

                            Text(
                                text = "${item.users?.size ?: 0} Sporcu - ${item.coaches?.size ?: 0} Antrenör",
                                style = AppTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(top = AppTheme.spacing.spacingTiny),
                                color = AppTheme.colors.generalColors.contentSoft600,
                            )
                        }

                        if (state.isDeleteMode) {
                            AppRadioButton.Secondary(
                                selected = state.selectedTrainingGroup?.groupId == item.groupId,
                                onClick = {
                                    setEvent.invoke(SelectTrainingGroupScreenContract.Event.SetSelectedTrainingGroup(item))
                                },
                            )
                        } else {
                            Image(
                                painter = painterResource(resourcesR.drawable.img_arrow_right),
                                contentDescription = null,
                            )
                        }
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
            SelectTrainingGroupScreenContent(
                state = SelectTrainingGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSelectTrainingGroupScreenRoute(),
                    groups = mockTrainingGroupUserUIModel.groups?.filterNotNull() ?: listOf(),
                ),
                setEvent = { },
            )
        }
    }
}

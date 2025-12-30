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
package com.iamkurtgoz.feature.home.selectTeam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.feature.home.selectTeam.domain.model.TeamsUIItemModel
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Composable
internal fun SelectTeamScreenContent(
    state: SelectTeamScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectTeamScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            AppCircleButton.SecondaryGrayLarge(
                icon = resourcesR.drawable.img_back_arrow,
                onClick = {
                    setEvent.invoke(SelectTeamScreenContract.Event.NavigateUp)
                },
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            val text = if (state.route.fromGenerateClub) {
                "Hangi kulüpte yetkilisin?" // TODO: Localize
            } else if (state.route.fromTrainingGroup) {
                "Hangi kulüpte antrenörsün?" // TODO: Localize
            } else {
                when (state.customUserRole) {
                    CustomUserRole.TRAINER -> "Hangi kulüpte antrenörsün?" // TODO: Localize
                    CustomUserRole.CLUB_OFFICIAL -> "Hangi kulüpte yetkilisin?" // TODO: Localize
                    CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> "Hangi kulüpte yetkilisin?" // TODO: Localize
                    else -> stringResource(resourcesR.string.onboardingscreen_label_which_team_do_you_play_in_title)
                }
            }
            Text(
                text = text,
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            val text = if (state.route.fromGenerateClub) {
                "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur." // TODO: Localize
            } else if (state.route.fromTrainingGroup) {
                "Antrenörü olduğun kulübü listeden seçebilirsin." // TODO: Localize
            } else {
                when (state.customUserRole) {
                    CustomUserRole.TRAINER -> "Antrenörü olduğun kulübü listeden seçebilirsin." // TODO: Localize
                    CustomUserRole.CLUB_OFFICIAL -> "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur." // TODO: Localize
                    CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur." // TODO: Localize
                    else -> stringResource(resourcesR.string.onboardingscreen_label_which_team_do_you_play_in_sub_title)
                }
            }
            Text(
                text = text,
                style = AppTheme.typography.bodyLargeCompact,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            AppTextField.SearchField(
                modifier = Modifier
                    .padding(
                        PaddingValues(
                            horizontal = AppTheme.dimens.dp16,
                            vertical = AppTheme.dimens.dp8,
                        ),
                    ),
                placeholder = stringResource(resourcesR.string.onboardingscreen_label_search_your_team),
                value = state.textSearch.value,
                trailingIcon = if (state.textSearch.value.isNotEmpty()) resourcesR.drawable.img_close_circle else null,
                trailingIconClick = {
                    setEvent.invoke(SelectTeamScreenContract.Event.SetTextSearch(""))
                },
                leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                onValueChange = {
                    setEvent.invoke(SelectTeamScreenContract.Event.SetTextSearch(it))
                },
            )
        }

        itemsIndexed(
            items = if (state.textSearch.isNotEmpty) state.filteredTeamsList else state.teamsList,
            key = { index, item ->
                "$index-${item.uuid}"
            },
            itemContent = { index, item ->
                val isSelected = remember(state.selectedTeamsList) {
                    state.selectedTeamsList.contains(item)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ifTrue(index == AppDefaults.ZERO) {
                            this.padding(top = AppTheme.spacing.spacingHuge)
                        }
                        .ifTrue(index != AppDefaults.ZERO) {
                            this.padding(top = AppTheme.spacing.spacingSmall)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppAsyncImageLoader.Load(
                        data = item.image,
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingHuge)
                            .size(AppTheme.dimens.dp48)
                            .clip(CircleShape),
                    )

                    Text(
                        text = item.name ?: "",
                        style = AppTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingSmall)
                            .weight(AppDefaults.WEIGHT_FULL),
                    )

                    if (isSelected) {
                        AppButton.OutlineSmall(
                            text = "Seçimi Kaldır", // TODO: Localize
                            onClick = {
                                setEvent.invoke(SelectTeamScreenContract.Event.SetSelectedTeam(item))
                            },
                            modifier = Modifier
                                .padding(end = AppTheme.spacing.spacingHuge),
                        )
                    } else {
                        AppButton.SecondarySmall(
                            text = "Seç", // TODO: Localize
                            onClick = {
                                setEvent.invoke(SelectTeamScreenContract.Event.SetSelectedTeam(item))
                            },
                            modifier = Modifier
                                .padding(end = AppTheme.spacing.spacingHuge),
                        )
                    }
                }
            },
        )

        if (state.textSearch.isNotEmpty && state.filteredTeamsList.isEmpty()) {
            item {
                Text(
                    text = "Kulübün listede yok mu? Hemen Oluştur.", // TODO: Localize,
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingHuge)
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                )

                AppButton.OutlineLarge(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingHuge)
                        .padding(top = AppTheme.spacing.spacingMedium),
                    leftIcon = resourcesR.drawable.img_add_circle,
                    text = "Kulübünü Oluştur", // TODO: Localize
                    onClick = {
                        setEvent.invoke(SelectTeamScreenContract.Event.CreateTeam)
                    },
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectTeamScreenContent(
                state = SelectTeamScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSelectTeamRoute(
                        isEdit = false,
                        fromGenerateClub = false,
                        fromTrainingGroup = false,
                    ),
                    teamsList = persistentListOf(
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Vakıfbank Minik Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Vakıfbank Genç Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Vakıfbank Yıldız Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Türk Havayolları Minik Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Türk Havayolları Genç Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Türk Havayolları Yıldız Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Eczacıbaşı Minik Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Eczacıbaşı Genç Takım",
                        ),
                        TeamsUIItemModel(
                            uuid = UUID.randomUUID().toString(),
                            image = null,
                            value = UUID.randomUUID().toString(),
                            name = "Eczacıbaşı Yıldız Takım",
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

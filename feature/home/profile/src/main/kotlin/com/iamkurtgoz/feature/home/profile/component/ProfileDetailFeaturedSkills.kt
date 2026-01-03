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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.chip.AppChip
import com.iamkurtgoz.core.designsystem.component.chip.AppChipColors
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.domain.model.base.BasicAppChipItem
import com.iamkurtgoz.feature.home.profile.ProfileScreenContract
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillDetailUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillUIModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProfileDetailFeaturedSkills(
    state: ProfileScreenContract.State,
    title: String?,
    skills: List<ProfileDetailComponentDataSkillUIModel>?,
    setEvent: (ProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingHuge)
                .padding(bottom = AppTheme.spacing.spacingSmall)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            text = title ?: "",
            style = AppTheme.typography.subtitleLarge,
        )

        Surface(
            shape = AppTheme.shapes.radiusMedium,
            color = AppTheme.colors.generalColors.backgroundWeak100,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
        ) {
            Column(
                modifier = Modifier,
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.spacingMedium),
                    contentPadding = PaddingValues(
                        horizontal = AppTheme.spacing.spacingMedium,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    itemsIndexed(
                        items = skills ?: listOf(),
                        key = { index, item ->
                            "$index - $item"
                        },
                        itemContent = { index, item ->
                            AppChip.ProfileBranch(
                                item = BasicAppChipItem(
                                    title = item.name,
                                ),
                                leftContent = {
                                    AppAsyncImageLoader.Load(
                                        data = item.icon,
                                        modifier = Modifier
                                            .padding(end = AppTheme.dimens.dp8)
                                            .size(AppTheme.dimens.dp24),
                                    )
                                },
                                modifier = Modifier
                                    .padding(vertical = AppTheme.spacing.spacingSmallest)
                                    .ifTrue(index != AppDefaults.ZERO) {
                                        this.padding(start = AppTheme.spacing.spacingSmall)
                                    },
                                onClick = {
                                    setEvent.invoke(ProfileScreenContract.Event.SetSelectedSkillState(item))
                                },
                                isSelected = state.selectedSkillState?.id == item.id,
                                colors = AppChipColors.profileBranchColors(
                                    unSelectedContainerColor = AppTheme.colors.generalColors.foregroundWhite,
                                ),
                            )
                        },
                    )
                }

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxItemsInEachRow = AppDefaults.TWO,
                ) {
                    state.selectedSkillState?.details?.filter { !it?.value.isNullOrBlank() }?.fastForEach {
                        key(it) {
                            InfoColumn(
                                modifier = Modifier
                                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                                    .weight(AppDefaults.WEIGHT_FULL),
                                label = it?.title ?: "",
                                value = it?.value ?: "",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(bottom = AppTheme.spacing.spacingMedium),
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = AppTheme.spacing.spacingSmallest),
            text = "$label:",
            style = AppTheme.typography.labelRegular,
            color = AppTheme.colors.generalColors.foregroundSecondary,
        )
        Text(
            text = value,
            style = AppTheme.typography.subtitleLarge,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    val skills = listOf(
        ProfileDetailComponentDataSkillUIModel(
            id = "volleyball",
            name = "Voleybol",
            icon = "https://cdn-icons-png.flaticon.com/512/2148/2148775.png",
            isSelected = true,
            details = listOf(
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Blok Yüksekliği",
                    value = "210",
                    unit = "cm",
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Smaç Yüksekliği",
                    value = "230",
                    unit = "cm",
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Pozisyon",
                    value = "Smaçör",
                    unit = null,
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Forma Numarası",
                    value = "14",
                    unit = null,
                ),
            ),
        ),
        ProfileDetailComponentDataSkillUIModel(
            id = "football",
            name = "Futbol",
            icon = "https://cdn-icons-png.flaticon.com/512/1165/1165187.png",
            isSelected = false,
            details = listOf(
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Şut Gücü",
                    value = "85",
                    unit = "km/s",
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Sprint Hızı",
                    value = "32",
                    unit = "km/s",
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Pozisyon",
                    value = "Forvet",
                    unit = null,
                ),
                ProfileDetailComponentDataSkillDetailUIModel(
                    title = "Forma Numarası",
                    value = "9",
                    unit = null,
                ),
            ),
        ),
    )
    AppTheme {
        AppThemeScaffold {
            ProfileDetailFeaturedSkills(
                state = ProfileScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenProfileRoute(
                        userId = null,
                    ),
                    selectedSkillState = skills.getOrNull(0),
                ),
                setEvent = {},
                title = "Öne Çıkan Özellikler",
                skills = skills,
            )
        }
    }
}

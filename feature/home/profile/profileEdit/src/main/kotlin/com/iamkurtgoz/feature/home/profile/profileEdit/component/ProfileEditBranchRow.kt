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
package com.iamkurtgoz.feature.home.profile.profileEdit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.chip.AppChip
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.BasicAppChipItem
import com.iamkurtgoz.feature.home.profile.profileEdit.ProfileEditScreenContract
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.mock.model.MockEditProfileSummaryUIModel

@Composable
internal fun ProfileEditBranchRow(
    state: ProfileEditScreenContract.State,
    branches: List<BranchItemUIModel>?,
    setEvent: (ProfileEditScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingMedium),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = AppTheme.spacing.spacingMedium,
        ),
    ) {
        itemsIndexed(
            items = branches ?: emptyList(),
            key = { index, item ->
                "$index - $item"
            },
        ) { index, item ->
            AppChip.ProfileBranch(
                item = BasicAppChipItem(
                    title = item.branchTitle,
                ),
                leftContent = {
                    AppAsyncImageLoader.Load(
                        data = item.branchImage,
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
                    setEvent.invoke(ProfileEditScreenContract.Event.SetSelectedBranchId(item.branchId))
                },
                isSelected = state.selectedBranchId == item.branchId,
            )
        }

        item {
            AppChip.ProfileBranch(
                item = BasicAppChipItem(
                    title = "Branş Ekle", // Todo: Localize
                ),
                leftContent = {
                    AppAsyncImageLoader.Load(
                        data = resourcesR.drawable.img_plus,
                        modifier = Modifier
                            .padding(end = AppTheme.dimens.dp8)
                            .size(AppTheme.dimens.dp24),
                    )
                },
                modifier = Modifier
                    .padding(vertical = AppTheme.spacing.spacingSmallest)
                    .padding(start = AppTheme.spacing.spacingSmall),
                onClick = {
                    setEvent.invoke(ProfileEditScreenContract.Event.OnClickAddBranch)
                },
                isSelected = false,
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileEditBranchRow(
                state = ProfileEditScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    profileSummaryModel = MockEditProfileSummaryUIModel.mockEditProfileSummary,
                ),
                setEvent = { },
                branches = MockEditProfileSummaryUIModel.mockEditProfileSummary.highlights?.branches,
            )
        }
    }
}

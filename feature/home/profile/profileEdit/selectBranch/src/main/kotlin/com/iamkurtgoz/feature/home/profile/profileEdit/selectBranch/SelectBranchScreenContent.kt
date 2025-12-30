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
package com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.selectableCard.SelectableCard
import com.iamkurtgoz.core.commonui.share.ShareUIUserBranchPage
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.domain.model.BranchesItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.domain.model.BranchesUIModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

@Composable
internal fun SelectBranchScreenContent(
    state: SelectBranchScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectBranchScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        ShareUIUserBranchPage(
            itemList = state.branchesList?.branches?.toPersistentList() ?: persistentListOf(),
            rowContent = { index, item ->
                SelectableCard.Primary(
                    index = index,
                    isSelected = item == state.selectedBranch,
                    imageData = item.branchImage,
                    text = item.branchTitle ?: "",
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .padding(start = if (index % 2 == AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall)
                        .padding(end = if (index % 2 != AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall),
                    onSelect = {
                        setEvent.invoke(SelectBranchScreenContract.Event.SetSelectedBranch(item))
                    },
                )
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectBranchScreenContent(
                state = SelectBranchScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    branchesList = BranchesUIModel(
                        branches = listOf(
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

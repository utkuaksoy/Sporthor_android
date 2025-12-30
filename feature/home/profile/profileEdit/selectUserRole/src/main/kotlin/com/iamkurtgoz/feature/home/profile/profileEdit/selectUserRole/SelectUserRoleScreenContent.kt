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
package com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.selectableCard.SelectableCard
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteGridList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectUserRoleScreenContent(
    state: SelectUserRoleScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectUserRoleScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Column(
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                contentAlignment = Alignment.CenterStart,
            ) {
                AppCircleButton.SecondaryGrayLarge(
                    icon = resourcesR.drawable.img_back_arrow,
                    onClick = {
                        setEvent.invoke(SelectUserRoleScreenContract.Event.NavigateUp)
                    },
                )
            }

            Text(
                text = "Rolünü Seç", // TODO: Localize
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )

            Text(
                text = "En az birini seçerek güncelleme işlemi yapabilirsiniz", // TODO: Localize
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textDisabled,
            )

            state.userRoleTypeList?.let {
                InfiniteGridList(
                    itemList = state.userRoleTypeList,
                    columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_TWO),
                    contentPadding = PaddingValues(
                        vertical = AppTheme.spacing.spacingMedium,
                    ),
                    rowContent = { index, item ->
                        SelectableCard.Primary(
                            index = index,
                            isSelected = state.selectedUserRolesTypeList.contains(item),
                            imageData = item.detail,
                            text = item.name ?: "",
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .padding(start = if (index % 2 == AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall)
                                .padding(end = if (index % 2 != AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall),
                            onSelect = {
                                val event = SelectUserRoleScreenContract.Event.SetSelectedUserRoleType(type = item)
                                setEvent.invoke(event)
                            },
                        )
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
            SelectUserRoleScreenContent(
                state = SelectUserRoleScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

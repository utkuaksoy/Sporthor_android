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
package com.iamkurtgoz.feature.home.customizeUserInfo.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.selectableCard.SelectableCard
import com.iamkurtgoz.core.commonui.share.ShareUIUserBranchPage
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.customizeUserInfo.CustomizeUserInfoScreenContract
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UserBranchPage(
    state: CustomizeUserInfoScreenContract.State,
    setEvent: (CustomizeUserInfoScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShareUIUserBranchPage(
        modifier = modifier,
        title = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_sport_category_title),
        subTitle = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_sport_category_sub_title),
        itemList = state.userBranchesTypeList ?: persistentListOf(),
        rowContent = { index, item ->
            SelectableCard.Primary(
                index = index,
                isSelected = state.selectedUserBranchesTypeList.contains(item),
                imageData = item.detail,
                text = item.name ?: "",
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(start = if (index % 2 == AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall)
                    .padding(end = if (index % 2 != AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall),
                onSelect = {
                    val event = CustomizeUserInfoScreenContract.Event.SetSelectedUserBranchType(type = item)
                    setEvent.invoke(event)
                },
            )
        },
    )
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            UserBranchPage(
                state = CustomizeUserInfoScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = {},
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.profile.userRelation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.navigation.HomeScreenProfileUserRelationRoute
import com.iamkurtgoz.feature.home.profile.userRelation.UserRelationScreenContract

@Composable
internal fun UserRelationSegments(
    state: UserRelationScreenContract.State,
    setEvent: (UserRelationScreenContract.Event) -> Unit,
    followerCount: String?,
    followingCount: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.spacing.spacingSmall),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserRelationItem(
            count = followerCount ?: "0",
            label = "Takipçi", // TODO: Localize
            selectedTabIndex = state.selectedTabIndex,
            index = AppDefaults.ZERO,
            setEvent = setEvent,
        )

        UserRelationItem(
            count = followingCount ?: "0",
            label = "Takip", // TODO: Localize
            selectedTabIndex = state.selectedTabIndex,
            index = AppDefaults.ONE,
            setEvent = setEvent,
        )
    }
}

@Composable
private fun UserRelationItem(
    count: String,
    label: String,
    selectedTabIndex: Int,
    index: Int,
    modifier: Modifier = Modifier,
    setEvent: (UserRelationScreenContract.Event) -> Unit = {},
) {
    val isSelected = remember(selectedTabIndex) { index == selectedTabIndex }
    Column(
        modifier = modifier
            .clickable {
                setEvent.invoke(UserRelationScreenContract.Event.SetSelectedTabIndex(index))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = AppTheme.spacing.spacingSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(end = AppTheme.spacing.spacingSmall),
                text = count,
                style = AppTheme.typography.subtitleSmall,
            )
            Text(
                text = label,
                style = AppTheme.typography.subtitleSmall,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .width(AppTheme.configuration.getHalfScreenWidthDp()),
            thickness = if (!isSelected) AppTheme.dimens.dp0dot5 else AppTheme.dimens.dp2,
            color = if (!isSelected) AppTheme.colors.generalColors.borderSoft200 else AppTheme.colors.generalColors.borderStrong900,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            UserRelationSegments(
                state = UserRelationScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigationRoute = HomeScreenProfileUserRelationRoute(
                        userRelationFollowingCount = 100,
                        userRelationFollowerCount = 100,
                        userId = "123",
                        userName = "",
                    ),
                ),
                setEvent = {},
                followerCount = "100",
                followingCount = "100",
            )
        }
    }
}

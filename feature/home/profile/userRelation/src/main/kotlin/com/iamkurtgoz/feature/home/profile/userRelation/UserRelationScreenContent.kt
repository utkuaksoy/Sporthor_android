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
package com.iamkurtgoz.feature.home.profile.userRelation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenProfileUserRelationRoute
import com.iamkurtgoz.feature.home.profile.userRelation.component.UserRelationFriends

@Composable
internal fun UserRelationScreenContent(
    state: UserRelationScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (UserRelationScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val users = when (state.selectedTabIndex) {
            AppDefaults.ZERO -> state.followersList?.users
            AppDefaults.ONE -> state.followingList?.users
            else -> state.followersList?.users
        }
        UserRelationFriends(
            users = users ?: arrayListOf(),
            setEvent = setEvent,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            UserRelationScreenContent(
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
            )
        }
    }
}

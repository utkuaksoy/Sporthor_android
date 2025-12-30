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
package com.iamkurtgoz.feature.home.customizeUserInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.indicator.IndicatorView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.customizeUserInfo.component.CoachRolesPage
import com.iamkurtgoz.feature.home.customizeUserInfo.component.UserBranchPage
import com.iamkurtgoz.feature.home.customizeUserInfo.component.UserInfosPage
import com.iamkurtgoz.feature.home.customizeUserInfo.component.UserRolesPage
import com.iamkurtgoz.feature.home.customizeUserInfo.component.WelcomePage
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.types.CustomizePageType

@Composable
internal fun CustomizeUserInfoScreenContent(
    state: CustomizeUserInfoScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CustomizeUserInfoScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        state.configurationModel?.let { configurationModel ->
            IndicatorView.Primary(
                totalPages = configurationModel.pageList.size,
                currentPage = state.currentPageIndex?.index ?: AppDefaults.ZERO,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        when (state.currentPageIndex) {
            CustomizePageType.Welcome -> WelcomePage()
            CustomizePageType.UserBranchPage -> UserBranchPage(
                state = state,
                setEvent = setEvent,
            )
            CustomizePageType.UserRoleCategory -> UserRolesPage(
                state = state,
                setEvent = setEvent,
            )
            CustomizePageType.CoachRoleCategory -> CoachRolesPage(
                state = state,
                setEvent = setEvent,
            )
            CustomizePageType.UserInfo -> UserInfosPage(
                state = state,
                setEvent = setEvent,
            )
            else -> Unit
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CustomizeUserInfoScreenContent(
                state = CustomizeUserInfoScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

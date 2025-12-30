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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.core.resources.R as resourceR
import com.iamkurtgoz.feature.home.profile.ProfileScreenContract

@Composable
internal fun ProfileTopBar(
    state: ProfileScreenContract.State,
    setEvent: (ProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.dp48)
            .padding(start = AppTheme.spacing.spacingMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = state.profileModel?.info?.username ?: "",
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL),
            style = AppTheme.typography.heading06,
        )

        IconButton(
            onClick = {
                setEvent.invoke(ProfileScreenContract.Event.NavigateToSettings)
            },
            content = {
                Image(
                    painter = painterResource(resourceR.drawable.img_setting),
                    contentDescription = "setting",
                    modifier = Modifier
                        .size(AppTheme.dimens.dp18),
                )
            },
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileTopBar(
                state = ProfileScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenProfileRoute(
                        userId = null,
                    ),
                ),
                setEvent = {},
            )
        }
    }
}

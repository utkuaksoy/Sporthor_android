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
package com.iamkurtgoz.feature.home.share.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.share.ShareScreenContract

@Composable
internal fun Header(
    state: ShareScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ShareScreenContract.Event) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.dp48),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(resourcesR.drawable.img_close),
            contentDescription = "",
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp24)
                .clip(AppTheme.shapes.radiusCircle)
                .clickable {
                    setEvent.invoke(ShareScreenContract.Event.NavigateUp)
                },
            colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
        )

        val title = remember {
            when (state.navigateRoute.routeType) {
                HomeScreenShareRouteScreenNavigateModel.CreateStory -> "Hikaye Ekle" // TODO: Localize
                HomeScreenShareRouteScreenNavigateModel.CreatePost -> "Yeni Gönderi" // TODO: Localize
            }
        }

        Text(
            text = title,
            style = AppTheme.typography.subtitleLarge,
            color = AppTheme.colors.generalColors.foregroundWhite,
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            maxLines = AppDefaults.LINE_LIMIT_SINGLE,
            textAlign = TextAlign.Center,
        )

        if (state.navigateRoute.routeType == HomeScreenShareRouteScreenNavigateModel.CreateStory) {
            Spacer(
                modifier = Modifier
                    .width(AppTheme.dimens.dp48),
            )
        } else {
            Text(
                text = "İleri", // TODO: Localize
                style = AppTheme.typography.subtitleLarge,
                color = AppTheme.colors.generalColors.primaryGreen,
                modifier = Modifier
                    .padding(end = AppTheme.spacing.spacingMedium)
                    .clickable {
                        setEvent.invoke(ShareScreenContract.Event.NavigateToShareCompleteScreen)
                    },
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.generalColors.foregroundPrimary)
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(resourcesR.drawable.img_share_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )

            Column {
                Header(
                    state = ShareScreenContract.State(
                        isLoading = true,
                        appBuildConfigStatePack = AppBuildConfigStatePack(),
                        appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                        navigateRoute = HomeScreenShareRoute(
                            routeType = HomeScreenShareRouteScreenNavigateModel.CreatePost,
                        ),
                    ),
                    setEvent = {},
                )

                Header(
                    state = ShareScreenContract.State(
                        isLoading = true,
                        appBuildConfigStatePack = AppBuildConfigStatePack(),
                        appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                        navigateRoute = HomeScreenShareRoute(
                            routeType = HomeScreenShareRouteScreenNavigateModel.CreateStory,
                        ),
                    ),
                    setEvent = {},
                )
            }
        }
    }
}

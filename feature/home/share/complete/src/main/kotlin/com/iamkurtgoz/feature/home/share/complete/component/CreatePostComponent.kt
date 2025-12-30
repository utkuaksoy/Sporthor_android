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
package com.iamkurtgoz.feature.home.share.complete.component

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.state.keyboardVisibility
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.imeAndStatusBarPadding
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.share.complete.CompleteScreenContract

@OptIn(UnstableApi::class)
@Composable
internal fun CreatePostComponent(
    pagerState: PagerState,
    exoPlayer: ExoPlayer?,
    state: CompleteScreenContract.State,
    setEvent: (CompleteScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isKeyboardShow by keyboardVisibility()

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL),
        ) {
            item {
                Header(
                    state = state,
                    setEvent = setEvent,
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(AppDefaults.ASPECT_RATIO_0_75)
                        .background(AppTheme.colors.generalColors.foregroundDisabled),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(AppDefaults.ASPECT_RATIO_0_75)
                            .background(AppTheme.colors.generalColors.foregroundDisabled),
                        state = pagerState,
                    ) { index ->
                        val media = state.navigateRoute.routeType.selectedMediaList[index]
                        if (media.customMediaType == CustomMediaType.VIDEO) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                AndroidView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(AppDefaults.ASPECT_RATIO_SQUARE)
                                        .background(AppTheme.colors.generalColors.foregroundDisabled),
                                    factory = { ctx ->
                                        PlayerView(ctx).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                            )
                                            useController = true
                                            player = exoPlayer
                                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                        }
                                    },
                                )

                                if (exoPlayer?.playbackState == Player.STATE_BUFFERING) {
                                    CircularProgressIndicator(
                                        color = AppTheme.colors.generalColors.foregroundWhite,
                                        modifier = Modifier.size(AppTheme.dimens.dp36),
                                    )
                                }
                            }
                        } else {
                            AppAsyncImageLoader.Load(
                                data = media.uri,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(AppDefaults.ASPECT_RATIO_0_75),
                            )
                        }
                    }

                    if (state.navigateRoute.routeType.selectedMediaList.size > AppDefaults.ONE) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppTheme.spacing.spacingSmallest),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            state.navigateRoute.routeType.selectedMediaList.forEachIndexed { index, _ ->
                                key(index) {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = AppTheme.spacing.spacingTiny)
                                            .size(AppTheme.dimens.dp8)
                                            .background(
                                                color = if (index == pagerState.currentPage) AppTheme.colors.generalColors.foregroundWhite else AppTheme.colors.generalColors.foregroundSecondary,
                                                shape = AppTheme.shapes.radiusCircle,
                                            ),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                CustomTextField(
                    state = state,
                    setEvent = setEvent,
                )
            }
        }

        AppButton.PrimaryLarge(
            text = "Paylaş", // TODO: Localize
            modifier = Modifier
                .fillMaxWidth()
                .imeAndStatusBarPadding(isKeyboardShow = isKeyboardShow)
                .padding(vertical = AppTheme.spacing.spacingMedium)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            onClick = {
                setEvent(CompleteScreenContract.Event.Share)
            },
        )
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
        }

        CreatePostComponent(
            pagerState = rememberPagerState { AppDefaults.ONE },
            exoPlayer = null,
            state = CompleteScreenContract.State(
                isLoading = true,
                appBuildConfigStatePack = AppBuildConfigStatePack(),
                appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                navigateRoute = HomeScreenShareCompleteRoute(
                    routeType = HomeScreenShareCompleteScreenNavigateModel(
                        shareType = HomeScreenShareCompleteShareTypeScreenNavigateModel.CreateStory,
                        selectedMediaList = listOf(
                            HomeScreenShareCompleteScreenNavigateModelMediaItem(
                                customMediaType = CustomMediaType.IMAGE,
                                uri = Uri.EMPTY,
                            ),
                        ),
                    ),
                ),
            ),
            setEvent = { },
        )
    }
}

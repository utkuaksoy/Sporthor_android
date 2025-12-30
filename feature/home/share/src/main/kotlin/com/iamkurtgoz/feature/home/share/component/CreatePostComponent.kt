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

import android.provider.MediaStore
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.resources.R
import com.iamkurtgoz.feature.home.share.ShareScreenContract
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import kotlinx.coroutines.flow.flowOf

@Suppress("LongMethod")
@OptIn(UnstableApi::class)
@Composable
internal fun CreatePostComponent(
    isCameraPermissionGranted: Boolean,
    isRecordAudioPermissionGranted: Boolean,
    pagerState: PagerState,
    exoPlayer: ExoPlayer?,
    state: ShareScreenContract.State,
    setEvent: (ShareScreenContract.Event) -> Unit,
    localMediaPagingFlow: LazyPagingItems<LocalMediaUIModel>,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
    updateCameraPermissionClicked: (cameraPermissionClicked: Boolean) -> Unit = {},
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_THREE),
        modifier = modifier
            .fillMaxSize(),
    ) {
        item(
            span = {
                GridItemSpan(maxLineSpan)
            },
        ) {
            Header(
                state = state,
                setEvent = setEvent,
            )
        }

        item(
            span = {
                GridItemSpan(maxLineSpan)
            },
        ) {
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
                    state.selectedLocalMediaModels.getOrNull(index)?.let { media ->
                        key(media.id) {
                            if (media.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                ) {
                                    AndroidView(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(AppDefaults.ASPECT_RATIO_0_75)
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
                                    contentDescription = media.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(AppDefaults.ASPECT_RATIO_0_75),
                                )
                            }
                        }
                    }
                }

                if (state.selectedLocalMediaModels.size > AppDefaults.ONE) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.spacing.spacingSmallest),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        state.selectedLocalMediaModels.fastForEachIndexed { index, _ ->
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

        item(
            span = {
                GridItemSpan(maxLineSpan)
            },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Galeri", // TODO: Localize
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.foregroundWhite,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingMedium),
                )

                Spacer(
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                )

                Image(
                    painter = painterResource(if (state.isSwitchedMultipleSelect) R.drawable.img_stack_filled else R.drawable.img_stack),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = AppTheme.spacing.spacingMedium)
                        .size(AppTheme.dimens.dp20)
                        .clickable {
                            setEvent.invoke(ShareScreenContract.Event.SetSwitchedMultipleSelect(!state.isSwitchedMultipleSelect))
                        },
                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                )

                Image(
                    painter = painterResource(R.drawable.img_camera),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = AppTheme.spacing.spacingMedium)
                        .size(AppTheme.dimens.dp20)
                        .clickable {
                            if (!isCameraPermissionGranted && !isRecordAudioPermissionGranted) {
                                updateCameraPermissionClicked.invoke(true)
                            } else {
                                setEvent.invoke(ShareScreenContract.Event.NavigateToCameraXScreen)
                            }
                        },
                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                )
            }
        }

        items(
            count = localMediaPagingFlow.itemCount,
            key = { index ->
                localMediaPagingFlow.peek(index)?.id ?: index
            },
        ) { index ->
            localMediaPagingFlow[index]?.let { media ->
                MediaCard(
                    state = state,
                    media = media,
                    modifier = Modifier
                        .clickable {
                            if (state.isSwitchedMultipleSelect) {
                                if (state.selectedLocalMediaModels.any { item -> item == media }) {
                                    setEvent.invoke(ShareScreenContract.Event.RemoveSelectedLocalMediaModel(media))
                                } else {
                                    setEvent.invoke(ShareScreenContract.Event.AddSelectedLocalMediaModel(media))
                                }
                            } else {
                                setEvent.invoke(ShareScreenContract.Event.SetSelectedLocalMediaModel(media))
                            }
                        },
                )
            }
        }

        localMediaPagingFlow.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            AppLoadingView()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        },
                    ) {
                        AppButton.PrimaryMedium(
                            text = "Tekrar Dene", // TODO: Localize,
                            onClick = {
                                localMediaPagingFlow.retry()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.spacing.spacingMedium),
                        )
                    }
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.generalColors.foregroundPrimary)
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(R.drawable.img_share_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )

            CreatePostComponent(
                isCameraPermissionGranted = false,
                isRecordAudioPermissionGranted = false,
                pagerState = rememberPagerState { AppDefaults.ONE },
                exoPlayer = null,
                state = ShareScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenShareRoute(
                        routeType = HomeScreenShareRouteScreenNavigateModel.CreatePost,
                    ),
                ),
                setEvent = { },
                localMediaPagingFlow = flowOf(PagingData.from(emptyList<LocalMediaUIModel>())).collectAsLazyPagingItems(),
                lazyGridState = rememberLazyGridState(),
                updateCameraPermissionClicked = {},
            )
        }
    }
}

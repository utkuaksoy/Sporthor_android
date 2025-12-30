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

import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.media3.common.util.UnstableApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.share.ShareScreenContract
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import kotlinx.coroutines.flow.flowOf

@Suppress("LongMethod")
@OptIn(UnstableApi::class)
@Composable
internal fun CreateStoryComponent(
    isCameraPermissionGranted: Boolean,
    isRecordAudioPermissionGranted: Boolean,
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
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.spacing.spacingMedium)
                    .height(AppTheme.dimens.dp80)
                    .clip(shape = AppTheme.shapes.radiusDoubleExtraLarge)
                    .clickable {
                        if (!isCameraPermissionGranted && !isRecordAudioPermissionGranted) {
                            updateCameraPermissionClicked.invoke(true)
                        } else {
                            setEvent.invoke(ShareScreenContract.Event.NavigateToCameraXScreen)
                        }
                    },
                shape = AppTheme.shapes.radiusDoubleExtraLarge,
                color = AppTheme.colors.generalColors.transparentWhite,
                border = BorderStroke(
                    width = AppTheme.dimens.dp1,
                    color = AppTheme.colors.generalColors.foregroundSecondary,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(resourcesR.drawable.img_camera),
                        contentDescription = "camera",
                        modifier = Modifier
                            .size(AppTheme.dimens.dp24),
                        colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                    )

                    Text(
                        text = "Kamera", // TODO: Localize
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.generalColors.foregroundWhite,
                    )
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
                        .fillMaxWidth()
                        .padding(start = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingMedium),
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
                            setEvent.invoke(ShareScreenContract.Event.SetSelectedLocalMediaModel(media))
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
                painter = painterResource(resourcesR.drawable.img_share_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )

            CreateStoryComponent(
                isCameraPermissionGranted = false,
                isRecordAudioPermissionGranted = false,
                state = ShareScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenShareRoute(
                        routeType = HomeScreenShareRouteScreenNavigateModel.CreateStory,
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

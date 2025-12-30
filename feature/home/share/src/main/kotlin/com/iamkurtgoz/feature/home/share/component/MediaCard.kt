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

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.extension.randomColor
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.feature.home.share.ShareScreenContract
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel

@Composable
internal fun MediaCard(
    state: ShareScreenContract.State,
    media: LocalMediaUIModel,
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.radiusNone,
    color: Color = AppTheme.colors.generalColors.backgroundSecondary,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(AppDefaults.ASPECT_RATIO_SQUARE),
        shape = shape,
        color = color,
        shadowElevation = AppTheme.dimens.dp0,
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(media.uri),
                contentDescription = media.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            if (state.isSwitchedMultipleSelect) {
                if (state.selectedLocalMediaModels.any { it.id == media.id }) {
                    Box(
                        modifier = Modifier
                            .padding(all = AppTheme.spacing.spacingSmall)
                            .size(AppTheme.dimens.dp24)
                            .background(
                                color = AppTheme.colors.generalColors.foregroundBlack,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = state.selectedLocalMediaModels.indexOf(media).plus(AppDefaults.ONE).toString(),
                            style = AppTheme.typography.subtitleSmall,
                            color = AppTheme.colors.generalColors.foregroundWhite,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(all = AppTheme.spacing.spacingSmall)
                            .size(AppTheme.dimens.dp24)
                            .background(
                                color = AppTheme.colors.generalColors.backgroundPrimary,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle)
                            .border(
                                width = AppTheme.dimens.dp1,
                                color = AppTheme.colors.generalColors.borderSub300,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .align(Alignment.TopEnd),
                    )
                }
            }

            if (media.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                Box(
                    modifier = Modifier
                        .size(AppTheme.dimens.dp36)
                        .background(
                            color = AppTheme.colors.generalColors.backgroundPrimary,
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .align(Alignment.Center),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Video",
                        modifier = Modifier
                            .size(AppTheme.dimens.dp24)
                            .align(Alignment.Center),
                        tint = AppTheme.colors.generalColors.foregroundBlack,
                    )
                }
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            LazyVerticalGrid(
                columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_THREE),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                itemsIndexed(
                    items = listOf("", "", "", "", "", "", "", "", "", "", "", ""),
                    key = { index, item ->
                        "$index$item"
                    },
                    itemContent = { index, _ ->
                        MediaCard(
                            state = ShareScreenContract.State(
                                isLoading = true,
                                appBuildConfigStatePack = AppBuildConfigStatePack(),
                                appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                                navigateRoute = HomeScreenShareRoute(
                                    routeType = HomeScreenShareRouteScreenNavigateModel.CreatePost,
                                ),
                                isSwitchedMultipleSelect = true,
                                selectedLocalMediaModels = listOf(
                                    LocalMediaUIModel(
                                        id = 1,
                                        mediaType = 1,
                                        name = "Display Name",
                                        dateAdded = 1,
                                        uri = Uri.EMPTY,
                                        mimeType = null,
                                        size = null,
                                        duration = null,
                                        directory = null,
                                        filepath = null,
                                    ),
                                ),
                            ),
                            media = LocalMediaUIModel(
                                id = index.toLong(),
                                mediaType = 3,
                                name = "Display Name",
                                dateAdded = 1,
                                uri = Uri.EMPTY,
                                mimeType = null,
                                size = null,
                                duration = null,
                                directory = null,
                                filepath = null,
                            ),
                            color = randomColor,
                        )
                    },
                )
            }
        }
    }
}

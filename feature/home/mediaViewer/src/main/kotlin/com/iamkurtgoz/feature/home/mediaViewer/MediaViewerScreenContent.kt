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
package com.iamkurtgoz.feature.home.mediaViewer

import android.util.Base64
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenMediaViewerRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.feature.home.mediaViewer.component.ImageViewer
import com.iamkurtgoz.feature.home.mediaViewer.component.PdfViewer
import com.iamkurtgoz.feature.home.mediaViewer.component.VideoViewer

@OptIn(UnstableApi::class)
@Composable
internal fun MediaViewerScreenContent(
    state: MediaViewerScreenContract.State,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        when (val routeType = state.navigateRoute.routeType) {
            is HomeScreenMediaViewerScreenNavigateModel.Base64 -> {
                when (routeType.signalRMessageType) {
                    SignalRMessageType.IMAGE -> {
                        item {
                            ImageViewer(
                                imageData = Base64.decode(routeType.base64, Base64.DEFAULT),
                            )
                        }
                    }
                    SignalRMessageType.VIDEO -> {
                        item {
                            VideoViewer(
                                videoBytes = Base64.decode(routeType.base64, Base64.DEFAULT),
                            )
                        }
                    }
                    SignalRMessageType.FILE -> {
                        if (routeType.extension == "pdf") {
                            item {
                                PdfViewer(
                                    base64 = routeType.base64,
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
            is HomeScreenMediaViewerScreenNavigateModel.RemoteOrLocalImage -> {
                item {
                    ImageViewer(
                        imageData = routeType.imageData,
                    )
                }
            }
            is HomeScreenMediaViewerScreenNavigateModel.RemoteVideo -> {
                item {
                    VideoViewer(
                        remoteUrl = routeType.videoUrl,
                    )
                }
            }
            is HomeScreenMediaViewerScreenNavigateModel.LocalVideo -> {
                item {
                    VideoViewer(
                        localUri = routeType.uriPath?.toUri(),
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            MediaViewerScreenContent(
                state = MediaViewerScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenMediaViewerRoute(
                        routeType = HomeScreenMediaViewerScreenNavigateModel.Base64(
                            base64 = "",
                            extension = "",
                            signalRMessageType = SignalRMessageType.TEXT,
                        ),
                    ),
                ),
            )
        }
    }
}

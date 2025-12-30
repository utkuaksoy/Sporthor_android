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
package com.iamkurtgoz.feature.home.share

import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.share.component.CreatePostComponent
import com.iamkurtgoz.feature.home.share.component.CreateStoryComponent
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@androidx.annotation.OptIn(UnstableApi::class)
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun ShareScreenContent(
    state: ShareScreenContract.State,
    setEvent: (ShareScreenContract.Event) -> Unit,
    localMediaPagingFlow: LazyPagingItems<LocalMediaUIModel>,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // Gallery Permission
    val storagePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val videoPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    LaunchedEffect(storagePermissionState.status.isGranted, videoPermissionState.status.isGranted) {
        if (!storagePermissionState.status.isGranted) {
            storagePermissionState.launchPermissionRequest()
        } else if (!videoPermissionState.status.isGranted) {
            videoPermissionState.launchPermissionRequest()
        } else {
            localMediaPagingFlow.refresh()
        }
    }

    // Camera
    var cameraPermissionClicked: Boolean by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val recordAudioPermissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    LaunchedEffect(cameraPermissionClicked, cameraPermissionState.status.isGranted, recordAudioPermissionState.status.isGranted) {
        if (cameraPermissionClicked) {
            if (!cameraPermissionState.status.isGranted) {
                cameraPermissionState.launchPermissionRequest()
            } else if (!recordAudioPermissionState.status.isGranted) {
                recordAudioPermissionState.launchPermissionRequest()
            } else {
                cameraPermissionClicked = false
                setEvent.invoke(ShareScreenContract.Event.NavigateToCameraXScreen)
            }
        }
    }

    // Pager
    val pagerState = rememberPagerState(
        pageCount = {
            state.selectedLocalMediaModels.size
        },
    )

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setLoadControl(DefaultLoadControl.Builder().build())
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }

    LaunchedEffect(state.selectedLocalMediaModels) {
        val mediaItems = state.selectedLocalMediaModels.mapNotNull { it.uri }.map { uri ->
            MediaItem.fromUri(uri)
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = !state.isSwitchedMultipleSelect && state.selectedLocalMediaModels.size == AppDefaults.ONE
    }

    @Suppress("TooGenericExceptionCaught")
    LaunchedEffect(pagerState.currentPage) {
        try {
            val currentIndex = pagerState.currentPage
            if (currentIndex >= AppDefaults.ZERO && currentIndex < exoPlayer.mediaItemCount) {
                exoPlayer.seekTo(currentIndex, AppDefaults.ZERO.toLong())
                val media = state.selectedLocalMediaModels.getOrNull(currentIndex)
                if (media?.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO && !state.isSwitchedMultipleSelect) {
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()
                } else {
                    exoPlayer.pause()
                }
            }
        } catch (e: Exception) {
            Timber.e("Video konumlandırma hatası: ${e.message}")
        }
    }

    LaunchedEffect(exoPlayer) {
        snapshotFlow { exoPlayer.playbackState }.collect { state ->
            when (state) {
                Player.STATE_READY -> {
                    Timber.d("Video hazır")
                }
                Player.STATE_ENDED -> {
                    Timber.d("Video bitti")
                }
                Player.STATE_BUFFERING -> {
                    Timber.d("Video yükleniyor")
                }
                Player.STATE_IDLE -> {
                    Timber.d("Player boşta")
                }
            }
        }
    }

    when (state.navigateRoute.routeType) {
        HomeScreenShareRouteScreenNavigateModel.CreatePost -> {
            CreatePostComponent(
                isCameraPermissionGranted = cameraPermissionState.status.isGranted,
                isRecordAudioPermissionGranted = recordAudioPermissionState.status.isGranted,
                pagerState = pagerState,
                exoPlayer = exoPlayer,
                state = state,
                setEvent = setEvent,
                localMediaPagingFlow = localMediaPagingFlow,
                lazyGridState = lazyGridState,
                modifier = modifier,
                updateCameraPermissionClicked = {
                    cameraPermissionClicked = it
                },
            )
        }
        HomeScreenShareRouteScreenNavigateModel.CreateStory -> {
            CreateStoryComponent(
                isCameraPermissionGranted = cameraPermissionState.status.isGranted,
                isRecordAudioPermissionGranted = recordAudioPermissionState.status.isGranted,
                state = state,
                setEvent = setEvent,
                localMediaPagingFlow = localMediaPagingFlow,
                lazyGridState = lazyGridState,
                modifier = modifier,
                updateCameraPermissionClicked = {
                    cameraPermissionClicked = it
                },
            )
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

            ShareScreenContent(
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
            )
        }
    }
}

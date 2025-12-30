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
package com.iamkurtgoz.feature.home.share.complete

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.share.complete.component.CreatePostComponent
import com.iamkurtgoz.feature.home.share.complete.component.CreateStoryComponent
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
internal fun CompleteScreenContent(
    state: CompleteScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CompleteScreenContract.Event) -> Unit,
) {
    val context: Context = LocalContext.current

    // Pager
    val pagerState = rememberPagerState(
        pageCount = {
            state.navigateRoute.routeType.selectedMediaList.size
        },
    )

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setLoadControl(DefaultLoadControl.Builder().build())
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }

    LaunchedEffect(state.navigateRoute.routeType.selectedMediaList) {
        val mediaItems = state.navigateRoute.routeType.selectedMediaList.mapNotNull { it.uri }.map { uri ->
            MediaItem.fromUri(uri)
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = false
    }

    @Suppress("TooGenericExceptionCaught")
    LaunchedEffect(pagerState.currentPage) {
        try {
            val currentIndex = pagerState.currentPage
            if (currentIndex >= AppDefaults.ZERO && currentIndex < exoPlayer.mediaItemCount) {
                exoPlayer.seekTo(currentIndex, AppDefaults.ZERO.toLong())
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

    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            exoPlayer.apply {
                stop()
                release()
            }
        }
    }

    when (state.navigateRoute.routeType.shareType) {
        is HomeScreenShareCompleteShareTypeScreenNavigateModel.CreatePost -> {
            CreatePostComponent(
                pagerState = pagerState,
                exoPlayer = exoPlayer,
                state = state,
                setEvent = setEvent,
                modifier = modifier,
            )
        }
        is HomeScreenShareCompleteShareTypeScreenNavigateModel.CreateStory -> {
            CreateStoryComponent(
                exoPlayer = exoPlayer,
                state = state,
                setEvent = setEvent,
                modifier = modifier,
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

            CompleteScreenContent(
                state = CompleteScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenShareCompleteRoute(
                        routeType = HomeScreenShareCompleteScreenNavigateModel(
                            shareType = HomeScreenShareCompleteShareTypeScreenNavigateModel.CreatePost,
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
}

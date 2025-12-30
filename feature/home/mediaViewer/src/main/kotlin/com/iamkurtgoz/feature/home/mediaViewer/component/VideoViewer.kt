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
package com.iamkurtgoz.feature.home.mediaViewer.component

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import java.io.File
import java.io.FileOutputStream

@UnstableApi
@Composable
fun VideoViewer(
    modifier: Modifier = Modifier,
    videoBytes: ByteArray? = null,
    localUri: Uri? = null,
    remoteUrl: String? = null,
    autoPlay: Boolean = true,
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    val videoUri = remember(videoBytes, localUri, remoteUrl) {
        when {
            videoBytes != null -> {
                val tempFile = File.createTempFile("video_", ".mp4", context.cacheDir).apply {
                    FileOutputStream(this).use { it.write(videoBytes) }
                    deleteOnExit()
                }
                Uri.fromFile(tempFile)
            }
            localUri != null -> localUri
            !remoteUrl.isNullOrEmpty() -> remoteUrl.toUri()
            else -> null
        }
    }

    DisposableEffect(videoUri) {
        if (videoUri != null) {
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            if (autoPlay) exoPlayer.play()
        }

        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { ctx ->
            PlayerView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                useController = true
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            }
        },
    )
}

@UnstableApi
@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            VideoViewer(
                remoteUrl = "",
            )
        }
    }
}

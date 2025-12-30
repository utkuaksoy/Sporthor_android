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
package com.iamkurtgoz.core.designsystem.component.image

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameMillis
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

object AppAsyncImageLoader {
    private const val VIDEO_FRAME_MILLIS = 1000L

    @Composable
    fun Load(
        data: Any?,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
        loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
        success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
        error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
        onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
        onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
        onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
        alignment: Alignment = Alignment.Center,
        contentScale: ContentScale = ContentScale.Fit,
        alpha: Float = DefaultAlpha,
        colorFilter: ColorFilter? = null,
        filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
        clipToBounds: Boolean = true,
    ) = Load(
        modifier = modifier,
        data = data,
        contentDescription = contentDescription,
        loading = loading,
        success = success,
        error = error,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,
        imageRequest = {
            it.build()
        },
    )

    @Composable
    fun Load(
        data: Any?,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
        loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
        success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
        error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
        onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
        onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
        onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
        alignment: Alignment = Alignment.Center,
        contentScale: ContentScale = ContentScale.Fit,
        alpha: Float = DefaultAlpha,
        colorFilter: ColorFilter? = null,
        filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
        clipToBounds: Boolean = true,
        imageRequest: (ImageRequest.Builder) -> ImageRequest,
    ) {
        val context = LocalContext.current
        val isPreview = LocalInspectionMode.current

        SubcomposeAsyncImage(
            model = imageRequest.invoke(
                imageRequest(
                    context = context,
                    data = data,
                    isPreview = isPreview,
                ),
            ),
            contentDescription = contentDescription,
            modifier = modifier,
            loading = loading,
            success = success,
            error = error,
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = onError,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
        )
    }

    private fun imageRequest(
        context: Context,
        data: Any?,
        isPreview: Boolean,
    ): ImageRequest.Builder {
        return ImageRequest.Builder(context)
            .data(data)
            .crossfade(!isPreview)
            .decoderFactory(VideoFrameDecoder.Factory())
            .videoFrameMillis(VIDEO_FRAME_MILLIS)
    }
}

@Preview
@Composable
private fun AppAsyncImageLoaderPreview() {
    AppTheme {
        AppThemeScaffold {
            Column(
                modifier = Modifier
                    .padding(all = AppTheme.dimens.dp16),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = "https://example.com/image.jpg",
                    contentDescription = null,
                )

                AppAsyncImageLoader.Load(
                    data = "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
                )
            }
        }
    }
}

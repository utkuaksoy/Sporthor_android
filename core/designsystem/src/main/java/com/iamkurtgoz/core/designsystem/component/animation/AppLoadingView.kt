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
package com.iamkurtgoz.core.designsystem.component.animation

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieComposition
import com.iamkurtgoz.core.designsystem.internal.PreviewAppDefault
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourceR

@Composable
fun AppLoadingView(
    modifier: Modifier = Modifier,
    size: Dp = AppTheme.dimens.dp32,
    shape: RoundedCornerShape = AppTheme.shapes.radiusDoubleExtraLarge,
    @RawRes res: Int = resourceR.raw.lottie_anim_loading,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    clipSpec: LottieClipSpec? = null,
    speed: Float = 1f,
    iterations: Int = LottieConstants.IterateForever,
    outlineMasksAndMattes: Boolean = false,
    applyOpacityToLayers: Boolean = false,
    enableMergePaths: Boolean = false,
    renderMode: RenderMode = RenderMode.AUTOMATIC,
    reverseOnRepeat: Boolean = false,
    maintainOriginalImageBounds: Boolean = false,
    dynamicProperties: LottieDynamicProperties? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    clipToCompositionBounds: Boolean = true,
) {
    Surface(
        modifier = modifier.size(size),
        shadowElevation = AppTheme.dimens.dp16,
        shape = shape,
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.spacingSmallest),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(res))

            LottieAnimation(
                composition = composition,
                isPlaying = isPlaying,
                restartOnPlay = restartOnPlay,
                clipSpec = clipSpec,
                speed = speed,
                iterations = iterations,
                outlineMasksAndMattes = outlineMasksAndMattes,
                applyOpacityToLayers = applyOpacityToLayers,
                enableMergePaths = enableMergePaths,
                renderMode = renderMode,
                reverseOnRepeat = reverseOnRepeat,
                maintainOriginalImageBounds = maintainOriginalImageBounds,
                dynamicProperties = dynamicProperties,
                alignment = alignment,
                contentScale = contentScale,
                clipToCompositionBounds = clipToCompositionBounds,
            )
        }
    }
}

@PreviewAppDefault
@Composable
private fun AppLoadingViewPreview() {
    AppTheme {
        Column {
            AppLoadingView(
                size = AppTheme.dimens.dp32,
            )
        }
    }
}

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
package com.iamkurtgoz.core.designsystem.component.progress

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppDefault
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
object AppProgressBar {
    @Composable
    fun ProgressBarLinear(
        progress: () -> Float,
        modifier: Modifier = Modifier,
        color: Color = ProgressIndicatorDefaults.linearColor,
        trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides AppTheme.dimens.dp0) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .then(modifier)
                    .height(AppTheme.dimens.dp4),
                color = color,
                trackColor = trackColor,
                strokeCap = strokeCap,
            )
        }
    }

    @Composable
    fun ProgressBarCircular(
        modifier: Modifier = Modifier,
        color: Color = ProgressIndicatorDefaults.circularColor,
        strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
        trackColor: Color = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides AppTheme.dimens.dp0) {
            CircularProgressIndicator(
                modifier = modifier,
                color = color,
                strokeWidth = strokeWidth,
                trackColor = trackColor,
                strokeCap = strokeCap,
            )
        }
    }
}

@Composable
@PreviewAppDefault
private fun AppProgressBarPreview() {
    AppTheme {
        AppProgressBar.ProgressBarLinear(
            progress = { 0.5f },
            modifier = Modifier,
        )

        AppProgressBar.ProgressBarCircular(
            modifier = Modifier
                .size(AppTheme.dimens.dp100),
        )
    }
}

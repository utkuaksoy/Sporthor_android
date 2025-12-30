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
package com.iamkurtgoz.core.commonui.component.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import kotlinx.coroutines.delay
import java.util.Locale

private object CountDownTimerStatic {
    const val DEFAULT_TOTAL_TIME: Float = 180f
    const val DELAY_ONE_SECOND: Long = 1000L
    const val DEFAULT_WARNING_THRESHOLD: Float = 60f
    const val TIMER_DURATION_MINUTES: Int = 60
    const val TIMER_DURATION_SECONDS: Int = 1
    const val TIMER_SECONDS_IN_MINUTE: Int = 60
    const val START_ANGLE = 270f
    const val FULL_CIRCLE_ANGLE = 360f
}

object CountDownTimer {
    @Composable
    fun Primary(
        totalTime: Float,
        remainingTime: Float,
        warningThreshold: Float,
        modifier: Modifier = Modifier,
        colors: TimerColors = CountDownTimerColors.primary(),
        sizes: TimerSizes = CountDownTimerSizes.primary(),
        borders: TimerBorders = CountDownTimerBorders.primary(),
        styles: TimerStyles = CountDownTimerStyles.primary(),
    ) = CountDownTimerImpl(
        totalTime = totalTime,
        remainingTime = remainingTime,
        warningThreshold = warningThreshold,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        borders = borders,
        styles = styles,
    )
}

@Composable
private fun CountDownTimerImpl(
    totalTime: Float,
    remainingTime: Float,
    warningThreshold: Float,
    colors: TimerColors,
    sizes: TimerSizes,
    borders: TimerBorders,
    styles: TimerStyles,
    modifier: Modifier = Modifier,
) {
    val coercedProgress by animateFloatAsState(
        targetValue = ((remainingTime / totalTime)).coerceIn(0f, 1f),
        animationSpec = tween(CountDownTimerStatic.TIMER_DURATION_SECONDS, easing = LinearEasing),
        label = "ProgressAnimation",
    )

    val remainingColor = if (remainingTime <= warningThreshold) colors.warningColor else colors.activeColor

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val stroke = with(LocalDensity.current) {
            Stroke(
                width = borders.strokeWidth.toPx(),
                cap = borders.strokeCap,
            )
        }
        Canvas(
            modifier = Modifier
                .semantics(mergeDescendants = true) {
                    progressBarRangeInfo = ProgressBarRangeInfo(coercedProgress, 0f..1f)
                }
                .size(sizes.height),
        ) {
            val sweep = coercedProgress * CountDownTimerStatic.FULL_CIRCLE_ANGLE

            drawDeterminateCircularIndicator(
                startAngle = CountDownTimerStatic.START_ANGLE,
                sweep = sweep,
                color = remainingColor,
                stroke = stroke,
            )
        }

        Text(
            text = String.format(
                Locale.getDefault(),
                "%d:%02d",
                (remainingTime.toLong() / CountDownTimerStatic.TIMER_DURATION_MINUTES),
                (remainingTime.toLong() / CountDownTimerStatic.TIMER_DURATION_SECONDS) % CountDownTimerStatic.TIMER_SECONDS_IN_MINUTE,
            ),
            style = styles.textStyle,
            color = colors.enabledContentColor,
        )
    }
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke,
    )
}

private fun DrawScope.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) = drawCircularIndicator(startAngle, sweep, color, stroke)

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    var remainingTime by remember { mutableFloatStateOf(CountDownTimerStatic.DEFAULT_TOTAL_TIME) }
    LaunchedEffect(key1 = remainingTime) {
        if (remainingTime > 0) {
            delay(CountDownTimerStatic.DELAY_ONE_SECOND)
            remainingTime -= AppDefaults.ONE
        }
    }

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.generalColors.backgroundPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CountDownTimer.Primary(
                totalTime = CountDownTimerStatic.DEFAULT_TOTAL_TIME,
                remainingTime = remainingTime,
                warningThreshold = CountDownTimerStatic.DEFAULT_WARNING_THRESHOLD,
            )
        }
    }
}

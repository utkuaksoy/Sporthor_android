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
package com.iamkurtgoz.core.designsystem.component.slider

import android.content.res.Configuration
import androidx.annotation.IntRange
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iamkurtgoz.core.designsystem.theme.AppTheme

object AppSlider {
    @Composable
    fun SliderPrimary(
        value: Float,
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        rightContent: (@Composable RowScope.() -> Unit)? = null,
        enabled: Boolean = true,
        valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        @IntRange(from = 0)
        steps: Int = 0,
        onValueChangeFinished: (() -> Unit)? = null,
        colors: SliderColors = SliderDefaults.colors(),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onValueChange: (Float) -> Unit = { },
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            modifier = modifier,
        ) {
            leftContent?.invoke(this)
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides AppTheme.dimens.dp0) {
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChangeFinished = onValueChangeFinished,
                    colors = colors,
                    interactionSource = interactionSource,
                )
            }
            rightContent?.invoke(this)
        }
    }

    @Composable
    fun SliderRangePrimary(
        value: ClosedFloatingPointRange<Float>,
        onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        rightContent: (@Composable RowScope.() -> Unit)? = null,
        enabled: Boolean = true,
        valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        steps: Int = 0,
        onValueChangeFinished: (() -> Unit)? = null,
        colors: SliderColors = SliderDefaults.colors(),
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            modifier = modifier,
        ) {
            leftContent?.invoke(this)
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides AppTheme.dimens.dp0) {
                RangeSlider(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChangeFinished = onValueChangeFinished,
                    colors = colors,
                )
            }
            rightContent?.invoke(this)
        }
    }
}

// Defaults
private const val PREVIEW_DEFAULT_STATE: Float = 0.5f
private const val PREVIEW_DEFAULT_MAX_VALUE: Float = 100f

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppSliderPreview() {
    var value by remember { mutableFloatStateOf(PREVIEW_DEFAULT_STATE) }
    var rangeSliderPosition by remember { mutableStateOf(0f..PREVIEW_DEFAULT_MAX_VALUE) }
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentColor = AppTheme.colors.generalColors.foregroundPrimary,
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                AppSlider.SliderPrimary(
                    value = value,
                    onValueChange = {
                        value = it
                    },
                )

                AppSlider.SliderRangePrimary(
                    value = rangeSliderPosition,
                    onValueChange = {
                        rangeSliderPosition = it
                    },
                )
            }
        }
    }
}

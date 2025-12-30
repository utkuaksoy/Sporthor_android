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
package com.iamkurtgoz.feature.home.camerax.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private object RecordCircleStatic {
    const val LONG_PRESS_DELAY: Long = 250
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
internal fun RecordCircle(
    takePhoto: () -> Unit,
    startVideoRecording: () -> Unit,
    stopVideoRecording: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = AppTheme.shapes.radiusCircle,
    defaultColor: Color = AppTheme.colors.generalColors.foregroundWhite,
    disabledColor: Color = AppTheme.colors.generalColors.borderSub300,
    longPressColor: Color = AppTheme.colors.generalColors.primitivesRed500,
    defaultBorderColor: Color = AppTheme.colors.generalColors.borderSub300,
    longPressBorderColor: Color = AppTheme.colors.generalColors.primitivesRed900,
) {
    val coroutineScope = rememberCoroutineScope()
    var isLongPress by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .size(AppTheme.dimens.dp64)
            .scale(if (isLongPress) AppDefaults.SCALE_1_2 else AppDefaults.SCALE_1)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isEnabled) {
                            if (!isLongPress) {
                                takePhoto.invoke()
                            }
                        }
                    },
                    onPress = {
                        if (isEnabled) {
                            val pressJob = coroutineScope.launch {
                                delay(RecordCircleStatic.LONG_PRESS_DELAY)
                                isLongPress = true
                                startVideoRecording.invoke()
                            }

                            try {
                                awaitRelease()
                                pressJob.cancel()
                                isLongPress = false
                                stopVideoRecording.invoke()
                            } catch (_: Exception) {
                                pressJob.cancel()
                                isLongPress = false
                                stopVideoRecording.invoke()
                            }
                        }
                    },
                )
            },
        shape = shape,
        color = if (!isEnabled) disabledColor else if (isLongPress) longPressColor else defaultColor,
        shadowElevation = AppTheme.dimens.dp1,
        border = BorderStroke(
            width = AppTheme.dimens.dp1,
            color = if (isLongPress) longPressBorderColor else defaultBorderColor,
        ),
    ) {
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = AppTheme.spacing.spacingMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                RecordCircle(
                    isEnabled = true,
                    takePhoto = {},
                    startVideoRecording = {},
                    stopVideoRecording = {},
                )

                RecordCircle(
                    isEnabled = false,
                    takePhoto = {},
                    startVideoRecording = {},
                    stopVideoRecording = {},
                )
            }
        }
    }
}

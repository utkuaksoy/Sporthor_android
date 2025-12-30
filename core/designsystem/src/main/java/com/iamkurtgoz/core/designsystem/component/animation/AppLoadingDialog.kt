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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourceR

@Composable
fun AppLoadingDialog(
    modifier: Modifier = Modifier,
    progress: Int? = null,
    size: Dp = AppTheme.dimens.dp56,
    shape: RoundedCornerShape = AppTheme.shapes.radiusPentaExtraLarge,
    @RawRes res: Int = resourceR.raw.lottie_anim_loading,
) {
    Dialog(
        onDismissRequest = {},
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            AppLoadingView(
                res = res,
                size = size,
                shape = shape,
            )

            progress?.let {
                Text(
                    text = "$progress%",
                    color = Color.Black,
                    style = AppTheme.typography.labelRegular.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun AppLoadingDialogPreview() {
    AppTheme {
        AppLoadingDialog(
            progress = 45,
        )
    }
}

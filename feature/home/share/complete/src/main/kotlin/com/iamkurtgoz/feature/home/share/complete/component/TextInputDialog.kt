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
package com.iamkurtgoz.feature.home.share.complete.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
internal fun TextInputDialog(
    onConfirm: (text: String, textColorInt: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialText: String = "",
    initialTextColor: Int? = null,
    onDismiss: () -> Unit = {},
) {
    var text by remember { mutableStateOf(initialText) }
    var textColor by remember { mutableIntStateOf(initialTextColor ?: Color.Black.toArgb()) }

    val colorPalette: List<Pair<Color, Color>> = listOf(
        Pair(
            first = Color.Black,
            second = Color.Red,
        ),
        Pair(
            first = Color.Red,
            second = Color.Black,
        ),
        Pair(
            first = Color.Green,
            second = Color.Black,
        ),
        Pair(
            first = Color.Blue,
            second = Color.Black,
        ),
        Pair(
            first = Color.Yellow,
            second = Color.Black,
        ),
        Pair(
            first = Color.Cyan,
            second = Color.Black,
        ),
        Pair(
            first = Color.Magenta,
            second = Color.Black,
        ),
        Pair(
            first = Color.Gray,
            second = Color.Red,
        ),
        Pair(
            first = Color.DarkGray,
            second = Color.Red,
        ),
    )

    AlertDialog(
        modifier = modifier,
        containerColor = AppTheme.colors.generalColors.foregroundWhite,
        onDismissRequest = onDismiss,
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Ekleyeceğiniz metni girin..", // TODO: Localize
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                Row(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth(),
                ) {
                    colorPalette.forEachIndexed { index, colorPair ->
                        Box(
                            modifier = Modifier
                                .ifTrue(index != AppDefaults.ZERO) {
                                    this.padding(start = AppTheme.spacing.spacingSmallest)
                                }
                                .ifTrue(index == colorPalette.lastIndex) {
                                    this.padding(end = AppTheme.spacing.spacingSmallest)
                                }
                                .size(AppTheme.dimens.dp32)
                                .clip(AppTheme.shapes.radiusCircle)
                                .background(colorPair.first)
                                .ifTrue(textColor == colorPair.first.toArgb()) {
                                    this.border(
                                        width = AppTheme.dimens.dp1,
                                        color = colorPair.second,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                }
                                .clickable {
                                    textColor = colorPair.first.toArgb()
                                },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = text.isNotEmpty(),
                onClick = {
                    onConfirm(text, textColor)
                },
            ) {
                Text(
                    text = if (initialText.isEmpty()) "Ekle" else "Güncelle", // TODO: Localize
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "İptal", // TODO: Localize
                )
            }
        },
    )
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            TextInputDialog(
                initialText = "",
                onConfirm = { _, _ -> },
                onDismiss = {},
            )
        }
    }
}

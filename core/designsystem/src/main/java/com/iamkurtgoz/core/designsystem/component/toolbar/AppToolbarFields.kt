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
package com.iamkurtgoz.core.designsystem.component.toolbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.extension.safeClickable
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

object AppToolbarFields {
    @Composable
    fun NavigateIcon(
        modifier: Modifier = Modifier,
        size: Dp = AppTheme.dimens.dp24,
        iconTint: Color = AppTheme.colors.generalColors.foregroundPrimary,
        onClick: () -> Unit = { },
    ) {
        IconButton(
            modifier = modifier
                .safeClickable(onClick = onClick),
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Navigate Button Arrow Back",
                modifier = Modifier
                    .size(size),
                tint = iconTint,
            )
        }
    }

    @Composable
    fun ImageIcon(
        @DrawableRes resId: Int,
        modifier: Modifier = Modifier,
        size: Dp = AppTheme.dimens.dp24,
        iconTint: Color = AppTheme.colors.generalColors.foregroundPrimary,
        onClick: () -> Unit = { },
    ) {
        IconButton(
            modifier = modifier
                .clickable(onClick = onClick),
            onClick = onClick,
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = "",
                modifier = Modifier
                    .size(size),
                colorFilter = ColorFilter.tint(iconTint),
            )
        }
    }

    @Composable
    fun Title(
        text: String,
        modifier: Modifier = Modifier,
        titleTextColor: Color = AppTheme.colors.generalColors.textPrimary,
    ) = TitleWithSubTitle(
        text = text, 
        subText = "", 
        modifier = modifier,
        titleTextColor = titleTextColor,
    )

    @Composable
    fun TitleWithSubTitle(
        text: String,
        subText: String,
        modifier: Modifier = Modifier,
        titleTextColor: Color = AppTheme.colors.generalColors.textPrimary,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.heading06,
                color = titleTextColor,
            )
            AnimatedVisibility(visible = subText.isNotEmpty()) {
                Text(
                    subText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                AppToolbarFields.Title(
                    text = "Title",
                )

                AppToolbarFields.TitleWithSubTitle(
                    text = "Title",
                    subText = "Sub Title",
                )
            }
        }
    }
}

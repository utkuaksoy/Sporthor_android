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
package com.iamkurtgoz.core.commonui.component.user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.designsystem.extension.safeClickable
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

object UserRowFields {
    @Composable
    fun CloseIcon(
        modifier: Modifier = Modifier,
        size: Dp = AppTheme.dimens.dp18,
        @DrawableRes icon: Int = resourcesR.drawable.img_close,
        iconTint: Color = AppTheme.colors.generalColors.contentSoft600,
        onClick: () -> Unit = { },
    ) {
        IconButton(
            modifier = modifier
                .safeClickable(onClick = onClick),
            onClick = onClick,
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "icon",
                modifier = Modifier
                    .size(size),
                colorFilter = ColorFilter.tint(iconTint),
            )
        }
    }
}

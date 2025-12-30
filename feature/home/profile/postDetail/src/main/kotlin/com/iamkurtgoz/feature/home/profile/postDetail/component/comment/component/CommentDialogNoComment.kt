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
package com.iamkurtgoz.feature.home.profile.postDetail.component.comment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun CommentDialogNoComment(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.dimens.dp72)
                .background(
                    color = AppTheme.colors.generalColors.backgroundWeak100,
                    shape = AppTheme.shapes.radiusCircle,
                )
                .clip(AppTheme.shapes.radiusCircle),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(resourcesR.drawable.img_comment_version_three),
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.dp32),
            )
        }

        Text(
            text = "Hiç yorum yok", // TODO: Localize
            style = AppTheme.typography.heading04,
            color = AppTheme.colors.generalColors.textDisabled,
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingMedium),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            CommentDialogNoComment()
        }
    }
}

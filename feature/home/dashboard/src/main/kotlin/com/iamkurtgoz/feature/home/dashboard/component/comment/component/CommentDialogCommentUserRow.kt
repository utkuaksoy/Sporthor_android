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
package com.iamkurtgoz.feature.home.dashboard.component.comment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R

@Composable
internal fun CommentDialogCommentUserRow(
    userHeaderData: Any?,
    userName: String?,
    comment: String?,
    modifier: Modifier = Modifier,
    headerSize: Dp = AppTheme.dimens.dp48,
    onClickAction: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickAction),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        UserImageView(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium),
            data = userHeaderData,
            size = headerSize,
        )

        Column(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL)
                .padding(start = AppTheme.spacing.spacingSmall),
            verticalArrangement = Arrangement.Center,
        ) {
            userName?.let {
                Text(
                    text = userName,
                    style = AppTheme.typography.subtitleSmall,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                comment?.let { comment ->
                    Text(
                        text = comment,
                        style = AppTheme.typography.helperText,
                        color = AppTheme.colors.generalColors.textSecondary,
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingSmallest),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = AppTheme.colors.generalColors.contentSoft600,
                                    shape = AppTheme.shapes.radiusCircle,
                                )
                                .size(AppTheme.dimens.dp2),
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.img_heart),
            contentDescription = "search",
            modifier = Modifier
                .padding(end = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp18)
                .alpha(AppDefaults.FLOAT_0),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                CommentDialogCommentUserRow(
                    userHeaderData = R.drawable.temp_img_profile_women,
                    userName = "Hakan Yılmaz",
                    comment = "Güzel Foto",
                )
            }
        }
    }
}

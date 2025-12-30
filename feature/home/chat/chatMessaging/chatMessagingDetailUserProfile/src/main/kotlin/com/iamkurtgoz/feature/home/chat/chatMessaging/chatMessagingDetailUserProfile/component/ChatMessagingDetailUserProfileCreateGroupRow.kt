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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.ChatMessagingDetailUserProfileScreenContract
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun ChatMessagingDetailUserProfileCreateGroupRow(
    modifier: Modifier = Modifier,
    setEvent: (ChatMessagingDetailUserProfileScreenContract.Event) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingLarge)
            .padding(horizontal = AppTheme.spacing.spacingMedium),
    ) {
        Text(
            modifier = Modifier
                .padding(end = AppTheme.spacing.spacingSmall)
                .clickable {
                    setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.Initialize)
                },
            text = buildAnnotatedString {
                append(
                    text = "Mehmet",
                )
                withStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.generalColors.textSecondary,
                        fontSize = AppTheme.dimens.sp14,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    block = {
                        append(" ile ortak grup yok") // TODO: Localize
                    },
                )
            },
            style = AppTheme.typography.subtitleSmall,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.spacingMedium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { },
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = AppTheme.spacing.spacingSmall),
                    painter = painterResource(resourcesR.drawable.img_add_circle),
                    contentDescription = null,
                )

                Text(
                    text = "Grup Oluştur", // TODO: Localize
                    style = AppTheme.typography.subtitleSmall,
                )
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ChatMessagingDetailUserProfileCreateGroupRow()
        }
    }
}

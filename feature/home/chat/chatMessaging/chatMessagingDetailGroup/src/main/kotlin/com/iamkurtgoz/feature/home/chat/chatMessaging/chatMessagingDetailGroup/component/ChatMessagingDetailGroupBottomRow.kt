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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.ChatMessagingDetailGroupScreenContract
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun ChatMessagingDetailGroupBottomRow(
    modifier: Modifier = Modifier,
    attachmentsCount: Int?,
    groupId: String? = null,
    setEvent: (ChatMessagingDetailGroupScreenContract.Event) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.dp56)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(resourcesR.drawable.img_image_version_three),
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )

            Text(
                text = "Medya ve belgeler", // TODO: Localize
                style = AppTheme.typography.subtitleSmall,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .clickable {
                        setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.NavigateToAttachments(userId = null, groupId = groupId))
                    }
                    .padding(horizontal = AppTheme.spacing.spacingSmall)
                    .weight(AppDefaults.WEIGHT_FULL),
            )

            Text(
                text = attachmentsCount.toString(), // TODO: Localize
                style = AppTheme.typography.subtitleSmall,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingSmall),
            )

            Image(
                painter = painterResource(resourcesR.drawable.img_arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.dp56)
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .clickable {
                    setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.LeaveChat)
                    setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.NavigateUp)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(resourcesR.drawable.img_comment_version_three),
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )

            Text(
                text = "Gruptan Ayrıl", // TODO: Localize
                style = AppTheme.typography.subtitleSmall,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingSmall)
                    .weight(AppDefaults.WEIGHT_FULL),
            )

            Image(
                painter = painterResource(resourcesR.drawable.img_arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )
        }

        HorizontalDivider()
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ChatMessagingDetailGroupBottomRow(
                attachmentsCount = 10,
            )
        }
    }
}

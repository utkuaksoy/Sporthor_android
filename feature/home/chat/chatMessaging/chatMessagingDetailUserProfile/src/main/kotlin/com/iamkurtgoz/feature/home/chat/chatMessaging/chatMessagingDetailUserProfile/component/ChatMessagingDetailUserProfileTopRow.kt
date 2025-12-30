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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.ChatMessagingDetailUserProfileScreenContract

@Composable
internal fun ChatMessagingDetailUserProfileTopRow(
    imageUrlData: Any?,
    followerCount: Int?,
    followingCount: Int?,
    userName: String?,
    postCount: Int?,
    isFollow: Boolean,
    userId: String?,
    setEvent: (ChatMessagingDetailUserProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserImageView(
            data = imageUrlData,
            size = AppTheme.dimens.dp72,
        )

        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingSmall),
            text = userName ?: "", // TODO: Localize
            style = AppTheme.typography.heading06,
        )

        Row(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(top = AppTheme.spacing.spacingSmall),
        ) {
            Text(
                modifier = Modifier
                    .padding(end = AppTheme.spacing.spacingSmall)
                    .clickable {
                        setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.Initialize)
                    },
                text = buildAnnotatedString {
                    append(
                        text = postCount.toString(),
                    )
                    withStyle(
                        style = SpanStyle(
                            color = AppTheme.colors.generalColors.textSecondary,
                            fontSize = AppTheme.dimens.sp14,
                            fontWeight = FontWeight.Normal,
                        ),
                        block = {
                            append(" Gönderi") // TODO: Localize
                        },
                    )
                },
                style = AppTheme.typography.subtitleSmall,
            )

            Text(
                modifier = Modifier
                    .padding(end = AppTheme.spacing.spacingSmall)
                    .clickable {
                        setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.Initialize)
                    },
                text = buildAnnotatedString {
                    append(
                        text = followerCount.toString(),
                    )
                    withStyle(
                        style = SpanStyle(
                            color = AppTheme.colors.generalColors.textSecondary,
                            fontSize = AppTheme.dimens.sp14,
                            fontWeight = FontWeight.Normal,
                        ),
                        block = {
                            append(" Takipçi") // TODO: Localize
                        },
                    )
                },
                style = AppTheme.typography.subtitleSmall,
            )

            Text(
                modifier = Modifier
                    .padding(end = AppTheme.spacing.spacingSmall)
                    .clickable {
                        setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.Initialize)
                    },
                text = buildAnnotatedString {
                    append(
                        text = followingCount.toString(),
                    )
                    withStyle(
                        style = SpanStyle(
                            color = AppTheme.colors.generalColors.textSecondary,
                            fontSize = AppTheme.dimens.sp14,
                            fontWeight = FontWeight.Normal,
                        ),
                        block = {
                            append(" Takip") // TODO: Localize
                        },
                    )
                },
                style = AppTheme.typography.subtitleSmall,
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(top = AppTheme.spacing.spacingHuge)
                .fillMaxWidth(),
        ) {
            if (isFollow) {
                AppButton.OutlineMedium(
                    text = "Takiptesin", // TODO: Localize
                    onClick = {
                        setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.OnClickActionButton(isFollow = isFollow, userId = userId))
                    },
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                )
            } else {
                AppButton.SecondaryMedium(
                    text = "Takip Et", // TODO: Localize
                    onClick = {
                        setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.OnClickActionButton(isFollow = isFollow, userId = userId))
                    },
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                )
            }

            AppButton.OutlineMedium(
                modifier = Modifier
                    .weight(AppDefaults.WEIGHT_FULL)
                    .padding(start = AppTheme.spacing.spacingSmallest),
                text = "Profili Görüntüle", // TODO: Localize
                onClick = {
                    setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.Initialize)
                },
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingLarge)
                .padding(horizontal = AppTheme.spacing.spacingMedium),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ChatMessagingDetailUserProfileTopRow(
                imageUrlData = "https://randomuser.me/api/portraits/men/32.jpg",
                setEvent = {},
                followerCount = 100,
                followingCount = 200,
                postCount = 100,
                userName = "",
                isFollow = false,
                userId = "",
            )
        }
    }
}

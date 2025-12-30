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
package com.iamkurtgoz.feature.home.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.ProfileScreenContract

@Composable
internal fun ProfileInfo(
    userId: String,
    imageUrlData: Any?,
    name: String?,
    postCount: Int?,
    followerCount: Int?,
    followingCount: Int?,
    setEvent: (ProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserImageView(
                data = imageUrlData ?: name.getUserNameFirstChar(),
                size = AppTheme.dimens.dp72,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = name ?: "",
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                    style = AppTheme.typography.subtitleLarge,
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingMedium)
                        .padding(top = AppTheme.spacing.spacingSmallest),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = AppTheme.spacing.spacingSmall),
                        text = buildAnnotatedString {
                            append(
                                text = "$postCount",
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
                                setEvent.invoke(ProfileScreenContract.Event.OnUserRelation(followingCount, followerCount, userId))
                            },
                        text = buildAnnotatedString {
                            append(
                                text = "$followerCount",
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
                                setEvent.invoke(ProfileScreenContract.Event.OnUserRelation(followingCount, followerCount, userId))
                            },
                        text = buildAnnotatedString {
                            append(
                                text = "$followingCount",
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
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileInfo(
                imageUrlData = "https://randomuser.me/api/portraits/men/32.jpg",
                name = "Test Name",
                postCount = 0,
                followerCount = 0,
                followingCount = 0,
                setEvent = {},
                userId = "",
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.chat.chatMessaging.component

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.feature.home.chat.chatMessaging.ChatMessagingScreenContract

@Composable
internal fun ChatRowIncoming(
    isParentMessageRow: Boolean,
    imageData: Any?,
    userName: String?,
    message: String?,
    time: String?,
    messageType: SignalRMessageType?,
    extension: String?,
    setEvent: (ChatMessagingScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        if (isParentMessageRow) {
            UserImageView(
                data = imageData ?: userName.getUserNameFirstChar(),
                backgroundColor = AppTheme.colors.generalColors.backgroundPrimary,
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )
        }

        Surface(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingSmall)
                .widthIn(max = AppTheme.configuration.getScreenWidthDp() * AppDefaults.WEIGHT_0_65),
            shape = AppTheme.shapes.radiusMedium,
            color = AppTheme.colors.generalColors.foregroundWhite,
            shadowElevation = AppTheme.dimens.dp4,
        ) {
            Column(
                modifier = Modifier
                    .padding(all = AppTheme.spacing.spacingSmall),
            ) {
                if (isParentMessageRow) {
                    Text(
                        text = userName ?: "",
                        style = AppTheme.typography.subtitleSmall.copy(
                            color = AppTheme.colors.generalColors.textPrimary,
                        ),
                    )
                }

                when (messageType) {
                    SignalRMessageType.TEXT -> {
                        Text(
                            text = message ?: "",
                            style = AppTheme.typography.bodyMediumCompact.copy(
                                color = AppTheme.colors.generalColors.textPrimary,
                            ),
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingTiny),
                        )
                    }
                    SignalRMessageType.IMAGE -> {
                        AppAsyncImageLoader.Load(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(AppDefaults.ASPECT_RATIO_SQUARE)
                                .clickable {
                                    val event = ChatMessagingScreenContract.Event.NavigateToMediaViewer(
                                        routeType = HomeScreenMediaViewerScreenNavigateModel.Base64(
                                            base64 = message ?: "",
                                            extension = extension ?: "",
                                            signalRMessageType = SignalRMessageType.IMAGE,
                                        ),
                                    )
                                    setEvent.invoke(event)
                                },
                            data = Base64.decode(message, Base64.DEFAULT),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    SignalRMessageType.VIDEO -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(AppDefaults.ASPECT_RATIO_SQUARE)
                                .clickable {
                                    val event = ChatMessagingScreenContract.Event.NavigateToMediaViewer(
                                        routeType = HomeScreenMediaViewerScreenNavigateModel.Base64(
                                            base64 = message ?: "",
                                            extension = extension ?: "",
                                            signalRMessageType = SignalRMessageType.VIDEO,
                                        ),
                                    )
                                    setEvent.invoke(event)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "play",
                            )
                        }
                    }
                    SignalRMessageType.FILE -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimens.dp48)
                                .clickable {
                                    val event = ChatMessagingScreenContract.Event.NavigateToMediaViewer(
                                        routeType = HomeScreenMediaViewerScreenNavigateModel.Base64(
                                            base64 = message ?: "",
                                            extension = extension ?: "",
                                            signalRMessageType = SignalRMessageType.FILE,
                                        ),
                                    )
                                    setEvent.invoke(event)
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp36)
                                    .background(
                                        color = AppTheme.colors.generalColors.backgroundWeak100,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                    .clip(AppTheme.shapes.radiusCircle),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = extension?.uppercase() ?: "",
                                    fontSize = AppTheme.dimens.sp12,
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            Text(
                                text = "Dosya", // TODO: Localize
                                fontSize = AppTheme.dimens.sp14,
                                modifier = Modifier
                                    .padding(start = AppTheme.spacing.spacingSmall),
                            )
                        }
                    }
                    null -> {
                    }
                }

                Text(
                    text = time ?: "",
                    style = AppTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = AppTheme.dimens.sp10,
                        color = AppTheme.colors.generalColors.contentSoft600,
                        textAlign = TextAlign.End,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.spacingTiny),
                )
            }
        }
    }
}

@Suppress("MaxLineLength")
@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                ChatRowIncoming(
                    isParentMessageRow = true,
                    imageData = resourcesR.drawable.temp_img_profile_women,
                    userName = "Cansu Bilgi",
                    message = "Selam kızlar, bugün antrenman öncesi buluşsak mı?",
                    time = "08:51",
                    messageType = SignalRMessageType.TEXT,
                    setEvent = {},
                    extension = null,
                )

                ChatRowIncoming(
                    isParentMessageRow = false,
                    imageData = resourcesR.drawable.temp_img_profile_women,
                    userName = "Cansu Bilgi",
                    message = "PHN2ZyB3aWR0aD0iMzgiIGhlaWdodD0iMzgiIHZpZXdCb3g9IjAgMCAzOCAzOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjM4IiBoZWlnaHQ9IjM4IiByeD0iMTkiIGZpbGw9IiNCMUZBNjMiLz4KPHBhdGggZD0iTTE5IDI1Ljc1TDE5IDEyLjI1TTE5IDEyLjI1TDI0LjI1IDE3LjVNMTkgMTIuMjVMMTMuNzUgMTcuNSIgc3Ryb2tlPSIjMTIxNTE1IiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+Cjwvc3ZnPgo=",
                    time = "08:51",
                    messageType = SignalRMessageType.IMAGE,
                    setEvent = {},
                    extension = null,
                )

                ChatRowIncoming(
                    isParentMessageRow = false,
                    imageData = resourcesR.drawable.temp_img_profile_women,
                    userName = "Cansu Bilgi",
                    message = "",
                    time = "08:51",
                    messageType = SignalRMessageType.VIDEO,
                    setEvent = {},
                    extension = null,
                )

                ChatRowIncoming(
                    isParentMessageRow = false,
                    imageData = resourcesR.drawable.temp_img_profile_women,
                    userName = "Cansu Bilgi",
                    message = "PHN2ZyB3aWR0aD0iMzgiIGhlaWdodD0iMzgiIHZpZXdCb3g9IjAgMCAzOCAzOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjM4IiBoZWlnaHQ9IjM4IiByeD0iMTkiIGZpbGw9IiNCMUZBNjMiLz4KPHBhdGggZD0iTTE5IDI1Ljc1TDE5IDEyLjI1TTE5IDEyLjI1TDI0LjI1IDE3LjVNMTkgMTIuMjVMMTMuNzUgMTcuNSIgc3Ryb2tlPSIjMTIxNTE1IiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+Cjwvc3ZnPgo=",
                    time = "08:51",
                    messageType = SignalRMessageType.FILE,
                    setEvent = {},
                    extension = "pdf",
                )
            }
        }
    }
}

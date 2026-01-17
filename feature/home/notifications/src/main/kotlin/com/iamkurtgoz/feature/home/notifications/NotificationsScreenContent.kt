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
package com.iamkurtgoz.feature.home.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.button.AppButtonColors
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.notifications.domain.model.GetNotificationsUIModel
import com.iamkurtgoz.feature.home.notifications.domain.model.GetNotificationsUIModelData
import com.iamkurtgoz.feature.home.notifications.domain.model.NotificationParamType
import com.iamkurtgoz.feature.home.notifications.domain.model.PushMessageType

@Composable
internal fun NotificationsScreenContent(
    state: NotificationsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (NotificationsScreenContract.Event) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        val buttons = mapOf(
            "Tümü" to NotifListFilterType.All,
            "Takip İsteği" to NotifListFilterType.Confirm,
            "Beğeniler" to NotifListFilterType.Like,
            "Etkinlikler" to NotifListFilterType.NewTask,
        )

        item {
            LazyRow(
                modifier = Modifier
                    .padding(vertical = AppTheme.spacing.spacingMedium),
            ) {
                items(count = buttons.size) { index ->
                    val item = buttons.entries.toList()[index]
                    Box(
                        modifier = Modifier
                            .ifTrue(index == 0) {
                                padding(start = AppTheme.spacing.spacingHuge)
                            }
                            .ifTrue(index != 0) {
                                padding(start = AppTheme.spacing.spacingRegular)
                            }
                            .ifTrue(index == buttons.entries.toList().lastIndex) {
                                padding(end = AppTheme.spacing.spacingHuge)
                            },
                    ) {
                        if (state.filterType == item.value) {
                            AppButton.PrimarySmall(
                                text = item.key,
                                onClick = {
                                    setEvent.invoke(NotificationsScreenContract.Event.SetFilterType(item.value))
                                },
                            )
                        } else {
                            AppButton.PrimarySmall(
                                text = item.key,
                                onClick = {
                                    setEvent.invoke(NotificationsScreenContract.Event.SetFilterType(item.value))
                                },
                                colors = AppButtonColors.primaryColors(
                                    enabledContainerColor = AppTheme.colors.generalColors.backgroundWeak100,
                                ),
                            )
                        }
                    }
                }
            }
        }

        itemsIndexed(
            items = when (state.filterType) {
                NotifListFilterType.All -> state.notificationList.sortedByDescending {
                    if (it.notificationType == NotificationParamType.CONFIRM) 1 else 0
                }
                NotifListFilterType.Confirm -> state.notificationList.filter {
                    it.notificationType == NotificationParamType.CONFIRM
                }
                NotifListFilterType.Like -> state.notificationList.filter {
                    it.pushMessageType == PushMessageType.LIKE
                }
                NotifListFilterType.NewTask -> state.notificationList.filter {
                    it.pushMessageType == PushMessageType.NEW_TASK
                }
            },
        ) { index, item ->
            NotificationRow(
                item = item,
                filterType = state.filterType,
                followRequestDecisionMap = state.followRequestDecisionMap,
                setEvent = setEvent,
            )

            if (index < state.notificationList.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
internal fun NotificationRow(
    item: GetNotificationsUIModel,
    filterType: NotifListFilterType,
    followRequestDecisionMap: Map<String, Boolean>,
    modifier: Modifier = Modifier,
    setEvent: (NotificationsScreenContract.Event) -> Unit,
) {
    val lowerMessage = item.message?.lowercase()
    val lowerTitle = item.title?.lowercase()
    val isFollowRequestConfirm = filterType == NotifListFilterType.Confirm &&
        ((lowerMessage?.contains("takip") == true && lowerMessage.contains("istiyor")) ||
            (lowerTitle?.contains("takipçi") == true))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.spacing.spacingSmall,
                horizontal = AppTheme.spacing.spacingMedium,
            ),
    ) {
        UserImageView(
            data = item.data?.profilePhoto ?: item.data?.username.getUserNameFirstChar(),
        )
        Spacer(Modifier.width(AppTheme.spacing.spacingMedium))

        if (isFollowRequestConfirm) {
            val decision = item.id?.let { followRequestDecisionMap[it] }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = AppTheme.spacing.spacingSmall,
                    ),
            ) {
                if (!item.title.isNullOrBlank()) {
                    Text(
                        text = item.title.orEmpty(),
                        style = AppTheme.typography.subtitleSmall,
                    )
                    Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                }
                if (decision != null) {
                    Text(
                        text = if (decision) {
                            "Takip isteğini kabul ettiniz."
                        } else {
                            "Takip isteğini reddettiniz."
                        },
                        style = AppTheme.typography.labelRegular,
                    )
                } else {
                    Text(
                        text = item.message.orEmpty(),
                        style = AppTheme.typography.labelRegular,
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = AppTheme.spacing.spacingSmall,
                            ),
                    ) {
                        AppButton.SecondarySmall(
                            text = "Kabul Et", // TODO: Localize,
                            onClick = {
                                val event = NotificationsScreenContract.Event.SendConfirmationFollow(
                                    notificationId = item.id,
                                    targetUserId = item.data?.userId,
                                    isAccepted = true,
                                )
                                setEvent.invoke(event)
                            },
                        )

                        AppButton.OutlineSmall(
                            text = "Reddet", // TODO: Localize,
                            onClick = {
                                val event = NotificationsScreenContract.Event.SendConfirmationFollow(
                                    notificationId = item.id,
                                    targetUserId = item.data?.userId,
                                    isAccepted = false,
                                )
                                setEvent.invoke(event)
                            },
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingSmall),
                        )
                    }
                }
            }
            return
        }

        when (item.pushMessageType) {
            PushMessageType.NEW_POST, PushMessageType.CHAT -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                        ),
                ) {
                    Text(
                        text = item.title.orEmpty(),
                        style = AppTheme.typography.subtitleSmall,
                    )
                    Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                    Text(
                        text = item.message.orEmpty(),
                        style = AppTheme.typography.labelRegular,
                    )
                }
            }
            PushMessageType.FOLLOW -> {
                val decision = item.id?.let { followRequestDecisionMap[it] }
                val shouldShowActions = filterType == NotifListFilterType.Confirm && decision == null
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                        ),
                ) {
                    if (decision != null) {
                        if (!item.title.isNullOrBlank()) {
                            Text(
                                text = item.title.orEmpty(),
                                style = AppTheme.typography.subtitleSmall,
                            )
                            Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                        }
                        Text(
                            text = if (decision) {
                                "Takip isteğini kabul ettiniz."
                            } else {
                                "Takip isteğini reddettiniz."
                            },
                            style = AppTheme.typography.labelRegular,
                        )
                    } else if (!item.title.isNullOrBlank()) {
                        Text(
                            text = item.title.orEmpty(),
                            style = AppTheme.typography.subtitleSmall,
                        )
                        Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                        Text(
                            text = item.message.orEmpty(),
                            style = AppTheme.typography.labelRegular,
                        )
                    } else {
                        Text(
                            text = item.message ?: "${item.data?.username.orEmpty()} seni takip etmeye başladı.",
                            style = AppTheme.typography.labelRegular,
                        )
                    }
                    if (shouldShowActions) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = AppTheme.spacing.spacingSmall,
                                ),
                        ) {
                            AppButton.SecondarySmall(
                                text = "Kabul Et", // TODO: Localize,
                                onClick = {
                                    val event = NotificationsScreenContract.Event.SendConfirmationFollow(
                                        notificationId = item.id,
                                        targetUserId = item.data?.userId,
                                        isAccepted = true,
                                    )
                                    setEvent.invoke(event)
                                },
                            )

                            AppButton.OutlineSmall(
                                text = "Reddet", // TODO: Localize,
                                onClick = {
                                    val event = NotificationsScreenContract.Event.SendConfirmationFollow(
                                        notificationId = item.id,
                                        targetUserId = item.data?.userId,
                                        isAccepted = false,
                                    )
                                    setEvent.invoke(event)
                                },
                                modifier = Modifier
                                    .padding(start = AppTheme.spacing.spacingSmall),
                            )
                        }
                    }
                }
            }
            PushMessageType.LIKE -> {
                Text(
                    text = "${item.data?.username.orEmpty()}, paylaşımını beğendi.",
                    style = AppTheme.typography.labelRegular,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                        ),
                )
            }
            PushMessageType.TRAINING_GROUP_REQUEST -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                        ),
                ) {
                    Text(
                        text = item.title.orEmpty(),
                        style = AppTheme.typography.subtitleSmall,
                    )
                    Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                    Text(
                        text = item.message.orEmpty(),
                        style = AppTheme.typography.labelRegular,
                    )
                    if (item.notificationType == NotificationParamType.CONFIRM) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = AppTheme.spacing.spacingSmall,
                                ),
                        ) {
                            AppButton.SecondarySmall(
                                text = "Kabul Et", // TODO: Localize,
                                onClick = {
                                    val event = NotificationsScreenContract.Event.SendConfirmationTrainingGroupUser(
                                        notificationId = item.id,
                                        groupId = item.data?.trainingGroupId,
                                        isAccepted = true,
                                    )
                                    setEvent.invoke(event)
                                },
                            )

                            AppButton.OutlineSmall(
                                text = "Reddet", // TODO: Localize,
                                onClick = {
                                    val event = NotificationsScreenContract.Event.SendConfirmationTrainingGroupUser(
                                        notificationId = item.id,
                                        groupId = item.data?.trainingGroupId,
                                        isAccepted = false,
                                    )
                                    setEvent.invoke(event)
                                },
                                modifier = Modifier
                                    .padding(start = AppTheme.spacing.spacingSmall),
                            )
                        }
                    }
                }
            }
            PushMessageType.NEW_TASK -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                        ),
                ) {
                    Text(
                        text = item.title.orEmpty(),
                        style = AppTheme.typography.subtitleSmall,
                    )
                    Spacer(Modifier.height(AppTheme.spacing.spacingTiny))
                    Text(
                        text = item.message.orEmpty(),
                        style = AppTheme.typography.labelRegular,
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val sample = listOf(
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "1", image = null,
            isDeleted = false, message = "Gönderisini hemen keşfet.",
            pushMessageType = PushMessageType.NEW_POST,
            notificationType = NotificationParamType.NOTIFICATION,
            sendDate = "", status = true, title = "Der Turke yeni bir gönderi paylaştı!",
            updatedAt = null, userId = "u1",
            data = GetNotificationsUIModelData(
                userId = "u1", username = "Der Turke", type = null,
                messageId = null, lastMessage = null, sendDate = null,
                unReadCount = null, senderName = null, imageURL = null,
                groupId = null, isGroup = null, toUserId = null,
                postId = null, profilePhoto = null, trainingGroupId = null,
            ),
        ),
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "2", image = null,
            isDeleted = false, message = "Gönderisini hemen keşfet.",
            pushMessageType = PushMessageType.NEW_POST,
            notificationType = NotificationParamType.NOTIFICATION,
            sendDate = "", status = true, title = "Der Turke yeni bir gönderi paylaştı!",
            updatedAt = null, userId = "u1",
            data = null,
        ),
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "3", image = null,
            isDeleted = false, message = null,
            pushMessageType = PushMessageType.FOLLOW,
            notificationType = NotificationParamType.NOTIFICATION,
            sendDate = "", status = true, title = null,
            updatedAt = null, userId = "u2",
            data = GetNotificationsUIModelData(
                userId = "u2", username = "Mehmet Kurtgöz", type = null,
                messageId = null, lastMessage = null, sendDate = null,
                unReadCount = null, senderName = null, imageURL = null,
                groupId = null, isGroup = null, toUserId = null,
                postId = null, profilePhoto = null, trainingGroupId = null,
            ),
        ),
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "4", image = null,
            isDeleted = false, message = null,
            pushMessageType = PushMessageType.FOLLOW,
            notificationType = NotificationParamType.NOTIFICATION,
            sendDate = "", status = true, title = null,
            updatedAt = null, userId = "u3",
            data = GetNotificationsUIModelData(
                userId = "u3", username = "Emre Öztürk", type = null,
                messageId = null, lastMessage = null, sendDate = null,
                unReadCount = null, senderName = null, imageURL = null,
                groupId = null, isGroup = null, toUserId = null,
                postId = null, profilePhoto = null, trainingGroupId = null,
            ),
        ),
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "5", image = null,
            isDeleted = false, message = null,
            pushMessageType = PushMessageType.LIKE,
            notificationType = NotificationParamType.NOTIFICATION,
            sendDate = "", status = true, title = null,
            updatedAt = null, userId = "u4",
            data = GetNotificationsUIModelData(
                userId = "u4", username = "Emre Öztürk", type = null,
                messageId = null, lastMessage = null, sendDate = null,
                unReadCount = null, senderName = null, imageURL = null,
                groupId = null, isGroup = null, toUserId = null,
                postId = null, profilePhoto = null, trainingGroupId = null,
            ),
        ),
        GetNotificationsUIModel(
            createdAt = "", deletedAt = null, id = "5", image = null,
            isDeleted = false, message = "adasda",
            pushMessageType = PushMessageType.TRAINING_GROUP_REQUEST,
            notificationType = NotificationParamType.CONFIRM,
            sendDate = "", status = true, title = "asdasda",
            updatedAt = null, userId = "u4",
            data = GetNotificationsUIModelData(
                userId = "u4", username = "Emre Öztürk", type = null,
                messageId = null, lastMessage = null, sendDate = null,
                unReadCount = null, senderName = null, imageURL = null,
                groupId = null, isGroup = null, toUserId = null,
                postId = null, profilePhoto = null, trainingGroupId = null,
            ),
        ),
    )

    AppTheme {
        AppThemeSurface {
            NotificationsScreenContent(
                state = NotificationsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    notificationList = sample,
                ),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.notifications.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.GetNotificationsDomainModel
import com.iamkurtgoz.feature.home.notifications.domain.model.GetNotificationsUIModel
import com.iamkurtgoz.feature.home.notifications.domain.model.GetNotificationsUIModelData
import com.iamkurtgoz.feature.home.notifications.domain.model.NotificationParamType
import com.iamkurtgoz.feature.home.notifications.domain.model.PushMessageType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNotificationsUIMapper @Inject constructor() : IMapper<GetNotificationsDomainModel, GetNotificationsUIModel> {
    override fun map(response: GetNotificationsDomainModel): GetNotificationsUIModel {
        return with(response) {
            GetNotificationsUIModel(
                createdAt = createdAt,
                deletedAt = deletedAt,
                id = id,
                image = image,
                isDeleted = isDeleted,
                message = message,
                pushMessageType = PushMessageType.fromInt(pushMessageType),
                notificationType = NotificationParamType.fromInt(notificationType),
                sendDate = sendDate,
                status = status,
                title = title,
                updatedAt = updatedAt,
                userId = userId,
                data = GetNotificationsUIModelData(
                    userId = data?.userId,
                    username = data?.username,
                    type = data?.type,
                    messageId = data?.messageId,
                    lastMessage = data?.lastMessage,
                    sendDate = data?.sendDate,
                    unReadCount = data?.unReadCount,
                    senderName = data?.senderName,
                    imageURL = data?.imageURL,
                    groupId = data?.groupId,
                    isGroup = data?.isGroup,
                    trainingGroupId = data?.trainingGroupId,
                    toUserId = data?.toUserId,
                    postId = data?.postId,
                    profilePhoto = data?.profilePhoto,
                ),
            )
        }
    }
}

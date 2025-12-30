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
package com.iamkurtgoz.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.data.model.GetNotificationsResponseModel
import com.iamkurtgoz.domain.model.response.GetNotificationsDomainModel
import com.iamkurtgoz.domain.model.response.GetNotificationsDomainModelData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNotificationsDomainMapper @Inject constructor() : IMapper<GetNotificationsResponseModel, List<GetNotificationsDomainModel>> {
    override fun map(response: GetNotificationsResponseModel): List<GetNotificationsDomainModel> {
        return response.notifications?.map { item ->
            GetNotificationsDomainModel(
                createdAt = item?.createdAt,
                deletedAt = item?.deletedAt,
                id = item?.id,
                image = item?.image,
                isDeleted = item?.isDeleted,
                message = item?.message,
                notificationType = item?.notificationType,
                pushMessageType = item?.pushMessageType,
                sendDate = item?.sendDate,
                status = item?.status,
                title = item?.title,
                updatedAt = item?.updatedAt,
                userId = item?.userId,
                data = GetNotificationsDomainModelData(
                    userId = item?.data?.userId,
                    username = item?.data?.username,
                    type = item?.data?.type,
                    messageId = item?.data?.messageId,
                    lastMessage = item?.data?.lastMessage,
                    sendDate = item?.data?.sendDate,
                    unReadCount = item?.data?.unReadCount,
                    senderName = item?.data?.senderName,
                    imageURL = item?.data?.imageURL,
                    groupId = item?.data?.groupId,
                    isGroup = item?.data?.isGroup,
                    trainingGroupId = item?.data?.trainingGroupId,
                    toUserId = item?.data?.toUserId,
                    postId = item?.data?.postId,
                    profilePhoto = item?.data?.profilePhoto,
                ),
            )
        } ?: emptyList()
    }
}

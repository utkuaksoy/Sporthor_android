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
package com.iamkurtgoz.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GetNotificationsResponseModel(
    @SerialName("notifications")
    val notifications: List<GetNotificationsResponseModelNotification?>?,
)

@Keep
@Serializable
data class GetNotificationsResponseModelNotification(
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("data")
    val data: GetNotificationsResponseModelNotificationData?,
    @SerialName("deletedAt")
    val deletedAt: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("isDeleted")
    val isDeleted: Boolean?,
    @SerialName("message")
    val message: String?,
    @SerialName("notificationType")
    val notificationType: Int?,
    @SerialName("pushMessageType")
    val pushMessageType: Int?,
    @SerialName("sendDate")
    val sendDate: String?,
    @SerialName("status")
    val status: Boolean?,
    @SerialName("title")
    val title: String?,
    @SerialName("updatedAt")
    val updatedAt: String?,
    @SerialName("userId")
    val userId: String?,
)

@Keep
@Serializable
data class GetNotificationsResponseModelNotificationData(
    @SerialName("userId")
    var userId: String?,
    @SerialName("username")
    var username: String?,
    @SerialName("type")
    var type: String?,
    @SerialName("messageId")
    var messageId: String?,
    @SerialName("lastMessage")
    var lastMessage: String?,
    @SerialName("sendDate")
    var sendDate: String?,
    @SerialName("unReadCount")
    var unReadCount: String?,
    @SerialName("senderName")
    var senderName: String?,
    @SerialName("imageURL")
    var imageURL: String?,
    @SerialName("groupId")
    var groupId: String?,
    @SerialName("isGroup")
    var isGroup: String?,
    @SerialName("trainingGroupId")
    var trainingGroupId: String?,
    @SerialName("toUserId")
    var toUserId: String?,
    @SerialName("postId")
    var postId: String?,
    @SerialName("profilePhoto")
    var profilePhoto: String?,
)

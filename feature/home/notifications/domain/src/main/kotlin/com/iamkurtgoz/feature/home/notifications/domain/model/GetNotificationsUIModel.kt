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
package com.iamkurtgoz.feature.home.notifications.domain.model

data class GetNotificationsUIModel(
    val createdAt: String?,
    val deletedAt: String?,
    val id: String?,
    val image: String?,
    val isDeleted: Boolean?,
    val message: String?,
    val pushMessageType: PushMessageType,
    val notificationType: NotificationParamType,
    val sendDate: String?,
    val status: Boolean?,
    val title: String?,
    val updatedAt: String?,
    val userId: String?,
    val data: GetNotificationsUIModelData?,
)

data class GetNotificationsUIModelData(
    var userId: String?,
    var username: String?,
    var type: String?,
    var messageId: String?,
    var lastMessage: String?,
    var sendDate: String?,
    var unReadCount: String?,
    var senderName: String?,
    var imageURL: String?,
    var groupId: String?,
    var trainingGroupId: String?,
    var isGroup: String?,
    var toUserId: String?,
    var postId: String?,
    var profilePhoto: String?,
)

enum class PushMessageType(val code: Int) {
    CHAT(0),
    LIKE(1),
    FOLLOW(2),
    NEW_POST(3),
    TRAINING_GROUP_REQUEST(4),
    NEW_TASK(5),
    ;

    companion object {
        fun fromInt(code: Int?): PushMessageType = entries.firstOrNull { it.code == code } ?: CHAT
    }
}

enum class NotificationParamType(val code: Int) {
    NOTIFICATION(0),
    CONFIRM(1),
    ;

    companion object {
        fun fromInt(code: Int?): NotificationParamType = entries.firstOrNull { it.code == code } ?: NOTIFICATION
    }
}

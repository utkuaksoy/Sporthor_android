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
package com.iamkurtgoz.domain.model.response

data class GetNotificationsDomainModel(
    val createdAt: String?,
    val deletedAt: String?,
    val id: String?,
    val image: String?,
    val isDeleted: Boolean?,
    val message: String?,
    val notificationType: Int?,
    val pushMessageType: Int?,
    val sendDate: String?,
    val status: Boolean?,
    val title: String?,
    val updatedAt: String?,
    val userId: String?,
    val data: GetNotificationsDomainModelData?,
)

data class GetNotificationsDomainModelData(
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

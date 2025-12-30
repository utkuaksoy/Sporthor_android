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

import java.time.LocalDateTime
import java.time.LocalTime

data class ChatAllMessageDomainModel(
    val messages: List<ChatAllMessageItemDomainModel>?,
)

data class ChatAllMessageItemDomainModel(
    val image: String?,
    val isGroup: Boolean?,
    val lastMessage: String?,
    val messageDate: LocalTime?,
    val messageDateLong: LocalDateTime?,
    val messageId: String?,
    val name: String?,
    val toUserId: String?,
    val unReadMessageCount: Int?,
    val userId: String?,
    val userName: String?,
)

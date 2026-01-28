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
import com.iamkurtgoz.domain.serializer.LocalDateTimeSerializer
import com.iamkurtgoz.domain.serializer.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

@Keep
@Serializable
data class ChatAllMessageResponseModel(
    @SerialName("messages") val messages: List<ChatAllMessageItemResponseModel>?,
)

@Keep
@Serializable
data class ChatAllMessageItemResponseModel(
    @SerialName("image") val image: String?,
    @SerialName("isGroup") val isGroup: Boolean?,
    @SerialName("lastMessage") val lastMessage: String?,
    @Serializable(with = LocalTimeSerializer::class)
    @SerialName("messageDate")
    val messageDate: LocalTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("messageDateLong")
    val messageDateLong: LocalDateTime?,
    @SerialName("shortDate") val shortDate: String?,
    @SerialName("messageId") val messageId: String?,
    @SerialName("name") val name: String?,
    @SerialName("toUserId") val toUserId: String?,
    @SerialName("unReadMessageCount") val unReadMessageCount: Int?,
    @SerialName("userId") val userId: String?,
    @SerialName("userName") val userName: String?,
)

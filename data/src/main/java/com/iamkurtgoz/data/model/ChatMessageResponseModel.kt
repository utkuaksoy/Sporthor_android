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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Keep
@Serializable
data class ChatMessageResponseModel(
    @SerialName("messages") val messages: List<ChatMessageItemResponseModel?>?,
    @SerialName("users") val users: List<ChatMessageUserItemResponseModel?>?,
)

@Keep
@Serializable
data class ChatMessageItemResponseModel(
    @SerialName("content") val content: String?,
    @SerialName("fileExtension") val fileExtension: String?,
    @SerialName("from") val from: ChatMessageItemFromResponseModel?,
    @SerialName("id") val id: String?,
    @SerialName("messageType") val messageType: Int?,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("sendDate")
    val sendDate: LocalDateTime?,
)

@Keep
@Serializable
data class ChatMessageItemFromResponseModel(
    @SerialName("id") val id: String?,
    @SerialName("image") val image: String?,
    @SerialName("name") val name: String?,
)

@Keep
@Serializable
data class ChatMessageUserItemResponseModel(
    @SerialName("id") val id: String?,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("isCurrentUser") val isCurrentUser: Boolean?,
    @SerialName("isFollow") val isFollow: Boolean?,
    @SerialName("name") val name: String?,
    @SerialName("summary") val summary: String?,
    @SerialName("username") val username: String?,
)

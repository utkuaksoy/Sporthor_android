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
data class CreatePostResponseModel(
    @SerialName("post")
    val post: PostsResponseModel?,
)

@Keep
@Serializable
data class PostsResponseModel(
    @SerialName("commentCount")
    val commentCount: Int?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("isLiked")
    val isLiked: Boolean?,
    @SerialName("lastComments")
    val lastComments: List<LastCommentResponseModel>?,
    @SerialName("lastLikedUsers")
    val lastLikedUsers: List<LastLikedUserResponseModel>?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("likeCount")
    val likeCount: Int?,
    @SerialName("media")
    val media: List<MediasResponseModel>?,
    @SerialName("name")
    val name: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("userId")
    val userId: String?,
    @SerialName("username")
    val username: String?,
)

@Keep
@Serializable
data class LastCommentResponseModel(
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("text")
    val text: String?,
    @SerialName("userId")
    val userId: String?,
    @SerialName("username")
    val username: String?,
)

@Keep
@Serializable
data class LastLikedUserResponseModel(
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("userId")
    val userId: String?,
    @SerialName("username")
    val username: String?,
)

@Keep
@Serializable
data class MediasResponseModel(
    @SerialName("type")
    val type: Int?,
    @SerialName("url")
    val url: String?,
)

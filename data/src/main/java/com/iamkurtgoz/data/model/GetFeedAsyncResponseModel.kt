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
data class GetFeedAsyncResponseModel(
    @SerialName("posts")
    val posts: List<DashboardPostResponseModel>? = null,
)

@Keep
@Serializable
data class DashboardPostResponseModel(
    @SerialName("id")
    val id: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("media")
    val media: List<DashboardPostMediaResponseModel>? = null,

    @SerialName("likeCount")
    val likeCount: Int? = null,

    @SerialName("commentCount")
    val commentCount: Int? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("userId")
    val userId: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("lastName")
    val lastName: String? = null,

    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,

    @SerialName("lastLikedUsers")
    val lastLikedUsers: List<DashboardPostLikedUserResponseModel>? = null,

    @SerialName("lastComments")
    val lastComments: List<DashboardPostCommentResponseModel>? = null,

    @SerialName("isLiked")
    val isLiked: Boolean? = null,

    @SerialName("time") val time: String? = null,
)

@Keep
@Serializable
data class DashboardPostMediaResponseModel(
    @SerialName("url")
    val url: String? = null,

    @SerialName("type")
    val type: Int? = null,
)

@Keep
@Serializable
data class DashboardPostLikedUserResponseModel(
    @SerialName("userId")
    val userId: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

@Keep
@Serializable
data class DashboardPostCommentResponseModel(
    @SerialName("id")
    val id: String? = null,

    @SerialName("userId")
    val userId: String? = null,

    @SerialName("text")
    val text: String? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

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

data class DashboardFeedDomainModel(
    val posts: List<DashboardPostDomainModel>? = null,
)

data class DashboardPostDomainModel(
    val id: String? = null,
    val description: String? = null,
    val media: List<DashboardPostMediaDomainModel>? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val createdAt: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val profileImageUrl: String? = null,
    val lastLikedUsers: List<DashboardPostLikedUserDomainModel>? = null,
    val lastComments: List<DashboardPostCommentDomainModel>? = null,
    val isLiked: Boolean? = null,
    val time: String?,
)

data class DashboardPostMediaDomainModel(
    val url: String? = null,
    val type: Int? = null,
)

data class DashboardPostLikedUserDomainModel(
    val userId: String? = null,
    val username: String? = null,
    val profileImageUrl: String? = null,
)

data class DashboardPostCommentDomainModel(
    val id: String? = null,
    val userId: String? = null,
    val text: String? = null,
    val createdAt: String? = null,
    val username: String? = null,
    val profileImageUrl: String? = null,
)

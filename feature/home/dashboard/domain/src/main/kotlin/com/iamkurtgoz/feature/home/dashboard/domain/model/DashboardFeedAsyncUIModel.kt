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
package com.iamkurtgoz.feature.home.dashboard.domain.model

import com.iamkurtgoz.domain.model.base.Listable

data class GetFeedAsyncUIModel(
    val posts: List<DashboardPostUIModel>? = null,
)

data class DashboardPostUIModel(
    val id: String?,
    val description: String?,
    val media: List<DashboardPostMediaUIModel>?,
    val likeCount: Int?,
    val commentCount: Int?,
    val createdAt: String?,
    val userId: String?,
    val username: String?,
    val name: String?,
    val lastName: String?,
    val profileImageUrl: String?,
    val lastLikedUsers: List<DashboardPostLikedUserUIModel>?,
    val lastComments: List<DashboardPostCommentUIModel>?,
    val isLiked: Boolean?,
    val time: String?,
    override val uuid: String? = id,
) : Listable {
    val commentPreviewList: List<Pair<String, String>>
        get() {
            val list: MutableList<Pair<String, String>> = mutableListOf()
            if (username != null && !description.isNullOrEmpty()) {
                list.add(
                    Pair(
                        first = username,
                        second = description,
                    ),
                )
            }
            lastComments?.forEach { comment ->
                if (comment.username != null && !comment.text.isNullOrEmpty()) {
                    list.add(
                        Pair(
                            first = comment.username,
                            second = comment.text,
                        ),
                    )
                }
            }
            return list
        }
}

data class DashboardPostMediaUIModel(
    val url: String? = null,
    val type: Int? = null,
)

data class DashboardPostLikedUserUIModel(
    val userId: String? = null,
    val username: String? = null,
    val profileImageUrl: String? = null,
)

data class DashboardPostCommentUIModel(
    val id: String? = null,
    val userId: String? = null,
    val text: String? = null,
    val createdAt: String? = null,
    val username: String? = null,
    val profileImageUrl: String? = null,
)

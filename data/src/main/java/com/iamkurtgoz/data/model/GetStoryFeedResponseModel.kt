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
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GetStoryFeedResponseModel(
    @SerialName("stories")
    val stories: List<StoryResponseModel>?,
)

@Keep
@Serializable
data class StoryResponseModel(
    @SerialName("userId")
    val userId: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("isOwn")
    val isOwn: Boolean?,
    @SerialName("isWatched")
    val isWatched: Boolean?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("details")
    val details: List<StoryDetailResponseModel>?,
)

@Keep
@Serializable
data class StoryDetailResponseModel(
    @SerialName("stroryId")
    val storyId: String?,
    @JsonNames("linkUrl", "externalUrl", "redirectUrl", "ctaUrl")
    @SerialName("link")
    val link: String? = null,
    @SerialName("linkDescription")
    val linkDescription: String? = null,
    @SerialName("media")
    val media: MediaResponseModel?,
    @SerialName("publishDate")
    val publishDate: String?,
    @SerialName("isWatched")
    val isWatched: Boolean?,
)

@Keep
@Serializable
data class MediaResponseModel(
    @SerialName("url")
    val url: String?,
    @SerialName("type")
    val type: Int?,
)

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
data class ProfileResponseModel(
    @SerialName("info") val info: ProfileInfoResponseModel?,
    @SerialName("components") val components: List<ProfileComponentResponseModel>?,
)

@Keep
@Serializable
data class ProfileInfoResponseModel(
    @SerialName("avatar") val avatar: String?,
    @SerialName("id") val id: String?,
    @SerialName("isCurrentUser") val isCurrentUser: Boolean?,
    @SerialName("name") val name: String?,
    @SerialName("username") val username: String?,
)

@Keep
@Serializable
data class ProfileComponentResponseModel(
    @SerialName("data") val data: ProfileComponentDataResponseModel?,
    @SerialName("id") val id: String?,
    @SerialName("type") val type: String?,
    @SerialName("typeId") val typeId: Int?,
)

@Keep
@Serializable
data class ProfileComponentDataResponseModel(
    @SerialName("buttons") val buttons: List<String>? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("extraInfo") val extraInfo: List<ProfileComponentDataExtraInfoResponseModel>? = null,
    @SerialName("followerCount") val followerCount: Int? = null,
    @SerialName("followingCount") val followingCount: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("matches") val matches: List<ProfileComponentDataMatchResponseModel>? = null,
    @SerialName("postCount") val postCount: Int? = null,
    @SerialName("segments") val segments: List<ProfileComponentDataSegmentResponseModel>? = null,
    @SerialName("selectedSegmentIndex") val selectedSegmentIndex: Int? = null,
    @SerialName("teams") val teams: List<ProfileComponentDataTeamResponseModel>? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("userId") val userId: String? = null,
    @SerialName("username") val username: String? = null,
)

@Keep
@Serializable
data class ProfileComponentDataExtraInfoResponseModel(
    @SerialName("icon") val icon: String?,
    @SerialName("key") val key: String?,
    @SerialName("value") val value: String?,
)

@Keep
@Serializable
data class ProfileComponentDataMatchResponseModel(
    @SerialName("matchId") val matchId: Int?,
    @SerialName("teams") val teams: List<ProfileComponentDataTeamResponseModel>?,
)

@Keep
@Serializable
data class ProfileComponentDataTeamResponseModel(
    @SerialName("teamId") val teamId: String?,
    @SerialName("teamLogoImageUrl") val teamLogoImageUrl: String?,
    @SerialName("teamName") val teamName: String?,
)

@Keep
@Serializable
data class ProfileComponentDataSegmentResponseModel(
    @SerialName("id") val id: String?,
    @SerialName("image") val image: String?,
    @SerialName("title") val title: String?,
    @SerialName("type") val type: String?,
)

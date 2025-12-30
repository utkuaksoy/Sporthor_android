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
package com.iamkurtgoz.feature.home.profile.domain.model

data class ProfileUIModel(
    val info: ProfileInfoUIModel?,
    val components: List<ProfileComponentUIModel>?,
)

data class ProfileInfoUIModel(
    val avatar: String?,
    val id: String?,
    val isCurrentUser: Boolean?,
    val name: String?,
    val username: String?,
)

data class ProfileComponentUIModel(
    val data: ProfileComponentDataUIModel?,
    val id: String?,
    val type: String?,
    val typeId: Int?,
)

data class ProfileComponentDataUIModel(
    val buttons: List<String>?,
    val description: String?,
    val extraInfo: List<ProfileComponentDataExtraInfoUIModel>?,
    val followerCount: Int?,
    val followingCount: Int?,
    val imageUrl: String?,
    val matches: List<ProfileComponentDataMatchUIModel>?,
    val postCount: Int?,
    val segments: List<ProfileComponentDataSegmentUIModel>?,
    val selectedSegmentIndex: Int?,
    val teams: List<ProfileComponentDataTeamUIModel>?,
    val title: String?,
    val userId: String?,
    val username: String?,
)

data class ProfileComponentDataExtraInfoUIModel(
    val icon: String?,
    val key: String?,
    val value: String?,
)

data class ProfileComponentDataMatchUIModel(
    val matchId: Int?,
    val teams: List<ProfileComponentDataTeamUIModel>?,
)

data class ProfileComponentDataTeamUIModel(
    val teamId: String?,
    val teamLogoImageUrl: String?,
    val teamName: String?,
)

data class ProfileComponentDataSegmentUIModel(
    val id: String?,
    val image: String?,
    val title: String?,
    val type: String?,
)

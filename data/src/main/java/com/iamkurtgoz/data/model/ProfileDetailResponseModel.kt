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
data class ProfileDetailResponseModel(
    @SerialName("personalId") val personalId: String?,
    @SerialName("components") val components: List<ProfileDetailComponentResponseModel?>?,
)

@Keep
@Serializable
data class ProfileDetailComponentResponseModel(
    @SerialName("data") val data: ProfileDetailComponentDataResponseModel?,
    @SerialName("id") val id: String?,
    @SerialName("type") val type: String?,
    @SerialName("typeId") val typeId: Int?,
)

@Keep
@Serializable
data class ProfileDetailComponentDataResponseModel(
    @SerialName("birthDate") val birthDate: String? = null,
    @SerialName("flagIcon") val flagIcon: String? = null,
    @SerialName("height") val height: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("items") val items: List<ProfileDetailComponentDataItemResponseModel?>? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("nationalityName") val nationalityName: String? = null,
    @SerialName("skills") val skills: List<ProfileDetailComponentDataSkillResponseModel?>? = null,
    @SerialName("teams") val teams: List<ProfileDetailComponentDataTeamResponseModel?>? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("tournaments") val tournaments: List<ProfileDetailComponentDataTournamentResponseModel?>? = null,
    @SerialName("weight") val weight: String? = null,
)

@Keep
@Serializable
data class ProfileDetailComponentDataItemResponseModel(
    @SerialName("endDate") val endDate: String?,
    @SerialName("startDate") val startDate: String?,
    @SerialName("teamLogoURL") val teamLogoURL: String?,
    @SerialName("teamName") val teamName: String?,
)

@Keep
@Serializable
data class ProfileDetailComponentDataSkillResponseModel(
    @SerialName("details") val details: List<ProfileDetailComponentDataSkillDetailResponseModel?>?,
    @SerialName("icon") val icon: String?,
    @SerialName("id") val id: String?,
    @SerialName("isSelected") val isSelected: Boolean?,
    @SerialName("name") val name: String?,
)

@Keep
@Serializable
data class ProfileDetailComponentDataSkillDetailResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("unit") val unit: String?,
    @SerialName("value") val value: String?,
)

@Keep
@Serializable
data class ProfileDetailComponentDataTeamResponseModel(
    @SerialName("teamId") val teamId: String?,
    @SerialName("teamLogoImageUrl") val teamLogoImageUrl: String?,
    @SerialName("teamName") val teamName: String?,
)

@Keep
@Serializable
data class ProfileDetailComponentDataTournamentResponseModel(
    @SerialName("icon") val icon: String?,
    @SerialName("stage") val stage: String?,
    @SerialName("stageIcon") val stageIcon: String?,
    @SerialName("title") val title: String?,
)

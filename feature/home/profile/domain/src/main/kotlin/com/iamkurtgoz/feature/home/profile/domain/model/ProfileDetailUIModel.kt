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

data class ProfileDetailUIModel(
    val personalId: String?,
    val components: List<ProfileDetailComponentUIModel?>?,
)

data class ProfileDetailComponentUIModel(
    val data: ProfileDetailComponentDataUIModel?,
    val id: String?,
    val type: String?,
    val typeId: Int?,
)

data class ProfileDetailComponentDataUIModel(
    val birthDate: String?,
    val flagIcon: String?,
    val height: String?,
    val imageUrl: String?,
    val items: List<ProfileDetailComponentDataItemUIModel?>?,
    val name: String?,
    val nationalityName: String?,
    val skills: List<ProfileDetailComponentDataSkillUIModel?>?,
    val teams: List<ProfileDetailComponentDataTeamUIModel?>?,
    val title: String?,
    val tournaments: List<ProfileDetailComponentDataTournamentUIModel?>?,
    val weight: String?,
)

data class ProfileDetailComponentDataItemUIModel(
    val endDate: String?,
    val startDate: String?,
    val teamLogoURL: String?,
    val teamName: String?,
)

data class ProfileDetailComponentDataSkillUIModel(
    val details: List<ProfileDetailComponentDataSkillDetailUIModel?>?,
    val icon: String?,
    val id: String?,
    val isSelected: Boolean?,
    val name: String?,
)

data class ProfileDetailComponentDataSkillDetailUIModel(
    val title: String?,
    val unit: String?,
    val value: String?,
)

data class ProfileDetailComponentDataTeamUIModel(
    val teamId: String?,
    val teamLogoImageUrl: String?,
    val teamName: String?,
)

data class ProfileDetailComponentDataTournamentUIModel(
    val icon: String?,
    val stage: String?,
    val stageIcon: String?,
    val title: String?,
)

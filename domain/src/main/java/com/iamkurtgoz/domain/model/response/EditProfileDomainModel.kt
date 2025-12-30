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

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EditProfileDomainModel(
    @SerialName("profileImage") val profileImage: String?,
    @SerialName("profileInfo") val profileInfo: EditProfileInfoDomainModel?,
    @SerialName("teamInfo") val teamInfo: TeamInfoDomainModel?,
    @SerialName("highlights") val highlights: HighlightItemDomainModel?,
)

@Keep
@Serializable
data class EditProfileInfoDomainModel(
    @SerialName("title") val title: String?,
    @SerialName("row") val row: List<ProfileRowDomainModel>?,
)

@Keep
@Serializable
data class ProfileRowDomainModel(
    @SerialName("title") val title: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("text") val text: String?,
    @SerialName("parameterName") val parameterName: String?,
    @SerialName("isRequired") val isRequired: Boolean?,
    @SerialName("type") val type: Int?,
)

@Keep
@Serializable
data class TeamInfoDomainModel(
    @SerialName("title") val title: String?,
    @SerialName("info") val info: String?,
    @SerialName("teams") val teams: List<TeamItemDomainModel>?,
)

@Keep
@Serializable
data class TeamItemDomainModel(
    @SerialName("teamImage") val teamImage: String?,
    @SerialName("teamName") val teamName: String?,
)

@Keep
@Serializable
data class HighlightItemDomainModel(
    @SerialName("title") val title: String?,
    @SerialName("branches") val branches: List<BranchItemDomainModel>?,
    @SerialName("branchesAttributes") val branchesAttributes: List<BranchesAttributeItemDomainModel>?,
)

@Keep
@Serializable
data class BranchItemDomainModel(
    @SerialName("branchImage") val branchImage: String?,
    @SerialName("branchTitle") val branchTitle: String?,
    @SerialName("branchId") val branchId: String?,
    @SerialName("isSelected") val isSelected: Boolean?,
)

@Keep
@Serializable
data class BranchesAttributeItemDomainModel(
    @SerialName("branchId") val branchId: String?,
    @SerialName("branchInfoRow") val branchInfoRow: List<BranchInfoRowDomainModel>?,
)

@Keep
@Serializable
data class BranchInfoRowDomainModel(
    @SerialName("title") val title: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("text") val text: String?,
    @SerialName("parameterName") val parameterName: String?,
    @SerialName("isRequired") val isRequired: Boolean?,
    @SerialName("type") val type: Int?,
)

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
data class EditProfileResponseModel(
    @SerialName("profileImage") val profileImage: String?,
    @SerialName("profileInfo") val profileInfo: EditProfileInfoResponseModel?,
    @SerialName("teamInfo") val teamInfo: TeamInfoResponseModel?,
    @SerialName("highlights") val highlights: HighlightItemResponseModel?,
)

@Keep
@Serializable
data class EditProfileInfoResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("row") val row: List<ProfileRowResponseModel>?,
)

@Keep
@Serializable
data class ProfileRowResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("text") val text: String?,
    @SerialName("parameterName") val parameterName: String?,
    @SerialName("isRequired") val isRequired: Boolean?,
    @SerialName("type") val type: Int?,
)

@Keep
@Serializable
data class TeamInfoResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("info") val info: String?,
    @SerialName("teams") val teams: List<TeamItemResponseModel>?,
)

@Keep
@Serializable
data class TeamItemResponseModel(
    @SerialName("teamImage") val teamImage: String?,
    @SerialName("teamName") val teamName: String?,
)

@Keep
@Serializable
data class HighlightItemResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("branches") val branches: List<BranchItemResponseModel>?,
    @SerialName("branchesAttributes") val branchesAttributes: List<BranchesAttributeItemResponseModel>?,
)

@Keep
@Serializable
data class BranchItemResponseModel(
    @SerialName("branchImage") val branchImage: String?,
    @SerialName("branchTitle") val branchTitle: String?,
    @SerialName("branchId") val branchId: String?,
    @SerialName("isSelected") val isSelected: Boolean?,
)

@Keep
@Serializable
data class BranchesAttributeItemResponseModel(
    @SerialName("branchId") val branchId: String?,
    @SerialName("branchInfoRow") val branchInfoRow: List<BranchInfoRowResponseModel>?,
)

@Keep
@Serializable
data class BranchInfoRowResponseModel(
    @SerialName("title") val title: String?,
    @SerialName("placeholder") val placeholder: String?,
    @SerialName("text") val text: String?,
    @SerialName("parameterName") val parameterName: String?,
    @SerialName("isRequired") val isRequired: Boolean?,
    @SerialName("type") val type: Int?,
)

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
data class GetTrainingGroupUserResponseModel(
    @SerialName("groups")
    val groups: List<GetTrainingGroupUserResponseModelGroup?>?,
)

@Keep
@Serializable
data class GetTrainingGroupUserResponseModelGroup(
    @SerialName("groupId")
    val groupId: String?,
    @SerialName("groupImage")
    val groupImage: String?,
    @SerialName("groupName")
    val groupName: String?,
    @SerialName("season")
    val season: String?,
    @SerialName("team")
    val team: GetTrainingGroupUserResponseModelTeam?,
    @SerialName("users")
    val users: List<GetTrainingGroupUserResponseModelUser?>?,
)

@Keep
@Serializable
data class GetTrainingGroupUserResponseModelTeam(
    @SerialName("detail")
    val detail: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("value")
    val value: String?,
)

@Keep
@Serializable
data class GetTrainingGroupUserResponseModelUser(
    @SerialName("id")
    val id: String?,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("isCurrentUser")
    val isCurrentUser: Boolean?,
    @SerialName("isFollow")
    val isFollow: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("username")
    val username: String?,
)

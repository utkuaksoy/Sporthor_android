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
data class SearchHistorySocialResponseModel(
    @SerialName("histories") val histories: List<SearchHistoryItemResponseModel>?,
)

@Keep
@Serializable
data class SearchHistoryItemResponseModel(
    @SerialName("id") val id: String?,
    @SerialName("status") val status: Boolean?,
    @SerialName("isDeleted") val isDeleted: Boolean?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("updatedAt") val updatedAt: String?,
    @SerialName("deletedAt") val deletedAt: String?,
    @SerialName("userId") val userId: String?,
    @SerialName("searchQuery") val searchQuery: String?,
    @SerialName("searchUser") val searchUser: SearchUserResponseModel?,
)

@Keep
@Serializable
data class SearchUserResponseModel(
    @SerialName("userId") val userId: String?,
    @SerialName("isTeam") val isTeam: Boolean?,
    @SerialName("image") val image: String?,
    @SerialName("name") val name: String?,
    @SerialName("summary") val summary: String?,
    @SerialName("userName") val userName: String?,
)

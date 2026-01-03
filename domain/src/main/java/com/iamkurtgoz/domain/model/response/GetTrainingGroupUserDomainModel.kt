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

data class GetTrainingGroupUserDomainModel(
    val groups: List<GetTrainingGroupUserDomainModelGroup?>?,
)

data class GetTrainingGroupUserDomainModelGroup(
    val groupId: String?,
    val groupImage: String?,
    val groupName: String?,
    val season: String?,
    val team: GetTrainingGroupUserDomainModelTeam?,
    val users: List<GetTrainingGroupUserDomainModelUser?>?,
    val coaches: List<GetTrainingGroupCoachDomainModelUser?>?,
)

data class GetTrainingGroupUserDomainModelTeam(
    val detail: String?,
    val name: String?,
    val value: String?,
)

data class GetTrainingGroupUserDomainModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
)

data class GetTrainingGroupCoachDomainModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
)

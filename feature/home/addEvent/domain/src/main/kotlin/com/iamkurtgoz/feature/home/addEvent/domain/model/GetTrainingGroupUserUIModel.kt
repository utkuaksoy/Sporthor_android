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
package com.iamkurtgoz.feature.home.addEvent.domain.model

data class GetTrainingGroupUserUIModel(
    val groups: List<GetTrainingGroupUserUIModelGroup?>?,
) {
    val allUsers: List<GetTrainingGroupUserUIModelUser>
        get() {
            val users: MutableSet<GetTrainingGroupUserUIModelUser> = mutableSetOf()
            groups?.forEach { group ->
                group?.users?.filterNotNull()?.forEach { user ->
                    users.add(user)
                }
            }
            return users.toList()
        }
}

data class GetTrainingGroupUserUIModelGroup(
    val groupId: String?,
    val groupImage: String?,
    val groupName: String?,
    val season: String?,
    val team: GetTrainingGroupUserUIModelTeam?,
    val users: List<GetTrainingGroupUserUIModelUser?>?,
    val coaches: List<GetTrainingGroupCoachUIModelUser?>?,
)

data class GetTrainingGroupUserUIModelTeam(
    val detail: String?,
    val name: String?,
    val value: String?,
)

data class GetTrainingGroupUserUIModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
) {
    fun isMatch(text: String): Boolean {
        return name?.contains(text, ignoreCase = true) == true || username?.contains(text, ignoreCase = true) == true
    }
}

data class GetTrainingGroupCoachUIModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
) {
    fun isMatch(text: String): Boolean {
        return name?.contains(text, ignoreCase = true) == true || username?.contains(text, ignoreCase = true) == true
    }
}

val mockTrainingGroupUserUIModel = GetTrainingGroupUserUIModel(
    groups = listOf(
        // 1. Grup
        GetTrainingGroupUserUIModelGroup(
            groupId = "6861986e08766379410d38bd",
            groupImage = "https://api.sporthor.com/Uploads/2f8423f9-fe02-4734-b0e9-2560bf92ded2.jpg",
            groupName = "Esref Tek Test Grup2",
            season = "2025-yaz",
            team = GetTrainingGroupUserUIModelTeam(
                detail = "https://api.sporthor.com/Uploads/2f8423f9-fe02-4734-b0e9-2560bf92ded2.jpg",
                name = "Fenerbahçe Test 3",
                value = "684ed12a5451af7c7d6772fd",
            ),
            users = listOf(
                GetTrainingGroupUserUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupUserUIModelUser(
                    id = "685c66d8e62dc93bcfc12e77",
                    name = "Emre Öztürk",
                    username = "emreozturk",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupUserUIModelUser(
                    id = "68611ddaa70665a7f6809091",
                    name = "Eşref Tek",
                    username = "esreftek",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupUserUIModelUser(
                    id = "6861983308766379410d38bc",
                    name = "Eşref Tek",
                    username = "esreftek1",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/f4531cbf-e44f-4a3f-850a-af0639935b9a.jpg",
                    isFollow = false,
                    isCurrentUser = true,
                ),
                // JSON içindeki tekrar eden kullanıcılar
                GetTrainingGroupUserUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupUserUIModelUser(
                    id = "685c66d8e62dc93bcfc12e77",
                    name = "Emre Öztürk",
                    username = "emreozturk",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
            ),
            coaches = listOf(
                GetTrainingGroupCoachUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupCoachUIModelUser(
                    id = "685c66d8e62dc93bcfc12e77",
                    name = "Emre Öztürk",
                    username = "emreozturk",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupCoachUIModelUser(
                    id = "68611ddaa70665a7f6809091",
                    name = "Eşref Tek",
                    username = "esreftek",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupCoachUIModelUser(
                    id = "6861983308766379410d38bc",
                    name = "Eşref Tek",
                    username = "esreftek1",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/f4531cbf-e44f-4a3f-850a-af0639935b9a.jpg",
                    isFollow = false,
                    isCurrentUser = true,
                ),
                // JSON içindeki tekrar eden kullanıcılar
                GetTrainingGroupCoachUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
                GetTrainingGroupCoachUIModelUser(
                    id = "685c66d8e62dc93bcfc12e77",
                    name = "Emre Öztürk",
                    username = "emreozturk",
                    summary = "",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
            ),
        ),

        // 2. Grup (users listesi boş)
        GetTrainingGroupUserUIModelGroup(
            groupId = "68659853f2e7bc3deb246beb",
            groupImage = "https://api.sporthor.com/Uploads/1dd57c11-631f-4161-82d3-f8d93dcc04f9.jpg",
            groupName = "USB U16 Grubu",
            season = "2025-2026",
            team = GetTrainingGroupUserUIModelTeam(
                detail = "https://api.sporthor.com/Uploads/1dd57c11-631f-4161-82d3-f8d93dcc04f9.jpg",
                name = "Fenerbahçe SK",
                value = "68658a727d304bb096270d4c",
            ),
            users = emptyList(),
            coaches = emptyList(),
        ),

        // 3. Grup
        GetTrainingGroupUserUIModelGroup(
            groupId = "6866b7f8bf72e00f237427d0",
            groupImage = "https://api.sporthor.com/Uploads/2f8423f9-fe02-4734-b0e9-2560bf92ded2.jpg",
            groupName = "2010-2011 Şampiyonları",
            season = "2025-2026",
            team = GetTrainingGroupUserUIModelTeam(
                detail = "https://api.sporthor.com/Uploads/2f8423f9-fe02-4734-b0e9-2560bf92ded2.jpg",
                name = "Fenerbahçe Test 3",
                value = "684ed12a5451af7c7d6772fd",
            ),
            users = listOf(
                GetTrainingGroupUserUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
            ),
            coaches = listOf(
                GetTrainingGroupCoachUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
            ),
        ),
    ),
)

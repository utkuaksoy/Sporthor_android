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
package com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model

import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.SocialSearchUIItemType

data class SocialSearchUIModel(
    val searchList: List<SocialSearchUIItemModel?>?,
)

data class SocialSearchUIItemModel(
    val id: String?,
    val image: String?,
    val name: String?,
    val attribute: String?,
    val type: SocialSearchUIItemType?,
    val role: InviteGroupMembersTab? = null,
)

enum class InviteGroupMembersTab {
    PLAYERS,
    STAFF,
}

object MockSocialSearchUIModel {
    val list = SocialSearchUIModel(
        searchList = listOf(
            SocialSearchUIItemModel(
                id = "team-01",
                image = "https://example.com/images/team_alpha.png",
                name = "Team Alpha",
                attribute = "Premier League",
                type = SocialSearchUIItemType.Team,
            ),
            SocialSearchUIItemModel(
                id = "user-42",
                image = "https://example.com/images/user_john.png",
                name = "John Doe",
                attribute = "Mobile Developer",
                type = SocialSearchUIItemType.User,
            ),
            SocialSearchUIItemModel(
                id = "team-02",
                image = "https://example.com/images/team_bravo.png",
                name = "Team Bravo",
                attribute = "La Liga",
                type = SocialSearchUIItemType.Team,
            ),
            SocialSearchUIItemModel(
                id = "user-99",
                image = null, // görseli olmayan kullanıcı
                name = "Jane Smith",
                attribute = "Designer",
                type = SocialSearchUIItemType.User,
            ),
        ),
    )
}

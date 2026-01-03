package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

data class SocialSearchUIModel(
    val searchList: List<SocialSearchUIItemModel?>?,
)

data class SocialSearchUIItemModel(
    val id: String?,
    val image: String?,
    val name: String?,
    val attribute: String?,
    val type: SocialSearchUIItemType?,
)

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

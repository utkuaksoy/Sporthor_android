package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain


enum class SocialSearchUIItemType(val value: String) {
    Team(value = "Team"),
    User(value = "User"),
    ;

    companion object {
        fun fromValue(value: String): SocialSearchUIItemType? {
            return when (value.lowercase()) {
                Team.value.lowercase() -> Team
                User.value.lowercase() -> User
                else -> null
            }
        }
    }
}

fun String?.toSocialSearchUIItemType(): SocialSearchUIItemType? {
    return this?.let { SocialSearchUIItemType.fromValue(it) }
}

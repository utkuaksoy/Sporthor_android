package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

enum class UserRelationUIItemType(val value: String) {
    FOLLOWING(value = "FOLLOWING"),
    FOLLOWERS(value = "FOLLOWERS"),
    ;

    companion object {
        fun fromValue(value: String): UserRelationUIItemType? {
            return when (value.lowercase()) {
                FOLLOWING.value.lowercase() -> FOLLOWING
                FOLLOWERS.value.lowercase() -> FOLLOWERS
                else -> null
            }
        }
    }
}

fun String?.toUserRelationUIItemType(): UserRelationUIItemType? {
    return this?.let { UserRelationUIItemType.fromValue(it) }
}

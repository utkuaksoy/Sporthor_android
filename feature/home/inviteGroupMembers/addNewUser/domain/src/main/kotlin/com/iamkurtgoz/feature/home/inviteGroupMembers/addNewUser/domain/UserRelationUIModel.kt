package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

data class UserRelationUIModel(
    val users: List<UserRelationUIItemModel>?,
    val coaches: List<CoachRelationUIItemModel>?,
)

data class UserRelationUIItemModel(
    val id: String?,
    val name: String?,
    val username: String?,
    val summary: String?,
    val imageUrl: String?,
    val isFollow: Boolean?,
    val isCurrentUser: Boolean?,
)

data class CoachRelationUIItemModel(
    val id: String?,
    val name: String?,
    val username: String?,
    val summary: String?,
    val imageUrl: String?,
    val isFollow: Boolean?,
    val isCurrentUser: Boolean?,
)

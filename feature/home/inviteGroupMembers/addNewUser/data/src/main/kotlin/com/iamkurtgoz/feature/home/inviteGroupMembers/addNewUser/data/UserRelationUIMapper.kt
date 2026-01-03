package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.UserRelationDomainModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.CoachRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIModel
import javax.inject.Inject

internal class UserRelationUIMapper @Inject constructor() : IMapper<UserRelationDomainModel, UserRelationUIModel> {
    override fun map(response: UserRelationDomainModel): UserRelationUIModel {
        return with(response) {
            UserRelationUIModel(
                users = users?.map {
                    UserRelationUIItemModel(
                        id = it?.id,
                        name = it?.name,
                        username = it?.username,
                        summary = it?.summary,
                        imageUrl = it?.imageUrl,
                        isFollow = it?.isFollow,
                        isCurrentUser = it?.isCurrentUser,
                    )
                },
                coaches = coaches?.map {
                    CoachRelationUIItemModel(
                        id = it?.id,
                        name = it?.name,
                        username = it?.username,
                        summary = it?.summary,
                        imageUrl = it?.imageUrl,
                        isFollow = it?.isFollow,
                        isCurrentUser = it?.isCurrentUser,
                    )
                },
            )
        }
    }
}

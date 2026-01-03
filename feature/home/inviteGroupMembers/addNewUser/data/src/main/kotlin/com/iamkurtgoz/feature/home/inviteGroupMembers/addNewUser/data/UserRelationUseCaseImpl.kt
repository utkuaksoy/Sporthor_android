package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.GetUserRelationUseCaseParams
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRelationUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
    private val userRelationUIMapper: UserRelationUIMapper,
) : GetUserRelationUseCase, CoreUseCase(coroutineDispatcher) {
    override fun invoke(params: GetUserRelationUseCaseParams): Flow<RestResult<UserRelationUIModel>> = prepare {
        val domainData = when (params.type) {
            UserRelationUIItemType.FOLLOWING -> repository.getFollowing(userId = params.userId)
            UserRelationUIItemType.FOLLOWERS -> repository.getFollowers(userId = params.userId)
            else -> throw IllegalArgumentException("Invalid type")
        }

        domainData.mapOnSuccess {
            userRelationUIMapper.map(
                it,
            )
        }
    }
}

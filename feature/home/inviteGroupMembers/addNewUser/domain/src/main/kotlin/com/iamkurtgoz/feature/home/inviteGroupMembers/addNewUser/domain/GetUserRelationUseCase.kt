package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult

interface GetUserRelationUseCase : IUseCase<GetUserRelationUseCaseParams, RestResult<UserRelationUIModel>>

data class GetUserRelationUseCaseParams(
    val userId: String?,
    val type: UserRelationUIItemType?,
)


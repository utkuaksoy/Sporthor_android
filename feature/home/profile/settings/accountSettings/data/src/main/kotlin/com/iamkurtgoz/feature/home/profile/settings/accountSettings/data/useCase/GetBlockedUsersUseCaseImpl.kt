package com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.response.BlockedUsersDomainModel
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.GetBlockedUsersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetBlockedUsersUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
) : GetBlockedUsersUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<BlockedUsersDomainModel>> = prepare {
        repository.getBlockedUsers()
    }
}

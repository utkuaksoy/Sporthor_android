package com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.RemoveBlockUserRequest
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.RemoveBlockedUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RemoveBlockedUserUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
) : RemoveBlockedUserUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: RemoveBlockUserRequest): Flow<RestResult<Unit>> = prepare {
        repository.removeBlockUser(params)
    }
}

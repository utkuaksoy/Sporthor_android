package com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.ProfileRepository
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.DeleteAccountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DeleteAccountUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ProfileRepository,
) : DeleteAccountUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<Unit>> = prepare {
        repository.deleteAccount().mapOnSuccess {
        }
    }
}

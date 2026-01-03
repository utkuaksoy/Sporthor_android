package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SearchUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SearchUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
    private val socialSearchUIMapper: SocialSearchUIMapper,
) : SearchUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: String): Flow<RestResult<SocialSearchUIModel>> = prepare {
        repository.getSearch(searchTerm = params)
            .mapOnSuccess {
                socialSearchUIMapper.map(
                    it,
                )
            }
    }
}

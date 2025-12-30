package com.iamkurtgoz.feature.home.trainingScreen.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import com.iamkurtgoz.domain.repository.CoachRepository
import com.iamkurtgoz.feature.home.trainingScreen.domain.useCase.GetRecommendedGroupNamesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetRecommendedGroupNamesUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoachRepository,
) : GetRecommendedGroupNamesUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: String?): Flow<RestResult<GetRecomendedGroupNamesDomainModel>> = prepare {
        repository.getRecommendedGroupNames(clubId = params)
    }
}

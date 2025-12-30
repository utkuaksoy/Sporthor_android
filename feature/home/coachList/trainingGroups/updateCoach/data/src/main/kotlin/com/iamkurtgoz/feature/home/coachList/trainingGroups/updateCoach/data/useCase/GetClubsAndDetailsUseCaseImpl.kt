package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import com.iamkurtgoz.domain.repository.ManagerRepository
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase.GetClubsAndDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetClubsAndDetailsUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ManagerRepository,
) : GetClubsAndDetailsUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<GetClubsAndDetailsDomainModel>> = prepare {
        repository.getClubsAndDetails()
    }
}

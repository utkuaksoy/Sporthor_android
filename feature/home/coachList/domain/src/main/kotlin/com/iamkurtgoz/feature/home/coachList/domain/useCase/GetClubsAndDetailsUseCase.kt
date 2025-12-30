package com.iamkurtgoz.feature.home.coachList.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCaseWithoutParams
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel

interface GetClubsAndDetailsUseCase : IUseCaseWithoutParams<RestResult<GetClubsAndDetailsDomainModel>>

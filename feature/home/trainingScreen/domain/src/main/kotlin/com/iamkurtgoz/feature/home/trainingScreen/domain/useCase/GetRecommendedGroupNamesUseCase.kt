package com.iamkurtgoz.feature.home.trainingScreen.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel

interface GetRecommendedGroupNamesUseCase : IUseCase<String?, RestResult<GetRecomendedGroupNamesDomainModel>>

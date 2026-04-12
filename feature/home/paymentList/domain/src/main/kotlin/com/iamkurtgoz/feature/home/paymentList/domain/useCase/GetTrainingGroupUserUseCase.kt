package com.iamkurtgoz.feature.home.paymentList.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCaseWithoutParams
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel

interface GetTrainingGroupUserUseCase : IUseCaseWithoutParams<RestResult<GetTrainingGroupUserDomainModel>>

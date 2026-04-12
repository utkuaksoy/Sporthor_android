package com.iamkurtgoz.feature.home.paymentList.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCaseWithoutParams
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.response.GetFeeCategoryDomainModel

interface GetFeeCategoryUseCase : IUseCaseWithoutParams<RestResult<GetFeeCategoryDomainModel>>

package com.iamkurtgoz.feature.home.paymentList.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel

interface GetFeesUseCase : IUseCase<GetFeesRequest, RestResult<GetFeesDomainModel>>

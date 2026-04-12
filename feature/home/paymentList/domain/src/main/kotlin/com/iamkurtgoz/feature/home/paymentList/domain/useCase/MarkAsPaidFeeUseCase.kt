package com.iamkurtgoz.feature.home.paymentList.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.MarkAsPaidFeeRequest

interface MarkAsPaidFeeUseCase :
    IUseCase<MarkAsPaidFeeRequest, RestResult<String>>

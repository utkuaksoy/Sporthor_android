package com.iamkurtgoz.domain.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.AddFeeUsersOrTrainingGroupsRequest
import com.iamkurtgoz.domain.model.request.GetFeesDetailRequest
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.domain.model.request.MarkAsPaidFeeRequest
import com.iamkurtgoz.domain.model.request.SendFeePaymentNotificationRequest
import com.iamkurtgoz.domain.model.response.AddFeeUsersOrTrainingGroupsDomainModel
import com.iamkurtgoz.domain.model.response.GetFeeCategoryDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDetailDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel

interface FeeRepository {
    suspend fun addFeeUsersOrTrainingGroups(body: AddFeeUsersOrTrainingGroupsRequest): RestResult<AddFeeUsersOrTrainingGroupsDomainModel>
    suspend fun getFeesDetail(body: GetFeesDetailRequest): RestResult<GetFeesDetailDomainModel>
    suspend fun getFees(body: GetFeesRequest?): RestResult<GetFeesDomainModel>
    suspend fun getFeeCategory(): RestResult<GetFeeCategoryDomainModel>
    suspend fun markAsPaidFee(body: MarkAsPaidFeeRequest): RestResult<String>
    suspend fun sendFeePaymentNotification(body: SendFeePaymentNotificationRequest): RestResult<String>

}

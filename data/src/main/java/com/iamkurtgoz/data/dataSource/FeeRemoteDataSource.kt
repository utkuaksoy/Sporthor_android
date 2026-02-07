package com.iamkurtgoz.data.dataSource

import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.domain.model.request.AddFeeUsersOrTrainingGroupsRequest
import com.iamkurtgoz.domain.model.request.GetFeesDetailRequest
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.domain.model.request.MarkAsPaidFeeRequest
import com.iamkurtgoz.domain.model.request.SendFeePaymentNotificationRequest
import com.iamkurtgoz.domain.model.response.AddFeeUsersOrTrainingGroupsDomainModel
import com.iamkurtgoz.domain.model.response.GetFeeCategoryDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDetailDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel

interface FeeRemoteDataSource {
    suspend fun addFeeUsersOrTrainingGroups(body: AddFeeUsersOrTrainingGroupsRequest): BaseResponse<AddFeeUsersOrTrainingGroupsDomainModel>
    suspend fun getFeesDetail(body: GetFeesDetailRequest): BaseResponse<GetFeesDetailDomainModel>
    suspend fun getFees(body: GetFeesRequest?): BaseResponse<GetFeesDomainModel>
    suspend fun getFeeCategory(): BaseResponse<GetFeeCategoryDomainModel>
    suspend fun markAsPaidFee(body: MarkAsPaidFeeRequest): BaseResponse<String>
    suspend fun sendFeePaymentNotification(body: SendFeePaymentNotificationRequest): BaseResponse<String>

}

package com.iamkurtgoz.data.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.data.core.CoreRepository
import com.iamkurtgoz.data.dataSource.FeeRemoteDataSource
import com.iamkurtgoz.domain.model.request.AddFeeUsersOrTrainingGroupsRequest
import com.iamkurtgoz.domain.model.request.GetFeesDetailRequest
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.domain.model.request.MarkAsPaidFeeRequest
import com.iamkurtgoz.domain.model.request.SendFeePaymentNotificationRequest
import com.iamkurtgoz.domain.model.response.AddFeeUsersOrTrainingGroupsDomainModel
import com.iamkurtgoz.domain.model.response.GetFeeCategoryDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDetailDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel
import com.iamkurtgoz.domain.repository.FeeRepository
import javax.inject.Inject

internal class FeeRepositoryImpl @Inject constructor(
    private val feeRemoteDataSource: FeeRemoteDataSource,
) : FeeRepository, CoreRepository() {

    override suspend fun addFeeUsersOrTrainingGroups(body: AddFeeUsersOrTrainingGroupsRequest): RestResult<AddFeeUsersOrTrainingGroupsDomainModel> = mapToRestResult {
        feeRemoteDataSource.addFeeUsersOrTrainingGroups(body)
    }

    override suspend fun getFeesDetail(body: GetFeesDetailRequest): RestResult<GetFeesDetailDomainModel> = mapToRestResult {
        feeRemoteDataSource.getFeesDetail(body)
    }

    override suspend fun getFees(body: GetFeesRequest?): RestResult<GetFeesDomainModel> = mapToRestResult {
        feeRemoteDataSource.getFees(body)
    }
    override suspend fun getFeeCategory(): RestResult<GetFeeCategoryDomainModel> = mapToRestResult {
        feeRemoteDataSource.getFeeCategory()
    }

    override suspend fun markAsPaidFee(body: MarkAsPaidFeeRequest): RestResult<String> = mapToRestResult {
        feeRemoteDataSource.markAsPaidFee(body)
    }

    override suspend fun sendFeePaymentNotification(body: SendFeePaymentNotificationRequest): RestResult<String> = mapToRestResult {
        feeRemoteDataSource.sendFeePaymentNotification(body)
    }

}

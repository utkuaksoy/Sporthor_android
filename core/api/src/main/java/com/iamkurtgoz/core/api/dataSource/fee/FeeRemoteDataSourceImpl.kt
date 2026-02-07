package com.iamkurtgoz.core.api.dataSource.fee

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.fee.FeeService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class FeeRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val feeService: FeeService,
) : FeeRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun addFeeUsersOrTrainingGroups(body: AddFeeUsersOrTrainingGroupsRequest): BaseResponse<AddFeeUsersOrTrainingGroupsDomainModel> = requestRetrofit {
        feeService.addFeeUsersOrTrainingGroups(body)
    }

    override suspend fun getFeesDetail(body: GetFeesDetailRequest): BaseResponse<GetFeesDetailDomainModel> = requestRetrofit {
        feeService.getFeesDetail(body)
    }

    override suspend fun getFees(body: GetFeesRequest?): BaseResponse<GetFeesDomainModel> = requestRetrofit {
        feeService.getFees(body ?: GetFeesRequest(filterType = 0))
    }
    override suspend fun getFeeCategory(): BaseResponse<GetFeeCategoryDomainModel> = requestRetrofit {
        feeService.getFeeCategory()
    }

    override suspend fun markAsPaidFee(body: MarkAsPaidFeeRequest): BaseResponse<String> = requestRetrofit {
        feeService.markAsPaidFee(body)
    }

    override suspend fun sendFeePaymentNotification(body: SendFeePaymentNotificationRequest): BaseResponse<String> = requestRetrofit {
        feeService.sendFeePaymentNotification(body)
    }

}

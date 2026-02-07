package com.iamkurtgoz.core.api.service.fee

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FeeService {
    @POST("Fee/GetFees")
    suspend fun getFees(
        @Body body: GetFeesRequest,
    ): Response<BaseResponse<GetFeesDomainModel>>

    @POST("Fee/GetFeesDetail")
    suspend fun getFeesDetail(
        @Body body: GetFeesDetailRequest,
    ): Response<BaseResponse<GetFeesDetailDomainModel>>

    @GET("Fee/GetFeeCategory")
    suspend fun getFeeCategory(): Response<BaseResponse<GetFeeCategoryDomainModel>>

    @POST("Fee/AddFeeUsersOrTrainingGroups")
    suspend fun addFeeUsersOrTrainingGroups(
        @Body body: AddFeeUsersOrTrainingGroupsRequest,
    ): Response<BaseResponse<AddFeeUsersOrTrainingGroupsDomainModel>>

    @POST("Fee/MarkAsPaidFee")
    suspend fun markAsPaidFee(
        @Body body: MarkAsPaidFeeRequest,
    ): Response<BaseResponse<String>>

    @POST("Fee/SendFeePaymentNotification")
    suspend fun sendFeePaymentNotification(
        @Body body: SendFeePaymentNotificationRequest,
    ): Response<BaseResponse<String>>

}

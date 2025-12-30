/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.core.api.service.coach

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.AddTrainingGroupResponseModel
import com.iamkurtgoz.data.model.GetTrainingGroupUserResponseModel
import com.iamkurtgoz.domain.model.request.AddTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.ConfirmationTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.UpdateTrainingGroupRequest
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface CoachService {

    @POST("Coach/AddTrainingGroup")
    suspend fun addTrainingGroup(@Body body: AddTrainingGroupRequest?): Response<BaseResponse<AddTrainingGroupResponseModel>>

    @POST("Coach/AddTrainingGroupUser")
    suspend fun addTrainingGroupUser(@Body body: AddTrainingGroupUserRequest?): Response<BaseResponse<Unit>>

    @POST("Coach/GetTrainingGroupUser")
    suspend fun getTrainingGroupUser(@Body body: Unit = Unit): Response<BaseResponse<GetTrainingGroupUserResponseModel>>

    @POST("Coach/UpdateTrainingGroup")
    suspend fun updateTrainingGroup(@Body body: UpdateTrainingGroupRequest?): Response<BaseResponse<AddTrainingGroupResponseModel>>

    @POST("Coach/ConfirmationTrainingGroupUser")
    suspend fun confirmationTrainingGroupUser(@Body body: ConfirmationTrainingGroupUserRequest?): Response<BaseResponse<Unit>>

    @GET("Coach/GetRecomendedGroupNames")
    suspend fun getRecommendedGroupNames(@Query("ClubId") clubId: String?): Response<BaseResponse<GetRecomendedGroupNamesDomainModel>>

    @POST("Coach/RemoveTrainingGroup")
    suspend fun removeTrainingGroup(@Body body: RemoveTrainingGroupRequest?): Response<BaseResponse<Unit>>
}

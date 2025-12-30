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
package com.iamkurtgoz.core.api.service.manager

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseRequest
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.ClubResponseModel
import com.iamkurtgoz.data.model.GetSportClubResponseModel
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Keep
interface ManagerService {

    @POST("Manager/AddSportClub")
    suspend fun addSportClub(@Body body: AddSportClubRequest?): Response<BaseResponse<ClubResponseModel>>

    @POST("Manager/UpdateSportClub")
    suspend fun updateSportClub(@Body body: UpdateSportClubRequest?): Response<BaseResponse<ClubResponseModel>>

    @POST("Manager/UpdateSportClubFiles")
    suspend fun updateSportClubFilesRequest(@Body body: UpdateSportClubFilesRequest?): Response<BaseResponse<ClubResponseModel>>

    @POST("Manager/GetSportClub")
    suspend fun getSportClub(): Response<BaseResponse<List<GetSportClubResponseModel>>>

    @POST("Manager/GetClubsAndDetails")
    suspend fun getClubsAndDetails(@Body body: BaseRequest = BaseRequest): Response<BaseResponse<GetClubsAndDetailsDomainModel>>

    @POST("Manager/DeleteCoach")
    suspend fun deleteCoach(@Body body: DeleteCoachRequest): Response<BaseResponse<Unit>>

    @POST("Manager/UpdateCoach")
    suspend fun updateCoach(@Body body: UpdateCoachRequest): Response<BaseResponse<Unit>>
}

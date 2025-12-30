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
package com.iamkurtgoz.data.dataSource

import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.ClubResponseModel
import com.iamkurtgoz.data.model.GetSportClubResponseModel
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel

interface ManagerRemoteDataSource {
    suspend fun addSportClub(body: AddSportClubRequest?): BaseResponse<ClubResponseModel>
    suspend fun updateSportClub(body: UpdateSportClubRequest?): BaseResponse<ClubResponseModel>
    suspend fun updateSportClubFilesRequest(body: UpdateSportClubFilesRequest?): BaseResponse<ClubResponseModel>
    suspend fun getSportClub(): BaseResponse<List<GetSportClubResponseModel>>
    suspend fun getClubsAndDetails(): BaseResponse<GetClubsAndDetailsDomainModel>
    suspend fun deleteCoach(body: DeleteCoachRequest): BaseResponse<Unit>
    suspend fun updateCoach(body: UpdateCoachRequest): BaseResponse<Unit>
}

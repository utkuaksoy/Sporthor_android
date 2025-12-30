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
package com.iamkurtgoz.domain.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.ClubDomainModel
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModel

interface ManagerRepository {
    suspend fun addSportClub(body: AddSportClubRequest?): RestResult<ClubDomainModel>
    suspend fun updateSportClub(body: UpdateSportClubRequest?): RestResult<ClubDomainModel>
    suspend fun updateSportClubFilesRequest(body: UpdateSportClubFilesRequest?): RestResult<ClubDomainModel>
    suspend fun getSportClub(): RestResult<List<GetSportClubDomainModel>>
    suspend fun getClubsAndDetails(): RestResult<GetClubsAndDetailsDomainModel>
    suspend fun deleteCoach(body: DeleteCoachRequest): RestResult<Unit>
    suspend fun updateCoach(body: UpdateCoachRequest): RestResult<Unit>
}

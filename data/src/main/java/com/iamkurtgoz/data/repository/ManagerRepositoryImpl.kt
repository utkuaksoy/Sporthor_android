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
package com.iamkurtgoz.data.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.data.core.CoreRepository
import com.iamkurtgoz.data.dataSource.ManagerRemoteDataSource
import com.iamkurtgoz.data.mapper.ClubDomainMapper
import com.iamkurtgoz.data.mapper.GetSportClubDomainMapper
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.ClubDomainModel
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModel
import com.iamkurtgoz.domain.repository.ManagerRepository
import javax.inject.Inject

internal class ManagerRepositoryImpl @Inject constructor(
    private val managerRemoteDataSource: ManagerRemoteDataSource,
    private val clubDomainMapper: ClubDomainMapper,
    private val getSportClubDomainMapper: GetSportClubDomainMapper,
) : ManagerRepository, CoreRepository() {

    override suspend fun addSportClub(body: AddSportClubRequest?): RestResult<ClubDomainModel> = mapToRestResult {
        managerRemoteDataSource.addSportClub(body)
    }.mapOnSuccess {
        clubDomainMapper.map(it)
    }

    override suspend fun updateSportClub(body: UpdateSportClubRequest?): RestResult<ClubDomainModel> = mapToRestResult {
        managerRemoteDataSource.updateSportClub(body)
    }.mapOnSuccess {
        clubDomainMapper.map(it)
    }

    override suspend fun updateSportClubFilesRequest(body: UpdateSportClubFilesRequest?): RestResult<ClubDomainModel> = mapToRestResult {
        managerRemoteDataSource.updateSportClubFilesRequest(body)
    }.mapOnSuccess {
        clubDomainMapper.map(it)
    }

    override suspend fun getSportClub(): RestResult<List<GetSportClubDomainModel>> = mapToRestResult {
        managerRemoteDataSource.getSportClub()
    }.mapOnSuccess {
        it.map(getSportClubDomainMapper::map)
    }

    override suspend fun getClubsAndDetails(): RestResult<GetClubsAndDetailsDomainModel> = mapToRestResult {
        managerRemoteDataSource.getClubsAndDetails()
    }

    override suspend fun deleteCoach(body: DeleteCoachRequest): RestResult<Unit> = mapToRestResult {
        managerRemoteDataSource.deleteCoach(body)
    }

    override suspend fun updateCoach(body: UpdateCoachRequest): RestResult<Unit> = mapToRestResult {
        managerRemoteDataSource.updateCoach(body)
    }
}

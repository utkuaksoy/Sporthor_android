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
package com.iamkurtgoz.core.api.dataSource.manager

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.manager.ManagerService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.ManagerRemoteDataSource
import com.iamkurtgoz.data.model.ClubResponseModel
import com.iamkurtgoz.data.model.GetSportClubResponseModel
import com.iamkurtgoz.domain.model.request.AddSportClubRequest
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubFilesRequest
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class ManagerRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val managerService: ManagerService,
) : ManagerRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun addSportClub(body: AddSportClubRequest?): BaseResponse<ClubResponseModel> = requestRetrofit {
        managerService.addSportClub(body)
    }

    override suspend fun updateSportClub(body: UpdateSportClubRequest?): BaseResponse<ClubResponseModel> = requestRetrofit {
        managerService.updateSportClub(body)
    }

    override suspend fun updateSportClubFilesRequest(body: UpdateSportClubFilesRequest?): BaseResponse<ClubResponseModel> = requestRetrofit {
        managerService.updateSportClubFilesRequest(body)
    }

    override suspend fun getSportClub(): BaseResponse<List<GetSportClubResponseModel>> = requestRetrofit {
        managerService.getSportClub()
    }

    override suspend fun getClubsAndDetails(): BaseResponse<GetClubsAndDetailsDomainModel> = requestRetrofit {
        managerService.getClubsAndDetails()
    }

    override suspend fun deleteCoach(body: DeleteCoachRequest): BaseResponse<Unit> = requestRetrofit {
        managerService.deleteCoach(body)
    }

    override suspend fun updateCoach(body: UpdateCoachRequest): BaseResponse<Unit> = requestRetrofit {
        managerService.updateCoach(body)
    }
}

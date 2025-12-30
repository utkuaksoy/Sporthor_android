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
package com.iamkurtgoz.core.api.dataSource.teams

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.teams.TeamsService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.TeamsRemoteDataSource
import com.iamkurtgoz.data.model.TeamsResponseModel
import com.iamkurtgoz.domain.model.request.SaveUserTeamsRequestBody
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class TeamsRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val teamsService: TeamsService,
) : TeamsRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getTeams(): BaseResponse<TeamsResponseModel> = requestRetrofit {
        teamsService.getTeams()
    }

    override suspend fun saveUserTeams(body: SaveUserTeamsRequestBody): BaseResponse<Unit> = requestRetrofitAny {
        teamsService.saveUserTeams(body)
    }
}

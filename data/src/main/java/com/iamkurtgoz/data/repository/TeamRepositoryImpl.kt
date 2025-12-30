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
import com.iamkurtgoz.data.dataSource.TeamsRemoteDataSource
import com.iamkurtgoz.data.mapper.TeamsDomainMapper
import com.iamkurtgoz.domain.model.request.SaveUserTeamsRequestBody
import com.iamkurtgoz.domain.model.response.TeamsDomainModel
import com.iamkurtgoz.domain.repository.TeamRepository
import javax.inject.Inject

internal class TeamRepositoryImpl @Inject constructor(
    private val teamsRemoteDataSource: TeamsRemoteDataSource,
    private val teamsDomainMapper: TeamsDomainMapper,
) : TeamRepository, CoreRepository() {

    override suspend fun getTeams(): RestResult<TeamsDomainModel> = mapToRestResult {
        teamsRemoteDataSource.getTeams()
    }.mapOnSuccess {
        teamsDomainMapper.map(it)
    }

    override suspend fun saveUserTeams(body: SaveUserTeamsRequestBody): RestResult<Unit> = mapToRestResultAny {
        teamsRemoteDataSource.saveUserTeams(body)
    }.mapOnSuccess {}
}

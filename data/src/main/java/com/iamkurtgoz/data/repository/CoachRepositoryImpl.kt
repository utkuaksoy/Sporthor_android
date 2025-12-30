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
import com.iamkurtgoz.data.dataSource.CoachRemoteDataSource
import com.iamkurtgoz.data.mapper.AddTrainingGroupDomainMapper
import com.iamkurtgoz.data.mapper.GetTrainingGroupUserDomainMapper
import com.iamkurtgoz.domain.model.request.AddTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.ConfirmationTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.UpdateTrainingGroupRequest
import com.iamkurtgoz.domain.model.response.AddTrainingGroupDomainModel
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel
import com.iamkurtgoz.domain.repository.CoachRepository
import javax.inject.Inject

internal class CoachRepositoryImpl @Inject constructor(
    private val coachRemoteDataSource: CoachRemoteDataSource,
    private val addTrainingGroupDomainMapper: AddTrainingGroupDomainMapper,
    private val getTrainingGroupUserDomainMapper: GetTrainingGroupUserDomainMapper,
) : CoachRepository, CoreRepository() {

    override suspend fun addTrainingGroup(body: AddTrainingGroupRequest?): RestResult<AddTrainingGroupDomainModel> = mapToRestResult {
        coachRemoteDataSource.addTrainingGroup(body)
    }.mapOnSuccess {
        addTrainingGroupDomainMapper.map(it)
    }

    override suspend fun addTrainingGroupUser(body: AddTrainingGroupUserRequest?): RestResult<Unit> = mapToRestResult {
        coachRemoteDataSource.addTrainingGroupUser(body)
    }

    override suspend fun getTrainingGroupUser(): RestResult<GetTrainingGroupUserDomainModel> = mapToRestResult {
        coachRemoteDataSource.getTrainingGroupUser()
    }.mapOnSuccess {
        getTrainingGroupUserDomainMapper.map(it)
    }

    override suspend fun updateTrainingGroup(body: UpdateTrainingGroupRequest?): RestResult<AddTrainingGroupDomainModel> = mapToRestResult {
        coachRemoteDataSource.updateTrainingGroup(body)
    }.mapOnSuccess {
        addTrainingGroupDomainMapper.map(it)
    }

    override suspend fun confirmationTrainingGroupUser(body: ConfirmationTrainingGroupUserRequest?): RestResult<Unit> = mapToRestResult {
        coachRemoteDataSource.confirmationTrainingGroupUser(body)
    }

    override suspend fun getRecommendedGroupNames(clubId: String?): RestResult<GetRecomendedGroupNamesDomainModel> = mapToRestResult {
        coachRemoteDataSource.getRecommendedGroupNames(clubId)
    }

    override suspend fun removeTrainingGroup(body: RemoveTrainingGroupRequest?): RestResult<Unit> = mapToRestResult {
        coachRemoteDataSource.removeTrainingGroup(body)
    }
}

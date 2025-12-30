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
package com.iamkurtgoz.core.api.dataSource.coach

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.coach.CoachService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.CoachRemoteDataSource
import com.iamkurtgoz.data.model.AddTrainingGroupResponseModel
import com.iamkurtgoz.data.model.GetTrainingGroupUserResponseModel
import com.iamkurtgoz.domain.model.request.AddTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.ConfirmationTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.UpdateTrainingGroupRequest
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class CoachRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val coachService: CoachService,
) : CoachRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun addTrainingGroup(body: AddTrainingGroupRequest?): BaseResponse<AddTrainingGroupResponseModel> = requestRetrofit {
        coachService.addTrainingGroup(body)
    }

    override suspend fun addTrainingGroupUser(body: AddTrainingGroupUserRequest?): BaseResponse<Unit> = requestRetrofitAny {
        coachService.addTrainingGroupUser(body)
    }

    override suspend fun getTrainingGroupUser(): BaseResponse<GetTrainingGroupUserResponseModel> = requestRetrofitAny {
        coachService.getTrainingGroupUser()
    }

    override suspend fun updateTrainingGroup(body: UpdateTrainingGroupRequest?): BaseResponse<AddTrainingGroupResponseModel> = requestRetrofit {
        coachService.updateTrainingGroup(body)
    }

    override suspend fun confirmationTrainingGroupUser(body: ConfirmationTrainingGroupUserRequest?): BaseResponse<Unit> = requestRetrofit {
        coachService.confirmationTrainingGroupUser(body)
    }

    override suspend fun getRecommendedGroupNames(clubId: String?): BaseResponse<GetRecomendedGroupNamesDomainModel> = requestRetrofit {
        coachService.getRecommendedGroupNames(clubId)
    }

    override suspend fun removeTrainingGroup(body: RemoveTrainingGroupRequest?): BaseResponse<Unit> = requestRetrofit {
        coachService.removeTrainingGroup(body)
    }
}

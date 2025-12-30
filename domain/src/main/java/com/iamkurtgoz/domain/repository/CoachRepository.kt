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
import com.iamkurtgoz.domain.model.request.AddTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.ConfirmationTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.RemoveTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.UpdateTrainingGroupRequest
import com.iamkurtgoz.domain.model.response.AddTrainingGroupDomainModel
import com.iamkurtgoz.domain.model.response.GetRecomendedGroupNamesDomainModel
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModel

interface CoachRepository {
    suspend fun addTrainingGroup(body: AddTrainingGroupRequest?): RestResult<AddTrainingGroupDomainModel>
    suspend fun addTrainingGroupUser(body: AddTrainingGroupUserRequest?): RestResult<Unit>
    suspend fun getTrainingGroupUser(): RestResult<GetTrainingGroupUserDomainModel>
    suspend fun updateTrainingGroup(body: UpdateTrainingGroupRequest?): RestResult<AddTrainingGroupDomainModel>
    suspend fun confirmationTrainingGroupUser(body: ConfirmationTrainingGroupUserRequest?): RestResult<Unit>
    suspend fun getRecommendedGroupNames(clubId: String?): RestResult<GetRecomendedGroupNamesDomainModel>
    suspend fun removeTrainingGroup(body: RemoveTrainingGroupRequest?): RestResult<Unit>
}

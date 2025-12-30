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
package com.iamkurtgoz.feature.home.profile.userRelation.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.profile.userRelation.data.mapper.UserRelationUIMapper
import com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIModel
import com.iamkurtgoz.feature.home.profile.userRelation.domain.types.UserRelationUIItemType
import com.iamkurtgoz.feature.home.profile.userRelation.domain.useCase.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.profile.userRelation.domain.useCase.GetUserRelationUseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRelationUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
    private val userRelationUIMapper: UserRelationUIMapper,
) : GetUserRelationUseCase, CoreUseCase(coroutineDispatcher) {
    override fun invoke(params: GetUserRelationUseCaseParams): Flow<RestResult<UserRelationUIModel>> = prepare {
        val domainData = when (params.type) {
            UserRelationUIItemType.FOLLOWING -> repository.getFollowing(userId = params.userId)
            UserRelationUIItemType.FOLLOWERS -> repository.getFollowers(userId = params.userId)
            else -> throw IllegalArgumentException("Invalid type")
        }

        domainData.mapOnSuccess {
            userRelationUIMapper.map(
                it,
            )
        }
    }
}

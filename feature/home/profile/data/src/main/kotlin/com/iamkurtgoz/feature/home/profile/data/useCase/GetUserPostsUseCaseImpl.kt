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
package com.iamkurtgoz.feature.home.profile.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.ProfileRepository
import com.iamkurtgoz.feature.home.profile.data.mapper.UserPostsUIMapper
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsUIModel
import com.iamkurtgoz.feature.home.profile.domain.useCase.UserPostsUseCase
import com.iamkurtgoz.feature.home.profile.domain.useCase.UserPostsUseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetUserPostsUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ProfileRepository,
    private val dashboardFeedAsyncUIMapper: UserPostsUIMapper,
) : UserPostsUseCase, CoreUseCase(coroutineDispatcher) {
    override fun invoke(params: UserPostsUseCaseParams): Flow<RestResult<UserPostsUIModel>> = prepare {
        repository.getUserPostsAsync(userId = params.userId, page = params.page, pageSize = params.pageSize)
            .mapOnSuccess {
                dashboardFeedAsyncUIMapper.map(it)
            }
    }
}

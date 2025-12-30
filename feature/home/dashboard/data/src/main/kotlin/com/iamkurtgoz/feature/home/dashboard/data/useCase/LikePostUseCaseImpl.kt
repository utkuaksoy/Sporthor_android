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
package com.iamkurtgoz.feature.home.dashboard.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.LikePostRequest
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.feature.home.dashboard.data.mapper.LikePostUIMapper
import com.iamkurtgoz.feature.home.dashboard.domain.model.LikePostUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.LikePostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LikePostUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: SocialRepository,
    private val likedPostUIMapper: LikePostUIMapper,
) : LikePostUseCase, CoreUseCase(coroutineDispatcher) {
    override fun invoke(params: LikePostRequest): Flow<RestResult<LikePostUIModel>> = prepare {
        repository.likePost(params)
            .mapOnSuccess {
                likedPostUIMapper.map(it)
            }
    }
}

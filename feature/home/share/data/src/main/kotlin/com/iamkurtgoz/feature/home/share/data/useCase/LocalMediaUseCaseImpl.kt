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
package com.iamkurtgoz.feature.home.share.data.useCase

import androidx.paging.PagingData
import androidx.paging.map
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.LocalMediaRepository
import com.iamkurtgoz.feature.home.share.domain.mapper.LocalMediaUIMapper
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import com.iamkurtgoz.feature.home.share.domain.useCase.LocalMediaUseCase
import com.iamkurtgoz.feature.home.share.domain.useCase.LocalMediaUseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalMediaUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocalMediaRepository,
    private val localMediaUIMapper: LocalMediaUIMapper,
) : LocalMediaUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: LocalMediaUseCaseParams): Flow<PagingData<LocalMediaUIModel>> {
        return repository
            .getLocalMediaFileList()
            .flow
            .map { pagingData ->
                pagingData.map(localMediaUIMapper::map)
            }
    }
}

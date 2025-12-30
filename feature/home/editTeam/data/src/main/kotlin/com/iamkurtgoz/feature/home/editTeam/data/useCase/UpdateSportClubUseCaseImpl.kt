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
package com.iamkurtgoz.feature.home.editTeam.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.UpdateSportClubRequest
import com.iamkurtgoz.domain.repository.ManagerRepository
import com.iamkurtgoz.feature.home.editTeam.data.mapper.ClubUIMapper
import com.iamkurtgoz.feature.home.editTeam.domain.model.ClubUIModel
import com.iamkurtgoz.feature.home.editTeam.domain.useCase.UpdateSportClubUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UpdateSportClubUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ManagerRepository,
    private val clubUIMapper: ClubUIMapper,
) : UpdateSportClubUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: UpdateSportClubRequest?): Flow<RestResult<ClubUIModel>> = prepare {
        repository.updateSportClub(params)
            .mapOnSuccess { clubUIMapper.map(it) }
    }
}

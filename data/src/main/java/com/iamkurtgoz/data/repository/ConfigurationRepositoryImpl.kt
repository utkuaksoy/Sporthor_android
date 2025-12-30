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
import com.iamkurtgoz.data.dataSource.ConfigurationRemoteDataSource
import com.iamkurtgoz.data.mapper.ConfigurationDomainMapper
import com.iamkurtgoz.data.mapper.GetSeasonsDomainMapper
import com.iamkurtgoz.data.mapper.MenuDomainMapper
import com.iamkurtgoz.data.mapper.OnBoardingDomainMapper
import com.iamkurtgoz.domain.model.response.ConfigurationDomainModel
import com.iamkurtgoz.domain.model.response.GetSeasonsDomainModel
import com.iamkurtgoz.domain.model.response.MenuDomainModel
import com.iamkurtgoz.domain.model.response.OnBoardingDomainModel
import com.iamkurtgoz.domain.repository.ConfigurationRepository
import javax.inject.Inject

internal class ConfigurationRepositoryImpl @Inject constructor(
    private val configurationRemoteDataSource: ConfigurationRemoteDataSource,
    private val configurationDomainMapper: ConfigurationDomainMapper,
    private val onBoardingDomainMapper: OnBoardingDomainMapper,
    private val getSeasonsDomainMapper: GetSeasonsDomainMapper,
    private val menuDomainMapper: MenuDomainMapper,
) : ConfigurationRepository, CoreRepository() {

    override suspend fun getConfiguration(): RestResult<ConfigurationDomainModel> = mapToRestResult {
        configurationRemoteDataSource.getConfiguration()
    }.mapOnSuccess {
        configurationDomainMapper.map(it)
    }

    override suspend fun getOnBoarding(): RestResult<OnBoardingDomainModel> = mapToRestResult {
        configurationRemoteDataSource.getOnboarding()
    }.mapOnSuccess {
        onBoardingDomainMapper.map(it)
    }

    override suspend fun getMenu(): RestResult<MenuDomainModel> = mapToRestResult {
        configurationRemoteDataSource.getMenu()
    }.mapOnSuccess {
        menuDomainMapper.map(it)
    }

    override suspend fun getSeasons(): RestResult<List<GetSeasonsDomainModel>> = mapToRestResult {
        configurationRemoteDataSource.getSeasons()
    }.mapOnSuccess {
        getSeasonsDomainMapper.map(it)
    }
}

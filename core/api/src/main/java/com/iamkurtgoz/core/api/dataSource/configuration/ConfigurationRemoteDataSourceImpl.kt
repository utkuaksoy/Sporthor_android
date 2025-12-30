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
package com.iamkurtgoz.core.api.dataSource.configuration

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.configuration.ConfigurationService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.ConfigurationRemoteDataSource
import com.iamkurtgoz.data.model.ConfigurationResponseModel
import com.iamkurtgoz.data.model.GetSeasonsResponseModel
import com.iamkurtgoz.data.model.MenuResponseModel
import com.iamkurtgoz.data.model.OnBoardingResponseModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class ConfigurationRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val configurationService: ConfigurationService,
) : ConfigurationRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getConfiguration(): BaseResponse<ConfigurationResponseModel> = requestRetrofit {
        configurationService.getConfiguration()
    }

    override suspend fun getOnboarding(): BaseResponse<OnBoardingResponseModel> = requestRetrofit {
        configurationService.getOnboarding()
    }

    override suspend fun getSeasons(): BaseResponse<GetSeasonsResponseModel> = requestRetrofit {
        configurationService.getSeasons()
    }

    override suspend fun getMenu(): BaseResponse<MenuResponseModel> = requestRetrofit {
        configurationService.getMenu()
    }
}

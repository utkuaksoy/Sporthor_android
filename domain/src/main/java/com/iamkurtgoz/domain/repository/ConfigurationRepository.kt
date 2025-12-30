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
import com.iamkurtgoz.domain.model.response.ConfigurationDomainModel
import com.iamkurtgoz.domain.model.response.GetSeasonsDomainModel
import com.iamkurtgoz.domain.model.response.MenuDomainModel
import com.iamkurtgoz.domain.model.response.OnBoardingDomainModel

interface ConfigurationRepository {
    suspend fun getConfiguration(): RestResult<ConfigurationDomainModel>
    suspend fun getOnBoarding(): RestResult<OnBoardingDomainModel>
    suspend fun getMenu(): RestResult<MenuDomainModel>
    suspend fun getSeasons(): RestResult<List<GetSeasonsDomainModel>>
}

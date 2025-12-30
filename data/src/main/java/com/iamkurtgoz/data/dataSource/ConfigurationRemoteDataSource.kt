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
package com.iamkurtgoz.data.dataSource

import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.ConfigurationResponseModel
import com.iamkurtgoz.data.model.GetSeasonsResponseModel
import com.iamkurtgoz.data.model.MenuResponseModel
import com.iamkurtgoz.data.model.OnBoardingResponseModel

interface ConfigurationRemoteDataSource {
    suspend fun getConfiguration(): BaseResponse<ConfigurationResponseModel>
    suspend fun getOnboarding(): BaseResponse<OnBoardingResponseModel>
    suspend fun getMenu(): BaseResponse<MenuResponseModel>
    suspend fun getSeasons(): BaseResponse<GetSeasonsResponseModel>
}

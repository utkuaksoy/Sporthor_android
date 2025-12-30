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
package com.iamkurtgoz.core.api.service.configuration

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.ConfigurationResponseModel
import com.iamkurtgoz.data.model.GetSeasonsResponseModel
import com.iamkurtgoz.data.model.MenuResponseModel
import com.iamkurtgoz.data.model.OnBoardingResponseModel
import retrofit2.Response
import retrofit2.http.GET

@Keep
interface ConfigurationService {

    @GET("Configuration/GetConfiguration")
    suspend fun getConfiguration(): Response<BaseResponse<ConfigurationResponseModel>>

    @GET("Configuration/GetOnboarding")
    suspend fun getOnboarding(): Response<BaseResponse<OnBoardingResponseModel>>

    @GET("Configuration/GetSeasons")
    suspend fun getSeasons(): Response<BaseResponse<GetSeasonsResponseModel>>

    @GET("Configuration/GetMenu")
    suspend fun getMenu(): Response<BaseResponse<MenuResponseModel>>
}

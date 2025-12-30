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
package com.iamkurtgoz.core.api.service.auth

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.AuthResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
import com.iamkurtgoz.domain.model.request.AuthRefreshTokenRequest
import com.iamkurtgoz.domain.model.request.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface AuthService {

    @POST("Authentication/UpdateProfile")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): Response<BaseResponse<Unit>>

    @POST("Authentication/RefreshToken")
    suspend fun authRefreshToken(@Body body: AuthRefreshTokenRequest): Response<BaseResponse<AuthResponseModel>>

    @GET("Profile/GetProfile")
    suspend fun getProfile(@Query("UserId") userId: String?): Response<BaseResponse<ProfileResponseModel>>
}

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
package com.iamkurtgoz.core.api.service.anonymous

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.AuthResponseModel
import com.iamkurtgoz.data.model.CheckUserNameResponseModel
import com.iamkurtgoz.domain.model.request.CheckUserNameRequest
import com.iamkurtgoz.domain.model.request.ForgotPasswordRequest
import com.iamkurtgoz.domain.model.request.GenerateOtpRequest
import com.iamkurtgoz.domain.model.request.LoginWithEmailRequest
import com.iamkurtgoz.domain.model.request.LoginWithPhoneRequest
import com.iamkurtgoz.domain.model.request.LoginWithSocialRequest
import com.iamkurtgoz.domain.model.request.LoginWithUserNameRequest
import com.iamkurtgoz.domain.model.request.RegisterRequest
import com.iamkurtgoz.domain.model.request.ValidateOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Keep
interface AnonymousAuthService {

    @POST("Authentication/GenerateOtp")
    suspend fun generateOtp(@Body body: GenerateOtpRequest): Response<BaseResponse<Unit>>

    @POST("Authentication/ValidateOtp")
    suspend fun validateOtp(@Body body: ValidateOtpRequest): Response<BaseResponse<Unit>>

    @POST("Authentication/CheckUsername")
    suspend fun checkUserName(@Body body: CheckUserNameRequest): Response<BaseResponse<CheckUserNameResponseModel>>

    @POST("Authentication/Register")
    suspend fun register(@Body body: RegisterRequest): Response<BaseResponse<AuthResponseModel>>

    @POST("Authentication/LoginWithPhone")
    suspend fun loginWithPhone(@Body body: LoginWithPhoneRequest): Response<BaseResponse<AuthResponseModel>>

    @POST("Authentication/LoginWithEmail")
    suspend fun loginWithEmail(@Body body: LoginWithEmailRequest): Response<BaseResponse<AuthResponseModel>>

    @POST("Authentication/LoginWithUserName")
    suspend fun loginWithUserName(@Body body: LoginWithUserNameRequest): Response<BaseResponse<AuthResponseModel>>

    @POST("Authentication/SocialLogin")
    suspend fun loginWithSocial(@Body body: LoginWithSocialRequest): Response<BaseResponse<AuthResponseModel>>

    @POST("Authentication/ForgotPassword")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<BaseResponse<Unit>>
}

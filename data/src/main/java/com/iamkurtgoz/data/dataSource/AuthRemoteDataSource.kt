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
import com.iamkurtgoz.data.model.AuthResponseModel
import com.iamkurtgoz.data.model.CheckUserNameResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
import com.iamkurtgoz.domain.model.request.AuthRefreshTokenRequest
import com.iamkurtgoz.domain.model.request.CheckUserNameRequest
import com.iamkurtgoz.domain.model.request.ForgotPasswordRequest
import com.iamkurtgoz.domain.model.request.GenerateOtpRequest
import com.iamkurtgoz.domain.model.request.LoginWithEmailRequest
import com.iamkurtgoz.domain.model.request.LoginWithPhoneRequest
import com.iamkurtgoz.domain.model.request.LoginWithSocialRequest
import com.iamkurtgoz.domain.model.request.LoginWithUserNameRequest
import com.iamkurtgoz.domain.model.request.RegisterRequest
import com.iamkurtgoz.domain.model.request.UpdateProfileRequest
import com.iamkurtgoz.domain.model.request.ValidateOtpRequest

interface AuthRemoteDataSource {
    suspend fun generateOtp(body: GenerateOtpRequest): BaseResponse<Unit>
    suspend fun validateOtp(body: ValidateOtpRequest): BaseResponse<Unit>
    suspend fun checkUserName(body: CheckUserNameRequest): BaseResponse<CheckUserNameResponseModel>
    suspend fun register(body: RegisterRequest): BaseResponse<AuthResponseModel>
    suspend fun loginWithPhone(body: LoginWithPhoneRequest): BaseResponse<AuthResponseModel>
    suspend fun loginWithEmail(body: LoginWithEmailRequest): BaseResponse<AuthResponseModel>
    suspend fun loginWithUserName(body: LoginWithUserNameRequest): BaseResponse<AuthResponseModel>
    suspend fun loginWithSocial(body: LoginWithSocialRequest): BaseResponse<AuthResponseModel>
    suspend fun forgotPassword(body: ForgotPasswordRequest): BaseResponse<Unit>
    suspend fun refreshToken(body: AuthRefreshTokenRequest): BaseResponse<ProfileResponseModel>
    suspend fun updateProfile(body: UpdateProfileRequest): BaseResponse<Unit>
}

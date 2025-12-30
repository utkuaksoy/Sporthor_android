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
import com.iamkurtgoz.domain.model.response.CheckUserNameDomainModel

interface AuthRepository {
    suspend fun generateOtp(body: GenerateOtpRequest): RestResult<Unit>
    suspend fun validateOtp(body: ValidateOtpRequest): RestResult<Unit>
    suspend fun checkUserName(body: CheckUserNameRequest): RestResult<CheckUserNameDomainModel>
    suspend fun register(body: RegisterRequest): RestResult<Unit>
    suspend fun loginWithPhone(body: LoginWithPhoneRequest): RestResult<Unit>
    suspend fun loginWithEmail(body: LoginWithEmailRequest): RestResult<Unit>
    suspend fun loginWithUserName(body: LoginWithUserNameRequest): RestResult<Unit>
    suspend fun loginWithSocial(body: LoginWithSocialRequest): RestResult<Unit>
    suspend fun forgotPassword(body: ForgotPasswordRequest): RestResult<Unit>
    suspend fun refreshToken(body: AuthRefreshTokenRequest): RestResult<Unit>
    suspend fun updateProfile(body: UpdateProfileRequest): RestResult<Unit>
}

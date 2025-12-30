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
import com.iamkurtgoz.data.dataSource.AuthRemoteDataSource
import com.iamkurtgoz.data.dataSource.ProfileDataSource
import com.iamkurtgoz.data.mapper.CheckUserNameDomainMapper
import com.iamkurtgoz.domain.dataStore.AppPreferences
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
import com.iamkurtgoz.domain.repository.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val profileRemoteDataSource: ProfileDataSource,
    private val checkUserNameDomainMapper: CheckUserNameDomainMapper,
    private val appPreferences: AppPreferences,
) : AuthRepository, CoreRepository() {

    override suspend fun generateOtp(body: GenerateOtpRequest): RestResult<Unit> = mapToRestResultAny {
        authRemoteDataSource.generateOtp(body)
    }

    override suspend fun validateOtp(body: ValidateOtpRequest): RestResult<Unit> = mapToRestResultAny {
        authRemoteDataSource.validateOtp(body)
    }

    override suspend fun checkUserName(body: CheckUserNameRequest): RestResult<CheckUserNameDomainModel> = mapToRestResult {
        authRemoteDataSource.checkUserName(body)
    }.mapOnSuccess {
        checkUserNameDomainMapper.map(it)
    }

    override suspend fun register(body: RegisterRequest): RestResult<Unit> = mapToRestResult {
        authRemoteDataSource.register(body)
    }.mapOnSuccess {
        appPreferences.setRefreshToken(it.refreshToken)
        appPreferences.setAccessToken(it.authToken)
        appPreferences.setLogin(true)

        profileRemoteDataSource.getProfile(null)
    }

    override suspend fun loginWithPhone(body: LoginWithPhoneRequest): RestResult<Unit> = mapToRestResult {
        authRemoteDataSource.loginWithPhone(body)
    }.mapOnSuccess {
        appPreferences.setRefreshToken(it.refreshToken)
        appPreferences.setAccessToken(it.authToken)
        appPreferences.setLogin(true)
    }

    override suspend fun loginWithEmail(body: LoginWithEmailRequest): RestResult<Unit> = mapToRestResult {
        authRemoteDataSource.loginWithEmail(body)
    }.mapOnSuccess {
        appPreferences.setRefreshToken(it.refreshToken)
        appPreferences.setAccessToken(it.authToken)
        appPreferences.setLogin(true)
    }

    override suspend fun loginWithUserName(body: LoginWithUserNameRequest): RestResult<Unit> = mapToRestResult {
        authRemoteDataSource.loginWithUserName(body)
    }.mapOnSuccess {
        appPreferences.setRefreshToken(it.refreshToken)
        appPreferences.setAccessToken(it.authToken)
        appPreferences.setLogin(true)

        profileRemoteDataSource.getProfile(null)
    }

    override suspend fun loginWithSocial(body: LoginWithSocialRequest): RestResult<Unit> = mapToRestResult {
        authRemoteDataSource.loginWithSocial(body)
    }.mapOnSuccess {
        appPreferences.setRefreshToken(it.refreshToken)
        appPreferences.setAccessToken(it.authToken)
        appPreferences.setLogin(true)
    }

    override suspend fun forgotPassword(body: ForgotPasswordRequest): RestResult<Unit> = mapToRestResultAny {
        authRemoteDataSource.forgotPassword(body)
    }

    override suspend fun refreshToken(body: AuthRefreshTokenRequest): RestResult<Unit> = mapToRestResultAny {
        authRemoteDataSource.refreshToken(body)
    }.mapOnSuccess { }

    override suspend fun updateProfile(body: UpdateProfileRequest): RestResult<Unit> = mapToRestResultAny {
        authRemoteDataSource.updateProfile(body)
    }
}

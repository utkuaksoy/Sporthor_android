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
package com.iamkurtgoz.core.api.dataSource.auth

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.anonymous.AnonymousAuthService
import com.iamkurtgoz.core.api.service.auth.AuthService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.AuthRemoteDataSource
import com.iamkurtgoz.data.model.AuthResponseModel
import com.iamkurtgoz.data.model.CheckUserNameResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val anonymousAuthService: AnonymousAuthService,
    private val authService: AuthService,
    private val appPreferences: AppPreferences,
) : AuthRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun generateOtp(body: GenerateOtpRequest): BaseResponse<Unit> = requestRetrofitAny {
        anonymousAuthService.generateOtp(body)
    }

    override suspend fun validateOtp(body: ValidateOtpRequest): BaseResponse<Unit> = requestRetrofitAny {
        anonymousAuthService.validateOtp(body)
    }

    override suspend fun checkUserName(body: CheckUserNameRequest): BaseResponse<CheckUserNameResponseModel> = requestRetrofit {
        anonymousAuthService.checkUserName(body)
    }

    override suspend fun register(body: RegisterRequest): BaseResponse<AuthResponseModel> = requestRetrofit {
        anonymousAuthService.register(body)
    }.also {
        it.data?.let { authResponse ->
            appPreferences.setUserId(authResponse.userId)
        }
    }

    override suspend fun loginWithPhone(body: LoginWithPhoneRequest): BaseResponse<AuthResponseModel> = requestRetrofit {
        anonymousAuthService.loginWithPhone(body)
    }.also {
        it.data?.let { authResponse ->
            appPreferences.setUserId(authResponse.userId)
        }
    }

    override suspend fun loginWithEmail(body: LoginWithEmailRequest): BaseResponse<AuthResponseModel> = requestRetrofit {
        anonymousAuthService.loginWithEmail(body)
    }.also {
        it.data?.let { authResponse ->
            appPreferences.setUserId(authResponse.userId)
        }
    }

    override suspend fun loginWithUserName(body: LoginWithUserNameRequest): BaseResponse<AuthResponseModel> = requestRetrofit {
        anonymousAuthService.loginWithUserName(body)
    }.also {
        it.data?.let { authResponse ->
            appPreferences.setUserId(authResponse.userId)
        }
    }

    override suspend fun loginWithSocial(body: LoginWithSocialRequest): BaseResponse<AuthResponseModel> = requestRetrofit {
        anonymousAuthService.loginWithSocial(body)
    }.also {
        it.data?.let { authResponse ->
            appPreferences.setUserId(authResponse.userId)
        }
    }

    override suspend fun forgotPassword(body: ForgotPasswordRequest): BaseResponse<Unit> = requestRetrofitAny {
        anonymousAuthService.forgotPassword(body)
    }

    override suspend fun refreshToken(body: AuthRefreshTokenRequest): BaseResponse<ProfileResponseModel> {
        requestRetrofit {
            authService.authRefreshToken(body)
        }.data?.let {
            appPreferences.setRefreshToken(it.refreshToken)
            appPreferences.setAccessToken(it.authToken)
            appPreferences.setLogin(true)
        }

        return requestRetrofit {
            authService.getProfile(null)
        }.also {
            it.data?.let { session ->
                if (session.info?.isCurrentUser == true) {
                    appPreferences.setUserId(session.info?.id)
                    appPreferences.setProfilePhoto(session.info?.avatar)
                }
            }
        }
    }

    override suspend fun updateProfile(body: UpdateProfileRequest): BaseResponse<Unit> = requestRetrofitAny {
        authService.updateProfile(body)
    }
}

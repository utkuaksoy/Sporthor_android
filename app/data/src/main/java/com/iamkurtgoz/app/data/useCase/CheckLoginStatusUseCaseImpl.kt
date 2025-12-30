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
package com.iamkurtgoz.app.data.useCase

import com.iamkurtgoz.app.domain.exception.UserNotLoginException
import com.iamkurtgoz.app.domain.useCase.CheckLoginStatusUseCase
import com.iamkurtgoz.app.domain.validator.JwtValidator
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.model.request.AuthRefreshTokenRequest
import com.iamkurtgoz.domain.repository.AuthRepository
import com.iamkurtgoz.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class CheckLoginStatusUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val jwtValidator: JwtValidator,
    private val appPreferences: AppPreferences,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : CheckLoginStatusUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(): Flow<RestResult<Unit>> = prepare {
        val currentPreferencesState = appPreferences.currentPreferenceState.firstOrNull()
        val isLogin = currentPreferencesState?.isLogin
        val accessToken = currentPreferencesState?.accessToken

        if (isLogin == false) {
            throw UserNotLoginException()
        } else if (!jwtValidator.isExpired(token = accessToken, leeway = 10)) {
            profileRepository.getProfile(null).mapOnSuccess { }
        } else {
            val refreshToken = currentPreferencesState?.refreshToken
            delay(AppDefaults.DELAY_REFRESH_TOKEN)
            val body = AuthRefreshTokenRequest(
                refreshToken = refreshToken,
            )
            authRepository.refreshToken(body).mapOnSuccess { Unit }
        }
    }
}

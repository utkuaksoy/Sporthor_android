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
package com.iamkurtgoz.core.api.interceptor

import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.core.common.qualifiers.QualifierAuthenticatorInterceptor
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.data.dataSource.AuthRemoteDataSource
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.model.request.AuthRefreshTokenRequest
import com.iamkurtgoz.domain.state.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@QualifierAuthenticatorInterceptor
@Singleton
class AuthenticatorInterceptor @Inject constructor(
    private val appPreferences: AppPreferences,
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authState: AuthState,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val currentPreferencesState = appPreferences.currentPreferenceState.firstOrNull()
        val isLogin = currentPreferencesState?.isLogin
        val refreshToken = currentPreferencesState?.refreshToken
        if (appBuildConfigStatePack.isDebug) {
            println("Authenticator: $isLogin - ${response.request.url} - ${response.code} - ${response.challenges()} - $refreshToken")
        }
        if (response.code == AppDefaults.AUTHORIZATION_ERROR) {
            if (!refreshToken(appPreferences = appPreferences, appBuildConfigStatePack = appBuildConfigStatePack, authRemoteDataSource = authRemoteDataSource)) {
                appPreferences.clear()
                authState.setUserEffect(AuthState.Effect.RouteToLoginWithClearBackStack)
                return@runBlocking null
            }
        }
        return@runBlocking response.request
    }
}

private suspend fun refreshToken(
    appPreferences: AppPreferences,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    authRemoteDataSource: AuthRemoteDataSource,
): Boolean {
    var currentPreferencesState = appPreferences.currentPreferenceState.firstOrNull()
    val isLogin = currentPreferencesState?.isLogin
    val refreshToken = currentPreferencesState?.refreshToken

    if (isLogin == false || refreshToken.isNullOrEmpty()) {
        return false
    }

    try {
        delay(AppDefaults.DELAY_REFRESH_TOKEN)
        val requestBody = AuthRefreshTokenRequest(refreshToken = refreshToken)

        authRemoteDataSource.refreshToken(requestBody)
        currentPreferencesState = appPreferences.currentPreferenceState.firstOrNull()
        return currentPreferencesState?.isLogin == true && !currentPreferencesState.refreshToken.isNullOrEmpty()
    } catch (e: BaseError) {
        if (appBuildConfigStatePack.isDebug) {
            println("error from authenticator indicator: ${e.localizedMessage}")
        }
        return false
    }
}

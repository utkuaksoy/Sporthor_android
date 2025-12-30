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
package com.iamkurtgoz.core.api.di

import com.iamkurtgoz.core.api.interceptor.AuthAuthorizationInterceptor
import com.iamkurtgoz.core.api.interceptor.AuthenticatorInterceptor
import com.iamkurtgoz.core.api.interceptor.ConnectivityInterceptor
import com.iamkurtgoz.core.api.interceptor.UserAgentInterceptor
import com.iamkurtgoz.core.common.qualifiers.AppVersionCodeQualifier
import com.iamkurtgoz.core.common.qualifiers.AppVersionNameQualifier
import com.iamkurtgoz.core.common.qualifiers.DeviceId
import com.iamkurtgoz.core.common.qualifiers.QualifierAuthAuthorizationInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierAuthenticatorInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierConnectivityInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierUserAgentInterceptor
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.data.dataSource.AuthRemoteDataSource
import com.iamkurtgoz.domain.connectivity.ConnectivityObserver
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import com.iamkurtgoz.domain.state.AuthState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @Singleton
    @QualifierAuthAuthorizationInterceptor
    fun provideAuthAuthorizationInterceptor(
        appPreferences: AppPreferences,
    ): Interceptor {
        return AuthAuthorizationInterceptor(appPreferences)
    }

    @Provides
    @Singleton
    @QualifierAuthenticatorInterceptor
    fun provideAuthenticatorInterceptor(
        appPreferences: AppPreferences,
        appBuildConfigStatePack: AppBuildConfigStatePack,
        authRemoteDataSource: AuthRemoteDataSource,
        authState: AuthState,
    ): Authenticator = AuthenticatorInterceptor(
        appPreferences = appPreferences,
        appBuildConfigStatePack = appBuildConfigStatePack,
        authRemoteDataSource = authRemoteDataSource,
        authState = authState,
    )

    @Provides
    @Singleton
    @QualifierConnectivityInterceptor
    fun provideConnectivityInterceptor(
        connectivityObserver: ConnectivityObserver,
    ): Interceptor {
        return ConnectivityInterceptor(connectivityObserver)
    }

    @Provides
    @Singleton
    @QualifierUserAgentInterceptor
    fun provideUserAgentInterceptor(
        @DeviceId deviceId: String,
        @AppVersionNameQualifier appVersionName: String,
        @AppVersionCodeQualifier appVersionCode: Int,
        notificationSettingsManager: INotificationSettingsManager,
    ): Interceptor {
        return UserAgentInterceptor(
            deviceId = deviceId,
            appVersionName = appVersionName,
            appVersionCode = appVersionCode,
            notificationSettingsManager = notificationSettingsManager,
        )
    }
}

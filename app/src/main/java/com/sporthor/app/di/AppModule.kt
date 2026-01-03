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
package com.sporthor.app.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import com.iamkurtgoz.core.common.qualifiers.AppVersionCodeQualifier
import com.iamkurtgoz.core.common.qualifiers.AppVersionNameQualifier
import com.iamkurtgoz.core.common.qualifiers.BaseUrlQualifier
import com.iamkurtgoz.core.common.qualifiers.DefaultDispatcher
import com.iamkurtgoz.core.common.qualifiers.DeviceId
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.domain.state.AuthState
import com.sporthor.`as`.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(app: Application) = app

    @Singleton
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    fun provideAppBuildConfigState(): AppBuildConfigStatePack {
        val appBuildConfigStatePack = AppBuildConfigStatePack()
        appBuildConfigStatePack.packageName = BuildConfig.APPLICATION_ID
        appBuildConfigStatePack.buildType = BuildConfig.BUILD_TYPE
        appBuildConfigStatePack.flavor = BuildConfig.FLAVOR
        appBuildConfigStatePack.isDebug = BuildConfig.DEBUG
        appBuildConfigStatePack.websiteAddress = BuildConfig.WEBSITE_ADDRESS
        appBuildConfigStatePack.termsAddress = BuildConfig.TERMS_ADDRESS
        appBuildConfigStatePack.privacyPolicyAddress = BuildConfig.PRIVACY_POLICY_ADDRESS
        appBuildConfigStatePack.contactAddress = BuildConfig.CONTACT_ADDRESS
        appBuildConfigStatePack.googlePlayAddress = BuildConfig.GOOGLE_PLAY_ADDRESS
        appBuildConfigStatePack.googlePlayAccountProfileAddress = BuildConfig.GOOGLE_PLAY_DEV_ACCOUNT_PROFILE_ADDRESS
        appBuildConfigStatePack.apiUrl = BuildConfig.API_URL
        appBuildConfigStatePack.socketUrl = BuildConfig.SOCKET_URL
        appBuildConfigStatePack.mediaFilePrefixUrl = BuildConfig.MEDIA_FILE_PREFIX_URL
        appBuildConfigStatePack.googleServiceClientId = BuildConfig.GOOGLE_SERVICE_CLIENT_ID
        appBuildConfigStatePack.googleMapsKey = BuildConfig.GOOGLE_MAPS_KEY
        appBuildConfigStatePack.versionCode = BuildConfig.VERSION_CODE
        appBuildConfigStatePack.versionName = BuildConfig.VERSION_NAME
        return appBuildConfigStatePack
    }

    @Singleton
    @Provides
    @BaseUrlQualifier
    fun provideBaseUrl(): String = BuildConfig.API_URL

    @Singleton
    @Provides
    @DeviceId
    @SuppressLint("HardwareIds")
    fun provideDeviceId(@ApplicationContext context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )
    }

    @Singleton
    @Provides
    @AppVersionNameQualifier
    fun provideAppVersionName(): String = BuildConfig.VERSION_NAME

    @Singleton
    @Provides
    @AppVersionCodeQualifier
    fun provideAppVersionCode(): Int = BuildConfig.VERSION_CODE

    @Provides
    @Singleton
    fun provideAuthState(): AuthState = AuthState
}

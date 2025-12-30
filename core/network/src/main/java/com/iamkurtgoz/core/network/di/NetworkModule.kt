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
package com.iamkurtgoz.core.network.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.qualifiers.QualifierConnectivityInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierUserAgentInterceptor
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        @QualifierUserAgentInterceptor userAgentInterceptor: Interceptor,
        @QualifierConnectivityInterceptor connectivityInterceptor: Interceptor,
        appBuildConfigStatePack: AppBuildConfigStatePack,
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(AppDefaults.TIMEOUT_MILLISECOND, TimeUnit.SECONDS)
        builder.readTimeout(AppDefaults.TIMEOUT_MILLISECOND, TimeUnit.SECONDS)
        builder.writeTimeout(AppDefaults.TIMEOUT_MILLISECOND, TimeUnit.SECONDS)

        builder.addNetworkInterceptor(userAgentInterceptor)
        builder.addNetworkInterceptor(connectivityInterceptor)
        if (appBuildConfigStatePack.isDebug) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addNetworkInterceptor(httpLoggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }

        return builder
    }
}

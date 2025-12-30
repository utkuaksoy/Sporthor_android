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

import com.iamkurtgoz.core.common.qualifiers.BaseUrlQualifier
import com.iamkurtgoz.core.common.qualifiers.QualifierAuthAuthorizationInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierAuthenticatorInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierConnectivityInterceptor
import com.iamkurtgoz.core.common.qualifiers.QualifierRetrofitAnonymous
import com.iamkurtgoz.core.common.qualifiers.QualifierRetrofitWithAuthorization
import com.iamkurtgoz.core.common.qualifiers.QualifierRetrofitWithAuthorizationAndAuthenticator
import com.iamkurtgoz.core.network.converter.NullOnEmptyConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {

    @QualifierRetrofitAnonymous
    @Provides
    @Singleton
    fun provideAnonymousRetrofit(
        @BaseUrlQualifier baseUrl: String,
        builder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
        converterFactory: Converter.Factory,
        @QualifierConnectivityInterceptor connectivityInterceptor: Interceptor,
    ): Retrofit {
        okHttpClientBuilder.addInterceptor(connectivityInterceptor)

        val client = okHttpClientBuilder.build()
        return builder.baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(converterFactory)
            .build()
    }

    @QualifierRetrofitWithAuthorization
    @Provides
    @Singleton
    fun provideRetrofitWithAuthorization(
        @BaseUrlQualifier baseUrl: String,
        builder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
        converterFactory: Converter.Factory,
        @QualifierConnectivityInterceptor connectivityInterceptor: Interceptor,
        @QualifierAuthAuthorizationInterceptor authorizationInterceptor: Interceptor,
    ): Retrofit {
        okHttpClientBuilder.addInterceptor(connectivityInterceptor)
        okHttpClientBuilder.addNetworkInterceptor(authorizationInterceptor)

        val client = okHttpClientBuilder.build()
        return builder.baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(converterFactory)
            .build()
    }

    @QualifierRetrofitWithAuthorizationAndAuthenticator
    @Provides
    @Singleton
    fun provideRetrofitWithAuthorizationAndAuthenticator(
        @BaseUrlQualifier baseUrl: String,
        builder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
        converterFactory: Converter.Factory,
        @QualifierAuthenticatorInterceptor authenticatorInterceptor: Authenticator,
        @QualifierConnectivityInterceptor connectivityInterceptor: Interceptor,
        @QualifierAuthAuthorizationInterceptor authorizationInterceptor: Interceptor,
    ): Retrofit {
        okHttpClientBuilder.authenticator(authenticatorInterceptor)
        okHttpClientBuilder.addInterceptor(connectivityInterceptor)
        okHttpClientBuilder.addNetworkInterceptor(authorizationInterceptor)

        val client = okHttpClientBuilder.build()
        return builder.baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(converterFactory)
            .build()
    }
}

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
package com.iamkurtgoz.data.di

import com.iamkurtgoz.data.analytics.AppAnalyticsImpl
import com.iamkurtgoz.data.crypto.CryptoHelperImpl
import com.iamkurtgoz.domain.analytics.AppAnalytics
import com.iamkurtgoz.domain.crypto.CryptoHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Singleton
    @Binds
    fun bindAppAnalytics(impl: AppAnalyticsImpl): AppAnalytics

    @Singleton
    @Binds
    fun bindCryptoHelper(
        impl: CryptoHelperImpl,
    ): CryptoHelper
}

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
package com.iamkurtgoz.feature.home.search.data.di

import com.iamkurtgoz.feature.home.search.data.useCase.AddSearchHistoryUseCaseImpl
import com.iamkurtgoz.feature.home.search.data.useCase.RemoveSearchHistoryUseCaseImpl
import com.iamkurtgoz.feature.home.search.data.useCase.SearchHistoryUseCaseImpl
import com.iamkurtgoz.feature.home.search.data.useCase.SearchUseCaseImpl
import com.iamkurtgoz.feature.home.search.domain.AddSearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.RemoveSearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.SearchHistoryUseCase
import com.iamkurtgoz.feature.home.search.domain.SearchUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindSearchUseCase(
        impl: SearchUseCaseImpl,
    ): SearchUseCase

    @Binds
    @Singleton
    abstract fun bindSearchHistoryUseCase(
        impl: SearchHistoryUseCaseImpl,
    ): SearchHistoryUseCase

    @Binds
    @Singleton
    abstract fun bindAddSearchHistoryUseCase(
        impl: AddSearchHistoryUseCaseImpl,
    ): AddSearchHistoryUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveSearchHistoryUseCase(
        impl: RemoveSearchHistoryUseCaseImpl,
    ): RemoveSearchHistoryUseCase
}

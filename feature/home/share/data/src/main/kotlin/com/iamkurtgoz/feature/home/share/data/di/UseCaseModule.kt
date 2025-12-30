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
package com.iamkurtgoz.feature.home.share.data.di

import com.iamkurtgoz.feature.home.share.data.mapper.LocalMediaUIMapperImpl
import com.iamkurtgoz.feature.home.share.data.useCase.LocalMediaUseCaseImpl
import com.iamkurtgoz.feature.home.share.domain.mapper.LocalMediaUIMapper
import com.iamkurtgoz.feature.home.share.domain.useCase.LocalMediaUseCase
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
    abstract fun bindLocalMediaUseCase(
        impl: LocalMediaUseCaseImpl,
    ): LocalMediaUseCase

    @Binds
    @Singleton
    abstract fun bindLocalMediaUIMapper(
        impl: LocalMediaUIMapperImpl,
    ): LocalMediaUIMapper
}

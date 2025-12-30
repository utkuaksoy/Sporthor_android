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
package com.iamkurtgoz.feature.home.editTrainingGroup.data.di

import com.iamkurtgoz.feature.home.editTrainingGroup.data.useCase.GetRecommendedGroupNamesUseCaseImpl
import com.iamkurtgoz.feature.home.editTrainingGroup.data.useCase.GetSeasonsUseCaseImpl
import com.iamkurtgoz.feature.home.editTrainingGroup.data.useCase.UpdateTrainingGroupUseCaseImpl
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.GetRecommendedGroupNamesUseCase
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.GetSeasonsUseCase
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.useCase.UpdateTrainingGroupUseCase
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
    abstract fun bindGetSeasonsUseCase(
        impl: GetSeasonsUseCaseImpl,
    ): GetSeasonsUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateTrainingGroupUseCase(
        impl: UpdateTrainingGroupUseCaseImpl,
    ): UpdateTrainingGroupUseCase

    @Binds
    @Singleton
    abstract fun bindGetRecommendedGroupNamesUseCase(
        impl: GetRecommendedGroupNamesUseCaseImpl,
    ): GetRecommendedGroupNamesUseCase
}

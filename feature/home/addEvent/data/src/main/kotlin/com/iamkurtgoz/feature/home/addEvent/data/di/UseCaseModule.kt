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
package com.iamkurtgoz.feature.home.addEvent.data.di

import com.iamkurtgoz.feature.home.addEvent.data.useCase.AddTaskTypeUseCaseImpl
import com.iamkurtgoz.feature.home.addEvent.data.useCase.AddTaskUseCaseImpl
import com.iamkurtgoz.feature.home.addEvent.data.useCase.GetTaskTypesUseCaseImpl
import com.iamkurtgoz.feature.home.addEvent.data.useCase.GetTrainingGroupUserUseCaseImpl
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.AddTaskTypeUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.AddTaskUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.GetTaskTypesUseCase
import com.iamkurtgoz.feature.home.addEvent.domain.useCase.GetTrainingGroupUserUseCase
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
    abstract fun bindGetTaskTypesUseCase(
        impl: GetTaskTypesUseCaseImpl,
    ): GetTaskTypesUseCase

    @Binds
    @Singleton
    abstract fun bindAddTaskTypeUseCase(
        impl: AddTaskTypeUseCaseImpl,
    ): AddTaskTypeUseCase

    @Binds
    @Singleton
    abstract fun bindGetTrainingGroupUserUseCase(
        impl: GetTrainingGroupUserUseCaseImpl,
    ): GetTrainingGroupUserUseCase

    @Binds
    @Singleton
    abstract fun bindAddTaskUseCase(
        impl: AddTaskUseCaseImpl,
    ): AddTaskUseCase
}

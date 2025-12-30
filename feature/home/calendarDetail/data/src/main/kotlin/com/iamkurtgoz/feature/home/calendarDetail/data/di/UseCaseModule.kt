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
package com.iamkurtgoz.feature.home.calendarDetail.data.di

import com.iamkurtgoz.feature.home.calendarDetail.data.useCase.GetCalendarDetailUseCaseImpl
import com.iamkurtgoz.feature.home.calendarDetail.data.useCase.RpeSurveyUseCaseImpl
import com.iamkurtgoz.feature.home.calendarDetail.domain.useCase.GetCalendarDetailUseCase
import com.iamkurtgoz.feature.home.calendarDetail.domain.useCase.RpeSurveyUseCase
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
    abstract fun bindGetCalendarDetailUseCase(
        impl: GetCalendarDetailUseCaseImpl,
    ): GetCalendarDetailUseCase

    @Binds
    @Singleton
    abstract fun bindRpeSurveyUseCase(
        impl: RpeSurveyUseCaseImpl,
    ): RpeSurveyUseCase
}

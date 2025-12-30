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
package com.iamkurtgoz.feature.home.dashboard.data.di

import com.iamkurtgoz.feature.home.dashboard.data.useCase.AddCommentUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.DashboardFeedUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.DeletePostUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.GetCommentsUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.GetMenuUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.HidePostUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.LikePostUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.ReportPostUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.data.useCase.StoryFeedUseCaseImpl
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.AddCommentUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.DashboardFeedAsyncUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.DeletePostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.GetCommentsUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.HidePostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.LikePostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.MenuUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.ReportPostUseCase
import com.iamkurtgoz.feature.home.dashboard.domain.useCase.StoryFeedUseCase
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
    abstract fun bindStoryFeedUseCase(
        impl: StoryFeedUseCaseImpl,
    ): StoryFeedUseCase

    @Binds
    @Singleton
    abstract fun bindDashboardFeedAsyncUseCase(
        impl: DashboardFeedUseCaseImpl,
    ): DashboardFeedAsyncUseCase

    @Binds
    @Singleton
    abstract fun bindLikePostUseCase(
        impl: LikePostUseCaseImpl,
    ): LikePostUseCase

    @Binds
    @Singleton
    abstract fun bindGetCommentsUseCase(
        impl: GetCommentsUseCaseImpl,
    ): GetCommentsUseCase

    @Binds
    @Singleton
    abstract fun bindAddCommentUseCase(
        impl: AddCommentUseCaseImpl,
    ): AddCommentUseCase

    @Binds
    @Singleton
    abstract fun bindGetMenuUseCase(
        impl: GetMenuUseCaseImpl,
    ): MenuUseCase

    @Binds
    @Singleton
    abstract fun bindHidePostUseCase(
        impl: HidePostUseCaseImpl,
    ): HidePostUseCase

    @Binds
    @Singleton
    abstract fun bindReportPostUseCase(
        impl: ReportPostUseCaseImpl,
    ): ReportPostUseCase

    @Binds
    @Singleton
    abstract fun bindDeletePostUseCase(
        impl: DeletePostUseCaseImpl,
    ): DeletePostUseCase
}

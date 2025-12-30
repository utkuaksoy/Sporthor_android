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
package com.iamkurtgoz.feature.home.profile.postDetail.data.di

import com.iamkurtgoz.feature.home.profile.postDetail.data.useCase.AddCommentUseCaseImpl
import com.iamkurtgoz.feature.home.profile.postDetail.data.useCase.GetCommentsUseCaseImpl
import com.iamkurtgoz.feature.home.profile.postDetail.data.useCase.GetUserPostsUseCaseImpl
import com.iamkurtgoz.feature.home.profile.postDetail.data.useCase.HidePostUseCaseImpl
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.AddCommentUseCase
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.GetCommentsUseCase
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.HidePostUseCase
import com.iamkurtgoz.feature.home.profile.postDetail.domain.useCase.UserPostsUseCase
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
    abstract fun bindDashboardFeedAsyncUseCase(
        impl: GetUserPostsUseCaseImpl,
    ): UserPostsUseCase

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
    abstract fun bindHidePostUseCase(
        impl: HidePostUseCaseImpl,
    ): HidePostUseCase
}

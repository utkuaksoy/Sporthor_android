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
package com.iamkurtgoz.feature.home.share.complete.data.di

import com.iamkurtgoz.feature.home.share.complete.data.useCase.CreatePostUseCaseImpl
import com.iamkurtgoz.feature.home.share.complete.data.useCase.CreateStoryUseCaseImpl
import com.iamkurtgoz.feature.home.share.complete.data.useCase.ImageUploadUseCaseImpl
import com.iamkurtgoz.feature.home.share.complete.data.useCase.VideoUploadUseCaseImpl
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.CreatePostUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.CreateStoryUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.share.complete.domain.useCase.VideoUploadUseCase
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
    abstract fun bindImageUploadUseCase(
        impl: ImageUploadUseCaseImpl,
    ): ImageUploadUseCase

    @Binds
    @Singleton
    abstract fun bindVideoUploadUseCase(
        impl: VideoUploadUseCaseImpl,
    ): VideoUploadUseCase

    @Binds
    @Singleton
    abstract fun bindCreatePostUseCase(
        impl: CreatePostUseCaseImpl,
    ): CreatePostUseCase

    @Binds
    @Singleton
    abstract fun bindCreateStoryUseCase(
        impl: CreateStoryUseCaseImpl,
    ): CreateStoryUseCase
}

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
package com.iamkurtgoz.feature.home.storyViewer.data.di

import com.iamkurtgoz.feature.home.storyViewer.data.useCase.DeleteStoryUseCaseImpl
import com.iamkurtgoz.feature.home.storyViewer.data.useCase.StoryFeedUseCaseImpl
import com.iamkurtgoz.feature.home.storyViewer.data.useCase.WatchedStoryUseCaseImpl
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.DeleteStoryUseCase
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.StoryFeedUseCase
import com.iamkurtgoz.feature.home.storyViewer.domain.useCase.WatchedStoryUseCase
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
    abstract fun bindWatchedStoryUseCase(
        impl: WatchedStoryUseCaseImpl,
    ): WatchedStoryUseCase
    
    @Binds
    @Singleton
    abstract fun bindDeleteStoryUseCase(
        impl: DeleteStoryUseCaseImpl,
    ): DeleteStoryUseCase
}

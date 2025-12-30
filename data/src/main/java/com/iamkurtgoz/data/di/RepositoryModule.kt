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

import com.iamkurtgoz.data.repository.AuthRepositoryImpl
import com.iamkurtgoz.data.repository.CalendarRepositoryImpl
import com.iamkurtgoz.data.repository.ChatRepositoryImpl
import com.iamkurtgoz.data.repository.CoachRepositoryImpl
import com.iamkurtgoz.data.repository.ConfigurationRepositoryImpl
import com.iamkurtgoz.data.repository.LocalMediaRepositoryImpl
import com.iamkurtgoz.data.repository.LocationRepositoryImpl
import com.iamkurtgoz.data.repository.ManagerRepositoryImpl
import com.iamkurtgoz.data.repository.ProfileRepositoryImpl
import com.iamkurtgoz.data.repository.SocialRepositoryImpl
import com.iamkurtgoz.data.repository.TeamRepositoryImpl
import com.iamkurtgoz.data.repository.UploadRepositoryImpl
import com.iamkurtgoz.domain.repository.AuthRepository
import com.iamkurtgoz.domain.repository.CalendarRepository
import com.iamkurtgoz.domain.repository.ChatRepository
import com.iamkurtgoz.domain.repository.CoachRepository
import com.iamkurtgoz.domain.repository.ConfigurationRepository
import com.iamkurtgoz.domain.repository.LocalMediaRepository
import com.iamkurtgoz.domain.repository.LocationRepository
import com.iamkurtgoz.domain.repository.ManagerRepository
import com.iamkurtgoz.domain.repository.ProfileRepository
import com.iamkurtgoz.domain.repository.SocialRepository
import com.iamkurtgoz.domain.repository.TeamRepository
import com.iamkurtgoz.domain.repository.UploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Singleton
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun bindConfigurationRepository(impl: ConfigurationRepositoryImpl): ConfigurationRepository

    @Singleton
    @Binds
    fun bindTeamRepository(impl: TeamRepositoryImpl): TeamRepository

    @Singleton
    @Binds
    fun bindSocialRepository(impl: SocialRepositoryImpl): SocialRepository

    @Singleton
    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    fun bindChatRemoteRepository(impl: ChatRepositoryImpl): ChatRepository

    @Singleton
    @Binds
    fun bindUploadRepository(impl: UploadRepositoryImpl): UploadRepository

    @Singleton
    @Binds
    fun bindLocalMediaRepository(impl: LocalMediaRepositoryImpl): LocalMediaRepository

    @Singleton
    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Singleton
    @Binds
    fun bindManagerRepository(impl: ManagerRepositoryImpl): ManagerRepository

    @Singleton
    @Binds
    fun bindCoachRepository(impl: CoachRepositoryImpl): CoachRepository

    @Singleton
    @Binds
    fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository
}

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
package com.iamkurtgoz.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.datastore.preferences.AppPreferencesImpl
import com.iamkurtgoz.core.datastore.serializer.UserPreferencesSerializer
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.dataStore.UserPreferencesState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UserDataStoreModule {

    private const val DATA_STORE_NAME = "user-preferences-crypt-v2"

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferencesState> {
        return DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler { UserPreferencesState() },
        )
    }

    @Singleton
    @Provides
    fun provideAppPreferences(
        appBuildConfigStatePack: AppBuildConfigStatePack,
        dataStore: DataStore<UserPreferencesState>,
    ): AppPreferences = AppPreferencesImpl(
        dataStore = dataStore,
        appBuildConfigStatePack = appBuildConfigStatePack,
    )
}

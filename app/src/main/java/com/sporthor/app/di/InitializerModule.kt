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
package com.sporthor.app.di

import com.iamkurtgoz.core.common.common.initializer.Initializer
import com.iamkurtgoz.core.datastore.UserDataStoreInitializer
import com.iamkurtgoz.core.firebase.FirebaseInitializer
import com.iamkurtgoz.core.timber.TimberInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class InitializerModule {
    @Binds
    @IntoSet
    abstract fun firebaseInitialize(initializer: FirebaseInitializer): Initializer

    @Binds
    @IntoSet
    abstract fun userDataStoreInitialize(initializer: UserDataStoreInitializer): Initializer

    @Binds
    @IntoSet
    abstract fun timberInitialize(initializer: TimberInitializer): Initializer
}

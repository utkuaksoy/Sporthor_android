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
package com.iamkurtgoz.core.datastore

import com.iamkurtgoz.core.common.common.initializer.Initializer
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.domain.dataStore.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStoreInitializer @Inject constructor(
    private val appPreferences: AppPreferences,
    private val globalScope: CoroutineScope,
) : Initializer {
    override fun initialize() {
        globalScope.launch {
            val currentPreferenceState = appPreferences.currentPreferenceState.firstOrNull()?.openCount ?: AppDefaults.ZERO
            appPreferences.setOpenCount(currentPreferenceState.plus(AppDefaults.ONE))
        }
    }
}

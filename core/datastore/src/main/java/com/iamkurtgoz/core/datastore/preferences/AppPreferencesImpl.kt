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
package com.iamkurtgoz.core.datastore.preferences

import androidx.datastore.core.DataStore
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.dataStore.UserPreferencesState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

internal class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferencesState>,
    appBuildConfigStatePack: AppBuildConfigStatePack,
) : AppPreferences {

    @OptIn(DelicateCoroutinesApi::class)
    private val singleThread = newSingleThreadContext("${appBuildConfigStatePack.packageName}-user-preferences-race-condition-thread")

    override val currentPreferenceState: Flow<UserPreferencesState>
        get() = dataStore.data

    override suspend fun clear(): Unit = withContext(singleThread) {
        dataStore.updateData {
            UserPreferencesState()
        }
    }

    override suspend fun state(state: UserPreferencesState?): UserPreferencesState? = withContext(singleThread) {
        return@withContext state?.let { safetyState -> dataStore.updateData { safetyState } }
    }

    override suspend fun setOpenCount(openCount: Int): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(openCount = openCount)
            }
        }
    }

    override suspend fun setFirebaseToken(firebaseToken: String?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(firebaseToken = firebaseToken)
            }
        }
    }

    override suspend fun setLastGdprCheckDate(lastGdprCheckDate: LocalDate?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(lastGdprCheckDate = lastGdprCheckDate)
            }
        }
    }

    override suspend fun setRefreshToken(refreshToken: String?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(refreshToken = refreshToken)
            }
        }
    }

    override suspend fun setAccessToken(accessToken: String?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(accessToken = accessToken)
            }
        }
    }

    override suspend fun setLogin(isLogin: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isLogin = isLogin)
            }
        }
    }

    override suspend fun setUserId(userId: String?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(userId = userId)
            }
        }
    }

    override suspend fun setProfilePhoto(profilePhoto: String?): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(profilePhoto = profilePhoto)
            }
        }
    }

    override suspend fun setUserInfoPageCompleted(isUserInfoPageCompleted: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isUserInfoPageCompleted = isUserInfoPageCompleted)
            }
        }
    }

    override suspend fun setOnboardingPageCompleted(isOnboardingPageCompleted: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isOnboardingPageCompleted = isOnboardingPageCompleted)
            }
        }
    }

    override suspend fun setUserTeamsPageCompleted(isUserTeamsPageCompleted: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isUserTeamsPageCompleted = isUserTeamsPageCompleted)
            }
        }
    }

    override suspend fun setIsAskedNotificationPermission(isAskedNotificationPermission: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isAskedNotificationPermission = isAskedNotificationPermission)
            }
        }
    }

    override suspend fun setSelectedTrainer(isSelectedTrainer: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isSelectedTrainer = isSelectedTrainer)
            }
        }
    }

    override suspend fun setSelectedClubOfficial(isSelectedClubOfficial: Boolean): UserPreferencesState? = withContext(singleThread) {
        return@withContext currentPreferenceState.firstOrNull()?.let { safetyState ->
            dataStore.updateData {
                safetyState.copy(isSelectedClubOfficial = isSelectedClubOfficial)
            }
        }
    }
}

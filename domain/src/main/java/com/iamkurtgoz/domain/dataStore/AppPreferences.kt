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
package com.iamkurtgoz.domain.dataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

interface AppPreferences {
    val currentPreferenceState: Flow<UserPreferencesState>
    suspend fun clear()
    suspend fun state(state: UserPreferencesState?): UserPreferencesState?
    suspend fun setOpenCount(openCount: Int): UserPreferencesState?
    suspend fun setFirebaseToken(firebaseToken: String?): UserPreferencesState?
    suspend fun setLastGdprCheckDate(lastGdprCheckDate: LocalDate?): UserPreferencesState?
    suspend fun setRefreshToken(refreshToken: String?): UserPreferencesState?
    suspend fun setAccessToken(accessToken: String?): UserPreferencesState?
    suspend fun setLogin(isLogin: Boolean): UserPreferencesState?
    suspend fun setUserId(userId: String?): UserPreferencesState?
    suspend fun setProfilePhoto(profilePhoto: String?): UserPreferencesState?
    suspend fun setOnboardingPageCompleted(isOnboardingPageCompleted: Boolean): UserPreferencesState?
    suspend fun setUserInfoPageCompleted(isUserInfoPageCompleted: Boolean): UserPreferencesState?
    suspend fun setUserTeamsPageCompleted(isUserTeamsPageCompleted: Boolean): UserPreferencesState?
    suspend fun setIsAskedNotificationPermission(isAskedNotificationPermission: Boolean): UserPreferencesState?
    suspend fun setSelectedTrainer(isSelectedTrainer: Boolean): UserPreferencesState?
    suspend fun setSelectedClubOfficial(isSelectedClubOfficial: Boolean): UserPreferencesState?
}

object FakeAppPreferences : AppPreferences {
    private var userPreferencesState: UserPreferencesState = UserPreferencesState()

    override val currentPreferenceState: Flow<UserPreferencesState>
        get() = flow { emit(userPreferencesState) }

    override suspend fun clear() {
        userPreferencesState = UserPreferencesState()
    }

    override suspend fun state(state: UserPreferencesState?): UserPreferencesState {
        state?.let { userPreferencesState = state }
        return userPreferencesState
    }

    override suspend fun setOpenCount(openCount: Int): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(openCount = openCount)
        return userPreferencesState
    }

    override suspend fun setFirebaseToken(firebaseToken: String?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(firebaseToken = firebaseToken)
        return userPreferencesState
    }

    override suspend fun setLastGdprCheckDate(lastGdprCheckDate: LocalDate?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(lastGdprCheckDate = lastGdprCheckDate)
        return userPreferencesState
    }

    override suspend fun setRefreshToken(refreshToken: String?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(refreshToken = refreshToken)
        return userPreferencesState
    }

    override suspend fun setAccessToken(accessToken: String?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(accessToken = accessToken)
        return userPreferencesState
    }

    override suspend fun setLogin(isLogin: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isLogin = isLogin)
        return userPreferencesState
    }

    override suspend fun setUserId(userId: String?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(userId = userId)
        return userPreferencesState
    }

    override suspend fun setProfilePhoto(profilePhoto: String?): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(profilePhoto = profilePhoto)
        return userPreferencesState
    }

    override suspend fun setOnboardingPageCompleted(isOnboardingPageCompleted: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isOnboardingPageCompleted = isOnboardingPageCompleted)
        return userPreferencesState
    }

    override suspend fun setUserInfoPageCompleted(isUserInfoPageCompleted: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isUserInfoPageCompleted = isUserInfoPageCompleted)
        return userPreferencesState
    }

    override suspend fun setUserTeamsPageCompleted(isUserTeamsPageCompleted: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isUserTeamsPageCompleted = isUserTeamsPageCompleted)
        return userPreferencesState
    }

    override suspend fun setIsAskedNotificationPermission(isAskedNotificationPermission: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isAskedNotificationPermission = isAskedNotificationPermission)
        return userPreferencesState
    }

    override suspend fun setSelectedTrainer(isSelectedTrainer: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isSelectedTrainer = isSelectedTrainer)
        return userPreferencesState
    }

    override suspend fun setSelectedClubOfficial(isSelectedClubOfficial: Boolean): UserPreferencesState {
        userPreferencesState = userPreferencesState.copy(isSelectedClubOfficial = isSelectedClubOfficial)
        return userPreferencesState
    }
}

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

import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.domain.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserPreferencesState(
    val openCount: Int = 0,
    val firebaseToken: String? = null,
    @Serializable(LocalDateSerializer::class)
    val lastGdprCheckDate: LocalDate? = null,
    val refreshToken: String? = null,
    val accessToken: String? = null,
    val isLogin: Boolean = false,
    val userId: String? = null,
    val profilePhoto: String? = null,
    val isUserInfoPageCompleted: Boolean = false,
    val isOnboardingPageCompleted: Boolean = false,
    val isUserTeamsPageCompleted: Boolean = false,
    val isAskedNotificationPermission: Boolean = false,
    val isSelectedTrainer: Boolean = false,
    val isSelectedClubOfficial: Boolean = false,
) {
    fun increaseOpenCount(): UserPreferencesState {
        return this.copy(
            openCount = openCount.plus(AppDefaults.ONE),
        )
    }

    val customUserRole: CustomUserRole
        get() {
            if (isSelectedTrainer && isSelectedClubOfficial) {
                return CustomUserRole.CLUB_OFFICIAL_AND_TRAINER
            }
            if (isSelectedTrainer) {
                return CustomUserRole.TRAINER
            }
            if (isSelectedClubOfficial) {
                return CustomUserRole.CLUB_OFFICIAL
            }
            return CustomUserRole.OTHER
        }
}

enum class CustomUserRole {
    TRAINER,
    CLUB_OFFICIAL,
    CLUB_OFFICIAL_AND_TRAINER,
    OTHER,
}

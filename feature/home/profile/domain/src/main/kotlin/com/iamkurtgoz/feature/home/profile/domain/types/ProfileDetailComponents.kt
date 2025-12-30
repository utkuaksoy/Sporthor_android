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
package com.iamkurtgoz.feature.home.profile.domain.types

enum class ProfileDetailComponents(
    val id: String,
    val type: String,
) {
    ProfileCard(
        id = "profile_information_profile_card",
        type = "ProfileCard",
    ),
    CurrentTeams(
        id = "current_teams_container_component",
        type = "CurrentTeams",
    ),
    FeaturedSkills(
        id = "featured_skills_component",
        type = "FeaturedSkills",
    ),
    Tournaments(
        id = "tournaments_container_component",
        type = "Tournaments",
    ),
    CareerHistory(
        id = "carear_container_component",
        type = "CareerHistory",
    ),
    ;

    companion object {
        fun fromValue(value: String): ProfileDetailComponents? {
            return when (value.lowercase()) {
                ProfileCard.id.lowercase() -> ProfileCard
                CurrentTeams.id.lowercase() -> CurrentTeams
                FeaturedSkills.id.lowercase() -> FeaturedSkills
                Tournaments.id.lowercase() -> Tournaments
                CareerHistory.id.lowercase() -> CareerHistory
                else -> null
            }
        }
    }
}

fun String?.toProfileDetailComponents(): ProfileDetailComponents? {
    return this?.let { ProfileDetailComponents.fromValue(it) }
}

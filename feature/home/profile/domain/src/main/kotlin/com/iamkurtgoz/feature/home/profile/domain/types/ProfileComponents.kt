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

enum class ProfileComponents(
    val id: String,
    val type: String,
) {
    ProfileInfo(
        id = "profile_info_component",
        type = "ProfileInfo",
    ),
    Teams(
        id = "teams_container_component",
        type = "Teams",
    ),
    ProfileAbout(
        id = "profile_about_component",
        type = "ProfileAbout",
    ),
    ProfileActionButtons(
        id = "profile_action_buttons_component",
        type = "ProfileActionButtons",
    ),
    NextMatches(
        id = "next_matches_component",
        type = "NextMatches",
    ),
    Segments(
        id = "segments_component",
        type = "Segments",
    ),
    ;

    companion object {
        fun fromValue(value: String): ProfileComponents? {
            return when (value.lowercase()) {
                ProfileInfo.id.lowercase() -> ProfileInfo
                Teams.id.lowercase() -> Teams
                ProfileAbout.id.lowercase() -> ProfileAbout
                ProfileActionButtons.id.lowercase() -> ProfileActionButtons
                NextMatches.id.lowercase() -> NextMatches
                Segments.id.lowercase() -> Segments
                else -> null
            }
        }
    }
}

fun String?.toProfileComponent(): ProfileComponents? {
    return this?.let { ProfileComponents.fromValue(it) }
}

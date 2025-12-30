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

enum class ProfileActionButtonComponentType {
    Primary,
    Outline,
}

enum class ProfileActionButtonType(val type: String) {
    Follow(type = "follow"),
    Following(type = "following"),
    FollowRequestSent(type = "followRequestSent"),
    Message(type = "message"),
    Invite(type = "invite"),
    EditProfile(type = "editProfile"),
    ;

    val title: String
        get() {
            return when (this) {
                Follow -> "Takip Et" // TODO: Localize
                Following -> "Takiptesin" // TODO: Localize
                FollowRequestSent -> "Takip isteği gönderildi" // TODO: Localize
                Message -> "Mesaj" // TODO: Localize
                Invite -> "Davet Et" // TODO: Localize
                EditProfile -> "Profili Düzenle" // TODO: Localize
            }
        }

    val buttonType: ProfileActionButtonComponentType
        get() {
            return when (this) {
                Follow -> ProfileActionButtonComponentType.Primary
                else -> ProfileActionButtonComponentType.Outline
            }
        }

    companion object {
        fun fromValue(value: String): ProfileActionButtonType {
            return when (value) {
                "follow" -> Follow
                "following" -> Following
                "followRequestSent" -> FollowRequestSent
                "message" -> Message
                "invite" -> Invite
                "editProfile" -> EditProfile
                else -> Follow
            }
        }
    }
}

fun String.toProfileActionButtonType(): ProfileActionButtonType {
    return this.let { ProfileActionButtonType.fromValue(it) }
}

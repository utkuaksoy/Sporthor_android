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
package com.iamkurtgoz.feature.home.chat.newChat.domain.model

data class MyFriendsUIModel(
    val friends: List<MyFriendsFriendItemUIModel?>?,
)

data class MyFriendsFriendItemUIModel(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
) {
    fun isMatch(text: String): Boolean {
        return name?.contains(text, ignoreCase = true) == true || username?.contains(text, ignoreCase = true) == true
    }
}

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
package com.iamkurtgoz.feature.home.dashboard.domain.mock

class MockStoriesModel {

    fun getMockStories(): List<StoryUserUiModel> {
        return listOf(
            StoryUserUiModel(
                username = "tugce.asar",
                profileImageUrl = "https://randomuser.me/api/portraits/women/1.jpg",
            ),
            StoryUserUiModel(
                username = "efe.yildiz",
                profileImageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
            ),
            StoryUserUiModel(
                username = "asli.turkmen",
                profileImageUrl = "https://randomuser.me/api/portraits/women/3.jpg",
            ),
            StoryUserUiModel(
                username = "cem.kaya",
                profileImageUrl = "https://randomuser.me/api/portraits/men/4.jpg",
            ),
            StoryUserUiModel(
                username = "efe.yildiz",
                profileImageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
            ),
            StoryUserUiModel(
                username = "asli.turkmen",
                profileImageUrl = "https://randomuser.me/api/portraits/women/3.jpg",
            ),
            StoryUserUiModel(
                username = "cem.kaya",
                profileImageUrl = "https://randomuser.me/api/portraits/men/4.jpg",
            ),
        )
    }
}

data class StoryUserUiModel(
    val profileImageUrl: String?,
    val username: String?,
)

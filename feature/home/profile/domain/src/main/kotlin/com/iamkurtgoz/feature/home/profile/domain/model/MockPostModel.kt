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
package com.iamkurtgoz.feature.home.profile.domain.model

data object MockPostModelData {
    val list: List<MockPostModel> = listOf(
        MockPostModel(
            id = 1,
            images = "https://images.unsplash.com/photo-1526232761682-d26e03ac148e?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 2,
            images = "https://images.unsplash.com/photo-1599058945522-28d584b6f0ff?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 3,
            images = "https://images.unsplash.com/photo-1554068865-24cecd4e34b8?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 4,
            images = "https://images.unsplash.com/photo-1577217534079-41d6bb68ac50?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 5,
            images = "https://images.unsplash.com/photo-1591117207239-788bf8de6c3b?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 6,
            images = "https://images.unsplash.com/photo-1599058917212-d750089bc07e?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 7,
            images = "https://images.unsplash.com/photo-1599058917765-a780eda07a3e?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 8,
            images = "https://images.unsplash.com/photo-1526232761682-d26e03ac148e?q=80&w=600&auto=format&fit=crop",
        ),
        MockPostModel(
            id = 9,
            images = "https://images.unsplash.com/photo-1554068865-24cecd4e34b8?q=80&w=600&auto=format&fit=crop",
        ),
    )
}

data class MockPostModel(
    val id: Int,
    val images: String,
)

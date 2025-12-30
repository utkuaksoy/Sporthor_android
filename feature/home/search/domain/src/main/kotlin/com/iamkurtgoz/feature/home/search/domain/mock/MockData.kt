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
package com.iamkurtgoz.feature.home.search.domain.mock

import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.search.domain.model.SuggestionUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SuggestionUserUIModel
import java.util.UUID

object MockData {
    val imageList: List<SuggestionUIModel> = listOf(
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
        SuggestionUIModel(uuid = UUID.randomUUID().toString()),
    )

    val suggestionUserList: List<SuggestionUserUIModel> = listOf(
        SuggestionUserUIModel(
            uuid = "1",
            index = 0,
            name = "Dilara Çevik",
            isFollowing = false,
            imageResId = resourcesR.drawable.img_user_role_club_official,
            badgeResId = resourcesR.drawable.img_user_role_club_official,
            buttonText = "Takip Et",
        ),
        SuggestionUserUIModel(
            uuid = "2",
            index = 1,
            name = "Celil Kırca",
            isFollowing = true,
            imageResId = resourcesR.drawable.img_user_role_club_official,
            badgeResId = resourcesR.drawable.img_user_role_club_official,
            buttonText = "Takip Ediliyor",
        ),
        SuggestionUserUIModel(
            uuid = "3",
            index = 2,
            name = "Mehmet",
            isFollowing = false,
            imageResId = resourcesR.drawable.img_user_role_club_official,
            badgeResId = resourcesR.drawable.img_user_role_club_official,
            buttonText = "Takip Et",
        ),
        SuggestionUserUIModel(
            uuid = "4",
            index = 3,
            name = "Nesrin",
            isFollowing = false,
            imageResId = resourcesR.drawable.img_user_role_club_official,
            badgeResId = resourcesR.drawable.img_user_role_club_official,
            buttonText = "Takip Et",
        ),
    )
}

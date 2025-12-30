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
package com.iamkurtgoz.fake.model.view

import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.view.BasicSuggestionUserCardModel
import java.util.UUID

object MockBasicSuggestionUserCardModel {
    val itemList = listOf(
        BasicSuggestionUserCardModel(
            uuid = UUID.randomUUID().toString(),
            index = 0,
            isFollowing = false,
            imageData = resourcesR.drawable.img_user_role_club_official,
            badgeData = resourcesR.drawable.img_user_role_club_official,
            name = "Dilara Ç.",
        ),
        BasicSuggestionUserCardModel(
            uuid = UUID.randomUUID().toString(),
            index = 0,
            isFollowing = false,
            imageData = resourcesR.drawable.img_user_role_club_official,
            badgeData = resourcesR.drawable.img_user_role_club_official,
            name = "Mehmet K.",
        ),
        BasicSuggestionUserCardModel(
            uuid = UUID.randomUUID().toString(),
            index = 0,
            isFollowing = false,
            imageData = resourcesR.drawable.img_user_role_club_official,
            badgeData = resourcesR.drawable.img_user_role_club_official,
            name = "Celil K.",
        ),
    )
}

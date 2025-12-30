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
package com.iamkurtgoz.feature.home.chat.newChat.domain.type

import androidx.annotation.DrawableRes
import com.iamkurtgoz.core.resources.R as resourcesR

enum class NewChatRowType(
    @DrawableRes val icon: Int,
    val title: String,
) {
    CREATE_GROUP_CHAT(
        icon = resourcesR.drawable.img_users,
        title = "Grup sohbeti oluştur",
    ), // TODO: Localize
    CREATE_COMMUNITY(
        icon = resourcesR.drawable.img_comment_version_two,
        title = "Topluluk oluştur",
    ), // TODO: Localize
    CONNECT_CONTACTS(
        icon = resourcesR.drawable.img_book_plus,
        title = "Kişilerini bağla",
    ), // TODO: Localize
}

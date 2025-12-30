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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model

data class GetAttachmentsUIModel(
    val files: List<FileUIModel>?,
    val medias: List<MediaItemUIModel>?,
)

data class FromUIModel(
    val id: String?,
    val image: String?,
    val name: String?,
)

data class FileUIModel(
    val content: String?,
    val fileExtension: String?,
    val from: FromUIModel?,
    val id: String?,
    val messageType: Int?,
    val sendDate: String?,
)

data class MediaItemUIModel(
    val content: String?,
    val fileExtension: String?,
    val from: FromUIModel?,
    val id: String?,
    val messageType: Int?,
    val sendDate: String?,
)

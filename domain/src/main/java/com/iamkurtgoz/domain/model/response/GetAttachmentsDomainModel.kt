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
package com.iamkurtgoz.domain.model.response

data class GetAttachmentsDomainModel(
    val files: List<FileDomainModel>?,
    val medias: List<MediaItemDomainModel>?,
)

data class FromDomainModel(
    val id: String?,
    val image: String?,
    val name: String?,
)

data class FileDomainModel(
    val content: String?,
    val fileExtension: String?,
    val from: FromDomainModel?,
    val id: String?,
    val messageType: Int?,
    val sendDate: String?,
)

data class MediaItemDomainModel(
    val content: String?,
    val fileExtension: String?,
    val from: FromDomainModel?,
    val id: String?,
    val messageType: Int?,
    val sendDate: String?,
)

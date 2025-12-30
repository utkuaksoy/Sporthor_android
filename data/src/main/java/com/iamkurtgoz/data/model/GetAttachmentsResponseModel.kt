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
package com.iamkurtgoz.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GetAttachmentsResponseModel(
    @SerialName("files")
    val files: List<FileResponseModel>?,
    @SerialName("medias")
    val medias: List<MediaItemResponseModel>?,
)

@Keep
@Serializable
data class FromResponseModel(
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("name")
    val name: String?,
)

@Keep
@Serializable
data class FileResponseModel(
    @SerialName("content")
    val content: String?,
    @SerialName("fileExtension")
    val fileExtension: String?,
    @SerialName("from")
    val from: FromResponseModel?,
    @SerialName("id")
    val id: String?,
    @SerialName("messageType")
    val messageType: Int?,
    @SerialName("sendDate")
    val sendDate: String?,
)

@Keep
@Serializable
data class MediaItemResponseModel(
    @SerialName("content")
    val content: String?,
    @SerialName("fileExtension")
    val fileExtension: String?,
    @SerialName("from")
    val from: FromResponseModel?,
    @SerialName("id")
    val id: String?,
    @SerialName("messageType")
    val messageType: Int?,
    @SerialName("sendDate")
    val sendDate: String?,
)

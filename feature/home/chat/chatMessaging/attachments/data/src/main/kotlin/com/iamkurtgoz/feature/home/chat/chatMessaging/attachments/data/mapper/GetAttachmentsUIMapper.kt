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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.FileDomainModel
import com.iamkurtgoz.domain.model.response.FromDomainModel
import com.iamkurtgoz.domain.model.response.GetAttachmentsDomainModel
import com.iamkurtgoz.domain.model.response.MediaItemDomainModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model.FileUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model.FromUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model.GetAttachmentsUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model.MediaItemUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetAttachmentsUIMapper @Inject constructor(
    private val fileUIMapper: FileUIMapper,
    private val mediaItemUIMapper: MediaItemUIMapper,
) : IMapper<GetAttachmentsDomainModel, GetAttachmentsUIModel> {

    override fun map(response: GetAttachmentsDomainModel): GetAttachmentsUIModel {
        return with(response) {
            GetAttachmentsUIModel(
                files = files?.map(fileUIMapper::map),
                medias = medias?.map(mediaItemUIMapper::map),
            )
        }
    }
}

@Singleton
internal class FileUIMapper @Inject constructor(
    private val fromUIMapper: FromUIMapper,
) : IMapper<FileDomainModel, FileUIModel> {

    override fun map(response: FileDomainModel): FileUIModel {
        return with(response) {
            FileUIModel(
                content = content,
                fileExtension = fileExtension,
                from = from?.let(fromUIMapper::map),
                id = id,
                messageType = messageType,
                sendDate = sendDate,
            )
        }
    }
}

@Singleton
internal class MediaItemUIMapper @Inject constructor(
    private val fromUIMapper: FromUIMapper,
) : IMapper<MediaItemDomainModel, MediaItemUIModel> {

    override fun map(response: MediaItemDomainModel): MediaItemUIModel {
        return with(response) {
            MediaItemUIModel(
                content = content,
                fileExtension = fileExtension,
                from = from?.let(fromUIMapper::map),
                id = id,
                messageType = messageType,
                sendDate = sendDate,
            )
        }
    }
}

@Singleton
internal class FromUIMapper @Inject constructor() : IMapper<FromDomainModel, FromUIModel> {

    override fun map(response: FromDomainModel): FromUIModel {
        return with(response) {
            FromUIModel(
                id = id,
                image = image,
                name = name,
            )
        }
    }
}

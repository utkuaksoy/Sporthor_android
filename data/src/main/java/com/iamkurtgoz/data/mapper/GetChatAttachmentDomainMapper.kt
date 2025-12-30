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
package com.iamkurtgoz.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.data.model.FileResponseModel
import com.iamkurtgoz.data.model.FromResponseModel
import com.iamkurtgoz.data.model.GetAttachmentsResponseModel
import com.iamkurtgoz.data.model.MediaItemResponseModel
import com.iamkurtgoz.domain.model.response.FileDomainModel
import com.iamkurtgoz.domain.model.response.FromDomainModel
import com.iamkurtgoz.domain.model.response.GetAttachmentsDomainModel
import com.iamkurtgoz.domain.model.response.MediaItemDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetChatAttachmentDomainMapper @Inject constructor(
    private val fileDomainMapper: FileDomainMapper,
    private val mediaItemDomainMapper: MediaItemDomainMapper,
) : IMapper<GetAttachmentsResponseModel, GetAttachmentsDomainModel> {

    override fun map(response: GetAttachmentsResponseModel): GetAttachmentsDomainModel {
        return with(response) {
            GetAttachmentsDomainModel(
                files = files?.filterNotNull()?.map(fileDomainMapper::map),
                medias = medias?.filterNotNull()?.map(mediaItemDomainMapper::map),
            )
        }
    }
}

@Singleton
internal class FileDomainMapper @Inject constructor(
    private val fromDomainMapper: FromDomainMapper,
) : IMapper<FileResponseModel, FileDomainModel> {

    override fun map(response: FileResponseModel): FileDomainModel {
        return with(response) {
            FileDomainModel(
                content = content,
                fileExtension = fileExtension,
                from = from?.let(fromDomainMapper::map),
                id = id,
                messageType = messageType,
                sendDate = sendDate,
            )
        }
    }
}

@Singleton
internal class MediaItemDomainMapper @Inject constructor(
    private val fromDomainMapper: FromDomainMapper,
) : IMapper<MediaItemResponseModel, MediaItemDomainModel> {

    override fun map(response: MediaItemResponseModel): MediaItemDomainModel {
        return with(response) {
            MediaItemDomainModel(
                content = content,
                fileExtension = fileExtension,
                from = from?.let(fromDomainMapper::map),
                id = id,
                messageType = messageType,
                sendDate = sendDate,
            )
        }
    }
}

@Singleton
internal class FromDomainMapper @Inject constructor() : IMapper<FromResponseModel, FromDomainModel> {

    override fun map(response: FromResponseModel): FromDomainModel {
        return with(response) {
            FromDomainModel(
                id = id,
                image = image,
                name = name,
            )
        }
    }
}

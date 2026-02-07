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
import com.iamkurtgoz.data.model.GetStoryFeedResponseModel
import com.iamkurtgoz.data.model.MediaResponseModel
import com.iamkurtgoz.data.model.StoryDetailResponseModel
import com.iamkurtgoz.data.model.StoryResponseModel
import com.iamkurtgoz.domain.model.response.MediaDomainModel
import com.iamkurtgoz.domain.model.response.StoryDetailDomainModel
import com.iamkurtgoz.domain.model.response.StoryDomainModel
import com.iamkurtgoz.domain.model.response.StoryFeedDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StoryFeedDomainMapper @Inject constructor(
    private val storyMapper: StoryDomainMapper,
) : IMapper<GetStoryFeedResponseModel, StoryFeedDomainModel> {

    override fun map(response: GetStoryFeedResponseModel): StoryFeedDomainModel {
        return with(response) {
            StoryFeedDomainModel(
                stories = stories?.map { storyMapper.map(it) },
            )
        }
    }
}

internal class StoryDomainMapper @Inject constructor(
    private val detailMapper: StoryDetailDomainMapper,
) : IMapper<StoryResponseModel, StoryDomainModel> {

    override fun map(response: StoryResponseModel): StoryDomainModel {
        return with(response) {
            StoryDomainModel(
                userId = userId,
                username = username,
                name = name,
                lastName = lastName,
                isOwn = isOwn,
                isWatched = isWatched,
                profileImageUrl = profileImageUrl,
                details = details?.map(detailMapper::map),
            )
        }
    }
}

internal class StoryDetailDomainMapper @Inject constructor(
    private val mediaMapper: MediaDomainMapper,
) : IMapper<StoryDetailResponseModel, StoryDetailDomainModel> {

    override fun map(response: StoryDetailResponseModel): StoryDetailDomainModel {
        return with(response) {
            StoryDetailDomainModel(
                storyId = storyId,
                link = link,
                linkDescription = linkDescription,
                media = media?.let { mediaMapper.map(it) },
                publishDate = publishDate,
                isWatched = isWatched,
            )
        }
    }
}

internal class MediaDomainMapper @Inject constructor() : IMapper<MediaResponseModel, MediaDomainModel> {

    override fun map(response: MediaResponseModel): MediaDomainModel {
        return with(response) {
            MediaDomainModel(
                type = type,
                url = url,
            )
        }
    }
}

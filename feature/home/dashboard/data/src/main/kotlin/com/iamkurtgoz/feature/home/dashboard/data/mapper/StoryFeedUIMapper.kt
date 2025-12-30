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
package com.iamkurtgoz.feature.home.dashboard.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.MediaDomainModel
import com.iamkurtgoz.domain.model.response.StoryDetailDomainModel
import com.iamkurtgoz.domain.model.response.StoryDomainModel
import com.iamkurtgoz.domain.model.response.StoryFeedDomainModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.MediaUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.StoryDetailUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.StoryFeedUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.StoryUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StoryFeedUIMapper @Inject constructor(
    private val storyUIMapper: StoryUIMapper,
) : IMapper<StoryFeedDomainModel, StoryFeedUIModel> {
    override fun map(response: StoryFeedDomainModel): StoryFeedUIModel {
        return with(response) {
            StoryFeedUIModel(
                stories = stories?.map(storyUIMapper::map),
            )
        }
    }
}

internal class StoryUIMapper @Inject constructor(
    private val detailUIMapper: StoryDetailUIMapper,
) : IMapper<StoryDomainModel, StoryUIModel> {
    override fun map(response: StoryDomainModel): StoryUIModel {
        return with(response) {
            StoryUIModel(
                userId = userId,
                username = username,
                name = name,
                lastName = lastName,
                isOwn = isOwn,
                isWatched = isWatched,
                profileImageUrl = profileImageUrl,
                details = details?.map(detailUIMapper::map),
            )
        }
    }
}

internal class StoryDetailUIMapper @Inject constructor(
    private val mediaUIMapper: MediaUIMapper,
) : IMapper<StoryDetailDomainModel, StoryDetailUIModel> {

    override fun map(response: StoryDetailDomainModel): StoryDetailUIModel {
        return with(response) {
            StoryDetailUIModel(
                storyId = storyId,
                media = media?.let { mediaUIMapper.map(it) },
                publishDate = publishDate,
                isWatched = isWatched,
            )
        }
    }
}

internal class MediaUIMapper @Inject constructor() : IMapper<MediaDomainModel, MediaUIModel> {
    override fun map(response: MediaDomainModel): MediaUIModel {
        return with(response) {
            MediaUIModel(
                type = type,
                url = url,
            )
        }
    }
}

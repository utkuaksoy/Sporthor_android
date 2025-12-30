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
import com.iamkurtgoz.data.model.DashboardPostCommentResponseModel
import com.iamkurtgoz.data.model.DashboardPostLikedUserResponseModel
import com.iamkurtgoz.data.model.DashboardPostMediaResponseModel
import com.iamkurtgoz.data.model.DashboardPostResponseModel
import com.iamkurtgoz.data.model.GetFeedAsyncResponseModel
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostCommentDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostLikedUserDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostMediaDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DashboardFeedDomainMapper @Inject constructor(
    private val postDomainMapper: DashboardPostDomainMapper,
) : IMapper<GetFeedAsyncResponseModel, DashboardFeedDomainModel> {

    override fun map(response: GetFeedAsyncResponseModel): DashboardFeedDomainModel {
        return DashboardFeedDomainModel(
            posts = response.posts?.map { postDomainMapper.map(it) },
        )
    }
}

internal class DashboardPostDomainMapper @Inject constructor(
    private val mediaMapper: DashboardPostMediaDomainMapper,
    private val likedUserMapper: DashboardPostLikedUserDomainMapper,
    private val commentMapper: DashboardPostCommentDomainMapper,
) : IMapper<DashboardPostResponseModel, DashboardPostDomainModel> {

    override fun map(response: DashboardPostResponseModel): DashboardPostDomainModel {
        return with(response) {
            DashboardPostDomainModel(
                id = id,
                description = description,
                media = media?.map { mediaMapper.map(it) },
                likeCount = likeCount,
                commentCount = commentCount,
                createdAt = createdAt,
                userId = userId,
                username = username,
                name = name,
                lastName = lastName,
                profileImageUrl = profileImageUrl,
                lastLikedUsers = lastLikedUsers?.map { likedUserMapper.map(it) },
                lastComments = lastComments?.map { commentMapper.map(it) },
                isLiked = isLiked,
                time = time,
            )
        }
    }
}

internal class DashboardPostMediaDomainMapper @Inject constructor() : IMapper<DashboardPostMediaResponseModel, DashboardPostMediaDomainModel> {

    override fun map(response: DashboardPostMediaResponseModel): DashboardPostMediaDomainModel {
        return with(response) {
            DashboardPostMediaDomainModel(
                url = url,
                type = type,
            )
        }
    }
}

internal class DashboardPostLikedUserDomainMapper @Inject constructor() : IMapper<DashboardPostLikedUserResponseModel, DashboardPostLikedUserDomainModel> {

    override fun map(response: DashboardPostLikedUserResponseModel): DashboardPostLikedUserDomainModel {
        return with(response) {
            DashboardPostLikedUserDomainModel(
                userId = userId,
                username = username,
                profileImageUrl = profileImageUrl,
            )
        }
    }
}

internal class DashboardPostCommentDomainMapper @Inject constructor() : IMapper<DashboardPostCommentResponseModel, DashboardPostCommentDomainModel> {

    override fun map(response: DashboardPostCommentResponseModel): DashboardPostCommentDomainModel {
        return with(response) {
            DashboardPostCommentDomainModel(
                id = id,
                userId = userId,
                text = text,
                createdAt = createdAt,
                username = username,
                profileImageUrl = profileImageUrl,
            )
        }
    }
}

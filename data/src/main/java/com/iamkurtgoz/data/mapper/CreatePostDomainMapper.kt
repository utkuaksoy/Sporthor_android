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
import com.iamkurtgoz.data.model.CreatePostResponseModel
import com.iamkurtgoz.data.model.LastCommentResponseModel
import com.iamkurtgoz.data.model.LastLikedUserResponseModel
import com.iamkurtgoz.data.model.MediasResponseModel
import com.iamkurtgoz.data.model.PostsResponseModel
import com.iamkurtgoz.domain.model.response.CreatePostDomainModel
import com.iamkurtgoz.domain.model.response.LastCommentDomainModel
import com.iamkurtgoz.domain.model.response.LastLikedUserDomainModel
import com.iamkurtgoz.domain.model.response.MediasDomainModel
import com.iamkurtgoz.domain.model.response.PostsDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CreatePostDomainMapper @Inject constructor(
    private val postsDomainMapper: PostsDomainMapper,
) : IMapper<CreatePostResponseModel, CreatePostDomainModel> {

    override fun map(response: CreatePostResponseModel): CreatePostDomainModel {
        return with(response) {
            CreatePostDomainModel(
                post = post?.let { postsDomainMapper.map(it) },
            )
        }
    }
}

internal class PostsDomainMapper @Inject constructor(
    private val lastCommentDomainMapper: LastCommentDomainMapper,
    private val lastLikedUserDomainMapper: LastLikedUserDomainMapper,
    private val mediasDomainMapper: MediasDomainMapper,
) : IMapper<PostsResponseModel, PostsDomainModel> {

    override fun map(response: PostsResponseModel): PostsDomainModel {
        return with(response) {
            PostsDomainModel(
                commentCount = commentCount,
                createdAt = createdAt,
                description = description,
                id = id,
                isLiked = isLiked,
                lastComments = lastComments?.map { lastCommentDomainMapper.map(it) },
                lastLikedUsers = lastLikedUsers?.map { lastLikedUserDomainMapper.map(it) },
                lastName = lastName,
                likeCount = likeCount,
                media = media?.map { mediasDomainMapper.map(it) },
                name = name,
                profileImageUrl = profileImageUrl,
                userId = userId,
                username = username,
            )
        }
    }
}

internal class LastCommentDomainMapper @Inject constructor() :
    IMapper<LastCommentResponseModel, LastCommentDomainModel> {

    override fun map(response: LastCommentResponseModel): LastCommentDomainModel {
        return with(response) {
            LastCommentDomainModel(
                createdAt = createdAt,
                id = id,
                profileImageUrl = profileImageUrl,
                text = text,
                userId = userId,
                username = username,
            )
        }
    }
}

internal class LastLikedUserDomainMapper @Inject constructor() :
    IMapper<LastLikedUserResponseModel, LastLikedUserDomainModel> {

    override fun map(response: LastLikedUserResponseModel): LastLikedUserDomainModel {
        return with(response) {
            LastLikedUserDomainModel(
                profileImageUrl = profileImageUrl,
                userId = userId,
                username = username,
            )
        }
    }
}

internal class MediasDomainMapper @Inject constructor() :
    IMapper<MediasResponseModel, MediasDomainModel> {

    override fun map(response: MediasResponseModel): MediasDomainModel {
        return with(response) {
            MediasDomainModel(
                type = type,
                url = url,
            )
        }
    }
}

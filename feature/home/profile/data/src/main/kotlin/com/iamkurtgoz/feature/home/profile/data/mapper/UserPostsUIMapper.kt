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
package com.iamkurtgoz.feature.home.profile.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostCommentDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostLikedUserDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostMediaDomainModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsCommentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsItemUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsLikedUserUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsMediaUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserPostsUIMapper @Inject constructor(
    private val dashboardPostUIMapper: UserPostsItemUIMapper,
) : IMapper<DashboardFeedDomainModel, UserPostsUIModel> {

    override fun map(response: DashboardFeedDomainModel): UserPostsUIModel {
        return with(response) {
            UserPostsUIModel(
                posts = posts?.map(dashboardPostUIMapper::map),
            )
        }
    }
}

@Singleton
internal class UserPostsItemUIMapper @Inject constructor(
    private val dashboardPostMediaUIMapper: UserPostsMediaUIMapper,
    private val dashboardPostLikedUserUIMapper: UserPostsLikedUserUIMapper,
    private val dashboardPostCommentUIMapper: UserPostsCommentUIMapper,
) : IMapper<DashboardPostDomainModel, UserPostsItemUIModel> {

    override fun map(response: DashboardPostDomainModel): UserPostsItemUIModel {
        return with(response) {
            UserPostsItemUIModel(
                id = id,
                description = description,
                media = media?.map(dashboardPostMediaUIMapper::map),
                likeCount = likeCount,
                commentCount = commentCount,
                createdAt = createdAt,
                userId = userId,
                username = username,
                name = name,
                lastName = lastName,
                profileImageUrl = profileImageUrl,
                lastLikedUsers = lastLikedUsers?.map(dashboardPostLikedUserUIMapper::map),
                lastComments = lastComments?.map(dashboardPostCommentUIMapper::map),
                isLiked = isLiked,
                time = time,
            )
        }
    }
}

@Singleton
internal class UserPostsMediaUIMapper @Inject constructor() :
    IMapper<DashboardPostMediaDomainModel, UserPostsMediaUIModel> {

    override fun map(response: DashboardPostMediaDomainModel): UserPostsMediaUIModel {
        return with(response) {
            UserPostsMediaUIModel(
                url = url,
                type = type,
            )
        }
    }
}

@Singleton
internal class UserPostsLikedUserUIMapper @Inject constructor() :
    IMapper<DashboardPostLikedUserDomainModel, UserPostsLikedUserUIModel> {

    override fun map(response: DashboardPostLikedUserDomainModel): UserPostsLikedUserUIModel {
        return with(response) {
            UserPostsLikedUserUIModel(
                userId = userId,
                username = username,
                profileImageUrl = profileImageUrl,
            )
        }
    }
}

@Singleton
internal class UserPostsCommentUIMapper @Inject constructor() :
    IMapper<DashboardPostCommentDomainModel, UserPostsCommentUIModel> {

    override fun map(response: DashboardPostCommentDomainModel): UserPostsCommentUIModel {
        return with(response) {
            UserPostsCommentUIModel(
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

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
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostCommentDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostLikedUserDomainModel
import com.iamkurtgoz.domain.model.response.DashboardPostMediaDomainModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostCommentUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostLikedUserUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostMediaUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.GetFeedAsyncUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DashboardFeedAsyncUIMapper @Inject constructor(
    private val dashboardPostUIMapper: DashboardPostUIMapper,
) : IMapper<DashboardFeedDomainModel, GetFeedAsyncUIModel> {

    override fun map(response: DashboardFeedDomainModel): GetFeedAsyncUIModel {
        return with(response) {
            GetFeedAsyncUIModel(
                posts = posts?.map(dashboardPostUIMapper::map),
            )
        }
    }
}

@Singleton
internal class DashboardPostUIMapper @Inject constructor(
    private val dashboardPostMediaUIMapper: DashboardPostMediaUIMapper,
    private val dashboardPostLikedUserUIMapper: DashboardPostLikedUserUIMapper,
    private val dashboardPostCommentUIMapper: DashboardPostCommentUIMapper,
) : IMapper<DashboardPostDomainModel, DashboardPostUIModel> {

    override fun map(response: DashboardPostDomainModel): DashboardPostUIModel {
        return with(response) {
            DashboardPostUIModel(
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
internal class DashboardPostMediaUIMapper @Inject constructor() :
    IMapper<DashboardPostMediaDomainModel, DashboardPostMediaUIModel> {

    override fun map(response: DashboardPostMediaDomainModel): DashboardPostMediaUIModel {
        return with(response) {
            DashboardPostMediaUIModel(
                url = url,
                type = type,
            )
        }
    }
}

@Singleton
internal class DashboardPostLikedUserUIMapper @Inject constructor() :
    IMapper<DashboardPostLikedUserDomainModel, DashboardPostLikedUserUIModel> {

    override fun map(response: DashboardPostLikedUserDomainModel): DashboardPostLikedUserUIModel {
        return with(response) {
            DashboardPostLikedUserUIModel(
                userId = userId,
                username = username,
                profileImageUrl = profileImageUrl,
            )
        }
    }
}

@Singleton
internal class DashboardPostCommentUIMapper @Inject constructor() :
    IMapper<DashboardPostCommentDomainModel, DashboardPostCommentUIModel> {

    override fun map(response: DashboardPostCommentDomainModel): DashboardPostCommentUIModel {
        return with(response) {
            DashboardPostCommentUIModel(
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

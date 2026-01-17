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
package com.iamkurtgoz.data.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.data.core.CoreRepository
import com.iamkurtgoz.data.dataSource.SocialRemoteDataSource
import com.iamkurtgoz.data.mapper.BlockedUsersDomainMapper
import com.iamkurtgoz.data.mapper.CommentsDomainMapper
import com.iamkurtgoz.data.mapper.CreatePostDomainMapper
import com.iamkurtgoz.data.mapper.DashboardFeedDomainMapper
import com.iamkurtgoz.data.mapper.FollowUserDomainMapper
import com.iamkurtgoz.data.mapper.LikePostDomainMapper
import com.iamkurtgoz.data.mapper.SearchHistoryDomainMapper
import com.iamkurtgoz.data.mapper.SearchSocialDomainMapper
import com.iamkurtgoz.data.mapper.StoryFeedDomainMapper
import com.iamkurtgoz.data.mapper.UserRelationDomainMapper
import com.iamkurtgoz.domain.model.request.AddCommentRequest
import com.iamkurtgoz.domain.model.request.AddSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.ConfirmationFollowRequest
import com.iamkurtgoz.domain.model.request.CreatePostRequest
import com.iamkurtgoz.domain.model.request.CreateStoryRequest
import com.iamkurtgoz.domain.model.request.DeletePostRequest
import com.iamkurtgoz.domain.model.request.DeleteStoryRequest
import com.iamkurtgoz.domain.model.request.FollowUserRequest
import com.iamkurtgoz.domain.model.request.HidePostRequest
import com.iamkurtgoz.domain.model.request.LikePostRequest
import com.iamkurtgoz.domain.model.request.RemoveBlockUserRequest
import com.iamkurtgoz.domain.model.request.RemoveSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.ReportPostRequest
import com.iamkurtgoz.domain.model.request.WatchedStoryRequest
import com.iamkurtgoz.domain.model.response.BlockedUsersDomainModel
import com.iamkurtgoz.domain.model.response.CommentsDomainModel
import com.iamkurtgoz.domain.model.response.CreatePostDomainModel
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.FollowUserDomainModel
import com.iamkurtgoz.domain.model.response.LikePostDomainModel
import com.iamkurtgoz.domain.model.response.SearchHistorySocialDomainModel
import com.iamkurtgoz.domain.model.response.SearchSocialDomainModel
import com.iamkurtgoz.domain.model.response.StoryFeedDomainModel
import com.iamkurtgoz.domain.model.response.UserRelationDomainModel
import com.iamkurtgoz.domain.repository.SocialRepository
import javax.inject.Inject

internal class SocialRepositoryImpl @Inject constructor(
    private val socialRemoteDataSource: SocialRemoteDataSource,
    private val searchSocialDomainMapper: SearchSocialDomainMapper,
    private val userRelationDomainMapper: UserRelationDomainMapper,
    private val searchHistoryDomainMapper: SearchHistoryDomainMapper,
    private val followUserDomainMapper: FollowUserDomainMapper,
    private val dashboardFeedDomainMapper: DashboardFeedDomainMapper,
    private val storyFeedDomainMapper: StoryFeedDomainMapper,
    private val getCommentsDomainMapper: CommentsDomainMapper,
    private val createPostDomainMapper: CreatePostDomainMapper,
    private val likePostDomainMapper: LikePostDomainMapper,
    private val blockedUsersDomainMapper: BlockedUsersDomainMapper,
) : SocialRepository, CoreRepository() {
    override suspend fun getSearch(searchTerm: String, role: Int?): RestResult<SearchSocialDomainModel> = mapToRestResult {
        socialRemoteDataSource.getSearch(searchTerm, role)
    }.mapOnSuccess {
        searchSocialDomainMapper.map(it)
    }

    override suspend fun getSearchHistory(): RestResult<SearchHistorySocialDomainModel> = mapToRestResult {
        socialRemoteDataSource.getSearchHistory()
    }.mapOnSuccess {
        searchHistoryDomainMapper.map(it)
    }

    override suspend fun addSearchHistory(body: AddSearchHistoryRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.addSearchHistory(body)
    }

    override suspend fun removeSearchHistory(body: RemoveSearchHistoryRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.removeSearchHistory(body)
    }.mapOnSuccess { }

    override suspend fun getFollowers(userId: String?): RestResult<UserRelationDomainModel> = mapToRestResult {
        socialRemoteDataSource.getFollowers(userId)
    }.mapOnSuccess {
        userRelationDomainMapper.map(it)
    }

    override suspend fun getFollowing(userId: String?, role: Int?): RestResult<UserRelationDomainModel> = mapToRestResult {
        socialRemoteDataSource.getFollowing(userId, role)
    }.mapOnSuccess {
        userRelationDomainMapper.map(it)
    }

    override suspend fun followUser(body: FollowUserRequest): RestResult<FollowUserDomainModel> = mapToRestResult {
        socialRemoteDataSource.followUser(body)
    }.mapOnSuccess(followUserDomainMapper::map)

    override suspend fun unFollowUser(body: FollowUserRequest): RestResult<FollowUserDomainModel> = mapToRestResult {
        socialRemoteDataSource.unFollowUser(body)
    }.mapOnSuccess(followUserDomainMapper::map)

    override suspend fun confirmationFollow(body: ConfirmationFollowRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.confirmationFollow(body)
    }.mapOnSuccess { }

    override suspend fun getFeedAsync(page: Int?, pageSize: Int?): RestResult<DashboardFeedDomainModel> = mapToRestResult {
        socialRemoteDataSource.getFeedAsync(page, pageSize)
    }.mapOnSuccess {
        dashboardFeedDomainMapper.map(it)
    }

    override suspend fun getStoryFeed(): RestResult<StoryFeedDomainModel> = mapToRestResult {
        socialRemoteDataSource.getStoryFeed()
    }.mapOnSuccess {
        storyFeedDomainMapper.map(it)
    }

    override suspend fun getComments(postId: String?): RestResult<CommentsDomainModel> = mapToRestResult {
        socialRemoteDataSource.getComments(postId)
    }.mapOnSuccess {
        getCommentsDomainMapper.map(it)
    }

    override suspend fun createPost(body: CreatePostRequest): RestResult<CreatePostDomainModel> = mapToRestResult {
        socialRemoteDataSource.createPost(body)
    }.mapOnSuccess {
        createPostDomainMapper.map(it)
    }

    override suspend fun deletePost(body: DeletePostRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.deletePost(body)
    }.mapOnSuccess {}

    override suspend fun reportPost(body: ReportPostRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.reportPost(body)
    }.mapOnSuccess {}

    override suspend fun hidePost(body: HidePostRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.hidePost(body)
    }.mapOnSuccess {}

    override suspend fun likePost(body: LikePostRequest): RestResult<LikePostDomainModel> = mapToRestResult {
        socialRemoteDataSource.likePost(body)
    }.mapOnSuccess {
        likePostDomainMapper.map(it)
    }

    override suspend fun unlikePost(body: LikePostRequest): RestResult<LikePostDomainModel> = mapToRestResult {
        socialRemoteDataSource.unlikePost(body)
    }.mapOnSuccess {
        likePostDomainMapper.map(it)
    }

    override suspend fun addComment(body: AddCommentRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.addComment(body)
    }.mapOnSuccess {}

    override suspend fun deleteStory(body: DeleteStoryRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.deleteStory(body)
    }.mapOnSuccess {}

    override suspend fun createStory(body: CreateStoryRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.createStory(body)
    }.mapOnSuccess {}

    override suspend fun watchedStory(body: WatchedStoryRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.watchedStory(body)
    }.mapOnSuccess {}

    override suspend fun getBlockedUsers(): RestResult<BlockedUsersDomainModel> = mapToRestResult {
        socialRemoteDataSource.getBlockedUsers()
    }.mapOnSuccess {
        blockedUsersDomainMapper.map(it)
    }

    override suspend fun removeBlockUser(body: RemoveBlockUserRequest): RestResult<Unit> = mapToRestResult {
        socialRemoteDataSource.removeBlockUser(body)
    }.mapOnSuccess {}
}

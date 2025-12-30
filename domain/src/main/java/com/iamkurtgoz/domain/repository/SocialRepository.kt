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
package com.iamkurtgoz.domain.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.AddCommentRequest
import com.iamkurtgoz.domain.model.request.AddSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.CreatePostRequest
import com.iamkurtgoz.domain.model.request.CreateStoryRequest
import com.iamkurtgoz.domain.model.request.DeletePostRequest
import com.iamkurtgoz.domain.model.request.DeleteStoryRequest
import com.iamkurtgoz.domain.model.request.FollowUserRequest
import com.iamkurtgoz.domain.model.request.HidePostRequest
import com.iamkurtgoz.domain.model.request.LikePostRequest
import com.iamkurtgoz.domain.model.request.RemoveSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.WatchedStoryRequest
import com.iamkurtgoz.domain.model.request.ReportPostRequest
import com.iamkurtgoz.domain.model.response.CommentsDomainModel
import com.iamkurtgoz.domain.model.response.CreatePostDomainModel
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.FollowUserDomainModel
import com.iamkurtgoz.domain.model.response.LikePostDomainModel
import com.iamkurtgoz.domain.model.response.SearchHistorySocialDomainModel
import com.iamkurtgoz.domain.model.response.SearchSocialDomainModel
import com.iamkurtgoz.domain.model.response.StoryFeedDomainModel
import com.iamkurtgoz.domain.model.response.UserRelationDomainModel

interface SocialRepository {
    suspend fun getSearch(searchTerm: String): RestResult<SearchSocialDomainModel>
    suspend fun getSearchHistory(): RestResult<SearchHistorySocialDomainModel>
    suspend fun addSearchHistory(body: AddSearchHistoryRequest): RestResult<Unit>
    suspend fun removeSearchHistory(body: RemoveSearchHistoryRequest): RestResult<Unit>
    suspend fun getFollowers(userId: String?): RestResult<UserRelationDomainModel>
    suspend fun getFollowing(userId: String?): RestResult<UserRelationDomainModel>
    suspend fun followUser(body: FollowUserRequest): RestResult<FollowUserDomainModel>
    suspend fun unFollowUser(body: FollowUserRequest): RestResult<FollowUserDomainModel>
    suspend fun getFeedAsync(page: Int?, pageSize: Int?): RestResult<DashboardFeedDomainModel>
    suspend fun getStoryFeed(): RestResult<StoryFeedDomainModel>
    suspend fun getComments(postId: String?): RestResult<CommentsDomainModel>
    suspend fun createPost(body: CreatePostRequest): RestResult<CreatePostDomainModel>
    suspend fun deletePost(body: DeletePostRequest): RestResult<Unit>
    suspend fun reportPost(body: ReportPostRequest): RestResult<Unit>
    suspend fun hidePost(body: HidePostRequest): RestResult<Unit>
    suspend fun likePost(body: LikePostRequest): RestResult<LikePostDomainModel>
    suspend fun unlikePost(body: LikePostRequest): RestResult<LikePostDomainModel>
    suspend fun addComment(body: AddCommentRequest): RestResult<Unit>
    suspend fun deleteStory(body: DeleteStoryRequest): RestResult<Unit>
    suspend fun createStory(body: CreateStoryRequest): RestResult<Unit>
    suspend fun watchedStory(body: WatchedStoryRequest): RestResult<Unit>
}

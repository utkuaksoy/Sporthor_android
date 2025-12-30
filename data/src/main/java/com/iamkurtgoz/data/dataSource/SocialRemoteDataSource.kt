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
package com.iamkurtgoz.data.dataSource

import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.CreatePostResponseModel
import com.iamkurtgoz.data.model.FollowUserResponseModel
import com.iamkurtgoz.data.model.GetCommentsResponseModel
import com.iamkurtgoz.data.model.GetFeedAsyncResponseModel
import com.iamkurtgoz.data.model.GetStoryFeedResponseModel
import com.iamkurtgoz.data.model.LikePostResponseModel
import com.iamkurtgoz.data.model.SearchHistorySocialResponseModel
import com.iamkurtgoz.data.model.SearchSocialResponseModel
import com.iamkurtgoz.data.model.UserRelationResponseModel
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

interface SocialRemoteDataSource {
    suspend fun getSearch(searchTerm: String): BaseResponse<SearchSocialResponseModel>
    suspend fun getSearchHistory(): BaseResponse<SearchHistorySocialResponseModel>
    suspend fun addSearchHistory(body: AddSearchHistoryRequest): BaseResponse<Unit>
    suspend fun removeSearchHistory(body: RemoveSearchHistoryRequest): BaseResponse<Unit>
    suspend fun getFollowers(userId: String?): BaseResponse<UserRelationResponseModel>
    suspend fun getFollowing(userId: String?): BaseResponse<UserRelationResponseModel>
    suspend fun followUser(body: FollowUserRequest): BaseResponse<FollowUserResponseModel>
    suspend fun unFollowUser(body: FollowUserRequest): BaseResponse<FollowUserResponseModel>
    suspend fun getFeedAsync(page: Int?, pageSize: Int?): BaseResponse<GetFeedAsyncResponseModel>
    suspend fun getStoryFeed(): BaseResponse<GetStoryFeedResponseModel>
    suspend fun getComments(postId: String?): BaseResponse<GetCommentsResponseModel>
    suspend fun createPost(body: CreatePostRequest): BaseResponse<CreatePostResponseModel>
    suspend fun deletePost(body: DeletePostRequest): BaseResponse<Unit>
    suspend fun reportPost(body: ReportPostRequest): BaseResponse<Unit>
    suspend fun hidePost(body: HidePostRequest): BaseResponse<Unit>
    suspend fun likePost(body: LikePostRequest): BaseResponse<LikePostResponseModel>
    suspend fun unlikePost(body: LikePostRequest): BaseResponse<LikePostResponseModel>
    suspend fun addComment(body: AddCommentRequest): BaseResponse<Unit>
    suspend fun deleteStory(body: DeleteStoryRequest): BaseResponse<Unit>
    suspend fun createStory(body: CreateStoryRequest): BaseResponse<Unit>
    suspend fun watchedStory(body: WatchedStoryRequest): BaseResponse<Unit>
}

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
package com.iamkurtgoz.core.api.dataSource.social

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.social.SocialService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.SocialRemoteDataSource
import com.iamkurtgoz.data.model.BlockedUsersResponseModel
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class SocialRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val socialService: SocialService,
) : SocialRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getSearch(searchTerm: String, role: Int?): BaseResponse<SearchSocialResponseModel> = requestRetrofit {
        socialService.getSearch(searchTerm, role)
    }

    override suspend fun getSearchHistory(): BaseResponse<SearchHistorySocialResponseModel> = requestRetrofit {
        socialService.getSearchHistory()
    }

    override suspend fun addSearchHistory(body: AddSearchHistoryRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.addSearchHistory(body)
    }

    override suspend fun removeSearchHistory(body: RemoveSearchHistoryRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.removeSearchHistory(body)
    }

    override suspend fun getFollowers(userId: String?): BaseResponse<UserRelationResponseModel> = requestRetrofit {
        socialService.getFollowers(userId)
    }

    override suspend fun getFollowing(userId: String?, role: Int?): BaseResponse<UserRelationResponseModel> = requestRetrofit {
        socialService.getFollowing(userId, role)
    }

    override suspend fun followUser(body: FollowUserRequest): BaseResponse<FollowUserResponseModel> = requestRetrofit {
        socialService.followUser(body)
    }

    override suspend fun unFollowUser(body: FollowUserRequest): BaseResponse<FollowUserResponseModel> = requestRetrofit {
        socialService.unFollowUser(body)
    }

    override suspend fun confirmationFollow(body: ConfirmationFollowRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.confirmationFollow(body)
    }

    override suspend fun getFeedAsync(page: Int?, pageSize: Int?): BaseResponse<GetFeedAsyncResponseModel> = requestRetrofit {
        socialService.getFeedAsync(page, pageSize)
    }

    override suspend fun getStoryFeed(): BaseResponse<GetStoryFeedResponseModel> = requestRetrofit {
        socialService.getStoryFeed()
    }

    override suspend fun getComments(postId: String?): BaseResponse<GetCommentsResponseModel> = requestRetrofit {
        socialService.getComments(postId)
    }

    override suspend fun createPost(body: CreatePostRequest): BaseResponse<CreatePostResponseModel> = requestRetrofit {
        socialService.createPost(body)
    }

    override suspend fun deletePost(body: DeletePostRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.deletePost(body)
    }

    override suspend fun reportPost(body: ReportPostRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.reportPost(body)
    }

    override suspend fun hidePost(body: HidePostRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.hidePost(body)
    }

    override suspend fun likePost(body: LikePostRequest): BaseResponse<LikePostResponseModel> = requestRetrofit {
        socialService.likePost(body)
    }

    override suspend fun unlikePost(body: LikePostRequest): BaseResponse<LikePostResponseModel> = requestRetrofit {
        socialService.unlikePost(body)
    }

    override suspend fun addComment(body: AddCommentRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.addComment(body)
    }

    override suspend fun deleteStory(body: DeleteStoryRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.deleteStory(body)
    }

    override suspend fun createStory(body: CreateStoryRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.createStory(body)
    }

    override suspend fun watchedStory(body: WatchedStoryRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.watchedStory(body)
    }

    override suspend fun getBlockedUsers(): BaseResponse<BlockedUsersResponseModel> = requestRetrofit {
        socialService.getBlockedUsers()
    }

    override suspend fun removeBlockUser(body: RemoveBlockUserRequest): BaseResponse<Unit> = requestRetrofit {
        socialService.removeBlockUser(body)
    }
}

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
package com.iamkurtgoz.core.api.service.social

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
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
import com.iamkurtgoz.domain.model.request.CreatePostRequest
import com.iamkurtgoz.domain.model.request.CreateStoryRequest
import com.iamkurtgoz.domain.model.request.DeletePostRequest
import com.iamkurtgoz.domain.model.request.DeleteStoryRequest
import com.iamkurtgoz.domain.model.request.FollowUserRequest
import com.iamkurtgoz.domain.model.request.HidePostRequest
import com.iamkurtgoz.domain.model.request.LikePostRequest
import com.iamkurtgoz.domain.model.request.RemoveSearchHistoryRequest
import com.iamkurtgoz.domain.model.request.ReportPostRequest
import com.iamkurtgoz.domain.model.request.WatchedStoryRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface SocialService {
    @GET("Social/Search")
    suspend fun getSearch(@Query("SearchTerm") searchTerm: String, @Query("Role") role: Int? = null): Response<BaseResponse<SearchSocialResponseModel>>

    @GET("Social/GetSearchHistory")
    suspend fun getSearchHistory(): Response<BaseResponse<SearchHistorySocialResponseModel>>

    @POST("Social/AddSearchHistory")
    suspend fun addSearchHistory(@Body body: AddSearchHistoryRequest): Response<BaseResponse<Unit>>

    @POST("Social/RemoveSearchHistory")
    suspend fun removeSearchHistory(@Body body: RemoveSearchHistoryRequest): Response<BaseResponse<Unit>>

    @GET("Social/GetFollowers")
    suspend fun getFollowers(@Query("UserId") userId: String?): Response<BaseResponse<UserRelationResponseModel>>

    @GET("Social/GetFollowing")
    suspend fun getFollowing(
        @Query("UserId") userId: String?,
        @Query("role") role: Int? = null,
    ): Response<BaseResponse<UserRelationResponseModel>>


    @POST("Social/FollowUser")
    suspend fun followUser(@Body body: FollowUserRequest): Response<BaseResponse<FollowUserResponseModel>>

    @POST("Social/UnFollowUser")
    suspend fun unFollowUser(@Body body: FollowUserRequest): Response<BaseResponse<FollowUserResponseModel>>

    @GET("Social/GetFeedAsync")
    suspend fun getFeedAsync(@Query("Page") page: Int?, @Query("PageSize") pageSize: Int?): Response<BaseResponse<GetFeedAsyncResponseModel>>

    @GET("Social/GetStoryFeed")
    suspend fun getStoryFeed(): Response<BaseResponse<GetStoryFeedResponseModel>>

    @GET("Social/GetComments")
    suspend fun getComments(@Query("postId") postId: String?): Response<BaseResponse<GetCommentsResponseModel>>

    @POST("Social/CreatePost")
    suspend fun createPost(@Body body: CreatePostRequest): Response<BaseResponse<CreatePostResponseModel>>

    @POST("Social/DeletePost")
    suspend fun deletePost(@Body body: DeletePostRequest): Response<BaseResponse<Unit>>

    @POST("Social/ReportPost")
    suspend fun reportPost(@Body body: ReportPostRequest): Response<BaseResponse<Unit>>

    @POST("Social/HidePost")
    suspend fun hidePost(@Body body: HidePostRequest): Response<BaseResponse<Unit>>

    @POST("Social/LikePost")
    suspend fun likePost(@Body body: LikePostRequest): Response<BaseResponse<LikePostResponseModel>>

    @POST("Social/UnlikePost")
    suspend fun unlikePost(@Body body: LikePostRequest): Response<BaseResponse<LikePostResponseModel>>

    @POST("Social/AddComment")
    suspend fun addComment(@Body body: AddCommentRequest): Response<BaseResponse<Unit>>

    @POST("Social/DeleteStory")
    suspend fun deleteStory(@Body body: DeleteStoryRequest): Response<BaseResponse<Unit>>

    @POST("Social/CreateStory")
    suspend fun createStory(@Body body: CreateStoryRequest): Response<BaseResponse<Unit>>

    @POST("Social/WatchedStory")
    suspend fun watchedStory(@Body body: WatchedStoryRequest): Response<BaseResponse<Unit>>

    @GET("Social/GetBlockUser")
    suspend fun getBlockedUsers(): Response<BaseResponse<BlockedUsersResponseModel>>
}

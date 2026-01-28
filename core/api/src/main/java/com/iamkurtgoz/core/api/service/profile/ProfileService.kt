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
package com.iamkurtgoz.core.api.service.profile

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.BranchesAttributeItemResponseModel
import com.iamkurtgoz.data.model.BranchesResponseModel
import com.iamkurtgoz.data.model.EditProfileResponseModel
import com.iamkurtgoz.data.model.GetFeedAsyncResponseModel
import com.iamkurtgoz.data.model.GetMyRolesResponseModel
import com.iamkurtgoz.data.model.GetNotificationsResponseModel
import com.iamkurtgoz.data.model.MyFriendsResponseModel
import com.iamkurtgoz.data.model.ProfileDetailResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
import com.iamkurtgoz.domain.model.request.UpdateProfileImageRequest
import com.iamkurtgoz.domain.model.request.UpdateProfilePublicPrivateRequest
import com.iamkurtgoz.domain.model.request.UpdateUserRolesRequest
import com.iamkurtgoz.domain.model.request.UpdateConfigurationRequest
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface ProfileService {

    @GET("Profile/GetProfile")
    suspend fun getProfile(@Query("UserId") userId: String?): Response<BaseResponse<ProfileResponseModel>>

    @GET("Profile/GetProfileDetail")
    suspend fun getProfileDetail(@Query("UserId") userId: String?): Response<BaseResponse<ProfileDetailResponseModel>>

    @GET("Profile/GetProfileSummary")
    suspend fun getProfileSummary(): Response<BaseResponse<EditProfileResponseModel>>

    @POST("Profile/UpdateProfileSummary")
    suspend fun updateProfileSummary(@Body body: JsonObject): Response<BaseResponse<Unit>>

    @GET("Profile/GetMyFriends")
    suspend fun getMyFriends(): Response<BaseResponse<MyFriendsResponseModel>>

    @GET("Profile/GetBranches")
    suspend fun getBranches(): Response<BaseResponse<BranchesResponseModel>>

    @GET("Profile/GetBranchAttributes")
    suspend fun getBranchAttributes(@Query("branchId") branchId: String?): Response<BaseResponse<BranchesAttributeItemResponseModel>>

    @POST("Profile/UpdateProfileImage")
    suspend fun updateProfileImage(@Body body: UpdateProfileImageRequest): Response<BaseResponse<Unit>>

    @GET("Social/GetUserPostsAsync")
    suspend fun getUserPostsAsync(@Query("UserId") userId: String?, @Query("Page") page: Int?, @Query("PageSize") pageSize: Int?): Response<BaseResponse<GetFeedAsyncResponseModel>>

    @GET("Profile/GetNotifications")
    suspend fun getNotifications(): Response<BaseResponse<GetNotificationsResponseModel>>

    @GET("Profile/GetMyRoles")
    suspend fun getMyRoles(): Response<BaseResponse<GetMyRolesResponseModel>>

    @POST("Profile/UpdataUserRoles")
    suspend fun updataUserRoles(@Body body: UpdateUserRolesRequest): Response<BaseResponse<Unit>>

    @POST("Authentication/DeleteAccount")
    suspend fun deleteAccount(): Response<BaseResponse<Unit>>

    @POST("Profile/UpdateProfilePublicPrivate")
    suspend fun updateProfilePublicPrivate(@Body body: UpdateProfilePublicPrivateRequest): Response<BaseResponse<Unit>>

    @POST("Profile/UpdateConfiguration")
    suspend fun updateConfiguration(@Body body: UpdateConfigurationRequest): Response<BaseResponse<Unit>>
}

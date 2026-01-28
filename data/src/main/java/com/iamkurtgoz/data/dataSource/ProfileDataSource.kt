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

interface ProfileDataSource {
    suspend fun getProfile(userId: String?): BaseResponse<ProfileResponseModel>
    suspend fun getProfileDetail(userId: String?): BaseResponse<ProfileDetailResponseModel>
    suspend fun getProfileSummary(): BaseResponse<EditProfileResponseModel>
    suspend fun updateProfileSummary(body: JsonObject): BaseResponse<Unit>
    suspend fun getMyFriends(): BaseResponse<MyFriendsResponseModel>
    suspend fun getBranches(): BaseResponse<BranchesResponseModel>
    suspend fun getBranchAttributes(branchId: String?): BaseResponse<BranchesAttributeItemResponseModel>
    suspend fun updateProfileImage(body: UpdateProfileImageRequest): BaseResponse<Unit>
    suspend fun getUserPostsAsync(userId: String?, page: Int?, pageSize: Int?): BaseResponse<GetFeedAsyncResponseModel>
    suspend fun getNotifications(): BaseResponse<GetNotificationsResponseModel>
    suspend fun getMyRoles(): BaseResponse<GetMyRolesResponseModel>
    suspend fun updataUserRoles(body: UpdateUserRolesRequest): BaseResponse<Unit>
    suspend fun deleteAccount(): BaseResponse<Unit>
    suspend fun updateProfilePublicPrivate(body: UpdateProfilePublicPrivateRequest): BaseResponse<Unit>
    suspend fun updateConfiguration(body: UpdateConfigurationRequest): BaseResponse<Unit>
}

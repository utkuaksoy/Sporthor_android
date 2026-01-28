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
import com.iamkurtgoz.domain.model.request.UpdateProfileImageRequest
import com.iamkurtgoz.domain.model.request.UpdateProfilePublicPrivateRequest
import com.iamkurtgoz.domain.model.request.UpdateUserRolesRequest
import com.iamkurtgoz.domain.model.request.UpdateConfigurationRequest
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.domain.model.response.BranchesDomainModel
import com.iamkurtgoz.domain.model.response.DashboardFeedDomainModel
import com.iamkurtgoz.domain.model.response.EditProfileDomainModel
import com.iamkurtgoz.domain.model.response.GetNotificationsDomainModel
import com.iamkurtgoz.domain.model.response.MyFriendsDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDomainModel
import kotlinx.serialization.json.JsonObject

interface ProfileRepository {
    suspend fun getProfile(userId: String?): RestResult<ProfileDomainModel>
    suspend fun getProfileDetail(userId: String?): RestResult<ProfileDetailDomainModel>
    suspend fun getProfileSummary(): RestResult<EditProfileDomainModel>
    suspend fun updateProfileSummary(body: JsonObject): RestResult<Unit>
    suspend fun getMyFriends(): RestResult<MyFriendsDomainModel>
    suspend fun getBranches(): RestResult<BranchesDomainModel>
    suspend fun getBranchAttributes(branchId: String?): RestResult<BranchesAttributeItemDomainModel>
    suspend fun updateProfileImage(body: UpdateProfileImageRequest): RestResult<Unit>
    suspend fun getUserPostsAsync(userId: String?, page: Int?, pageSize: Int?): RestResult<DashboardFeedDomainModel>
    suspend fun getNotifications(): RestResult<List<GetNotificationsDomainModel>>
    suspend fun getMyRoles(): RestResult<List<String>>
    suspend fun updataUserRoles(body: UpdateUserRolesRequest): RestResult<Unit>
    suspend fun deleteAccount(): RestResult<Unit>
    suspend fun updateProfilePublicPrivate(body: UpdateProfilePublicPrivateRequest): RestResult<Unit>
    suspend fun updateConfiguration(body: UpdateConfigurationRequest): RestResult<Unit>
}

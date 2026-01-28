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
import com.iamkurtgoz.data.dataSource.ProfileDataSource
import com.iamkurtgoz.data.mapper.BranchesAttributeItemResponseMapper
import com.iamkurtgoz.data.mapper.BranchesDomainMapper
import com.iamkurtgoz.data.mapper.DashboardFeedDomainMapper
import com.iamkurtgoz.data.mapper.EditProfileDomainMapper
import com.iamkurtgoz.data.mapper.GetNotificationsDomainMapper
import com.iamkurtgoz.data.mapper.MyFriendsDomainMapper
import com.iamkurtgoz.data.mapper.ProfileDetailDomainMapper
import com.iamkurtgoz.data.mapper.ProfileDomainMapper
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
import com.iamkurtgoz.domain.repository.ProfileRepository
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val profileDomainMapper: ProfileDomainMapper,
    private val editProfileDomainMapper: EditProfileDomainMapper,
    private val profileDetailDomainMapper: ProfileDetailDomainMapper,
    private val myFriendsDomainMapper: MyFriendsDomainMapper,
    private val branchesDomainMapper: BranchesDomainMapper,
    private val branchesAttributeItemDomainMapper: BranchesAttributeItemResponseMapper,
    private val dashboardFeedDomainMapper: DashboardFeedDomainMapper,
    private val getNotificationsDomainMapper: GetNotificationsDomainMapper,
) : ProfileRepository, CoreRepository() {

    override suspend fun getProfile(userId: String?): RestResult<ProfileDomainModel> = mapToRestResult {
        profileDataSource.getProfile(userId)
    }.mapOnSuccess {
        profileDomainMapper.map(it)
    }

    override suspend fun getProfileDetail(userId: String?): RestResult<ProfileDetailDomainModel> = mapToRestResult {
        profileDataSource.getProfileDetail(userId)
    }.mapOnSuccess {
        profileDetailDomainMapper.map(it)
    }

    override suspend fun getProfileSummary(): RestResult<EditProfileDomainModel> = mapToRestResult {
        profileDataSource.getProfileSummary()
    }.mapOnSuccess {
        editProfileDomainMapper.map(it)
    }

    override suspend fun updateProfileSummary(body: JsonObject): RestResult<Unit> = mapToRestResult {
        profileDataSource.updateProfileSummary(body)
    }

    override suspend fun getMyFriends(): RestResult<MyFriendsDomainModel> = mapToRestResult {
        profileDataSource.getMyFriends()
    }.mapOnSuccess {
        myFriendsDomainMapper.map(it)
    }

    override suspend fun getBranches(): RestResult<BranchesDomainModel> = mapToRestResult {
        profileDataSource.getBranches()
    }.mapOnSuccess {
        branchesDomainMapper.map(it)
    }

    override suspend fun getBranchAttributes(branchId: String?): RestResult<BranchesAttributeItemDomainModel> = mapToRestResult {
        profileDataSource.getBranchAttributes(branchId)
    }.mapOnSuccess {
        branchesAttributeItemDomainMapper.map(it)
    }

    override suspend fun updateProfileImage(body: UpdateProfileImageRequest): RestResult<Unit> = mapToRestResult {
        profileDataSource.updateProfileImage(body)
    }

    override suspend fun getUserPostsAsync(userId: String?, page: Int?, pageSize: Int?): RestResult<DashboardFeedDomainModel> = mapToRestResult {
        profileDataSource.getUserPostsAsync(userId, page, pageSize)
    }.mapOnSuccess {
        dashboardFeedDomainMapper.map(it)
    }

    override suspend fun getNotifications(): RestResult<List<GetNotificationsDomainModel>> = mapToRestResult {
        profileDataSource.getNotifications()
    }.mapOnSuccess(getNotificationsDomainMapper::map)

    override suspend fun getMyRoles(): RestResult<List<String>> = mapToRestResult {
        profileDataSource.getMyRoles()
    }.mapOnSuccess {
        it.roles?.filterNotNull() ?: listOf()
    }

    override suspend fun updataUserRoles(body: UpdateUserRolesRequest): RestResult<Unit> = mapToRestResult {
        profileDataSource.updataUserRoles(body)
    }

    override suspend fun deleteAccount(): RestResult<Unit> = mapToRestResult {
        profileDataSource.deleteAccount()
    }

    override suspend fun updateProfilePublicPrivate(body: UpdateProfilePublicPrivateRequest): RestResult<Unit> = mapToRestResult {
        profileDataSource.updateProfilePublicPrivate(body)
    }

    override suspend fun updateConfiguration(body: UpdateConfigurationRequest): RestResult<Unit> = mapToRestResult {
        profileDataSource.updateConfiguration(body)
    }
}

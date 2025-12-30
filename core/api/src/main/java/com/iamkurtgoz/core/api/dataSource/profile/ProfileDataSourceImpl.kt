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
package com.iamkurtgoz.core.api.dataSource.profile

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.profile.ProfileService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.ProfileDataSource
import com.iamkurtgoz.data.model.BranchesAttributeItemResponseModel
import com.iamkurtgoz.data.model.BranchesResponseModel
import com.iamkurtgoz.data.model.EditProfileResponseModel
import com.iamkurtgoz.data.model.GetFeedAsyncResponseModel
import com.iamkurtgoz.data.model.GetMyRolesResponseModel
import com.iamkurtgoz.data.model.GetNotificationsResponseModel
import com.iamkurtgoz.data.model.MyFriendsResponseModel
import com.iamkurtgoz.data.model.ProfileDetailResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.model.request.UpdateProfileImageRequest
import com.iamkurtgoz.domain.model.request.UpdateProfilePublicPrivateRequest
import com.iamkurtgoz.domain.model.request.UpdateUserRolesRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject

internal class ProfileDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val profileService: ProfileService,
    private val appPreferences: AppPreferences,
) : ProfileDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getProfile(userId: String?): BaseResponse<ProfileResponseModel> = requestRetrofit {
        profileService.getProfile(userId)
    }.also {
        it.data?.let { session ->
            if (session.info?.isCurrentUser == true) {
                appPreferences.setUserId(session.info?.id)
                appPreferences.setProfilePhoto(session.info?.avatar)
            }
        }
    }

    override suspend fun getProfileDetail(userId: String?): BaseResponse<ProfileDetailResponseModel> = requestRetrofit {
        profileService.getProfileDetail(userId)
    }

    override suspend fun getProfileSummary(): BaseResponse<EditProfileResponseModel> = requestRetrofit {
        profileService.getProfileSummary()
    }

    override suspend fun updateProfileSummary(body: JsonObject): BaseResponse<Unit> = requestRetrofit {
        profileService.updateProfileSummary(body)
    }

    override suspend fun getMyFriends(): BaseResponse<MyFriendsResponseModel> = requestRetrofit {
        profileService.getMyFriends()
    }

    override suspend fun getBranches(): BaseResponse<BranchesResponseModel> = requestRetrofit {
        profileService.getBranches()
    }

    override suspend fun getBranchAttributes(branchId: String?): BaseResponse<BranchesAttributeItemResponseModel> = requestRetrofit {
        profileService.getBranchAttributes(branchId)
    }

    override suspend fun updateProfileImage(body: UpdateProfileImageRequest): BaseResponse<Unit> = requestRetrofit {
        profileService.updateProfileImage(body)
    }

    override suspend fun getUserPostsAsync(userId: String?, page: Int?, pageSize: Int?): BaseResponse<GetFeedAsyncResponseModel> = requestRetrofit {
        profileService.getUserPostsAsync(userId, page, pageSize)
    }

    override suspend fun getNotifications(): BaseResponse<GetNotificationsResponseModel> = requestRetrofit {
        profileService.getNotifications()
    }

    override suspend fun getMyRoles(): BaseResponse<GetMyRolesResponseModel> = requestRetrofit {
        profileService.getMyRoles()
    }

    override suspend fun updataUserRoles(body: UpdateUserRolesRequest): BaseResponse<Unit> = requestRetrofit {
        profileService.updataUserRoles(body)
    }

    override suspend fun deleteAccount(): BaseResponse<Unit> = requestRetrofit {
        profileService.deleteAccount()
    }

    override suspend fun updateProfilePublicPrivate(body: UpdateProfilePublicPrivateRequest): BaseResponse<Unit> = requestRetrofit {
        profileService.updateProfilePublicPrivate(body)
    }
}

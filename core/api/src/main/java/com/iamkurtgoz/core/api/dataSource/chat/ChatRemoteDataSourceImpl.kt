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
package com.iamkurtgoz.core.api.dataSource.chat

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.chat.ChatService
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.ChatRemoteDataSource
import com.iamkurtgoz.data.model.ChatAllMessageResponseModel
import com.iamkurtgoz.data.model.ChatMessageResponseModel
import com.iamkurtgoz.data.model.GenerateChatGroupResponseModel
import com.iamkurtgoz.data.model.GetAttachmentsResponseModel
import com.iamkurtgoz.data.model.GetChatGroupDetailResponseModel
import com.iamkurtgoz.data.model.GetChatGroupSummaryResponseModel
import com.iamkurtgoz.data.model.GetChatUserProfileResponseModel
import com.iamkurtgoz.domain.model.enums.OrderByParams
import com.iamkurtgoz.domain.model.request.GenerateChatGroupRequest
import com.iamkurtgoz.domain.model.request.HideMessagesRequest
import com.iamkurtgoz.domain.model.request.LeaveChatRequest
import com.iamkurtgoz.domain.model.request.UpdateChatGroupRequest
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class ChatRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val chatService: ChatService,
) : ChatRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    override suspend fun getAllMessages(page: Int?): BaseResponse<ChatAllMessageResponseModel> = requestRetrofit {
        chatService.getAllMessages(page)
    }

    override suspend fun getMessages(page: Int?, channelId: String?, orderBy: OrderByParams?): BaseResponse<ChatMessageResponseModel> = requestRetrofit {
        chatService.getMessages(page, channelId, orderBy?.rawValue)
    }

    override suspend fun generateChatGroup(body: GenerateChatGroupRequest): BaseResponse<GenerateChatGroupResponseModel> = requestRetrofit {
        chatService.generateChatGroup(body)
    }

    override suspend fun getChatUserProfile(userId: String?, groupId: String?): BaseResponse<GetChatUserProfileResponseModel> = requestRetrofit {
        chatService.getChatUserProfile(userId, groupId)
    }

    override suspend fun getChatGroupDetail(groupId: String?): BaseResponse<GetChatGroupDetailResponseModel> = requestRetrofit {
        chatService.getChatGroupDetail(groupId)
    }

    override suspend fun getChatGroupSummary(groupId: String?): BaseResponse<GetChatGroupSummaryResponseModel> = requestRetrofit {
        chatService.getChatGroupSummary(groupId)
    }

    override suspend fun getChatUserProfileAttachments(userId: String?, groupId: String?): BaseResponse<GetAttachmentsResponseModel> = requestRetrofit {
        chatService.getChatUserProfileAttachments(userId, groupId)
    }

    override suspend fun hideMessages(body: HideMessagesRequest): BaseResponse<Unit> = requestRetrofit {
        chatService.hideMessages(body)
    }

    override suspend fun leaveChat(body: LeaveChatRequest): BaseResponse<Unit> = requestRetrofit {
        chatService.leaveChat(body)
    }

    override suspend fun updateChatGroup(body: UpdateChatGroupRequest): BaseResponse<Unit> = requestRetrofit {
        chatService.updateChatGroup(body)
    }
}

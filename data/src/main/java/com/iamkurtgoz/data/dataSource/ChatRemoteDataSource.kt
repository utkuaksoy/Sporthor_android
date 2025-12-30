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

interface ChatRemoteDataSource {
    suspend fun getAllMessages(page: Int?): BaseResponse<ChatAllMessageResponseModel>
    suspend fun getMessages(page: Int?, channelId: String?, orderBy: OrderByParams?): BaseResponse<ChatMessageResponseModel>
    suspend fun generateChatGroup(body: GenerateChatGroupRequest): BaseResponse<GenerateChatGroupResponseModel>
    suspend fun getChatUserProfile(userId: String?, groupId: String?): BaseResponse<GetChatUserProfileResponseModel>
    suspend fun getChatGroupDetail(groupId: String?): BaseResponse<GetChatGroupDetailResponseModel>
    suspend fun getChatGroupSummary(groupId: String?): BaseResponse<GetChatGroupSummaryResponseModel>
    suspend fun getChatUserProfileAttachments(userId: String?, groupId: String?): BaseResponse<GetAttachmentsResponseModel>
    suspend fun hideMessages(body: HideMessagesRequest): BaseResponse<Unit>
    suspend fun leaveChat(body: LeaveChatRequest): BaseResponse<Unit>
    suspend fun updateChatGroup(body: UpdateChatGroupRequest): BaseResponse<Unit>
}

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
import com.iamkurtgoz.domain.model.enums.OrderByParams
import com.iamkurtgoz.domain.model.request.GenerateChatGroupRequest
import com.iamkurtgoz.domain.model.request.HideMessagesRequest
import com.iamkurtgoz.domain.model.request.LeaveChatRequest
import com.iamkurtgoz.domain.model.request.UpdateChatGroupRequest
import com.iamkurtgoz.domain.model.response.ChatAllMessageDomainModel
import com.iamkurtgoz.domain.model.response.ChatMessageDomainModel
import com.iamkurtgoz.domain.model.response.GenerateChatGroupDomainModel
import com.iamkurtgoz.domain.model.response.GetAttachmentsDomainModel
import com.iamkurtgoz.domain.model.response.GetChatGroupDetailDomainModel
import com.iamkurtgoz.domain.model.response.GetChatGroupSummaryDomainModel
import com.iamkurtgoz.domain.model.response.GetChatUserProfileDomainModel

interface ChatRepository {
    suspend fun getAllMessages(page: Int?): RestResult<ChatAllMessageDomainModel>
    suspend fun getMessages(page: Int?, channelId: String?, orderBy: OrderByParams?): RestResult<ChatMessageDomainModel>
    suspend fun generateChatGroup(body: GenerateChatGroupRequest): RestResult<GenerateChatGroupDomainModel>
    suspend fun getChatUserProfile(userId: String?, groupId: String?): RestResult<GetChatUserProfileDomainModel>
    suspend fun getChatGroupDetail(groupId: String?): RestResult<GetChatGroupDetailDomainModel>
    suspend fun getChatGroupSummary(groupId: String?): RestResult<GetChatGroupSummaryDomainModel>
    suspend fun getChatUserProfileAttachments(userId: String?, groupId: String?): RestResult<GetAttachmentsDomainModel>
    suspend fun hideMessages(body: HideMessagesRequest): RestResult<Unit>
    suspend fun leaveChat(body: LeaveChatRequest): RestResult<Unit>
    suspend fun updateChatGroup(body: UpdateChatGroupRequest): RestResult<Unit>
}

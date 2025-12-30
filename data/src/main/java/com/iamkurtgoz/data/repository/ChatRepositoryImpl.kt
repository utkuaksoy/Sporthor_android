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
import com.iamkurtgoz.data.dataSource.ChatRemoteDataSource
import com.iamkurtgoz.data.mapper.ChatAllMessageDomainMapper
import com.iamkurtgoz.data.mapper.ChatMessageDomainMapper
import com.iamkurtgoz.data.mapper.GenerateChatGroupDomainMapper
import com.iamkurtgoz.data.mapper.GetChatAttachmentDomainMapper
import com.iamkurtgoz.data.mapper.GetChatGroupDetailDomainMapper
import com.iamkurtgoz.data.mapper.GetChatGroupSummaryDomainMapper
import com.iamkurtgoz.data.mapper.GetChatUserProfileDomainMapper
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
import com.iamkurtgoz.domain.repository.ChatRepository
import javax.inject.Inject

internal class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatAllMessageDomainMapper: ChatAllMessageDomainMapper,
    private val chatMessageDomainMapper: ChatMessageDomainMapper,
    private val generateChatGroupDomainMapper: GenerateChatGroupDomainMapper,
    private val getChatUserProfileDomainMapper: GetChatUserProfileDomainMapper,
    private val getChatGroupDetailDomainMapper: GetChatGroupDetailDomainMapper,
    private val getChatGroupSummaryDomainMapper: GetChatGroupSummaryDomainMapper,
    private val getChatAttachmentDomainMapper: GetChatAttachmentDomainMapper,
) : ChatRepository, CoreRepository() {

    override suspend fun getAllMessages(page: Int?): RestResult<ChatAllMessageDomainModel> = mapToRestResult {
        chatRemoteDataSource.getAllMessages(page)
    }.mapOnSuccess(chatAllMessageDomainMapper::map)

    override suspend fun getMessages(page: Int?, channelId: String?, orderBy: OrderByParams?): RestResult<ChatMessageDomainModel> = mapToRestResult {
        chatRemoteDataSource.getMessages(page, channelId, orderBy)
    }.mapOnSuccess(chatMessageDomainMapper::map)

    override suspend fun generateChatGroup(body: GenerateChatGroupRequest): RestResult<GenerateChatGroupDomainModel> = mapToRestResult {
        chatRemoteDataSource.generateChatGroup(body)
    }.mapOnSuccess(generateChatGroupDomainMapper::map)

    override suspend fun getChatUserProfile(userId: String?, groupId: String?): RestResult<GetChatUserProfileDomainModel> = mapToRestResult {
        chatRemoteDataSource.getChatUserProfile(userId, groupId)
    }.mapOnSuccess(getChatUserProfileDomainMapper::map)

    override suspend fun getChatGroupDetail(groupId: String?): RestResult<GetChatGroupDetailDomainModel> = mapToRestResult {
        chatRemoteDataSource.getChatGroupDetail(groupId)
    }.mapOnSuccess(getChatGroupDetailDomainMapper::map)

    override suspend fun getChatGroupSummary(groupId: String?): RestResult<GetChatGroupSummaryDomainModel> = mapToRestResult {
        chatRemoteDataSource.getChatGroupSummary(groupId)
    }.mapOnSuccess(getChatGroupSummaryDomainMapper::map)

    override suspend fun getChatUserProfileAttachments(userId: String?, groupId: String?): RestResult<GetAttachmentsDomainModel> = mapToRestResult {
        chatRemoteDataSource.getChatUserProfileAttachments(userId, groupId)
    }.mapOnSuccess(getChatAttachmentDomainMapper::map)

    override suspend fun hideMessages(body: HideMessagesRequest): RestResult<Unit> = mapToRestResult {
        chatRemoteDataSource.hideMessages(body)
    }

    override suspend fun leaveChat(body: LeaveChatRequest): RestResult<Unit> = mapToRestResult {
        chatRemoteDataSource.leaveChat(body)
    }

    override suspend fun updateChatGroup(body: UpdateChatGroupRequest): RestResult<Unit> = mapToRestResult {
        chatRemoteDataSource.updateChatGroup(body)
    }
}

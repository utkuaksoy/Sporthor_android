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
package com.iamkurtgoz.core.api.service.chat

import androidx.annotation.Keep
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.model.ChatAllMessageResponseModel
import com.iamkurtgoz.data.model.ChatMessageResponseModel
import com.iamkurtgoz.data.model.GenerateChatGroupResponseModel
import com.iamkurtgoz.data.model.GetAttachmentsResponseModel
import com.iamkurtgoz.data.model.GetChatGroupDetailResponseModel
import com.iamkurtgoz.data.model.GetChatGroupSummaryResponseModel
import com.iamkurtgoz.data.model.GetChatUserProfileResponseModel
import com.iamkurtgoz.domain.model.request.GenerateChatGroupRequest
import com.iamkurtgoz.domain.model.request.HideMessagesRequest
import com.iamkurtgoz.domain.model.request.LeaveChatRequest
import com.iamkurtgoz.domain.model.request.UpdateChatGroupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Keep
interface ChatService {

    @GET("Chat/GetAllMessages")
    suspend fun getAllMessages(@Query("Page") page: Int?): Response<BaseResponse<ChatAllMessageResponseModel>>

    @GET("Chat/GetMessages")
    suspend fun getMessages(@Query("Page") page: Int?, @Query("ChannelId") channelId: String?, @Query("OrderBy") orderBy: String?): Response<BaseResponse<ChatMessageResponseModel>>

    @POST("Chat/GenerateChatGroup")
    suspend fun generateChatGroup(@Body body: GenerateChatGroupRequest): Response<BaseResponse<GenerateChatGroupResponseModel>>

    @GET("Chat/GetChatUserProfile")
    suspend fun getChatUserProfile(@Query("UserId") userId: String?, @Query("GroupId") groupId: String?): Response<BaseResponse<GetChatUserProfileResponseModel>>

    @GET("Chat/GetChatGroupDetail")
    suspend fun getChatGroupDetail(@Query("GroupId") groupId: String?): Response<BaseResponse<GetChatGroupDetailResponseModel>>

    @GET("Chat/GetChatGroupSummary")
    suspend fun getChatGroupSummary(@Query("GroupId") groupId: String?): Response<BaseResponse<GetChatGroupSummaryResponseModel>>

    @GET("Chat/GetChatGroupAttachments")
    suspend fun getChatUserProfileAttachments(@Query("UserId") userId: String?, @Query("GroupId") groupId: String?): Response<BaseResponse<GetAttachmentsResponseModel>>

    @POST("Chat/HideMessages")
    suspend fun hideMessages(@Body body: HideMessagesRequest): Response<BaseResponse<Unit>>

    @POST("Chat/LeaveChat")
    suspend fun leaveChat(@Body body: LeaveChatRequest): Response<BaseResponse<Unit>>

    @POST("Chat/UpdateChatGroup")
    suspend fun updateChatGroup(@Body body: UpdateChatGroupRequest): Response<BaseResponse<Unit>>
}

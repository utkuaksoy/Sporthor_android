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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.repository.ChatRepository
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.data.mapper.GetAttachmentsUIMapper
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.model.GetAttachmentsUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.useCase.GetAttachmentsUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.domain.useCase.GetAttachmentsUseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetAttachmentsUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: ChatRepository,
    private val getAttachmentsUIMapper: GetAttachmentsUIMapper,
) : GetAttachmentsUseCase, CoreUseCase(coroutineDispatcher) {

    override fun invoke(params: GetAttachmentsUseCaseParams?): Flow<RestResult<GetAttachmentsUIModel>> = prepare {
        repository.getChatUserProfileAttachments(params?.userId, params?.groupId)
            .mapOnSuccess {
                getAttachmentsUIMapper.map(it)
            }
    }
}

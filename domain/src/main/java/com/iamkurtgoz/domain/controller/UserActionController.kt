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
package com.iamkurtgoz.domain.controller

import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface UserActionController {
    fun changeFollowStatus(
        scope: CoroutineScope,
        targetUserId: String?,
        followType: UserActionFollowType,
        onErrorAction: (BaseError) -> Unit,
    ): Job

    fun changePostLikeStatus(
        scope: CoroutineScope,
        postId: String?,
        actionType: UserActionPostLikeType,
        onErrorAction: (BaseError) -> Unit,
    ): Job
}

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
package com.iamkurtgoz.data.controller.userAction

import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.core.common.requester.onError
import com.iamkurtgoz.core.common.requester.onSuccess
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.domain.model.request.FollowUserRequest
import com.iamkurtgoz.domain.model.request.LikePostRequest
import com.iamkurtgoz.domain.model.response.FollowUserDomainModel
import com.iamkurtgoz.domain.model.response.LikePostDomainModel
import com.iamkurtgoz.domain.repository.SocialRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

internal class UserActionControllerImpl @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val appEventBus: AppEventBus,
    private val socialRepository: SocialRepository,
) : UserActionController, CoreUseCase(ioDispatcher) {

    companion object {
        const val EXPIRE_TIME_MILLIS = 1000L
    }

    private val cache = mutableListOf<UserActionCache>()

    private fun addToCache(userActionCache: UserActionCache) {
        cache.removeAll { it.processId == userActionCache.processId }
        cache.add(userActionCache)
    }

    override fun changeFollowStatus(scope: CoroutineScope, targetUserId: String?, followType: UserActionFollowType, onErrorAction: (BaseError) -> Unit): Job = scope.launch {
        val changeFollowStatusUserActionCache = ChangeFollowStatusUserActionCache(
            uuid = UUID.randomUUID(),
            processId = targetUserId,
            followType = followType,
        )
        addToCache(changeFollowStatusUserActionCache)
        delay(EXPIRE_TIME_MILLIS)
        val cacheValue = cache.firstOrNull { it.uuid == changeFollowStatusUserActionCache.uuid } as? ChangeFollowStatusUserActionCache
        if (cacheValue != null && cacheValue.processId == targetUserId && cacheValue.followType == followType) {
            val body = FollowUserRequest(
                targetUserId = targetUserId,
            )
            val request: Flow<RestResult<FollowUserDomainModel>> = when (followType) {
                UserActionFollowType.Follow -> prepare { socialRepository.followUser(body) }
                UserActionFollowType.UnFollow -> prepare { socialRepository.unFollowUser(body) }
            }
            request
                .onError {
                    onErrorAction.invoke(it)
                }
                .onSuccess { result, _, _ ->
                    appEventBus.updateFollowingStatus(
                        targetUserId = targetUserId ?: "",
                        followType = when (result.isFollow) {
                            true -> UserActionFollowType.Follow
                            false -> UserActionFollowType.UnFollow
                            null -> UserActionFollowType.UnFollow
                        },
                    )
                }
                .launchIn(this)
        }
    }

    override fun changePostLikeStatus(scope: CoroutineScope, postId: String?, actionType: UserActionPostLikeType, onErrorAction: (BaseError) -> Unit): Job = scope.launch {
        val postLikeStatusUserActionCache = PostLikeStatusUserActionCache(
            uuid = UUID.randomUUID(),
            processId = postId,
            likeType = actionType,
        )
        addToCache(postLikeStatusUserActionCache)
        delay(EXPIRE_TIME_MILLIS)
        val cacheValue = cache.firstOrNull { it.uuid == postLikeStatusUserActionCache.uuid } as? PostLikeStatusUserActionCache
        if (cacheValue != null && cacheValue.processId == postId && cacheValue.likeType == actionType) {
            val body = LikePostRequest(
                postId = postId,
            )

            val request: Flow<RestResult<LikePostDomainModel>> = when (actionType) {
                UserActionPostLikeType.Like -> prepare { socialRepository.likePost(body) }
                UserActionPostLikeType.UnLike -> prepare { socialRepository.unlikePost(body) }
            }
            request
                .onError {
                    onErrorAction.invoke(it)
                }
                .onSuccess { result, _, _ ->
                    appEventBus.updateLikeStatus(
                        postId = postId ?: "",
                        actionType = when (result.isLike) {
                            true -> UserActionPostLikeType.Like
                            false -> UserActionPostLikeType.UnLike
                            null -> UserActionPostLikeType.UnLike
                        },
                        likeCount = result.likeCount ?: 0,
                    )
                }
                .launchIn(this)
        }
    }
}

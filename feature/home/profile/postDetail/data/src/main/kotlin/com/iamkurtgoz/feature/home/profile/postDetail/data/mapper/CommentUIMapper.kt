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
package com.iamkurtgoz.feature.home.profile.postDetail.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.CommentDomainModel
import com.iamkurtgoz.feature.home.profile.postDetail.domain.model.CommentUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentUIMapper @Inject constructor() : IMapper<CommentDomainModel, CommentUIModel> {
    override fun map(response: CommentDomainModel): CommentUIModel {
        return with(response) {
            CommentUIModel(
                createdAt = createdAt,
                deletedAt = deletedAt,
                id = id,
                isDeleted = isDeleted,
                postId = postId,
                status = status,
                text = text,
                updatedAt = updatedAt,
                userId = userId,
            )
        }
    }
}

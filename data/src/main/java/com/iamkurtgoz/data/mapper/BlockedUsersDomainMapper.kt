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
package com.iamkurtgoz.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.data.model.BlockedUserResponseModel
import com.iamkurtgoz.data.model.BlockedUsersResponseModel
import com.iamkurtgoz.domain.model.response.BlockedUserDomainModel
import com.iamkurtgoz.domain.model.response.BlockedUsersDomainModel
import javax.inject.Inject

internal class BlockedUsersDomainMapper @Inject constructor(
    private val blockedUserDomainMapper: BlockedUserDomainMapper,
) : IMapper<BlockedUsersResponseModel, BlockedUsersDomainModel> {
    override fun map(response: BlockedUsersResponseModel): BlockedUsersDomainModel {
        return with(response) {
            BlockedUsersDomainModel(
                users = users?.map(blockedUserDomainMapper::map),
            )
        }
    }
}

internal class BlockedUserDomainMapper @Inject constructor() : IMapper<BlockedUserResponseModel, BlockedUserDomainModel> {
    override fun map(response: BlockedUserResponseModel): BlockedUserDomainModel {
        return with(response) {
            BlockedUserDomainModel(
                id = id,
                name = name,
                username = username,
                imageUrl = imageUrl,
            )
        }
    }
}

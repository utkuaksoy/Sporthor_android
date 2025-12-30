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
import com.iamkurtgoz.data.model.SearchHistoryItemResponseModel
import com.iamkurtgoz.data.model.SearchHistorySocialResponseModel
import com.iamkurtgoz.data.model.SearchUserResponseModel
import com.iamkurtgoz.domain.model.response.SearchHistoryItemDomainModel
import com.iamkurtgoz.domain.model.response.SearchHistorySocialDomainModel
import com.iamkurtgoz.domain.model.response.SearchUserDomainModel
import javax.inject.Inject

internal class SearchHistoryDomainMapper @Inject constructor(
    private val searchHistoryItemDomainMapper: SearchHistoryItemDomainMapper,
) : IMapper<SearchHistorySocialResponseModel, SearchHistorySocialDomainModel> {

    override fun map(response: SearchHistorySocialResponseModel): SearchHistorySocialDomainModel {
        return with(response) {
            SearchHistorySocialDomainModel(
                histories = histories?.map(searchHistoryItemDomainMapper::map),
            )
        }
    }
}

internal class SearchHistoryItemDomainMapper @Inject constructor(
    private val searchUserDomainMapper: SearchUserDomainMapper,
) : IMapper<SearchHistoryItemResponseModel, SearchHistoryItemDomainModel> {
    override fun map(response: SearchHistoryItemResponseModel): SearchHistoryItemDomainModel {
        return with(response) {
            SearchHistoryItemDomainModel(
                id = id,
                status = status,
                isDeleted = isDeleted,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                userId = userId,
                searchQuery = searchQuery,
                searchUser = searchUser?.let { searchUserDomainMapper.map(it) },
            )
        }
    }
}

internal class SearchUserDomainMapper @Inject constructor() : IMapper<SearchUserResponseModel, SearchUserDomainModel> {
    override fun map(response: SearchUserResponseModel): SearchUserDomainModel {
        return with(response) {
            SearchUserDomainModel(
                userId = userId,
                isTeam = isTeam,
                image = image,
                name = name,
                summary = summary,
                userName = userName,
            )
        }
    }
}

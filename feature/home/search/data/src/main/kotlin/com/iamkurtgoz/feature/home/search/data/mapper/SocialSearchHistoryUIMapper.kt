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
package com.iamkurtgoz.feature.home.search.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.SearchHistoryItemDomainModel
import com.iamkurtgoz.domain.model.response.SearchHistorySocialDomainModel
import com.iamkurtgoz.domain.model.response.SearchUserDomainModel
import com.iamkurtgoz.feature.home.search.domain.model.SearchHistoryItemUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SearchHistoryUIModel
import com.iamkurtgoz.feature.home.search.domain.model.SearchUserUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SocialSearchHistoryUIMapper @Inject constructor(
    private val searchHistoryItemUIMapper: SocialHistoryItemUIMapper,
) : IMapper<SearchHistorySocialDomainModel, SearchHistoryUIModel> {
    override fun map(response: SearchHistorySocialDomainModel): SearchHistoryUIModel {
        return with(response) {
            SearchHistoryUIModel(
                histories = histories?.map(searchHistoryItemUIMapper::map),
            )
        }
    }
}

@Singleton
internal class SocialHistoryItemUIMapper @Inject constructor(
    private val searchUserUIMapper: SearchUserUIMapper,
) : IMapper<SearchHistoryItemDomainModel, SearchHistoryItemUIModel> {
    override fun map(response: SearchHistoryItemDomainModel): SearchHistoryItemUIModel {
        return with(response) {
            SearchHistoryItemUIModel(
                id = id,
                status = status,
                isDeleted = isDeleted,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                userId = userId,
                searchQuery = searchQuery,
                searchUser = searchUser?.let { searchUserUIMapper.map(it) },
            )
        }
    }
}

@Singleton
internal class SearchUserUIMapper @Inject constructor() : IMapper<SearchUserDomainModel, SearchUserUIModel> {
    override fun map(response: SearchUserDomainModel): SearchUserUIModel {
        return with(response) {
            SearchUserUIModel(
                isTeam = isTeam,
                image = image,
                name = name,
                summary = summary,
                userName = userName,
                userId = userId,
            )
        }
    }
}

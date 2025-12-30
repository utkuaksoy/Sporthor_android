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
package com.iamkurtgoz.feature.home.editTeam.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.ClubDomainModel
import com.iamkurtgoz.feature.home.editTeam.domain.model.ClubUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ClubUIMapper @Inject constructor() : IMapper<ClubDomainModel, ClubUIModel> {
    override fun map(response: ClubDomainModel): ClubUIModel {
        return ClubUIModel(
            address = response.address,
            city = response.city,
            clubName = response.clubName,
            confirmationStatus = response.confirmationStatus,
            county = response.county,
            createdAt = response.createdAt,
            deletedAt = response.deletedAt,
            files = response.files,
            foundationYear = response.foundationYear,
            founderUserId = response.founderUserId,
            id = response.id,
            isDeleted = response.isDeleted,
            logo = response.logo,
            status = response.status,
            updatedAt = response.updatedAt,
        )
    }
}

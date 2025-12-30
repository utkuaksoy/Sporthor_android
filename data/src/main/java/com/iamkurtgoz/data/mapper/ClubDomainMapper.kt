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
import com.iamkurtgoz.data.model.ClubResponseModel
import com.iamkurtgoz.domain.model.response.ClubDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ClubDomainMapper @Inject constructor() : IMapper<ClubResponseModel, ClubDomainModel> {
    override fun map(response: ClubResponseModel): ClubDomainModel {
        return ClubDomainModel(
            address = response.club?.address,
            city = response.club?.city,
            clubName = response.club?.clubName,
            confirmationStatus = response.club?.confirmationStatus,
            county = response.club?.county,
            createdAt = response.club?.createdAt,
            deletedAt = response.club?.deletedAt,
            files = response.club?.files,
            foundationYear = response.club?.foundationYear,
            founderUserId = response.club?.founderUserId,
            id = response.club?.id,
            isDeleted = response.club?.isDeleted,
            logo = response.club?.logo,
            status = response.club?.status,
            updatedAt = response.club?.updatedAt,
        )
    }
}

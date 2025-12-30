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
import com.iamkurtgoz.data.model.GetSportClubResponseModel
import com.iamkurtgoz.domain.model.response.GetSportClubDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetSportClubDomainMapper @Inject constructor() : IMapper<GetSportClubResponseModel, GetSportClubDomainModel> {

    override fun map(response: GetSportClubResponseModel): GetSportClubDomainModel {
        return GetSportClubDomainModel(
            address = response.address,
            city = response.city,
            clubId = response.clubId,
            clubName = response.clubName,
            confirmationStatus = response.confirmationStatus,
            county = response.county,
            foundationYear = response.foundationYear,
            logo = response.logo,
            branch = response.branch,
        )
    }
}

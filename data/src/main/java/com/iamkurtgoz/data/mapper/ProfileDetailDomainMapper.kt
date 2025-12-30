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
import com.iamkurtgoz.data.model.ProfileDetailComponentDataItemResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentDataResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentDataSkillDetailResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentDataSkillResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentDataTeamResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentDataTournamentResponseModel
import com.iamkurtgoz.data.model.ProfileDetailComponentResponseModel
import com.iamkurtgoz.data.model.ProfileDetailResponseModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataItemDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataSkillDetailDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataSkillDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataTeamDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataTournamentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileDetailDomainMapper @Inject constructor(
    private val profileDetailComponentResponseMapper: ProfileDetailComponentResponseMapper,
) : IMapper<ProfileDetailResponseModel, ProfileDetailDomainModel> {
    override fun map(response: ProfileDetailResponseModel): ProfileDetailDomainModel {
        return with(response) {
            ProfileDetailDomainModel(
                personalId = personalId,
                components = components?.filterNotNull()?.map(profileDetailComponentResponseMapper::map),
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentResponseMapper @Inject constructor(
    private val profileDetailComponentDataResponseMapper: ProfileDetailComponentDataResponseMapper,
) : IMapper<ProfileDetailComponentResponseModel, ProfileDetailComponentDomainModel> {
    override fun map(response: ProfileDetailComponentResponseModel): ProfileDetailComponentDomainModel {
        return with(response) {
            ProfileDetailComponentDomainModel(
                data = data?.let { profileDetailComponentDataResponseMapper.map(it) },
                id = id,
                type = type,
                typeId = typeId,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataResponseMapper @Inject constructor(
    private val profileDetailComponentDataItemResponseMapper: ProfileDetailComponentDataItemResponseMapper,
    private val profileDetailComponentDataSkillResponseMapper: ProfileDetailComponentDataSkillResponseMapper,
    private val profileDetailComponentDataTeamResponseMapper: ProfileDetailComponentDataTeamResponseMapper,
    private val profileDetailComponentDataTournamentResponseMapper: ProfileDetailComponentDataTournamentResponseMapper,
) : IMapper<ProfileDetailComponentDataResponseModel, ProfileDetailComponentDataDomainModel> {
    override fun map(response: ProfileDetailComponentDataResponseModel): ProfileDetailComponentDataDomainModel {
        return with(response) {
            ProfileDetailComponentDataDomainModel(
                birthDate = birthDate,
                flagIcon = flagIcon,
                height = height,
                imageUrl = imageUrl,
                items = items?.filterNotNull()?.map { profileDetailComponentDataItemResponseMapper.map(it) },
                name = name,
                nationalityName = nationalityName,
                skills = skills?.filterNotNull()?.map { profileDetailComponentDataSkillResponseMapper.map(it) },
                teams = teams?.filterNotNull()?.map { profileDetailComponentDataTeamResponseMapper.map(it) },
                title = title,
                tournaments = tournaments?.filterNotNull()?.map { profileDetailComponentDataTournamentResponseMapper.map(it) },
                weight = weight,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataItemResponseMapper @Inject constructor() : IMapper<ProfileDetailComponentDataItemResponseModel, ProfileDetailComponentDataItemDomainModel> {
    override fun map(response: ProfileDetailComponentDataItemResponseModel): ProfileDetailComponentDataItemDomainModel {
        return with(response) {
            ProfileDetailComponentDataItemDomainModel(
                endDate = endDate,
                startDate = startDate,
                teamLogoURL = teamLogoURL,
                teamName = teamName,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataSkillResponseMapper @Inject constructor(
    private val profileDetailComponentDataSkillDetailResponseMapper: ProfileDetailComponentDataSkillDetailResponseMapper,
) : IMapper<ProfileDetailComponentDataSkillResponseModel, ProfileDetailComponentDataSkillDomainModel> {
    override fun map(response: ProfileDetailComponentDataSkillResponseModel): ProfileDetailComponentDataSkillDomainModel {
        return with(response) {
            ProfileDetailComponentDataSkillDomainModel(
                details = details?.filterNotNull()?.map(profileDetailComponentDataSkillDetailResponseMapper::map),
                icon = icon,
                id = id,
                isSelected = isSelected,
                name = name,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataSkillDetailResponseMapper @Inject constructor() : IMapper<ProfileDetailComponentDataSkillDetailResponseModel, ProfileDetailComponentDataSkillDetailDomainModel> {
    override fun map(response: ProfileDetailComponentDataSkillDetailResponseModel): ProfileDetailComponentDataSkillDetailDomainModel {
        return with(response) {
            ProfileDetailComponentDataSkillDetailDomainModel(
                title = title,
                unit = unit,
                value = value,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataTeamResponseMapper @Inject constructor() : IMapper<ProfileDetailComponentDataTeamResponseModel, ProfileDetailComponentDataTeamDomainModel> {
    override fun map(response: ProfileDetailComponentDataTeamResponseModel): ProfileDetailComponentDataTeamDomainModel {
        return with(response) {
            ProfileDetailComponentDataTeamDomainModel(
                teamId = teamId,
                teamLogoImageUrl = teamLogoImageUrl,
                teamName = teamName,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataTournamentResponseMapper @Inject constructor() : IMapper<ProfileDetailComponentDataTournamentResponseModel, ProfileDetailComponentDataTournamentDomainModel> {
    override fun map(response: ProfileDetailComponentDataTournamentResponseModel): ProfileDetailComponentDataTournamentDomainModel {
        return with(response) {
            ProfileDetailComponentDataTournamentDomainModel(
                icon = icon,
                stage = stage,
                stageIcon = stageIcon,
                title = title,
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.profile.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataItemDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataSkillDetailDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataSkillDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataTeamDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDataTournamentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailComponentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDetailDomainModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataItemUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillDetailUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataTeamUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataTournamentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileDetailUIMapper @Inject constructor(
    private val profileDetailComponentUIMapper: ProfileDetailComponentUIMapper,
) : IMapper<ProfileDetailDomainModel, ProfileDetailUIModel> {
    override fun map(response: ProfileDetailDomainModel): ProfileDetailUIModel {
        return with(response) {
            ProfileDetailUIModel(
                personalId = personalId,
                components = components?.filterNotNull()?.map(profileDetailComponentUIMapper::map),
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentUIMapper @Inject constructor(
    private val profileDetailComponentDataUIMapper: ProfileDetailComponentDataUIMapper,
) : IMapper<ProfileDetailComponentDomainModel, ProfileDetailComponentUIModel> {
    override fun map(response: ProfileDetailComponentDomainModel): ProfileDetailComponentUIModel {
        return with(response) {
            ProfileDetailComponentUIModel(
                data = data?.let { profileDetailComponentDataUIMapper.map(it) },
                id = id,
                type = type,
                typeId = typeId,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataUIMapper @Inject constructor(
    private val profileDetailComponentDataItemUIMapper: ProfileDetailComponentDataItemUIMapper,
    private val profileDetailComponentDataSkillUIMapper: ProfileDetailComponentDataSkillUIMapper,
    private val profileDetailComponentDataTeamUIMapper: ProfileDetailComponentDataTeamUIMapper,
    private val profileDetailComponentDataTournamentUIMapper: ProfileDetailComponentDataTournamentUIMapper,
) : IMapper<ProfileDetailComponentDataDomainModel, ProfileDetailComponentDataUIModel> {
    override fun map(response: ProfileDetailComponentDataDomainModel): ProfileDetailComponentDataUIModel {
        return with(response) {
            ProfileDetailComponentDataUIModel(
                birthDate = birthDate,
                flagIcon = flagIcon,
                height = height,
                imageUrl = imageUrl,
                items = items?.filterNotNull()?.map { profileDetailComponentDataItemUIMapper.map(it) },
                name = name,
                nationalityName = nationalityName,
                skills = skills?.filterNotNull()?.map { profileDetailComponentDataSkillUIMapper.map(it) },
                teams = teams?.filterNotNull()?.map { profileDetailComponentDataTeamUIMapper.map(it) },
                title = title,
                tournaments = tournaments?.filterNotNull()?.map { profileDetailComponentDataTournamentUIMapper.map(it) },
                weight = weight,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataItemUIMapper @Inject constructor() : IMapper<ProfileDetailComponentDataItemDomainModel, ProfileDetailComponentDataItemUIModel> {
    override fun map(response: ProfileDetailComponentDataItemDomainModel): ProfileDetailComponentDataItemUIModel {
        return with(response) {
            ProfileDetailComponentDataItemUIModel(
                endDate = endDate,
                startDate = startDate,
                teamLogoURL = teamLogoURL,
                teamName = teamName,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataSkillUIMapper @Inject constructor(
    private val profileDetailComponentDataSkillDetailUIMapper: ProfileDetailComponentDataSkillDetailUIMapper,
) : IMapper<ProfileDetailComponentDataSkillDomainModel, ProfileDetailComponentDataSkillUIModel> {
    override fun map(response: ProfileDetailComponentDataSkillDomainModel): ProfileDetailComponentDataSkillUIModel {
        return with(response) {
            ProfileDetailComponentDataSkillUIModel(
                details = details?.filterNotNull()?.map(profileDetailComponentDataSkillDetailUIMapper::map),
                icon = icon,
                id = id,
                isSelected = isSelected,
                name = name,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataSkillDetailUIMapper @Inject constructor() : IMapper<ProfileDetailComponentDataSkillDetailDomainModel, ProfileDetailComponentDataSkillDetailUIModel> {
    override fun map(response: ProfileDetailComponentDataSkillDetailDomainModel): ProfileDetailComponentDataSkillDetailUIModel {
        return with(response) {
            ProfileDetailComponentDataSkillDetailUIModel(
                title = title,
                unit = unit,
                value = value,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataTeamUIMapper @Inject constructor() : IMapper<ProfileDetailComponentDataTeamDomainModel, ProfileDetailComponentDataTeamUIModel> {
    override fun map(response: ProfileDetailComponentDataTeamDomainModel): ProfileDetailComponentDataTeamUIModel {
        return with(response) {
            ProfileDetailComponentDataTeamUIModel(
                teamId = teamId,
                teamLogoImageUrl = teamLogoImageUrl,
                teamName = teamName,
            )
        }
    }
}

@Singleton
internal class ProfileDetailComponentDataTournamentUIMapper @Inject constructor() : IMapper<ProfileDetailComponentDataTournamentDomainModel, ProfileDetailComponentDataTournamentUIModel> {
    override fun map(response: ProfileDetailComponentDataTournamentDomainModel): ProfileDetailComponentDataTournamentUIModel {
        return with(response) {
            ProfileDetailComponentDataTournamentUIModel(
                icon = icon,
                stage = stage,
                stageIcon = stageIcon,
                title = title,
            )
        }
    }
}

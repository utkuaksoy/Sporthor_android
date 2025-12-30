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
import com.iamkurtgoz.domain.model.response.ProfileComponentDataDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataExtraInfoDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataMatchDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataSegmentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataTeamDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDomainModel
import com.iamkurtgoz.domain.model.response.ProfileInfoDomainModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataExtraInfoUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataMatchUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataSegmentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataTeamUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileInfoUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileUIMapper @Inject constructor(
    private val profileInfoDomainMapper: ProfileInfoUIMapper,
    private val profileComponentDomainMapper: ProfileComponentUIMapper,
) : IMapper<ProfileDomainModel, ProfileUIModel> {
    override fun map(response: ProfileDomainModel): ProfileUIModel {
        return with(response) {
            ProfileUIModel(
                info = info?.let { profileInfoDomainMapper.map(it) },
                components = components?.map(profileComponentDomainMapper::map),
            )
        }
    }
}

internal class ProfileInfoUIMapper @Inject constructor() : IMapper<ProfileInfoDomainModel, ProfileInfoUIModel> {
    override fun map(response: ProfileInfoDomainModel): ProfileInfoUIModel {
        return with(response) {
            ProfileInfoUIModel(
                avatar = avatar,
                id = id,
                isCurrentUser = isCurrentUser,
                name = name,
                username = username,
            )
        }
    }
}

internal class ProfileComponentUIMapper @Inject constructor(
    private val profileComponentDataUIMapper: ProfileComponentDataUIMapper,
) : IMapper<ProfileComponentDomainModel, ProfileComponentUIModel> {
    override fun map(response: ProfileComponentDomainModel): ProfileComponentUIModel {
        return with(response) {
            ProfileComponentUIModel(
                data = response.data?.let { profileComponentDataUIMapper.map(it) },
                id = id,
                type = type,
                typeId = typeId,
            )
        }
    }
}

internal class ProfileComponentDataUIMapper @Inject constructor(
    private val profileComponentDataExtraInfoUIMapper: ProfileComponentDataExtraInfoUIMapper,
    private val profileComponentDataMatchUIMapper: ProfileComponentDataMatchUIMapper,
    private val profileComponentDataSegmentUIMapper: ProfileComponentDataSegmentUIMapper,
    private val profileComponentDataTeamUIMapper: ProfileComponentDataTeamUIMapper,
) : IMapper<ProfileComponentDataDomainModel, ProfileComponentDataUIModel> {
    override fun map(response: ProfileComponentDataDomainModel): ProfileComponentDataUIModel {
        return with(response) {
            ProfileComponentDataUIModel(
                buttons = buttons,
                description = description,
                extraInfo = extraInfo?.map(profileComponentDataExtraInfoUIMapper::map),
                followerCount = followerCount,
                followingCount = followingCount,
                imageUrl = imageUrl,
                matches = matches?.map(profileComponentDataMatchUIMapper::map),
                postCount = postCount,
                segments = segments?.map(profileComponentDataSegmentUIMapper::map),
                selectedSegmentIndex = selectedSegmentIndex,
                teams = teams?.map(profileComponentDataTeamUIMapper::map),
                title = title,
                userId = userId,
                username = username,
            )
        }
    }
}

internal class ProfileComponentDataExtraInfoUIMapper @Inject constructor() : IMapper<ProfileComponentDataExtraInfoDomainModel, ProfileComponentDataExtraInfoUIModel> {
    override fun map(response: ProfileComponentDataExtraInfoDomainModel): ProfileComponentDataExtraInfoUIModel {
        return with(response) {
            ProfileComponentDataExtraInfoUIModel(
                icon = icon,
                key = key,
                value = value,
            )
        }
    }
}

internal class ProfileComponentDataMatchUIMapper @Inject constructor(
    private val profileComponentDataTeamUIMapper: ProfileComponentDataTeamUIMapper,
) : IMapper<ProfileComponentDataMatchDomainModel, ProfileComponentDataMatchUIModel> {
    override fun map(response: ProfileComponentDataMatchDomainModel): ProfileComponentDataMatchUIModel {
        return with(response) {
            ProfileComponentDataMatchUIModel(
                matchId = matchId,
                teams = response.teams?.map(profileComponentDataTeamUIMapper::map),
            )
        }
    }
}

internal class ProfileComponentDataTeamUIMapper @Inject constructor() : IMapper<ProfileComponentDataTeamDomainModel, ProfileComponentDataTeamUIModel> {
    override fun map(response: ProfileComponentDataTeamDomainModel): ProfileComponentDataTeamUIModel {
        return with(response) {
            ProfileComponentDataTeamUIModel(
                teamId = teamId,
                teamLogoImageUrl = teamLogoImageUrl,
                teamName = teamName,
            )
        }
    }
}

internal class ProfileComponentDataSegmentUIMapper @Inject constructor() : IMapper<ProfileComponentDataSegmentDomainModel, ProfileComponentDataSegmentUIModel> {
    override fun map(response: ProfileComponentDataSegmentDomainModel): ProfileComponentDataSegmentUIModel {
        return with(response) {
            ProfileComponentDataSegmentUIModel(
                id = id,
                image = image,
                title = title,
                type = type,
            )
        }
    }
}

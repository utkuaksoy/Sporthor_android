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
import com.iamkurtgoz.data.model.ProfileComponentDataExtraInfoResponseModel
import com.iamkurtgoz.data.model.ProfileComponentDataMatchResponseModel
import com.iamkurtgoz.data.model.ProfileComponentDataResponseModel
import com.iamkurtgoz.data.model.ProfileComponentDataSegmentResponseModel
import com.iamkurtgoz.data.model.ProfileComponentDataTeamResponseModel
import com.iamkurtgoz.data.model.ProfileComponentResponseModel
import com.iamkurtgoz.data.model.ProfileInfoResponseModel
import com.iamkurtgoz.data.model.ProfileResponseModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataExtraInfoDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataMatchDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataSegmentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDataTeamDomainModel
import com.iamkurtgoz.domain.model.response.ProfileComponentDomainModel
import com.iamkurtgoz.domain.model.response.ProfileDomainModel
import com.iamkurtgoz.domain.model.response.ProfileInfoDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileDomainMapper @Inject constructor(
    private val profileInfoDomainMapper: ProfileInfoDomainMapper,
    private val profileComponentDomainMapper: ProfileComponentDomainMapper,
) : IMapper<ProfileResponseModel, ProfileDomainModel> {
    override fun map(response: ProfileResponseModel): ProfileDomainModel {
        return with(response) {
            ProfileDomainModel(
                info = info?.let { profileInfoDomainMapper.map(it) },
                components = components?.map(profileComponentDomainMapper::map),
            )
        }
    }
}

internal class ProfileInfoDomainMapper @Inject constructor() : IMapper<ProfileInfoResponseModel, ProfileInfoDomainModel> {
    override fun map(response: ProfileInfoResponseModel): ProfileInfoDomainModel {
        return with(response) {
            ProfileInfoDomainModel(
                avatar = avatar,
                id = id,
                isCurrentUser = isCurrentUser,
                name = name,
                username = username,
            )
        }
    }
}

internal class ProfileComponentDomainMapper @Inject constructor(
    private val profileComponentDataDomainMapper: ProfileComponentDataDomainMapper,
) : IMapper<ProfileComponentResponseModel, ProfileComponentDomainModel> {
    override fun map(response: ProfileComponentResponseModel): ProfileComponentDomainModel {
        return with(response) {
            ProfileComponentDomainModel(
                data = response.data?.let { profileComponentDataDomainMapper.map(it) },
                id = id,
                type = type,
                typeId = typeId,
            )
        }
    }
}

internal class ProfileComponentDataDomainMapper @Inject constructor(
    private val profileComponentDataExtraInfoDomainMapper: ProfileComponentDataExtraInfoDomainMapper,
    private val profileComponentDataMatchDomainMapper: ProfileComponentDataMatchDomainMapper,
    private val profileComponentDataSegmentDomainMapper: ProfileComponentDataSegmentDomainMapper,
    private val profileComponentDataTeamDomainMapper: ProfileComponentDataTeamDomainMapper,
) : IMapper<ProfileComponentDataResponseModel, ProfileComponentDataDomainModel> {
    override fun map(response: ProfileComponentDataResponseModel): ProfileComponentDataDomainModel {
        return with(response) {
            ProfileComponentDataDomainModel(
                buttons = buttons,
                description = description,
                extraInfo = extraInfo?.map(profileComponentDataExtraInfoDomainMapper::map),
                followerCount = followerCount,
                followingCount = followingCount,
                imageUrl = imageUrl,
                matches = matches?.map(profileComponentDataMatchDomainMapper::map),
                postCount = postCount,
                segments = segments?.map(profileComponentDataSegmentDomainMapper::map),
                selectedSegmentIndex = selectedSegmentIndex,
                teams = teams?.map(profileComponentDataTeamDomainMapper::map),
                title = title,
                userId = userId,
                username = username,
            )
        }
    }
}

internal class ProfileComponentDataExtraInfoDomainMapper @Inject constructor() : IMapper<ProfileComponentDataExtraInfoResponseModel, ProfileComponentDataExtraInfoDomainModel> {
    override fun map(response: ProfileComponentDataExtraInfoResponseModel): ProfileComponentDataExtraInfoDomainModel {
        return with(response) {
            ProfileComponentDataExtraInfoDomainModel(
                icon = icon,
                key = key,
                value = value,
            )
        }
    }
}

internal class ProfileComponentDataMatchDomainMapper @Inject constructor(
    private val profileComponentDataTeamDomainMapper: ProfileComponentDataTeamDomainMapper,
) : IMapper<ProfileComponentDataMatchResponseModel, ProfileComponentDataMatchDomainModel> {
    override fun map(response: ProfileComponentDataMatchResponseModel): ProfileComponentDataMatchDomainModel {
        return with(response) {
            ProfileComponentDataMatchDomainModel(
                matchId = matchId,
                teams = response.teams?.map(profileComponentDataTeamDomainMapper::map),
            )
        }
    }
}

internal class ProfileComponentDataTeamDomainMapper @Inject constructor() : IMapper<ProfileComponentDataTeamResponseModel, ProfileComponentDataTeamDomainModel> {
    override fun map(response: ProfileComponentDataTeamResponseModel): ProfileComponentDataTeamDomainModel {
        return with(response) {
            ProfileComponentDataTeamDomainModel(
                teamId = teamId,
                teamLogoImageUrl = teamLogoImageUrl,
                teamName = teamName,
            )
        }
    }
}

internal class ProfileComponentDataSegmentDomainMapper @Inject constructor() : IMapper<ProfileComponentDataSegmentResponseModel, ProfileComponentDataSegmentDomainModel> {
    override fun map(response: ProfileComponentDataSegmentResponseModel): ProfileComponentDataSegmentDomainModel {
        return with(response) {
            ProfileComponentDataSegmentDomainModel(
                id = id,
                image = image,
                title = title,
                type = type,
            )
        }
    }
}

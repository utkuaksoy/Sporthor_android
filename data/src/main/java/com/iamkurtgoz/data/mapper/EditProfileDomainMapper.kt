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
import com.iamkurtgoz.data.model.BranchInfoRowResponseModel
import com.iamkurtgoz.data.model.BranchItemResponseModel
import com.iamkurtgoz.data.model.BranchesAttributeItemResponseModel
import com.iamkurtgoz.data.model.EditProfileInfoResponseModel
import com.iamkurtgoz.data.model.EditProfileResponseModel
import com.iamkurtgoz.data.model.HighlightItemResponseModel
import com.iamkurtgoz.data.model.ProfileRowResponseModel
import com.iamkurtgoz.data.model.TeamInfoResponseModel
import com.iamkurtgoz.data.model.TeamItemResponseModel
import com.iamkurtgoz.domain.model.response.BranchInfoRowDomainModel
import com.iamkurtgoz.domain.model.response.BranchItemDomainModel
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.domain.model.response.EditProfileDomainModel
import com.iamkurtgoz.domain.model.response.EditProfileInfoDomainModel
import com.iamkurtgoz.domain.model.response.HighlightItemDomainModel
import com.iamkurtgoz.domain.model.response.ProfileRowDomainModel
import com.iamkurtgoz.domain.model.response.TeamInfoDomainModel
import com.iamkurtgoz.domain.model.response.TeamItemDomainModel
import javax.inject.Inject

internal class EditProfileDomainMapper @Inject constructor(
    private val editProfileInfoDomainMapper: EditProfileInfoDomainMapper,
    private val teamInfoResponseMapper: TeamInfoResponseMapper,
    private val highlightItemResponseMapper: HighlightItemResponseMapper,
) : IMapper<EditProfileResponseModel, EditProfileDomainModel> {
    override fun map(response: EditProfileResponseModel): EditProfileDomainModel {
        return with(response) {
            EditProfileDomainModel(
                profileImage = profileImage,
                profileInfo = profileInfo?.let { editProfileInfoDomainMapper.map(it) },
                teamInfo = teamInfo?.let(teamInfoResponseMapper::map),
                highlights = highlights?.let(highlightItemResponseMapper::map),
            )
        }
    }
}

internal class EditProfileInfoDomainMapper @Inject constructor(
    private val profileRowDomainMapper: ProfileRowDomainMapper,
) : IMapper<EditProfileInfoResponseModel, EditProfileInfoDomainModel> {
    override fun map(response: EditProfileInfoResponseModel): EditProfileInfoDomainModel {
        return with(response) {
            EditProfileInfoDomainModel(
                title = title,
                row = row?.map(profileRowDomainMapper::map),
            )
        }
    }
}

internal class ProfileRowDomainMapper @Inject constructor() : IMapper<ProfileRowResponseModel, ProfileRowDomainModel> {
    override fun map(response: ProfileRowResponseModel): ProfileRowDomainModel {
        return with(response) {
            ProfileRowDomainModel(
                title = title,
                placeholder = placeholder,
                text = text,
                parameterName = parameterName,
                isRequired = isRequired,
                type = type,
            )
        }
    }
}

internal class TeamInfoResponseMapper @Inject constructor(
    private val teamItemResponseMapper: TeamItemResponseMapper,
) : IMapper<TeamInfoResponseModel, TeamInfoDomainModel> {
    override fun map(response: TeamInfoResponseModel): TeamInfoDomainModel {
        return with(response) {
            TeamInfoDomainModel(
                title = title,
                info = info,
                teams = teams?.map(teamItemResponseMapper::map),
            )
        }
    }
}

internal class HighlightItemResponseMapper @Inject constructor(
    private val branchItemResponseMapper: BranchItemResponseMapper,
    private val branchesAttributeItemResponseMapper: BranchesAttributeItemResponseMapper,
) : IMapper<HighlightItemResponseModel, HighlightItemDomainModel> {
    override fun map(response: HighlightItemResponseModel): HighlightItemDomainModel {
        return with(response) {
            HighlightItemDomainModel(
                title = title,
                branches = branches?.map(branchItemResponseMapper::map),
                branchesAttributes = branchesAttributes?.map(branchesAttributeItemResponseMapper::map),
            )
        }
    }
}

internal class BranchItemResponseMapper @Inject constructor() : IMapper<BranchItemResponseModel, BranchItemDomainModel> {
    override fun map(response: BranchItemResponseModel): BranchItemDomainModel {
        return with(response) {
            BranchItemDomainModel(
                branchImage = branchImage,
                branchTitle = branchTitle,
                branchId = branchId,
                isSelected = isSelected,
            )
        }
    }
}

internal class BranchesAttributeItemResponseMapper @Inject constructor(
    private val branchInfoRowResponseMapper: BranchInfoRowResponseMapper,
) : IMapper<BranchesAttributeItemResponseModel, BranchesAttributeItemDomainModel> {
    override fun map(response: BranchesAttributeItemResponseModel): BranchesAttributeItemDomainModel {
        return with(response) {
            BranchesAttributeItemDomainModel(
                branchId = branchId,
                branchInfoRow = branchInfoRow?.map(branchInfoRowResponseMapper::map),
            )
        }
    }
}

internal class BranchInfoRowResponseMapper @Inject constructor() : IMapper<BranchInfoRowResponseModel, BranchInfoRowDomainModel> {
    override fun map(response: BranchInfoRowResponseModel): BranchInfoRowDomainModel {
        return with(response) {
            BranchInfoRowDomainModel(
                title = title,
                placeholder = placeholder,
                text = text,
                parameterName = parameterName,
                isRequired = isRequired,
                type = type,
            )
        }
    }
}

internal class TeamItemResponseMapper @Inject constructor() : IMapper<TeamItemResponseModel, TeamItemDomainModel> {
    override fun map(response: TeamItemResponseModel): TeamItemDomainModel {
        return with(response) {
            TeamItemDomainModel(
                teamImage = teamImage,
                teamName = teamName,
            )
        }
    }
}

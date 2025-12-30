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
package com.iamkurtgoz.feature.home.profile.profileEdit.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.BranchInfoRowDomainModel
import com.iamkurtgoz.domain.model.response.BranchItemDomainModel
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.domain.model.response.EditProfileDomainModel
import com.iamkurtgoz.domain.model.response.EditProfileInfoDomainModel
import com.iamkurtgoz.domain.model.response.HighlightItemDomainModel
import com.iamkurtgoz.domain.model.response.ProfileRowDomainModel
import com.iamkurtgoz.domain.model.response.TeamInfoDomainModel
import com.iamkurtgoz.domain.model.response.TeamItemDomainModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchInfoRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchesAttributeItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.EditProfileInfoUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.EditProfileSummaryUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.HighlightItemResponseUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.ProfileRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.TeamInfoUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.TeamItemResponseUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.types.toInfoRowType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EditProfileUIMapper @Inject constructor(
    private val editProfileInfoUIMapper: EditProfileInfoUIMapper,
    private val teamInfoResponseUIMapper: TeamInfoResponseUIMapper,
    private val highlightItemResponseUIMapper: HighlightItemResponseUIMapper,
) : IMapper<EditProfileDomainModel, EditProfileSummaryUIModel> {
    override fun map(response: EditProfileDomainModel): EditProfileSummaryUIModel {
        return with(response) {
            EditProfileSummaryUIModel(
                profileImage = profileImage,
                profileInfo = profileInfo?.let { editProfileInfoUIMapper.map(it) },
                teamInfo = teamInfo?.let(teamInfoResponseUIMapper::map),
                highlights = highlights?.let(highlightItemResponseUIMapper::map),
            )
        }
    }
}

@Singleton
internal class EditProfileInfoUIMapper @Inject constructor(
    private val profileRowUIMapper: ProfileRowUIMapper,
) : IMapper<EditProfileInfoDomainModel, EditProfileInfoUIModel> {
    override fun map(response: EditProfileInfoDomainModel): EditProfileInfoUIModel {
        return with(response) {
            EditProfileInfoUIModel(
                title = title,
                row = row?.map(profileRowUIMapper::map),
            )
        }
    }
}

@Singleton
internal class ProfileRowUIMapper @Inject constructor() : IMapper<ProfileRowDomainModel, ProfileRowUIModel> {
    override fun map(response: ProfileRowDomainModel): ProfileRowUIModel {
        return with(response) {
            ProfileRowUIModel(
                title = title,
                placeholder = placeholder,
                text = text,
                parameterName = parameterName,
                isRequired = isRequired,
                type = type.toInfoRowType(),
            )
        }
    }
}

@Singleton
internal class TeamInfoResponseUIMapper @Inject constructor(
    private val teamItemResponseUIMapper: TeamItemResponseUIMapper,
) : IMapper<TeamInfoDomainModel, TeamInfoUIModel> {
    override fun map(response: TeamInfoDomainModel): TeamInfoUIModel {
        return with(response) {
            TeamInfoUIModel(
                title = title,
                info = info,
                teams = teams?.map(teamItemResponseUIMapper::map),
            )
        }
    }
}

@Singleton
internal class TeamItemResponseUIMapper @Inject constructor() : IMapper<TeamItemDomainModel, TeamItemResponseUIModel> {
    override fun map(response: TeamItemDomainModel): TeamItemResponseUIModel {
        return with(response) {
            TeamItemResponseUIModel(
                teamImage = teamImage,
                teamName = teamName,
            )
        }
    }
}

@Singleton
internal class HighlightItemResponseUIMapper @Inject constructor(
    private val branchItemResponseUIMapper: BranchItemResponseUIMapper,
    private val branchesAttributeItemResponseUIMapper: BranchesAttributeItemResponseUIMapper,
) : IMapper<HighlightItemDomainModel, HighlightItemResponseUIModel> {
    override fun map(response: HighlightItemDomainModel): HighlightItemResponseUIModel {
        return with(response) {
            HighlightItemResponseUIModel(
                title = title,
                branches = branches?.map(branchItemResponseUIMapper::map),
                branchesAttributes = branchesAttributes?.map(branchesAttributeItemResponseUIMapper::map),
            )
        }
    }
}

@Singleton
internal class BranchItemResponseUIMapper @Inject constructor() : IMapper<BranchItemDomainModel, BranchItemUIModel> {
    override fun map(response: BranchItemDomainModel): BranchItemUIModel {
        return with(response) {
            BranchItemUIModel(
                branchImage = branchImage,
                branchTitle = branchTitle,
                branchId = branchId,
                isSelected = isSelected,
            )
        }
    }
}

@Singleton
internal class BranchesAttributeItemResponseUIMapper @Inject constructor(
    private val branchInfoRowResponseUIMapper: BranchInfoRowResponseUIMapper,
) : IMapper<BranchesAttributeItemDomainModel, BranchesAttributeItemUIModel> {
    override fun map(response: BranchesAttributeItemDomainModel): BranchesAttributeItemUIModel {
        return with(response) {
            BranchesAttributeItemUIModel(
                branchId = branchId,
                branchInfoRow = branchInfoRow?.map(branchInfoRowResponseUIMapper::map),
            )
        }
    }
}

@Singleton
internal class BranchInfoRowResponseUIMapper @Inject constructor() : IMapper<BranchInfoRowDomainModel, BranchInfoRowUIModel> {
    override fun map(response: BranchInfoRowDomainModel): BranchInfoRowUIModel {
        return with(response) {
            BranchInfoRowUIModel(
                title = title,
                placeholder = placeholder,
                text = text,
                parameterName = parameterName,
                isRequired = isRequired,
                type = type.toInfoRowType(),
            )
        }
    }
}

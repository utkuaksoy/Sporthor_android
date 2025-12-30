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
package com.iamkurtgoz.feature.home.editTeam.selectBranch.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.BranchesDomainModel
import com.iamkurtgoz.domain.model.response.BranchesItemDomainModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesItemUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BranchesUIMapper @Inject constructor(
    private val branchesItemUIMapper: BranchesItemUIMapper,
) : IMapper<BranchesDomainModel, BranchesUIModel> {

    override fun map(response: BranchesDomainModel): BranchesUIModel {
        return BranchesUIModel(
            branches = response.branches?.map { branchesItemUIMapper.map(it) },
        )
    }
}

internal class BranchesItemUIMapper @Inject constructor() :
    IMapper<BranchesItemDomainModel, BranchesItemUIModel> {

    override fun map(response: BranchesItemDomainModel): BranchesItemUIModel {
        return with(response) {
            BranchesItemUIModel(
                branchId = branchId,
                branchImage = branchImage,
                branchTitle = branchTitle,
                isSelected = isSelected,
            )
        }
    }
}

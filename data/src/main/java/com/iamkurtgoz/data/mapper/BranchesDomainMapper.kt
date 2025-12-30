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
import com.iamkurtgoz.data.model.BranchesItemResponseModel
import com.iamkurtgoz.data.model.BranchesResponseModel
import com.iamkurtgoz.domain.model.response.BranchesDomainModel
import com.iamkurtgoz.domain.model.response.BranchesItemDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BranchesDomainMapper @Inject constructor(
    private val branchesItemDomainMapper: BranchesItemDomainMapper,
) : IMapper<BranchesResponseModel, BranchesDomainModel> {

    override fun map(response: BranchesResponseModel): BranchesDomainModel {
        return BranchesDomainModel(
            branches = response.branches?.map { branchesItemDomainMapper.map(it) },
        )
    }
}

internal class BranchesItemDomainMapper @Inject constructor() :
    IMapper<BranchesItemResponseModel, BranchesItemDomainModel> {

    override fun map(response: BranchesItemResponseModel): BranchesItemDomainModel {
        return with(response) {
            BranchesItemDomainModel(
                branchId = branchId,
                branchImage = branchImage,
                branchTitle = branchTitle,
                isSelected = isSelected,
            )
        }
    }
}

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
import com.iamkurtgoz.domain.model.response.BranchInfoRowDomainModel
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchInfoRowUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesAttributeUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.type.toInfoRowType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BranchesAttributeUIMapper @Inject constructor(
    private val branchInfoRowResponseUIMapper: BranchInfoRowResponseUIMapper,
) : IMapper<BranchesAttributeItemDomainModel, BranchesAttributeUIModel> {
    override fun map(response: BranchesAttributeItemDomainModel): BranchesAttributeUIModel {
        return with(response) {
            BranchesAttributeUIModel(
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

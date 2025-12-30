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
package com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model

import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.type.InfoRowType

data class BranchesAttributeUIModel(
    val branchId: String?,
    val branchInfoRow: List<BranchInfoRowUIModel>?,
)

data class BranchInfoRowUIModel(
    val title: String?,
    val placeholder: String?,
    val text: String?,
    val parameterName: String?,
    val isRequired: Boolean?,
    val type: InfoRowType?,
)

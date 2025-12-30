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
package com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.ConfigurationDomainModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.model.ConfigurationBranchUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.model.ConfigurationCoachRoleUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.model.ConfigurationUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.domain.model.ConfigurationUserRoleUIModel
import javax.inject.Inject

internal class ConfigurationUIMapper @Inject constructor() : IMapper<ConfigurationDomainModel, ConfigurationUIModel> {
    override fun map(response: ConfigurationDomainModel): ConfigurationUIModel {
        return with(response) {
            ConfigurationUIModel(
                showExperience = showExperience,
                branches = branches?.map {
                    ConfigurationBranchUIModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
                userRoles = userRoles?.map {
                    ConfigurationUserRoleUIModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
                coachRoles = coachRoles?.map {
                    ConfigurationCoachRoleUIModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
                selectedUserRolesTypeList = emptyList(),
            )
        }
    }
}

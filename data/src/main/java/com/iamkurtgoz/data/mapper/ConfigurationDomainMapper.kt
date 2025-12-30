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
import com.iamkurtgoz.data.model.ConfigurationResponseModel
import com.iamkurtgoz.domain.model.response.ConfigurationBranchDomainModel
import com.iamkurtgoz.domain.model.response.ConfigurationCoachRoleDomainModel
import com.iamkurtgoz.domain.model.response.ConfigurationDomainModel
import com.iamkurtgoz.domain.model.response.ConfigurationUserRoleDomainModel
import javax.inject.Inject

class ConfigurationDomainMapper @Inject constructor() : IMapper<ConfigurationResponseModel, ConfigurationDomainModel> {
    override fun map(response: ConfigurationResponseModel): ConfigurationDomainModel {
        return with(response) {
            ConfigurationDomainModel(
                showExperience = showExperience,
                branches = branches?.map {
                    ConfigurationBranchDomainModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
                userRoles = userRoles?.map {
                    ConfigurationUserRoleDomainModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
                coachRoles = coachRoles?.map {
                    ConfigurationCoachRoleDomainModel(
                        detail = it?.detail,
                        name = it?.name,
                        value = it?.value,
                    )
                },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.auth.userName.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.CheckUserNameDomainModel
import com.iamkurtgoz.feature.auth.userName.domain.model.CheckUserNameUIModel
import javax.inject.Inject

internal class CheckUserNameUIMapper @Inject constructor() : IMapper<CheckUserNameDomainModel, CheckUserNameUIModel> {
    override fun map(response: CheckUserNameDomainModel): CheckUserNameUIModel {
        return with(response) {
            CheckUserNameUIModel(
                isUsable = isUsable,
                suggestions = suggestions?.filterNotNull()?.map {
                    Pair(
                        first = it,
                        second = it,
                    )
                } ?: listOf(),
            )
        }
    }
}

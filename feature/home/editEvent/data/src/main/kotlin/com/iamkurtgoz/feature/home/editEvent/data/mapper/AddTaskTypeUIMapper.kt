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
package com.iamkurtgoz.feature.home.editEvent.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.AddTaskTypeDomainModel
import com.iamkurtgoz.feature.home.editEvent.domain.model.AddTaskTypeUIModel
import com.iamkurtgoz.feature.home.editEvent.domain.model.AddTaskTypeUIModelDetail
import javax.inject.Inject

class AddTaskTypeUIMapper @Inject constructor() : IMapper<AddTaskTypeDomainModel, AddTaskTypeUIModel> {
    override fun map(response: AddTaskTypeDomainModel): AddTaskTypeUIModel {
        return with(response) {
            AddTaskTypeUIModel(
                detail = AddTaskTypeUIModelDetail(
                    name = response.detail?.name,
                    value = response.detail?.value,
                    detail = response.detail?.detail,
                ),
            )
        }
    }
}

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
import com.iamkurtgoz.data.model.OnBoardingResponseModel
import com.iamkurtgoz.domain.model.response.OnBoardingDomainModel
import com.iamkurtgoz.domain.model.response.OnBoardingPageItemDomainModel
import javax.inject.Inject

internal class OnBoardingDomainMapper @Inject constructor() : IMapper<OnBoardingResponseModel, OnBoardingDomainModel> {
    override fun map(response: OnBoardingResponseModel): OnBoardingDomainModel {
        return with(response) {
            OnBoardingDomainModel(
                pages = pages?.map {
                    OnBoardingPageItemDomainModel(
                        description = it?.description,
                        image = it?.image,
                        title = it?.title,
                    )
                },
            )
        }
    }
}

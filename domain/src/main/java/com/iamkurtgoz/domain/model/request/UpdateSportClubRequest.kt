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
package com.iamkurtgoz.domain.model.request

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateSportClubRequest(
    @SerialName("address")
    val address: String?,
    @SerialName("city")
    val city: String?,
    @SerialName("clubName")
    val clubName: String?,
    @SerialName("county")
    val county: String?,
    @SerialName("foundationYear")
    val foundationYear: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("logo")
    val logo: String?,
    @SerialName("branchId")
    val branchId: String?,
)

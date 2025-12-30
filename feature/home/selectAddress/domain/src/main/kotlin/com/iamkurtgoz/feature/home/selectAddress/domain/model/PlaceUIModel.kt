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
package com.iamkurtgoz.feature.home.selectAddress.domain.model

data class PlaceUIModel(
    val name: String,
    val address: String,
    val city: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?,
)

fun toUIModel(primaryText: String, fullText: String, latitude: Double?, longitude: Double?): PlaceUIModel {
    val parts = fullText.split(", ").map { it.trim() }
    val country = parts.lastOrNull()
    val city = parts.getOrNull(parts.size - 2)
    val address = parts
        .take(parts.size - (if (city != null && country != null) 2 else 0))
        .joinToString(", ")
    return PlaceUIModel(
        name = primaryText,
        address = address,
        city = city,
        country = country,
        latitude = latitude,
        longitude = longitude,
    )
}

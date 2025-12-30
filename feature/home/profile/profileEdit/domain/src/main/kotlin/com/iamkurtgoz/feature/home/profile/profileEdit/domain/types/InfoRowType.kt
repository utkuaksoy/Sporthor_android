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
package com.iamkurtgoz.feature.home.profile.profileEdit.domain.types

import com.iamkurtgoz.core.common.contract.AppDefaults

enum class InfoRowKeyboardType {
    NORMAL,
    NUMBER,
}

enum class InfoRowType(
    val type: Int,
    val extension: String,
    val keyboardType: InfoRowKeyboardType,
) {
    POSITION(
        type = AppDefaults.ZERO,
        extension = "",
        keyboardType = InfoRowKeyboardType.NORMAL,
    ),
    DATE(
        type = AppDefaults.ONE,
        extension = "",
        keyboardType = InfoRowKeyboardType.NORMAL,
    ),
    LENGTH(
        type = AppDefaults.TWO,
        extension = "cm",
        keyboardType = InfoRowKeyboardType.NUMBER,
    ),
    WEIGHT(
        type = AppDefaults.THREE,
        extension = "kg",
        keyboardType = InfoRowKeyboardType.NUMBER,
    ),
    SECOND(
        type = AppDefaults.FOUR,
        extension = "s",
        keyboardType = InfoRowKeyboardType.NUMBER,
    ),
    LADDER_DRILL(
        type = AppDefaults.FIVE,
        extension = "s",
        keyboardType = InfoRowKeyboardType.NUMBER,
    ),
    ;

    companion object {
        fun fromValue(type: Int): InfoRowType? {
            return when (type) {
                AppDefaults.ZERO -> POSITION
                AppDefaults.ONE -> DATE
                AppDefaults.TWO -> LENGTH
                AppDefaults.THREE -> WEIGHT
                AppDefaults.FOUR -> SECOND
                AppDefaults.FIVE -> LADDER_DRILL
                else -> null
            }
        }
    }
}

fun Int?.toInfoRowType(): InfoRowType? {
    return this?.let { InfoRowType.fromValue(it) }
}

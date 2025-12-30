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
package com.iamkurtgoz.domain.model.enums

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
enum class CustomMediaType(val value: Int) : Parcelable {
    IMAGE(value = 0),
    VIDEO(value = 1),
    ;

    companion object {
        fun fromValue(value: Int): CustomMediaType? {
            return when (value) {
                IMAGE.value -> IMAGE
                VIDEO.value -> VIDEO
                else -> null
            }
        }
    }
}

fun Int?.toCustomMediaType(): CustomMediaType? {
    return this?.let { CustomMediaType.fromValue(it) }
}

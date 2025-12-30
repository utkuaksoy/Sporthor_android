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
import com.iamkurtgoz.core.common.contract.AppDefaults
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
enum class SignalRMessageType(val type: Int) : Parcelable {
    TEXT(AppDefaults.ZERO),
    IMAGE(AppDefaults.ONE),
    VIDEO(AppDefaults.TWO),
    FILE(AppDefaults.THREE),
    ;

    companion object {
        fun fromValue(type: Int): SignalRMessageType? {
            return when (type) {
                TEXT.type -> TEXT
                IMAGE.type -> IMAGE
                VIDEO.type -> VIDEO
                FILE.type -> FILE
                else -> null
            }
        }
    }
}

fun Int?.toSignalRMessageType(): SignalRMessageType? {
    return this?.let { SignalRMessageType.fromValue(it) }
}

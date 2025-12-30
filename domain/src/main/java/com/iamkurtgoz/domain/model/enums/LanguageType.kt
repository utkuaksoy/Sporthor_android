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

import java.util.Locale

enum class LanguageType(val value: String, val id: Int, val language: String) {
    Turkish(value = "Turkish", id = 0, language = "tr"),
    English(value = "English", id = 1, language = "en"),
    ;

    companion object {
        fun fromValue(value: String): LanguageType? {
            return when (value.lowercase()) {
                Turkish.value.lowercase() -> Turkish
                English.value.lowercase() -> English
                else -> null
            }
        }

        fun fromLocale(locale: Locale): LanguageType {
            return if (locale.language.contains(Turkish.language)) {
                Turkish
            } else {
                English
            }
        }
    }
}

fun String?.toLanguageType(): LanguageType? {
    return this?.let { LanguageType.fromValue(it) }
}

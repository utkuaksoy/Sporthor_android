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
package com.iamkurtgoz.core.common.extensions

import com.iamkurtgoz.core.common.contract.AppDefaults

fun String.isNumber(): Boolean {
    return this.all { it.isDigit() }
}

fun String.isEmail(): Boolean {
    val emailRegex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
    return emailRegex.matches(this)
}

fun String.isTurkishMobileNumber(): Boolean {
    val turkishMobileRegex = Regex(
        pattern = """^5\d{2}[-\s]?\d{3}[-\s]?\d{2}[-\s]?\d{2}$""",
    )
    return turkishMobileRegex.matches(this)
}

fun String.isPasswordValid(): Boolean {
    val regex = Regex(
        pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
    )
    return regex.matches(this)
}

fun String.isUserNameValid(): Boolean {
    val regex = Regex(
        pattern = "^[a-z0-9._]{4,15}\$",
    )
    return regex.matches(this)
}

fun String.isDay(): Boolean {
    if (this.isNumber()) {
        val valueLength = this.length
        val value = this.toIntOrNull() ?: AppDefaults.ZERO
        return (value in AppDefaults.DAY_MIN..AppDefaults.DAY_MAX && valueLength in AppDefaults.DAY_MIN_LENGTH..AppDefaults.DAY_MAX_LENGTH)
    }
    return false
}

fun String.isMonth(): Boolean {
    if (this.isNumber()) {
        val valueLength = this.length
        val value = this.toIntOrNull() ?: AppDefaults.ZERO
        return (value in AppDefaults.MONTH_MIN..AppDefaults.MONTH_MAX && valueLength in AppDefaults.MONTH_MIN_LENGTH..AppDefaults.MONTH_MAX_LENGTH)
    }
    return false
}

fun String.isYear(): Boolean {
    if (this.isNumber()) {
        val valueLength = this.length
        val value = this.toIntOrNull() ?: AppDefaults.ZERO
        return (value in AppDefaults.YEAR_MIN..AppDefaults.YEAR_MAX && valueLength in AppDefaults.YEAR_MIN_LENGTH..AppDefaults.YEAR_MAX_LENGTH)
    }
    return false
}

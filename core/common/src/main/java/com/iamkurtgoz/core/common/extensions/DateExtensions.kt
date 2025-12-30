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

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// LocalDate Extensions
fun LocalDate.toString(
    format: DateFormat? = null,
    customFormat: String? = null,
): String {
    val formatter = if (customFormat != null) {
        DateTimeFormatter.ofPattern(customFormat)
    } else if (format != null) {
        DateTimeFormatter.ofPattern(format.pattern)
    } else {
        DateTimeFormatter.ISO_LOCAL_DATE
    }
    return this.format(formatter)
}

fun String.toLocalDate(
    format: DateFormat? = null,
    customFormat: String? = null,
): LocalDate? {
    return try {
        val formatter = if (customFormat != null) {
            DateTimeFormatter.ofPattern(customFormat)
        } else if (format != null) {
            DateTimeFormatter.ofPattern(format.pattern)
        } else {
            DateTimeFormatter.ISO_LOCAL_DATE
        }
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun LocalDate.toEpochMilli(zoneId: ZoneId = ZoneId.of("UTC")): Long {
    return this.atStartOfDay(zoneId).toInstant().toEpochMilli()
}

// LocalDateTime Extensions
fun LocalDateTime.toString(
    format: DateFormat? = null,
    customFormat: String? = null,
): String {
    val formatter = if (customFormat != null) {
        DateTimeFormatter.ofPattern(customFormat)
    } else if (format != null) {
        DateTimeFormatter.ofPattern(format.pattern)
    } else {
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
    return this.format(formatter)
}

fun String.toLocalDateTime(
    format: DateFormat? = null,
    customFormat: String? = null,
): LocalDateTime? {
    return try {
        val formatter = if (customFormat != null) {
            DateTimeFormatter.ofPattern(customFormat)
        } else if (format != null) {
            DateTimeFormatter.ofPattern(format.pattern)
        } else {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        }
        LocalDateTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

// Predefined Date Formats
enum class DateFormat(val pattern: String) {
    YYYY_MM_DD_HH_MM_SS_SSSSSS("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
    YYYY_MM_DD_T_HH_MM_SSZ("yyyy-MM-dd'T'HH:mm:ssZ"),
    YYYY_MM_DD("yyyy-MM-dd"),
    DD_MM_YYYY("dd-MM-yyyy"),
    D_MMMM_EEEEE_YYYY("d MMMM EEEE, yyyy"),
    SHORT("yyyy.MM.dd"),
    TIME("HH:mm"),
}

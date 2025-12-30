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

import android.content.Context
import android.util.Base64
import android.webkit.MimeTypeMap
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.resources.R as resourcesR
import timber.log.Timber
import java.io.File
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale

private object StringExtensionsDefaults {
    const val SECONDS_IN_AN_HOUR = 3600
    const val SECONDS_IN_A_MINUTE = 60
    const val SECONDS_IN_MINUTE = 60.0
    const val SECONDS_IN_HOUR = 60.0 * SECONDS_IN_MINUTE
    const val SECONDS_IN_DAY = 24.0 * SECONDS_IN_HOUR
    const val SECONDS_IN_WEEK = 7.0 * SECONDS_IN_DAY
    const val SECONDS_IN_MONTH = 30.0 * SECONDS_IN_DAY
    const val SECONDS_IN_YEAR = 365.0 * SECONDS_IN_DAY
}

val String.Companion.Empty
    get() = ""

val String.Companion.Brackets
    get() = "("

val String.Companion.ReverseBrackets
    get() = ")"

val String?.orEmpty
    get() = this ?: String.Empty

fun String?.encodeBase64(): String? {
    val data: ByteArray
    try {
        data = (this ?: "").toByteArray(charset("UTF-8"))
        return Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: UnsupportedEncodingException) {
        Timber.tag("StringExtensions").d(e, "Error")
    }
    return null
}

fun String?.capitalizeFirstLetter(): String {
    this?.let {
        val firstChar = it.substring(0, 1)
        val other = it.substring(1)
        return "${firstChar.uppercase()}${other.lowercase()}"
    } ?: kotlin.run {
        return ""
    }
}

fun String.findAllIndexes(subStr: String): List<Int> {
    val indexes = mutableListOf<Int>()
    var index = this.indexOf(subStr)
    while (index != -1) {
        indexes.add(index)
        index = this.indexOf(subStr, index + 1)
    }
    return indexes
}

fun BigDecimal.formatPrice(): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(this)
}

fun Double.formattedTime(): String {
    val hours = (this / StringExtensionsDefaults.SECONDS_IN_AN_HOUR).toInt()
    val minutes = ((this % StringExtensionsDefaults.SECONDS_IN_AN_HOUR) / StringExtensionsDefaults.SECONDS_IN_A_MINUTE).toInt()
    val seconds = (this % StringExtensionsDefaults.SECONDS_IN_A_MINUTE).toInt()

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}

fun Double.formatRating(): String {
    val formatter = DecimalFormat("0.0")
    return formatter.format(this)
}

fun Double.timeElapsed(context: Context): String {
    val minute = StringExtensionsDefaults.SECONDS_IN_MINUTE
    val hour = StringExtensionsDefaults.SECONDS_IN_HOUR * minute
    val day = StringExtensionsDefaults.SECONDS_IN_DAY * hour
    val week = StringExtensionsDefaults.SECONDS_IN_WEEK * day
    val month = StringExtensionsDefaults.SECONDS_IN_MONTH * day
    val year = StringExtensionsDefaults.SECONDS_IN_YEAR * day

    return when (this) {
        in 0.0..minute -> context.getString(resourcesR.string.time_just_now)
        in minute..hour -> {
            val minutesElapsed = (this / minute).toInt()
            context.getString(resourcesR.string.time_filter_minute_ago, minutesElapsed)
        }
        in hour..day -> {
            val hoursElapsed = (this / hour).toInt()
            context.getString(resourcesR.string.time_filter_hour_ago, hoursElapsed)
        }
        in day..week -> {
            val daysElapsed = (this / day).toInt()
            context.getString(resourcesR.string.time_filter_day_ago, daysElapsed)
        }
        in week..month -> {
            val weeksElapsed = (this / week).toInt()
            context.getString(resourcesR.string.time_filter_week_ago, weeksElapsed)
        }
        in month..year -> {
            val monthsElapsed = (this / month).toInt()
            context.getString(resourcesR.string.time_filter_month_ago, monthsElapsed)
        }
        else -> {
            val yearsElapsed = (this / year).toInt()
            context.getString(resourcesR.string.time_filter_year_ago, yearsElapsed)
        }
    }
}

fun File?.getMimeType(): String? {
    var extension: String = this?.extension ?: return "*/*"
    val lastDot = extension.lastIndexOf('.')
    if (lastDot != -1) {
        extension = extension.substring(lastDot + 1)
    }

    extension = extension.lowercase(Locale.getDefault())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun String?.applyMaskedPhoneNumber(): String {
    if (this == null) return ""
    if (this.length != AppDefaults.TEN) return this
    val raw = take(AppDefaults.TEN)
        .trim()
        .replace(" ".toRegex(), "")
    if (raw.isEmpty()) return ""

    val sb = StringBuilder()

    for (i in raw.indices) {
        sb.append(raw[i])
        if (i == AppDefaults.TWO && i < raw.lastIndex) {
            sb.append(' ')
        }
        if (i == AppDefaults.FIVE && i < raw.lastIndex) {
            sb.append(' ')
        }

        if (i == AppDefaults.SEVEN && i < raw.lastIndex) {
            sb.append(' ')
        }
    }
    return sb.toString()
}

fun String?.getUserNameFirstChar(): String {
    if (this == null) return ""
    val userNameList = this.split(" ").map { it.firstOrNull() ?: "" }
    return userNameList.joinToString(",").replace(",".toRegex(), "").trim().uppercase()
}

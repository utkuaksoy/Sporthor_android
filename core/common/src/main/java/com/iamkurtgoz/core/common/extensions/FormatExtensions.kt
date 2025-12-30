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

import java.util.Locale

private object FormatExtensionsDefaults {
    const val ONE_THOUSAND = 1000
    const val ONE_MILLION = 1_000_000
}

fun Int.formatNumber(): String {
    return when {
        this < FormatExtensionsDefaults.ONE_THOUSAND -> this.toString()
        this < FormatExtensionsDefaults.ONE_MILLION -> String.format(Locale.getDefault(), "%.1fK", this / FormatExtensionsDefaults.ONE_THOUSAND).replace(".0K", "K")
        else -> String.format(Locale.getDefault(), "%.1fM", this / FormatExtensionsDefaults.ONE_MILLION).replace(".0M", "M")
    }
}

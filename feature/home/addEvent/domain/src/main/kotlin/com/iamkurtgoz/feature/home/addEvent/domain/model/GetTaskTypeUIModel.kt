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
package com.iamkurtgoz.feature.home.addEvent.domain.model

import androidx.core.graphics.toColorInt

data class GetTaskTypeUIModel(
    val types: List<GetTaskTypeUIModelType?>?,
)

data class GetTaskTypeUIModelType(
    val detail: String?,
    val name: String?,
    val value: String?,
) {
    val color: Int
        get() {
            val color = detail ?: return "#000000".toColorInt()
            return try {
                color.toColorInt()
            } catch (_: Exception) {
                "#000000".toColorInt()
            }
        }
}

val mockGetTaskTypeUIModel: List<GetTaskTypeUIModelType> = listOf(
    GetTaskTypeUIModelType(detail = "#B1FA63", name = "Antreman", value = "685d9899450913f6cbe99bb3"),
    GetTaskTypeUIModelType(detail = "#947AFF", name = "Egzersiz", value = "685d989c450913f6cbe99bb4"),
    GetTaskTypeUIModelType(detail = "#FF7BF0", name = "Kamp", value = "685d98a0450913f6cbe99bb5"),
    GetTaskTypeUIModelType(detail = "#FF6359", name = "Fitness", value = "685d98a1450913f6cbe99bb6"),
    GetTaskTypeUIModelType(detail = "#335CFF", name = "Rehabilitasyon", value = "685d98a2450913f6cbe99bb7"),
    GetTaskTypeUIModelType(detail = "#335CFF", name = "Reserr", value = "68657454502e0da303870985"),
    GetTaskTypeUIModelType(detail = "#335CFF", name = "Test", value = "6865953a7d304bb096270d5e"),
    GetTaskTypeUIModelType(detail = "#335CFF", name = "Düzenleme", value = "686595407d304bb096270d5f"),
    GetTaskTypeUIModelType(detail = "#335CFF", name = "İdman", value = "686595547d304bb096270d60"),
)

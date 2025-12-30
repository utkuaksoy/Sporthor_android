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
package com.iamkurtgoz.feature.home.selectEventDrafts.domain.model

import androidx.core.graphics.toColorInt
import java.time.LocalDateTime

data class CalendarDetailEventUIModel(
    val tasks: List<CalendarDetailEventUIModelTask?>?,
)

data class CalendarDetailEventUIModelTask(
    val allDay: Boolean?,
    val description: String?,
    val endDate: LocalDateTime?,
    val hour: String?,
    val id: String?,
    val isRecurring: Boolean?,
    val location: CalendarDetailEventUIModelLocation?,
    val recurrence: Int?,
    val startDate: LocalDateTime?,
    val taskType: CalendarDetailEventUIModelTaskType?,
    val title: String?,
    val trainingGroup: TeamsUIItemModel?,
    val users: List<CalendarDetailEventUIModelUser?>?,
)

data class CalendarDetailEventUIModelLocation(
    val address: String?,
    val lat: Double?,
    val lng: Double?,
    val title: String?,
)

data class CalendarDetailEventUIModelTaskType(
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

data class CalendarDetailEventUIModelUser(
    val id: String?,
    val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    val name: String?,
    val summary: String?,
    val username: String?,
)

data class TeamsUIItemModel(
    val name: String?,
    val value: String?,
    val image: String?,
    val detail: String?,
)

val mockCalendarDetailEvent = CalendarDetailEventUIModel(
    tasks = listOf(
        // 1. JSON'dan birebir gelen veri
        CalendarDetailEventUIModelTask(
            id = "6862eb993e4b649b85e61c0c",
            title = "Kamp Test v1",
            hour = "19 - 21 Her Gün",
            allDay = false,
            location = CalendarDetailEventUIModelLocation(
                title = "Ümraniye Çarşı Saat Kulesi",
                address = "Alemdağ Cd., 115, Ümraniye, İstanbul, Türkiye",
                lat = 41.0258838,
                lng = 29.0967321,
            ),
            isRecurring = false,
            startDate = null,
            endDate = null,
            recurrence = 0,
            taskType = CalendarDetailEventUIModelTaskType(
                name = "Kamp",
                value = "685d98a0450913f6cbe99bb5",
                detail = "#FF7BF0",
            ),
            description = "Test Test",
            trainingGroup = null,
            users = listOf(
                CalendarDetailEventUIModelUser(
                    id = "685c63585970f8eef1545150",
                    name = "Der Turke",
                    username = "derturke3",
                    summary = "User",
                    imageUrl = "https://api.sporthor.com/Uploads/ba89bf51-c58b-483e-b63a-abf21160f52e.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
                CalendarDetailEventUIModelUser(
                    id = "685c66d8e62dc93bcfc12e77",
                    name = "Emre Öztürk",
                    username = "emreozturk",
                    summary = "User",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                CalendarDetailEventUIModelUser(
                    id = "68611ddaa70665a7f6809091",
                    name = "Eşref Tek",
                    username = "esreftek",
                    summary = "User",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = false,
                ),
                CalendarDetailEventUIModelUser(
                    id = "6861983308766379410d38bc",
                    name = "Eşref Tek",
                    username = "esreftek1",
                    summary = "User",
                    imageUrl = "https://api.sporthor.com/Uploads/f4531cbf-e44f-4a3f-850a-af0639935b9a.jpg",
                    isFollow = false,
                    isCurrentUser = false,
                ),
            ),
        ),

        // 2. Rastgele veri #1
        CalendarDetailEventUIModelTask(
            id = "1a2b3c4d5e6f7g8h9i0j",
            title = "Serbest Çalışma",
            hour = "08:00 - 12:00",
            allDay = false,
            location = CalendarDetailEventUIModelLocation(
                title = "Ev Ofisi",
                address = "Nişantaşı, İstanbul, Türkiye",
                lat = 41.0619,
                lng = 28.9974,
            ),
            isRecurring = true,
            startDate = LocalDateTime.of(2025, 7, 10, 8, 0),
            endDate = LocalDateTime.of(2025, 7, 10, 12, 0),
            recurrence = 1, // günlük tekrar
            taskType = CalendarDetailEventUIModelTaskType(
                name = "Çalışma",
                value = "work01",
                detail = "#00FF00",
            ),
            description = "Projeye devam etme zamanı",
            trainingGroup = null,
            users = listOf(
                CalendarDetailEventUIModelUser(
                    id = "1234567890abcdef",
                    name = "Mehmet Kurt",
                    username = "mehmetk",
                    summary = "You",
                    imageUrl = null,
                    isFollow = false,
                    isCurrentUser = true,
                ),
            ),
        ),

        // 3. Rastgele veri #2
        CalendarDetailEventUIModelTask(
            id = "abcdef1234567890abcd",
            title = "Takım Antrenmanı",
            hour = "17:00 - 18:30",
            allDay = false,
            location = CalendarDetailEventUIModelLocation(
                title = "Kadıköy Stadyumu",
                address = "Rıhtım Cd. No:51, Kadıköy, İstanbul, Türkiye",
                lat = 40.9900,
                lng = 29.0289,
            ),
            isRecurring = true,
            startDate = LocalDateTime.of(2025, 7, 12, 17, 0),
            endDate = LocalDateTime.of(2025, 7, 12, 18, 30),
            recurrence = 7, // haftalık tekrar
            taskType = CalendarDetailEventUIModelTaskType(
                name = "Antrenman",
                value = "trainingGroup",
                detail = "#FFA500",
            ),
            description = "Haftalık takım antrenmanı",
            trainingGroup = TeamsUIItemModel(
                name = "Galatasaray",
                value = "1905",
                image = "https://picsum.photos/seed/gs/48",
                detail = "#FFD700",
            ),
            users = emptyList(),
        ),
    ),
)

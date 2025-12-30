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
package com.iamkurtgoz.core.navigation.model.home.addEvent

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.Base64StringSerializer
import com.iamkurtgoz.domain.serializer.LocalDateSerializer
import com.iamkurtgoz.domain.serializer.LocalDateTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenNavigationModel(
    @Serializable(LocalDateSerializer::class) val selectedDate: LocalDate?,
    val task: HomeScreenAddEventScreenTask? = null,
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenTask(
    val allDay: Boolean?,
    @Serializable(Base64StringSerializer::class) val description: String?,
    @Serializable(LocalDateTimeSerializer::class) val endDate: LocalDateTime?,
    @Serializable(Base64StringSerializer::class) val hour: String?,
    @Serializable(Base64StringSerializer::class) val id: String?,
    val isRecurring: Boolean?,
    val location: HomeScreenAddEventScreenLocation?,
    val recurrence: Int?,
    @Serializable(LocalDateTimeSerializer::class) val startDate: LocalDateTime?,
    val taskType: HomeScreenAddEventScreenTaskType?,
    @Serializable(Base64StringSerializer::class) val title: String?,
    val trainingGroup: HomeScreenAddEventScreenModel?,
    val users: List<HomeScreenAddEventScreenUser?>?,
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenLocation(
    @Serializable(Base64StringSerializer::class) val address: String?,
    val lat: Double?,
    val lng: Double?,
    @Serializable(Base64StringSerializer::class) val title: String?,
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenTaskType(
    @Serializable(Base64StringSerializer::class) val detail: String?,
    @Serializable(Base64StringSerializer::class) val name: String?,
    @Serializable(Base64StringSerializer::class) val value: String?,
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenUser(
    @Serializable(Base64StringSerializer::class) val id: String?,
    @Serializable(Base64StringSerializer::class) val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    @Serializable(Base64StringSerializer::class) val name: String?,
    @Serializable(Base64StringSerializer::class) val summary: String?,
    @Serializable(Base64StringSerializer::class) val username: String?,
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddEventScreenModel(
    @Serializable(Base64StringSerializer::class) val name: String?,
    @Serializable(Base64StringSerializer::class) val value: String?,
    @Serializable(Base64StringSerializer::class) val image: String?,
    @Serializable(Base64StringSerializer::class) val detail: String?,
) : Parcelable

val homeScreenAddEventRouteTypeMap = mapOf(
    typeOf<HomeScreenAddEventScreenNavigationModel>() to CustomNavType(
        HomeScreenAddEventScreenNavigationModel::class.java,
        HomeScreenAddEventScreenNavigationModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenAddEventRouteTypeMap(): HomeScreenAddEventRoute {
    return this.toRoute<HomeScreenAddEventRoute>(
        typeMap = homeScreenAddEventRouteTypeMap,
    )
}

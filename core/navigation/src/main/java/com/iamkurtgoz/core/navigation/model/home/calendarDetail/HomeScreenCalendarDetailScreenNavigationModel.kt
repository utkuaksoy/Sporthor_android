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
package com.iamkurtgoz.core.navigation.model.home.calendarDetail

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenCalendarDetailRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.LocalDateSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenCalendarDetailScreenNavigationModel(
    @Serializable(LocalDateSerializer::class) val selectedDate: LocalDate?,
) : Parcelable

val homeScreenCalendarDetailRouteTypeMap = mapOf(
    typeOf<HomeScreenCalendarDetailScreenNavigationModel>() to CustomNavType(
        HomeScreenCalendarDetailScreenNavigationModel::class.java,
        HomeScreenCalendarDetailScreenNavigationModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenCalendarDetailRouteTypeMap(): HomeScreenCalendarDetailRoute {
    return this.toRoute<HomeScreenCalendarDetailRoute>(
        typeMap = homeScreenCalendarDetailRouteTypeMap,
    )
}

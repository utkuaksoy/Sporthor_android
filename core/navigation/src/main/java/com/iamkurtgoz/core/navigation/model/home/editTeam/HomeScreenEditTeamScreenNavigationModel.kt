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
package com.iamkurtgoz.core.navigation.model.home.editTeam

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenEditTeamRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.Base64StringSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenEditTeamScreenNavigationModel(
    @Serializable(Base64StringSerializer::class) val address: String?,
    @Serializable(Base64StringSerializer::class) val city: String?,
    @Serializable(Base64StringSerializer::class) val clubId: String?,
    @Serializable(Base64StringSerializer::class) val clubName: String?,
    val confirmationStatus: Int?,
    @Serializable(Base64StringSerializer::class) val county: String?,
    @Serializable(Base64StringSerializer::class) val foundationYear: String?,
    @Serializable(Base64StringSerializer::class) val logo: String?,
    @Serializable(Base64StringSerializer::class) val branchName: String?,
    @Serializable(Base64StringSerializer::class) val value: String?,
    @Serializable(Base64StringSerializer::class) val val2: String?,
) : Parcelable

val homeScreenEditTeamRouteTypeMap = mapOf(
    typeOf<HomeScreenEditTeamScreenNavigationModel>() to CustomNavType(
        HomeScreenEditTeamScreenNavigationModel::class.java,
        HomeScreenEditTeamScreenNavigationModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenEditTeamRouteTypeMap(): HomeScreenEditTeamRoute {
    return this.toRoute<HomeScreenEditTeamRoute>(
        typeMap = homeScreenEditTeamRouteTypeMap,
    )
}

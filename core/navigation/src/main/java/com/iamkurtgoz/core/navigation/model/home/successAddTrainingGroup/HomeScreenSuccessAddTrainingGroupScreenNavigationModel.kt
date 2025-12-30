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
package com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.Base64StringSerializer
import com.iamkurtgoz.domain.serializer.EncodedUrlSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenSuccessAddTrainingGroupScreenNavigationModel(
    val isEdit: Boolean = false,
    @Serializable(Base64StringSerializer::class) val clubId: String?,
    @Serializable(Base64StringSerializer::class) val clubName: String?,
    @Serializable(EncodedUrlSerializer::class) val clubLogo: String?,
    @Serializable(Base64StringSerializer::class) val groupId: String?,
    @Serializable(Base64StringSerializer::class) val groupName: String?,
    val users: List<HomeScreenSuccessAddTrainingGroupScreenNavigationModelUser> = listOf(),
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenSuccessAddTrainingGroupScreenNavigationModelUser(
    @Serializable(Base64StringSerializer::class) val id: String?,
    @Serializable(Base64StringSerializer::class) val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    @Serializable(Base64StringSerializer::class) val name: String?,
    @Serializable(Base64StringSerializer::class) val summary: String?,
    @Serializable(Base64StringSerializer::class) val username: String?,
) : Parcelable

val homeScreenSuccessAddTrainingGroupRoute = mapOf(
    typeOf<HomeScreenSuccessAddTrainingGroupScreenNavigationModel>() to CustomNavType(
        HomeScreenSuccessAddTrainingGroupScreenNavigationModel::class.java,
        HomeScreenSuccessAddTrainingGroupScreenNavigationModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenSuccessAddTrainingGroupRoute(): HomeScreenSuccessAddTrainingGroupRoute {
    return this.toRoute<HomeScreenSuccessAddTrainingGroupRoute>(
        typeMap = homeScreenSuccessAddTrainingGroupRoute,
    )
}

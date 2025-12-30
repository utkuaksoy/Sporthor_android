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
package com.iamkurtgoz.core.navigation.model.home.webview

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenDashboardWebviewRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.EncodedUrlSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenWebViewScreenNavigateModel(
    @Serializable(EncodedUrlSerializer::class) val url: String?,
    val title: String,
) : Parcelable

val homeScreenWebViewRouteTypeMap = mapOf(
    typeOf<HomeScreenWebViewScreenNavigateModel>() to CustomNavType(
        HomeScreenWebViewScreenNavigateModel::class.java,
        HomeScreenWebViewScreenNavigateModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenWebViewRoute(): HomeScreenDashboardWebviewRoute {
    return this.toRoute<HomeScreenDashboardWebviewRoute>(
        typeMap = homeScreenWebViewRouteTypeMap,
    )
}

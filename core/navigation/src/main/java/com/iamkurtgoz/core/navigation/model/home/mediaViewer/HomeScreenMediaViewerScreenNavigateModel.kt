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
package com.iamkurtgoz.core.navigation.model.home.mediaViewer

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenMediaViewerRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.domain.serializer.Base64StringSerializer
import com.iamkurtgoz.domain.serializer.EncodedUrlSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
sealed class HomeScreenMediaViewerScreenNavigateModel : Parcelable {

    @Keep
    @Serializable
    @Parcelize
    data class Base64(
        @Serializable(Base64StringSerializer::class) val base64: String?,
        val extension: String,
        val signalRMessageType: SignalRMessageType,
    ) : HomeScreenMediaViewerScreenNavigateModel(), Parcelable

    @Keep
    @Serializable
    @Parcelize
    data class RemoteVideo(
        @Serializable(EncodedUrlSerializer::class) val videoUrl: String?,
    ) : HomeScreenMediaViewerScreenNavigateModel(), Parcelable

    @Keep
    @Serializable
    @Parcelize
    data class LocalVideo(
        @Serializable(Base64StringSerializer::class) val uriPath: String?,
    ) : HomeScreenMediaViewerScreenNavigateModel(), Parcelable

    @Keep
    @Serializable
    @Parcelize
    data class RemoteOrLocalImage(
        @Serializable(Base64StringSerializer::class) val imageData: String?,
    ) : HomeScreenMediaViewerScreenNavigateModel(), Parcelable
}

val homeScreenMediaViewerRouteTypeMap = mapOf(
    typeOf<HomeScreenMediaViewerScreenNavigateModel>() to CustomNavType(
        HomeScreenMediaViewerScreenNavigateModel::class.java,
        HomeScreenMediaViewerScreenNavigateModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenMediaViewerRoute(): HomeScreenMediaViewerRoute {
    return this.toRoute<HomeScreenMediaViewerRoute>(
        typeMap = homeScreenMediaViewerRouteTypeMap,
    )
}

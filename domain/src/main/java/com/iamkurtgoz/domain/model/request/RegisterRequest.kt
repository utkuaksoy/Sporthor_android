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
package com.iamkurtgoz.domain.model.request

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RegisterRequest(
    @SerialName("email") val email: String?,
    @SerialName("firebaseId") val firebaseId: String?,
    @SerialName("firebaseToken") val firebaseToken: String?,
    @SerialName("mobilePhone") val mobilePhone: String?,
    @SerialName("name") val name: String?,
    @SerialName("password") val password: String?,
    @SerialName("socialInfo") val socialInfo: RegisterSocialInfoRequest?,
    @SerialName("surname") val surname: String?,
    @SerialName("username") val username: String?,
)

@Keep
@Serializable
@Parcelize
data class RegisterSocialInfoRequest(
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("accountId") val accountId: String?,
    @SerialName("email") val email: String?,
    @SerialName("platform") val platform: Int?,
) : Parcelable

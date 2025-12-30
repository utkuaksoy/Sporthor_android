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
package com.iamkurtgoz.core.navigation.model.auth.register

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.AuthRegisterScreenRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.model.request.RegisterSocialInfoRequest
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class AuthRegisterScreenNavigateModel(
    val registerSocialInfoRequest: RegisterSocialInfoRequest?,
) : Parcelable

val authRegisterScreenRouteTypeMap = mapOf(typeOf<AuthRegisterScreenNavigateModel>() to CustomNavType(AuthRegisterScreenNavigateModel::class.java, AuthRegisterScreenNavigateModel.serializer()))

fun SavedStateHandle.toAuthRegisterScreenRoute(): AuthRegisterScreenRoute {
    return this.toRoute<AuthRegisterScreenRoute>(
        typeMap = authRegisterScreenRouteTypeMap,
    )
}

val fakeAuthRegisterScreenNavigateModel: AuthRegisterScreenNavigateModel = AuthRegisterScreenNavigateModel(
    registerSocialInfoRequest = RegisterSocialInfoRequest(
        accountId = "account-id",
        accessToken = "access-token",
        email = "email",
        platform = 0,
    ),
)

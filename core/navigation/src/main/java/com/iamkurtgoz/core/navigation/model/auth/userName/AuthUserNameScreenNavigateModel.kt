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
package com.iamkurtgoz.core.navigation.model.auth.userName

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.AuthUserNameScreenRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.model.request.RegisterSocialInfoRequest
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class AuthUserNameScreenNavigateModel(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val registerSocialInfoRequest: RegisterSocialInfoRequest?,
) : Parcelable

val authUserNameScreenRoute = mapOf(typeOf<AuthUserNameScreenNavigateModel>() to CustomNavType(AuthUserNameScreenNavigateModel::class.java, AuthUserNameScreenNavigateModel.serializer()))

fun SavedStateHandle.toAuthUserNameScreenRoute(): AuthUserNameScreenRoute {
    return this.toRoute<AuthUserNameScreenRoute>(
        typeMap = authUserNameScreenRoute,
    )
}

val fakeAuthUserNameScreenNavigateModel: AuthUserNameScreenNavigateModel = AuthUserNameScreenNavigateModel(
    phoneNumber = "555555555",
    firstName = "Test",
    lastName = "Test",
    email = "test@gmail.com",
    password = "test.12345",
    registerSocialInfoRequest = null,
)

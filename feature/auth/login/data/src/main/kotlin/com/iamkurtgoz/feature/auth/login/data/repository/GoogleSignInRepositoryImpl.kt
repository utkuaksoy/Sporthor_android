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
package com.iamkurtgoz.feature.auth.login.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.auth0.android.jwt.JWT
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.iamkurtgoz.core.common.exception.NullableException
import com.iamkurtgoz.core.common.extensions.toBaseError
import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.feature.auth.login.domain.model.GoogleLoginResult
import com.iamkurtgoz.feature.auth.login.domain.repository.GoogleSignInRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GoogleSignInRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
) : GoogleSignInRepository {

    override suspend fun login(): GoogleLoginResult {
        val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = appBuildConfigStatePack.googleServiceClientId,
        ).build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            if (result.credential is CustomCredential && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = googleIdTokenCredential.idToken
                val accountId = getAccountId(idToken)
                val email = getEmail(idToken)
                return GoogleLoginResult(
                    idToken = idToken,
                    accountId = accountId,
                    email = email,
                )
            } else {
                throw BaseError()
            }
        } catch (e: GetCredentialException) {
            Timber.d(e.message)
            throw e.toBaseError
        } catch (e: NullableException) {
            Timber.d(e.message)
            throw e.toBaseError
        } catch (e: BaseError) {
            Timber.d(e.message)
            throw e
        }
    }

    private fun getAccountId(idToken: String): String? {
        try {
            val jwt = JWT(idToken)
            return jwt.getClaim("sub").asString()
        } catch (_: Exception) {
            return null
        }
    }

    private fun getEmail(idToken: String): String? {
        try {
            val jwt = JWT(idToken)
            return jwt.getClaim("email").asString()
        } catch (_: Exception) {
            return null
        }
    }
}

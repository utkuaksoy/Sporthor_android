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
package com.iamkurtgoz.core.datastore.serializer

import androidx.datastore.core.Serializer
import com.iamkurtgoz.domain.crypto.CryptoHelper
import com.iamkurtgoz.domain.dataStore.UserPreferencesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserPreferencesSerializer @Inject constructor(
    private val cryptoHelper: CryptoHelper,
) : Serializer<UserPreferencesState> {

    override val defaultValue: UserPreferencesState
        get() = UserPreferencesState()

    override suspend fun readFrom(input: InputStream): UserPreferencesState {
        val encryptedString = withContext(Dispatchers.IO) {
            input.use { it.readBytes().toString(Charsets.UTF_8) }
        }
        val jsonString: String = try {
            when (val decryptedData = cryptoHelper.decryptData(encryptedString)) {
                is String -> decryptedData
                else -> decryptedData.toString()
            }
        } catch (_: Exception) {
            "{}"
        }

        Timber.d("UserPreferencesSerializer")
        Timber.d(jsonString)
        return Json.decodeFromString(jsonString)
    }

    override suspend fun writeTo(t: UserPreferencesState, output: OutputStream) {
        val json = Json.encodeToString(t)

        val encryptedString = cryptoHelper.encryptData(json)

        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedString.toByteArray(Charsets.UTF_8))
            }
        }
    }
}

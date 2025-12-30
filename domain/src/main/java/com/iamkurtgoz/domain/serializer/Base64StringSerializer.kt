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
package com.iamkurtgoz.domain.serializer

import android.util.Base64
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object Base64StringSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Base64String", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        val encoded = value?.let {
            Base64.encodeToString(it.toByteArray(Charsets.UTF_8), Base64.NO_WRAP or Base64.URL_SAFE)
        }
        encoder.encodeString(encoded ?: "")
    }

    override fun deserialize(decoder: Decoder): String? {
        val encoded = decoder.decodeString()
        return try {
            val decodedBytes = Base64.decode(encoded, Base64.NO_WRAP or Base64.URL_SAFE)
            String(decodedBytes, Charsets.UTF_8)
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}

object SafeBase64StringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Base64String", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        val encoded = value.let {
            Base64.encodeToString(it.toByteArray(Charsets.UTF_8), Base64.NO_WRAP or Base64.URL_SAFE)
        }
        encoder.encodeString(encoded ?: "")
    }

    override fun deserialize(decoder: Decoder): String {
        val encoded = decoder.decodeString()
        val decodedBytes = Base64.decode(encoded, Base64.NO_WRAP or Base64.URL_SAFE)
        return String(decodedBytes, Charsets.UTF_8)
    }
}

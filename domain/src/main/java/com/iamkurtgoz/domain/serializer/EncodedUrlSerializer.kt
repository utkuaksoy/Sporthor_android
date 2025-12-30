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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object EncodedUrlSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("EncodedUrl", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        val encoded = value?.let {
            URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
        }
        encoder.encodeString(encoded ?: "")
    }

    override fun deserialize(decoder: Decoder): String? {
        val decoded = decoder.decodeString()
        return URLDecoder.decode(decoded, StandardCharsets.UTF_8.toString())
    }
}

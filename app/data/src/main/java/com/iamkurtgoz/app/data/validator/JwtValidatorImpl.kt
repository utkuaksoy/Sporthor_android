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
package com.iamkurtgoz.app.data.validator

import android.util.Base64
import com.iamkurtgoz.app.domain.validator.JwtValidator
import org.json.JSONObject
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.floor

@Singleton
class JwtValidatorImpl @Inject constructor() : JwtValidator {

    companion object {
        private const val PAYLOAD_INDEX = 1
        private const val EXPIRE_DATE_KEY_NAME = "exp"
        private const val IAT_DATE_KEY_NAME = "iat"
        private const val SECONDS_TO_MILLISECONDS = 1000L
        private const val EXPIRE_DATE_MULTIPLIER = 1000L
        private const val IAT_DATE_MULTIPLIER = 1000L
    }

    override fun isExpired(token: String?, leeway: Long): Boolean {
        return try {
            require(token != null) {
                "Token cannot be null"
            }
            require(leeway > 0) {
                "The leeway must be a positive value."
            }
            val exp = getExpireDate(token)
            val iat = getIatDate(token)
            val todayTime = (floor((Date().time / SECONDS_TO_MILLISECONDS).toDouble()) * SECONDS_TO_MILLISECONDS).toLong()
            val futureToday = Date(todayTime + leeway * SECONDS_TO_MILLISECONDS)
            val pastToday = Date(todayTime - leeway * SECONDS_TO_MILLISECONDS)
            val expValid = !pastToday.after(exp)
            val iatValid = !futureToday.before(iat)
            !expValid || !iatValid
        } catch (e: IllegalArgumentException) {
            println(e.message)
            true
        }
    }

    override fun getExpireDate(token: String?): Date {
        val payload = getJson(decoded(token)[PAYLOAD_INDEX])
        val jsonObject = JSONObject(payload)
        val expireDateLong = jsonObject.getLong(EXPIRE_DATE_KEY_NAME) * EXPIRE_DATE_MULTIPLIER
        return Date(expireDateLong)
    }

    override fun getIatDate(token: String?): Date {
        val payload = getJson(decoded(token)[PAYLOAD_INDEX])
        val jsonObject = JSONObject(payload)
        if (jsonObject.has(IAT_DATE_KEY_NAME)) {
            val iatDateLong = jsonObject.getLong(IAT_DATE_KEY_NAME) * IAT_DATE_MULTIPLIER
            return Date(iatDateLong)
        }
        return Date()
    }

    private fun decoded(encoded: String?): Array<String> {
        return encoded?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray() ?: arrayOf()
    }

    private fun getJson(strEncoded: String?): String {
        val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }
}

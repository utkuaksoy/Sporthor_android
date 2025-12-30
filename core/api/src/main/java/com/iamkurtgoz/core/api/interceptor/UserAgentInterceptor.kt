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
package com.iamkurtgoz.core.api.interceptor

import com.iamkurtgoz.core.api.helper.NetworkUtil
import com.iamkurtgoz.core.common.qualifiers.AppVersionCodeQualifier
import com.iamkurtgoz.core.common.qualifiers.AppVersionNameQualifier
import com.iamkurtgoz.core.common.qualifiers.DeviceId
import com.iamkurtgoz.core.common.qualifiers.QualifierUserAgentInterceptor
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@QualifierUserAgentInterceptor
@Singleton
class UserAgentInterceptor @Inject constructor(
    @DeviceId val deviceId: String,
    @AppVersionNameQualifier private val appVersionName: String,
    @AppVersionCodeQualifier private val appVersionCode: Int,
    private val notificationSettingsManager: INotificationSettingsManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val firebaseNotificationToken = runBlocking { notificationSettingsManager.getRegisterFcmToken() }
        val contentType = chain.request().headers.find { it.first.lowercase(Locale.getDefault()) == "content-type" }?.second?.lowercase(Locale.getDefault()) ?: ""
        val isMultipartFormDataRequest = (contentType.contains("multipart/form-data"))

        val requestBuilder = chain.request().newBuilder()
            .header("Device-Token", deviceId)
            .header("Accept", "application/json")
            .header("Operation-System", "android")
            .header("Accept-Language", language)
            .header("Timezone", TimeZone.getDefault().id)
            .header("User-Agent", userAgent ?: "-")
            .header("App-Version-Name", appVersionName)
            .header("App-Version-Code", appVersionCode.toString())
            .header("Notification-Token", firebaseNotificationToken ?: "-")

        if (!isMultipartFormDataRequest) {
            requestBuilder.header("Content-Type", "application/json")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private val language: String
        get() = Locale.getDefault().language

    private val userAgent: String?
        get() = NetworkUtil.userAgent
}

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
package com.iamkurtgoz.core.firebase.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.iamkurtgoz.core.common.exception.NoNotificationPermissionException
import com.iamkurtgoz.domain.notification.INotificationSettingsManager
import kotlinx.coroutines.CompletableDeferred
import timber.log.Timber
import javax.inject.Inject

internal class FirebaseNotificationSettingsManager @Inject constructor() : INotificationSettingsManager {
    override suspend fun checkNotificationPermissionAndRegisterFcmToken(context: Context): String? {
        val response = CompletableDeferred<String?>()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            val token = getRegisterFcmToken()
            response.complete(token)
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val token = getRegisterFcmToken()
            response.complete(token)
        } else {
            response.completeExceptionally(NoNotificationPermissionException())
        }
        return response.await()
    }

    override suspend fun getRegisterFcmToken(): String? {
        val response = CompletableDeferred<String?>()
        val instance = FirebaseMessaging.getInstance()
        instance.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d("Fetching FCM registration token failed: " + task.exception)
                response.complete(null)
            } else {
                val token = task.result
                Timber.d("Firebase Token: $token")
                response.complete(token)
            }
        }
        return response.await()
    }
}

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
package com.sporthor.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.dataStore.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("onMessageReceived")

        val title = remoteMessage.notification?.title ?: "Yeni Bildirim"
        val messageBody = remoteMessage.notification?.body ?: "Mesaj içeriği yok"

        Timber.d("Title: $title")
        Timber.d("Body: $messageBody")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = this.getString(com.sporthor.`as`.R.string.defaultNotificationChannelId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Genel Bildirimler",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(resourcesR.drawable.img_notification_default)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) = runBlocking {
        appPreferences.setFirebaseToken(firebaseToken = token)
        return@runBlocking
    }
}

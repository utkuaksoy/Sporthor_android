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
package com.iamkurtgoz.core.firebase.repository

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.firebase.R
import com.iamkurtgoz.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

internal class RemoteConfigRepositoryImpl @Inject constructor() : RemoteConfigRepository {

    companion object {
        private const val REMOTE_CONFIG_MIN_FETCH_INTERVAL = 900L
    }

    override suspend fun getConfig(appRemoteConfigStatePack: AppRemoteConfigStatePack): Boolean {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = REMOTE_CONFIG_MIN_FETCH_INTERVAL
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val response = CompletableDeferred<Boolean>()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                // Save values from Remote Config
                appRemoteConfigStatePack.defaultAppOpenAdShowMinOpenCount = remoteConfig.getLong(AppRemoteConfigStatePack.DEFAULT_APP_OPEN_AD_SHOW_MIN_OPEN_COUNT).toInt()
                appRemoteConfigStatePack.defaultInterstitialAdShowMinOpenCount = remoteConfig.getLong(AppRemoteConfigStatePack.DEFAULT_INTERSTITIAL_AD_SHOW_MIN_OPEN_COUNT).toInt()
                appRemoteConfigStatePack.isWatchRewardCreditAdButtonActive = remoteConfig.getBoolean(AppRemoteConfigStatePack.IS_WATCH_REWARD_CREDIT_AD_BUTTON_ACTIVE)
                appRemoteConfigStatePack.isEnableLoginWithEmailButton = remoteConfig.getBoolean(AppRemoteConfigStatePack.IS_ENABLE_LOGIN_WITH_EMAIL_BUTTON)
                appRemoteConfigStatePack.isRepairMode = remoteConfig.getBoolean(AppRemoteConfigStatePack.IS_REPAIR_MODE)
                appRemoteConfigStatePack.isStartAdActive = remoteConfig.getBoolean(AppRemoteConfigStatePack.IS_START_AD_ACTIVE)
                appRemoteConfigStatePack.minVersionAndroid = remoteConfig.getLong(AppRemoteConfigStatePack.MIN_VERSION_ANDROID)
            }
            response.complete(it.isSuccessful)
        }.addOnFailureListener {
            response.complete(false)
        }
        return response.await()
    }
}

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
package com.iamkurtgoz.core.common.state

import com.iamkurtgoz.core.common.contract.AppDefaults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRemoteConfigStatePack @Inject constructor() {
    companion object {
        const val DEFAULT_APP_OPEN_AD_SHOW_MIN_OPEN_COUNT = "default_app_open_ad_show_min_open_count"
        const val DEFAULT_INTERSTITIAL_AD_SHOW_MIN_OPEN_COUNT = "default_interstitial_ad_show_min_open_count"
        const val IS_WATCH_REWARD_CREDIT_AD_BUTTON_ACTIVE = "is_watch_reward_credit_ad_button_active"
        const val IS_ENABLE_LOGIN_WITH_EMAIL_BUTTON = "isEnableLoginWithEmailButton"
        const val IS_REPAIR_MODE = "isRepairMode"
        const val IS_START_AD_ACTIVE = "isStartAdActive"
        const val MIN_VERSION_ANDROID = "minVersionAndroid"
    }

    var defaultAppOpenAdShowMinOpenCount: Int = AppDefaults.FIVE
    var defaultInterstitialAdShowMinOpenCount: Int = AppDefaults.FIVE
    var isWatchRewardCreditAdButtonActive: Boolean = false
    var isEnableLoginWithEmailButton: Boolean = false
    var isRepairMode: Boolean = false
    var isStartAdActive: Boolean = false
    var minVersionAndroid: Long = AppDefaults.APP_MIN_ANDROID_VERSION
}

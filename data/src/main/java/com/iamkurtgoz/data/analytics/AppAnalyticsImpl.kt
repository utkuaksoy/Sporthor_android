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
package com.iamkurtgoz.data.analytics

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.iamkurtgoz.domain.analytics.AppAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAnalyticsImpl @Inject constructor() : AppAnalytics {
    companion object {
        // EVENT
        private const val EVENT_PURCHASE_ERROR = "event_purchase_error"
        private const val EVENT_PURCHASE_SUCCESS = "event_purchase_success"

        // PARAM
        private const val PARAM_PURCHASE_ERROR_CODE = "param_purchase_error_code"
        private const val PARAM_PURCHASE_ERROR_MESSAGE = "param_purchase_error_message"
        private const val PARAM_PURCHASE_ERROR_DETAIL_MESSAGE = "param_purchase_error_detail_message"
        private const val PARAM_PURCHASE_SUCCESS_IDENTIFIER = "param_purchase_success_identifier"
    }

    override fun logPurchaseError(errorCode: Int, errorMessage: String, errorDetailMessage: String) {
        val parameters = Bundle().apply {
            putInt(PARAM_PURCHASE_ERROR_CODE, errorCode)
            putString(PARAM_PURCHASE_ERROR_MESSAGE, errorMessage)
            putString(PARAM_PURCHASE_ERROR_DETAIL_MESSAGE, errorDetailMessage)
        }
        Firebase.analytics.logEvent(EVENT_PURCHASE_ERROR, parameters)
    }

    override fun logPurchaseSuccess(identifier: String) {
        val parameters = Bundle().apply {
            putString(PARAM_PURCHASE_SUCCESS_IDENTIFIER, identifier)
        }
        Firebase.analytics.logEvent(EVENT_PURCHASE_SUCCESS, parameters)
    }
}

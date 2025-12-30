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

import com.iamkurtgoz.core.common.common.enums.ConnectivityStatus
import com.iamkurtgoz.core.common.exception.NoConnectivityException
import com.iamkurtgoz.core.common.qualifiers.QualifierConnectivityInterceptor
import com.iamkurtgoz.domain.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@QualifierConnectivityInterceptor
@Singleton
class ConnectivityInterceptor @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val connectivityStatus = runBlocking { connectivityObserver.isConnected.firstOrNull() }
        return if (connectivityStatus == ConnectivityStatus.Disconnected) {
            chain.call().cancel()
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }
}

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
package com.iamkurtgoz.core.connectivity

import android.app.Application
import android.app.Service
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import com.iamkurtgoz.core.common.common.enums.ConnectivityStatus
import com.iamkurtgoz.domain.connectivity.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserver @Inject constructor(application: Application) : ConnectivityObserver {
    private val connectivityManager = application.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnected: Flow<ConnectivityStatus>
        get() = callbackFlow {
            val callback = object : NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities,
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED,
                    )
                    trySend(if (connected) ConnectivityStatus.Connected else ConnectivityStatus.Disconnected)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(ConnectivityStatus.Disconnected)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectivityStatus.Disconnected)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(ConnectivityStatus.Connected)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
}

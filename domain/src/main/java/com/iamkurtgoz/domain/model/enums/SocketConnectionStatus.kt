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
package com.iamkurtgoz.domain.model.enums

enum class SocketConnectionStatus(val value: String) {
    IDLE(value = "idle"),
    CONNECTED(value = "connected"),
    DISCONNECTED(value = "disconnected"),
    CONNECTING(value = "connecting"),
    ;

    companion object {
        fun fromValue(value: String): SocketConnectionStatus? {
            return when (value.lowercase()) {
                IDLE.value.lowercase() -> IDLE
                CONNECTED.value.lowercase() -> CONNECTED
                DISCONNECTED.value.lowercase() -> DISCONNECTED
                CONNECTING.value.lowercase() -> CONNECTING
                else -> null
            }
        }
    }
}

fun String?.toSocketConnectionStatus(): SocketConnectionStatus? {
    return this?.let { SocketConnectionStatus.fromValue(it) }
}

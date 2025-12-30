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
package com.iamkurtgoz.domain.eventbus.impl

import com.iamkurtgoz.domain.core.CoreEventBus

object AddEventTaskEventBus : CoreEventBus<AddEventTaskEventBus.Event>() {
    sealed class Event {
        data class SelectedAddressChanged(
            val title: String,
            val address: String,
            val city: String?,
            val country: String?,
            val latitude: Double?,
            val longitude: Double?
        ) : Event()
    }
}

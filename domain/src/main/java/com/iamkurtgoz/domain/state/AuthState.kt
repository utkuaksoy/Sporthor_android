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
package com.iamkurtgoz.domain.state

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
object AuthState {

    private val _userEffect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val userEffect: SharedFlow<Effect> = _userEffect.asSharedFlow()

    suspend fun setUserEffect(effect: Effect) {
        _userEffect.emit(effect)
    }

    suspend fun clearUserEffect() {
        setUserEffect(Effect.None)
    }

    sealed class Effect {
        data object None : Effect()
        data object RouteToLoginWithClearBackStack : Effect()
    }
}

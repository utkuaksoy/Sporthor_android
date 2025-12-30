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
package com.iamkurtgoz.fake.core

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FakeCoreViewModel : CoreViewModel<FakeViewState, FakeSideEffect, FakeEvent>(
    initialState = FakeViewState(
        isLoading = false,
        alertError = null,
    ),
) {
    override fun setEvent(event: FakeEvent) {
        when (event) {
            is FakeEvent.Initialize -> handleOneTimeEvent(event, ::initialize)
            is FakeEvent.Increment -> increment()
            is FakeEvent.Decrease -> decrease()
            is FakeEvent.ShowErrorAlert -> showErrorAlert(event.errorMessage)
        }
    }

    private fun initialize() = Unit

    private fun increment() = viewModelScope.launch {
        flow<RestResult<Unit>> {
            delay(AppDefaults.DELAY_ONE_SECOND)
            emit(RestResult.Success(Unit))
        }.requester.callWithSuccess {
            updateState { currentState ->
                currentState.copy(count = currentState.count + AppDefaults.ONE)
            }
            setSideEffect(FakeSideEffect.ShowToast)
        }
    }

    private fun decrease() = viewModelScope.launch {
        flow<RestResult<Unit>> {
            delay(AppDefaults.DELAY_ONE_SECOND)
            emit(RestResult.Success(Unit))
        }.requesterWithoutWait.callWithSuccess {
            updateState { currentState ->
                currentState.copy(count = currentState.count - AppDefaults.ONE)
            }
            setSideEffect(FakeSideEffect.ShowToast)
        }
    }

    private fun showErrorAlert(errorMessage: String?) {
        val error = Throwable(errorMessage)
        updateState { currentState ->
            currentState.copy(alertError = error.toAlertDialog)
        }
    }
}

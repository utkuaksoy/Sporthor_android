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
package com.iamkurtgoz.domain.core.useCase

import com.iamkurtgoz.core.common.exception.ErrorBodyException
import com.iamkurtgoz.core.common.exception.ErrorRequestNotSuccessException
import com.iamkurtgoz.core.common.exception.NoConnectivityException
import com.iamkurtgoz.core.common.exception.NullableException
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import java.io.IOException

open class CoreUseCase(
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher,
) {
    fun <T : Any> prepare(
        block: suspend () -> RestResult<T>,
    ): Flow<RestResult<T>> = flow {
        try {
            this.emit(block.invoke())
        } catch (e: NoConnectivityException) {
            emit(RestResult.Error(e))
        } catch (e: ErrorBodyException) {
            emit(RestResult.Error(e))
        } catch (e: ErrorRequestNotSuccessException) {
            emit(RestResult.Error(e))
        } catch (e: NullableException) {
            emit(RestResult.Error(e))
        } catch (e: IOException) {
            emit(RestResult.Error(e))
        }
    }.onStart {
        emit(RestResult.Loading)
    }.catch { throwable ->
        emit(RestResult.Error(throwable))
    }.flowOn(coroutineDispatcher)

    fun <T : Any> prepareNullable(
        block: suspend () -> RestResult<T?>,
    ): Flow<RestResult<T?>> = flow {
        try {
            this.emit(block.invoke())
        } catch (e: NoConnectivityException) {
            emit(RestResult.Error(e))
        } catch (e: ErrorBodyException) {
            emit(RestResult.Error(e))
        } catch (e: ErrorRequestNotSuccessException) {
            emit(RestResult.Error(e))
        } catch (e: NullableException) {
            emit(RestResult.Error(e))
        } catch (e: IOException) {
            emit(RestResult.Error(e))
        }
    }.onStart {
        emit(RestResult.Loading)
    }.catch { throwable ->
        emit(RestResult.Error(throwable))
    }.flowOn(coroutineDispatcher)
}

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
package com.iamkurtgoz.data.core

import com.iamkurtgoz.core.common.exception.NullableException
import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.network.model.BaseResponse

internal open class CoreRepository {
    // Base Response
    suspend inline fun <reified T : Any> mapToRestResult(
        crossinline call: suspend () -> BaseResponse<T>,
    ): RestResult<T> {
        return call.invoke().asRestResultRequest
    }

    inline val <reified T> BaseResponse<T>.asRestResultRequest: RestResult<T>
        get() {
            val response = this.data
            val responseError = this.error
            val totalCount = 0 // this.totalCount
            val filterCount = 0 // this.filterCount

            return if (!this.isSuccess && responseError != null) {
                RestResult.Error(responseError)
            } else if (response == null) {
                val nullableException = NullableException()
                val baseError = BaseError(nullableException)
                RestResult.Error(baseError)
            } else {
                RestResult.Success(
                    result = response,
                    totalCount = totalCount,
                    filterCount = filterCount,
                )
            }
        }

    // Any Response
    suspend inline fun <reified T : Any> mapToRestResultAny(
        crossinline call: suspend () -> BaseResponse<T?>?,
    ): RestResult<T> {
        return call.invoke().asRestResultRequestAny
    }

    inline val <reified T> BaseResponse<T?>?.asRestResultRequestAny: RestResult<T>
        get() {
            val response = this?.data
            val responseError = this?.error

            return if (this?.isSuccess == false && responseError != null) {
                RestResult.Error(responseError)
            } else if (response == null) {
                val nullableException = NullableException()
                val baseError = BaseError(nullableException)
                RestResult.Error(baseError)
            } else {
                RestResult.Success(
                    result = response,
                )
            }
        }
}

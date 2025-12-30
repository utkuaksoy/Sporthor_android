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
package com.iamkurtgoz.core.api.core

import com.iamkurtgoz.core.common.exception.ErrorRequestNotSuccessException
import com.iamkurtgoz.core.common.exception.NullableException
import com.iamkurtgoz.core.common.model.BaseError
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.model.BaseResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response

internal open class CoreRemoteDataSource(
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    private val json: Json,
) {
    // BaseResponse Retrofit Request
    suspend inline fun <reified T : Any> requestRetrofit(
        crossinline call: suspend () -> Response<BaseResponse<T>>,
    ): BaseResponse<T> {
        return call.invoke().asRestResultRetrofitRequest
    }

    inline val <reified T> Response<BaseResponse<T>>.asRestResultRetrofitRequest: BaseResponse<T>
        get() {
            if (!isSuccessful) {
                throw getBaseError()
            }

            return body() ?: throw getBaseError()
        }

    // Any Response Retrofit Request
    suspend inline fun <reified T : Any> requestRetrofitAny(
        crossinline call: suspend () -> Response<T>,
    ): T {
        return call.invoke().asRestResultRetrofitAny
    }

    inline val <reified T> Response<T>.asRestResultRetrofitAny: T
        get() {
            if (!isSuccessful) {
                throw ErrorRequestNotSuccessException()
            }
            return body() ?: throw NullableException()
        }

    @Suppress("SwallowedException")
    private fun Response<*>.getBaseError(): BaseError {
        val errorContent = errorBody()?.charStream()?.readText()

        if (appBuildConfigStatePack.isDebug) {
            println("Error Body is null: ${errorContent == null}")
            println("Code: ${this.code()}")
        }

        return try {
            if (errorContent != null) {
                json.decodeFromString<BaseError>(errorContent)
            } else {
                BaseError(isUserFriendly = true, errorMessage = "", code = 500)
            }
        } catch (e: SerializationException) {
            BaseError(isUserFriendly = true, errorMessage = e.message ?: e.localizedMessage ?: "", code = 500)
        } catch (e: IllegalArgumentException) {
            BaseError(isUserFriendly = true, errorMessage = e.message ?: e.localizedMessage ?: "", code = 500)
        }
    }
}

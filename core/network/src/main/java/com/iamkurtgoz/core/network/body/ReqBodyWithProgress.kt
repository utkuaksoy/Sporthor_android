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
package com.iamkurtgoz.core.network.body

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.IOException
import okio.Sink
import okio.buffer

class ReqBodyWithProgress(
    private val multipartBody: MultipartBody,
    var onUploadProgress: ((progress: Int) -> Unit)? = null,
) : RequestBody() {
    companion object {
        private const val PROGRESS_MAX: Float = 100f
    }

    private var mCountingSink: CountingSink? = null

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return multipartBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return multipartBody.contentType()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        mCountingSink = CountingSink(sink)
        val bufferedSink: BufferedSink = mCountingSink!!.buffer()
        multipartBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private inner class CountingSink(delegate: Sink?) : ForwardingSink(delegate!!) {
        private var bytesWritten: Long = 0

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            bytesWritten += byteCount
            onUploadProgress?.invoke((PROGRESS_MAX * bytesWritten / contentLength()).toInt())
            super.write(source, byteCount)
            delegate.flush()
        }
    }
}

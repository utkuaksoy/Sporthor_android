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
package com.iamkurtgoz.core.api.dataSource.upload

import com.iamkurtgoz.core.api.core.CoreRemoteDataSource
import com.iamkurtgoz.core.api.service.upload.UploadService
import com.iamkurtgoz.core.common.extensions.getMimeType
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.network.body.ReqBodyWithProgress
import com.iamkurtgoz.core.network.model.BaseResponse
import com.iamkurtgoz.data.dataSource.UploadRemoteDataSource
import com.iamkurtgoz.data.model.MediaUploadResponseModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject

internal class UploadRemoteDataSourceImpl @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    json: Json,
    private val uploadService: UploadService,
) : UploadRemoteDataSource, CoreRemoteDataSource(appBuildConfigStatePack, json) {

    companion object {
        private const val UPLOAD_IMAGES_FORM_DATA_PART_NAME = "imageFiles"
        private const val UPLOAD_VIDEO_FORM_DATA_PART_NAME = "videoFile"
        private const val UPLOAD_FILE_FORM_DATA_PART_NAME = "videoFile"
    }

    override suspend fun imageUpload(files: List<File>, onUploadProgress: (progress: Int) -> Unit): BaseResponse<List<MediaUploadResponseModel>> {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        files.forEach {
            val filename = "${UUID.randomUUID()}.${it.extension}"
            val mediaType = it.getMimeType()?.toMediaTypeOrNull()
            val body = it.asRequestBody(mediaType)
            bodyBuilder.addFormDataPart(
                name = UPLOAD_IMAGES_FORM_DATA_PART_NAME,
                filename = filename,
                body = body,
            )
        }
        val multipartBody = bodyBuilder.build()
        val requestBodyWithProgress = ReqBodyWithProgress(
            multipartBody = multipartBody,
            onUploadProgress = onUploadProgress,
        )

        return requestRetrofit {
            uploadService.imageUpload(requestBodyWithProgress)
        }
    }

    override suspend fun videoUpload(file: File, onUploadProgress: (progress: Int) -> Unit): BaseResponse<MediaUploadResponseModel> {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val filename = "${UUID.randomUUID()}.${file.extension}"
        val mediaType = file.getMimeType()?.toMediaTypeOrNull()
        val body = file.asRequestBody(mediaType)
        bodyBuilder.addFormDataPart(
            name = UPLOAD_VIDEO_FORM_DATA_PART_NAME,
            filename = filename,
            body = body,
        )
        val multipartBody = bodyBuilder.build()
        val requestBodyWithProgress = ReqBodyWithProgress(
            multipartBody = multipartBody,
            onUploadProgress = onUploadProgress,
        )

        return requestRetrofit {
            uploadService.videoUpload(requestBodyWithProgress)
        }
    }

    override suspend fun fileUpload(file: File, onUploadProgress: (Int) -> Unit): BaseResponse<MediaUploadResponseModel> {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val filename = "${UUID.randomUUID()}.${file.extension}"
        val mediaType = file.getMimeType()?.toMediaTypeOrNull()
        val body = file.asRequestBody(mediaType)
        bodyBuilder.addFormDataPart(
            name = UPLOAD_FILE_FORM_DATA_PART_NAME,
            filename = filename,
            body = body,
        )
        val multipartBody = bodyBuilder.build()
        val requestBodyWithProgress = ReqBodyWithProgress(
            multipartBody = multipartBody,
            onUploadProgress = onUploadProgress,
        )

        return requestRetrofit {
            uploadService.fileUpload(requestBodyWithProgress)
        }
    }
}

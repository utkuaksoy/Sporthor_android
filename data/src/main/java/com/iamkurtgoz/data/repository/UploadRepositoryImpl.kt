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
package com.iamkurtgoz.data.repository

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.model.mapOnSuccess
import com.iamkurtgoz.data.core.CoreRepository
import com.iamkurtgoz.data.dataSource.UploadRemoteDataSource
import com.iamkurtgoz.domain.repository.UploadRepository
import java.io.File
import javax.inject.Inject

internal class UploadRepositoryImpl @Inject constructor(
    private val uploadRemoteDataSource: UploadRemoteDataSource,
) : UploadRepository, CoreRepository() {

    override suspend fun imageUpload(files: List<File>, onUploadProgress: (progress: Int) -> Unit): RestResult<List<String>> = mapToRestResult {
        uploadRemoteDataSource.imageUpload(
            files = files,
            onUploadProgress = onUploadProgress,
        )
    }.mapOnSuccess {
        it.mapNotNull { item -> item.filePath }
    }

    override suspend fun videoUpload(files: List<File>, onUploadProgress: (progress: Int) -> Unit): RestResult<List<String>> {
        val list: MutableList<String?> = mutableListOf()
        files.forEach {
            val response = uploadRemoteDataSource.videoUpload(
                file = it,
                onUploadProgress = onUploadProgress,
            )
            list.add(response.data?.filePath)
        }
        return RestResult.Success(list.filterNotNull())
    }

    override suspend fun fileUpload(files: List<File>, onUploadProgress: (Int) -> Unit): RestResult<List<String>> {
        val list: MutableList<String?> = mutableListOf()
        files.forEach {
            val response = uploadRemoteDataSource.fileUpload(
                file = it,
                onUploadProgress = onUploadProgress,
            )
            list.add(response.data?.filePath)
        }
        return RestResult.Success(list.filterNotNull())
    }
}

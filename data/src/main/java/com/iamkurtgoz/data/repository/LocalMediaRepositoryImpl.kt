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

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.data.source.LocalMediaPagingSource
import com.iamkurtgoz.domain.model.response.LocalMediaDomainModel
import com.iamkurtgoz.domain.repository.LocalMediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LocalMediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val application: Application,
) : LocalMediaRepository {
    override fun getLocalMediaFileList(): Pager<Int, LocalMediaDomainModel> {
        return Pager(
            config = PagingConfig(
                pageSize = AppDefaults.LOCAL_MEDIA_LIST_SIZE,
                initialLoadSize = AppDefaults.LOCAL_MEDIA_LIST_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                LocalMediaPagingSource(application)
            },
        )
    }
}

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
package com.iamkurtgoz.data.source

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.domain.model.response.LocalMediaDomainModel
import timber.log.Timber

internal class LocalMediaPagingSource(
    private val context: Context,
    private val mediaType: MediaType = MediaType.COMMON,
) : PagingSource<Int, LocalMediaDomainModel>() {
    private val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Video.Media.DURATION,
        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATA,
    )

    @Suppress("NestedBlockDepth", "TooGenericExceptionCaught", "MagicNumber")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocalMediaDomainModel> {
        try {
            val page = params.key ?: AppDefaults.ZERO
            val limit = params.loadSize
            val offset = page * limit

            val items = context.fetch(
                limit = limit,
                offset = offset,
            )

            val prevKey = if (page > AppDefaults.ZERO) page.minus(AppDefaults.ONE) else null
            val nextKey = if (items.isNotEmpty()) page.plus(AppDefaults.ONE) else null

            Timber.d("page: $page")
            Timber.d("limit: $limit")
            Timber.d("offset: $offset")
            Timber.d("Loaded ${items.size} media items")
            Timber.d("Next key: $nextKey")

            return LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            Timber.e(e, "Error loading media items")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocalMediaDomainModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(AppDefaults.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(AppDefaults.ONE)
        }
    }

    @Suppress("MagicNumber")
    private fun Context.fetch(limit: Int, offset: Int): List<LocalMediaDomainModel> {
        val pictures = ArrayList<LocalMediaDomainModel>()
        val cursor = createCursor(limit, offset)
        cursor?.use {
            val indexId = it.getColumnIndex(projection[0])
            val indexFilename = it.getColumnIndex(projection[1])
            val indexDate = it.getColumnIndex(projection[2])
            val indexMediaType = it.getColumnIndex(projection[3])
            val indexMimeType = it.getColumnIndex(projection[4])
            val indexSize = it.getColumnIndex(projection[5])
            val indexDuration = it.getColumnIndex(projection[6])
            val indexDirectory = it.getColumnIndex(projection[7])
            val indexFilepath = it.getColumnIndex(projection[8])

            while (it.moveToNext()) {
                val id = it.getLong(indexId)
                val mediaTypeValue = it.getInt(indexMediaType)
                val contentUri = if (mediaTypeValue == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                } else {
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                }

                pictures.add(
                    LocalMediaDomainModel(
                        id = id,
                        mediaType = mediaTypeValue,
                        name = it.getString(indexFilename),
                        dateAdded = it.getLong(indexDate),
                        uri = contentUri,
                        mimeType = it.getString(indexMimeType),
                        size = it.getLong(indexSize),
                        duration = it.getLong(indexDuration),
                        directory = it.getString(indexDirectory),
                        filepath = it.getString(indexFilepath),
                    ),
                )
            }
        }
        cursor?.close()
        return pictures
    }

    private fun Context.createCursor(limit: Int, offset: Int): Cursor? {
        val selection = createSelection(mediaType)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_OFFSET to offset,
                ContentResolver.QUERY_ARG_LIMIT to limit,
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_ADDED),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
            ).apply {
                selection.let {
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, it.selection)
                    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, it.arguments.toTypedArray())
                }
            }
            contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                bundle,
                null,
            )
        } else {
            contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection.selection,
                selection.arguments.toTypedArray(),
                "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset",
                null,
            )
        }
    }

    private fun createSelection(mediaType: MediaType): Selection {
        val mediaTypeColumn = MediaStore.Files.FileColumns.MEDIA_TYPE
        val image = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
        val video = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

        return when (mediaType) {
            MediaType.COMMON -> Selection(
                selection = "$mediaTypeColumn=? OR $mediaTypeColumn=?",
                arguments = listOf(image.toString(), video.toString()),
            )
            MediaType.IMAGE -> Selection(
                selection = "$mediaTypeColumn=?",
                arguments = listOf(image.toString()),
            )
            MediaType.VIDEO -> Selection(
                selection = "$mediaTypeColumn=?",
                arguments = listOf(video.toString()),
            )
        }
    }

    private data class Selection(val selection: String, val arguments: List<String>)

    enum class MediaType {
        COMMON,
        IMAGE,
        VIDEO,
    }
}

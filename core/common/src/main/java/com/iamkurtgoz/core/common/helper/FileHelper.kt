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
package com.iamkurtgoz.core.common.helper

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileHelper {
    private const val DEFAULT_BYTE_ARRAY_SIZE = 1024

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var path: String? = ""
        try {
            path = processUri(context, uri)
        } catch (_: Exception) { }
        if (TextUtils.isEmpty(path)) {
            path = copyFile(context, uri)
        }
        return path
    }

    private fun processUri(context: Context, uri: Uri): String? {
        var path: String? = ""
        if (DocumentsContract.isDocumentUri(context, uri)) {
            when {
                isExternalStorageDocument(uri) -> {
                    path = processUriIsExternalStorageDocument(uri)
                }
                isDownloadsDocument(uri) -> {
                    path = processUriIsDownloadsDocument(context, uri)
                }
                isMediaDocument(uri) -> {
                    path = processUriIsMediaDocument(context, uri)
                }
                "content".equals(uri.scheme, ignoreCase = true) -> {
                    path = getDataColumn(context, uri, null, null)
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            path = getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        }
        return path
    }

    private fun processUriIsExternalStorageDocument(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":")
        val type = split[0]
        if ("primary".equals(type, ignoreCase = true)) {
            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
        }
        return null
    }

    private fun processUriIsDownloadsDocument(context: Context, uri: Uri): String? {
        val id = DocumentsContract.getDocumentId(uri)
        if (id != null && id.startsWith("raw:/")) {
            val rawUri = Uri.parse(id)
            return rawUri.path
        } else {
            val contentUriPrefixesToTry = arrayOf(
                "content://downloads/public_downloads",
                "content://downloads/my_downloads",
            )
            for (prefix in contentUriPrefixesToTry) {
                val potentialPath = runCatching {
                    val contentUri = ContentUris.withAppendedId(Uri.parse(prefix), id.toLong())
                    getDataColumn(context, contentUri, null, null)
                }.getOrNull()

                if (!TextUtils.isEmpty(potentialPath)) {
                    return potentialPath
                }
            }
        }
        return null
    }

    private fun processUriIsMediaDocument(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":")
        val type = split[0]
        val contentUri: Uri? = when (type) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> null
        }
        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])
        return getDataColumn(context, contentUri, selection, selectionArgs)
    }

    private fun copyFile(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val fileName = getContentName(context.contentResolver, uri)
                if (!fileName.isNullOrEmpty()) {
                    copyFileFileNameIsNotEmpty(context, fileName, inputStream)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun copyFileFileNameIsNotEmpty(context: Context, fileName: String, inputStream: InputStream): String? {
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(DEFAULT_BYTE_ARRAY_SIZE)
            var read: Int
            while (inputStream.read(buffer).also { read = it } > 0) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
        }
        inputStream.close()
        return file.absolutePath
    }

    private fun getContentName(resolver: ContentResolver, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = resolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            } else {
                null
            }
        } finally {
            cursor?.close()
        }
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        if (uri == null) return null
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        var result: String? = null

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                result = cursor.getString(index)
            }
        } catch (_: Exception) {
        } finally {
            cursor?.close()
        }
        return result
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun getFileExtension(path: String): String {
        val ext1 = File(path).extension.takeIf { it.isNotBlank() }
        if (ext1 != null) return ext1

        val ext2 = path.substringAfterLast('.', "").takeIf { it.isNotBlank() }
        if (ext2 != null) return ext2

        val ext3 = MimeTypeMap.getFileExtensionFromUrl(path).takeIf { it.isNotBlank() }
        if (ext3 != null) return ext3

        return path.substringAfterLast('.', "")
    }
}

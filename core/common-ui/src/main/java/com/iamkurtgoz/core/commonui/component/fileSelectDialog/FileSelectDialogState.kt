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
package com.iamkurtgoz.core.commonui.component.fileSelectDialog

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.NotActiveException

data class FileSelectDialogState(
    val shouldOpenCameraWhenAllowPermission: Boolean = false,
    val shouldOpenVideoCameraWhenAllowPermission: Boolean = false,
    val shouldOpenGalleryWhenAllowPermission: Boolean = false,
    val shouldOpenDocumentPickerWhenAllowPermission: Boolean = false,
    val tempFileUri: Uri? = null,
    val tempFile: File? = null,
    val lastSelectedFileType: FileSelectDialogFileType = FileSelectDialogFileType.IMAGE,
) {
    fun createTempFile(context: Context, fileType: FileSelectDialogFileType): FileSelectDialogState {
        val extension = when (fileType) {
            FileSelectDialogFileType.IMAGE -> "jpg"
            FileSelectDialogFileType.VIDEO -> "mp4"
            else -> throw NotActiveException()
        }

        val prefix = when (fileType) {
            FileSelectDialogFileType.IMAGE -> "photo"
            FileSelectDialogFileType.VIDEO -> "video"
            else -> throw NotActiveException()
        }

        val fileName = "${prefix}_${System.currentTimeMillis()}.$extension"
        val tempFile = File(context.cacheDir, fileName)
        val uriFile = FileProvider.getUriForFile(
            context,
            "${context.packageName}.file_provider",
            tempFile,
        )

        return copy(
            tempFile = tempFile,
            tempFileUri = uriFile,
            lastSelectedFileType = fileType,
        )
    }

    companion object {
        /**
         * Custom Saver to allow FileSelectDialogState to be stored/restored via rememberSaveable.
         * We convert non-Bundle-friendly types (File, Uri) to strings, then restore them back.
         */
        val Saver: Saver<FileSelectDialogState, Any> = mapSaver(
            save = { state ->
                mapOf(
                    "shouldOpenCameraWhenAllowPermission" to state.shouldOpenCameraWhenAllowPermission,
                    "shouldOpenVideoCameraWhenAllowPermission" to state.shouldOpenVideoCameraWhenAllowPermission,
                    "shouldOpenGalleryWhenAllowPermission" to state.shouldOpenGalleryWhenAllowPermission,
                    "shouldOpenDocumentPickerWhenAllowPermission" to state.shouldOpenDocumentPickerWhenAllowPermission,
                    "tempFileUri" to state.tempFileUri?.toString(),
                    "tempFilePath" to state.tempFile?.absolutePath,
                    "lastSelectedFileType" to state.lastSelectedFileType.name,
                )
            },
            restore = { map ->
                FileSelectDialogState(
                    shouldOpenCameraWhenAllowPermission = map["shouldOpenCameraWhenAllowPermission"] as Boolean,
                    shouldOpenVideoCameraWhenAllowPermission = map["shouldOpenVideoCameraWhenAllowPermission"] as Boolean,
                    shouldOpenGalleryWhenAllowPermission = map["shouldOpenGalleryWhenAllowPermission"] as Boolean,
                    shouldOpenDocumentPickerWhenAllowPermission = map["shouldOpenDocumentPickerWhenAllowPermission"] as Boolean,
                    tempFileUri = (map["tempFileUri"] as? String)?.toUri(),
                    tempFile = (map["tempFilePath"] as? String)?.let { File(it) },
                    lastSelectedFileType = (map["lastSelectedFileType"] as? String)?.let {
                        FileSelectDialogFileType.valueOf(it)
                    } ?: FileSelectDialogFileType.IMAGE,
                )
            },
        )
    }
}

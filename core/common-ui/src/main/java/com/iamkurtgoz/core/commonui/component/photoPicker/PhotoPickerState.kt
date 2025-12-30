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
package com.iamkurtgoz.core.commonui.component.photoPicker

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

data class PhotoPickerState(
    val shouldOpenCameraWhenAllowPermission: Boolean = false,
    val shouldOpenGalleryWhenAllowPermission: Boolean = false,
    val tempImageFileUri: Uri? = null,
    val tempImageFile: File? = null,
) {

    fun createTempImageFile(context: Context): PhotoPickerState {
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val tempFile = File(context.cacheDir, fileName)
        val uriFile = FileProvider.getUriForFile(
            context,
            "${context.packageName}.file_provider",
            tempFile,
        )
        return copy(
            tempImageFile = tempFile,
            tempImageFileUri = uriFile,
        )
    }

    companion object {
        /**
         * Custom Saver to allow PhotoSelectorState to be stored/restored via rememberSaveable.
         * We convert non-Bundle-friendly types (File, Uri) to strings, then restore them back.
         */
        val Saver: Saver<PhotoPickerState, Any> = mapSaver(
            save = { state ->
                mapOf(
                    "shouldOpenCameraWhenAllowPermission" to state.shouldOpenCameraWhenAllowPermission,
                    "shouldOpenGalleryWhenAllowPermission" to state.shouldOpenGalleryWhenAllowPermission,
                    "tempImageFileUri" to state.tempImageFileUri?.toString(),
                    "tempImageFilePath" to state.tempImageFile?.absolutePath,
                )
            },
            restore = { map ->
                PhotoPickerState(
                    shouldOpenCameraWhenAllowPermission = map["shouldOpenCameraWhenAllowPermission"] as Boolean,
                    shouldOpenGalleryWhenAllowPermission = map["shouldOpenGalleryWhenAllowPermission"] as Boolean,
                    tempImageFileUri = (map["tempImageFileUri"] as? String)?.toUri(),
                    tempImageFile = (map["tempImageFilePath"] as? String)?.let { File(it) },
                )
            },
        )
    }
}

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
package com.iamkurtgoz.feature.home.mediaViewer.component

import android.content.Context
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Suppress("MagicNumber")
@Composable
fun PdfViewer(
    base64: String?,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var filePath by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(base64) {
        if (base64?.isNotEmpty() == true) {
            withContext(Dispatchers.IO) {
                try {
                    val pdfBytes = Base64.decode(base64, Base64.DEFAULT)
                    val tempFile = writeBytesToFile(context, pdfBytes)
                    filePath = tempFile.absolutePath
                    isLoading = false
                } catch (_: Exception) {
                    isLoading = false
                }
            }
        } else {
            isLoading = false
        }
    }

    PdfViewerActivity.launchPdfFromPath(
        context = context,
        path = filePath,
        pdfTitle = "null",
        saveTo = saveTo.ASK_EVERYTIME,
    )

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        AppLoadingDialog()
    }
}

private fun writeBytesToFile(context: Context, bytes: ByteArray): File {
    val prefix = "pdf_"
    val suffix = ".pdf"
    val tempFile = File.createTempFile(prefix, suffix, context.cacheDir)
    FileOutputStream(tempFile).use { it.write(bytes) }
    return tempFile
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            PdfViewer(
                base64 = "",
            )
        }
    }
}

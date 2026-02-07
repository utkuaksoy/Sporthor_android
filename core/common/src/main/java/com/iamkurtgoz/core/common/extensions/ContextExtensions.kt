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
package com.iamkurtgoz.core.common.extensions

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.io.IOException

@SuppressLint("QueryPermissionsNeeded")
fun Context.shareFile(
    uri: Uri?,
    mimeType: String = "*/*",
) {
    try {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = mimeType
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntent)
        }
    } catch (e: IOException) {
        Log.d("IntentExtensions", "Error", e)
    }
}

fun Context.openFile(
    uri: Uri?,
    mimeType: String = "*/*",
) {
    try {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    } catch (e: IOException) {
        Log.d("IntentExtensions", "Error", e)
    }
}

fun Context.openUrl(url: String?) {
    try {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        val chooserIntent = Intent.createChooser(browserIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (browserIntent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        }
    } catch (e: java.lang.Exception) {
        Log.d("IntentExtensions", "Error", e)
    }
}

fun Context.openGooglePlay(packageName: String) {
    try {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName"),
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        println(e.message)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

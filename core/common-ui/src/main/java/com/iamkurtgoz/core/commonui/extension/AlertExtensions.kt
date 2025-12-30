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
package com.iamkurtgoz.core.commonui.extension

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel

internal fun AlertDialogModel?.getTitle(context: Context): String {
    val model = this ?: return ""
    return when (model) {
        is AnyAlertDialogModel -> {
            when (val title = model.title) {
                is String -> title
                is Int -> context.getString(title)
                else -> context.getString(resourcesR.string.general_warning)
            }
        }
    }
}

internal fun AlertDialogModel?.getMessage(context: Context): String {
    val model = this ?: return ""
    return when (model) {
        is AnyAlertDialogModel -> {
            when (val message = model.message) {
                is String -> message
                is Int -> context.getString(message)
                else -> context.getString(resourcesR.string.general_any_error_message)
            }
        }
    }
}

internal fun AlertDialogModel?.getConfirmButton(context: Context): String? {
    val model = this ?: return ""
    return when (model) {
        is AnyAlertDialogModel -> {
            when (val confirmButton = model.confirmButton) {
                is String -> confirmButton
                is Int -> context.getString(confirmButton)
                else -> null
            }
        }
    }
}

internal fun AlertDialogModel?.getDismissButton(context: Context): String? {
    val model = this ?: return ""
    return when (model) {
        is AnyAlertDialogModel -> {
            when (val dismissButton = model.dismissButton) {
                is String -> dismissButton
                is Int -> context.getString(dismissButton)
                else -> null
            }
        }
    }
}

@Composable
fun AlertDialogModel?.Alert(
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    val context: Context = LocalContext.current

    if (this != null) {
        val titleContent: @Composable () -> Unit = {
            BasicText(
                text = this.getTitle(context),
                style = AppTheme.typography.subtitleLarge.copy(
                    textAlign = TextAlign.Center,
                ),
                maxLines = AppDefaults.TWO,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        val messageContent: @Composable () -> Unit = {
            BasicText(
                text = this.getMessage(context),
                style = AppTheme.typography.labelRegular.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        val confirmButtonContent: @Composable () -> Unit = {
            val text = this.getConfirmButton(context) ?: ""
            TextButton(
                onClick = onConfirmClick,
                content = {
                    BasicText(
                        text = text,
                        style = AppTheme.typography.subtitleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.dialogColors.alertDialogColors.confirmButtonContentColor,
                        ),
                    )
                },
            )
        }
        val dismissButtonContent: @Composable () -> Unit = {
            val text = this.getDismissButton(context) ?: ""
            TextButton(
                onClick = onDismissRequest,
                content = {
                    BasicText(
                        text = text,
                        style = AppTheme.typography.subtitleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.dialogColors.alertDialogColors.dismissButtonContentColor,
                        ),
                    )
                },
            )
        }
        AlertDialog(
            modifier = modifier,
            properties = properties,
            onDismissRequest = onDismissRequest,
            title = titleContent,
            text = messageContent,
            confirmButton = confirmButtonContent,
            dismissButton = if (this.getDismissButton(context) == null) null else dismissButtonContent,
            containerColor = AppTheme.colors.dialogColors.alertDialogColors.containerColor,
            iconContentColor = AppTheme.colors.dialogColors.alertDialogColors.iconContentColor,
            titleContentColor = AppTheme.colors.dialogColors.alertDialogColors.titleContentColor,
            textContentColor = AppTheme.colors.dialogColors.alertDialogColors.textContentColor,
        )
    }
}

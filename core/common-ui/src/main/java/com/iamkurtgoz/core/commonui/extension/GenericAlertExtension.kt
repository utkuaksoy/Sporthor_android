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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.iamkurtgoz.core.commonui.component.bottomsheet.CustomBottomSheet
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.FailureGenericAlertDialogModel
import com.iamkurtgoz.domain.model.base.GenericAlertDialogModel
import com.iamkurtgoz.domain.model.base.SuccessGenericAlertDialogModel

internal fun GenericAlertDialogModel?.getTitle(context: Context): String {
    val model = this ?: return ""
    return when (model) {
        is SuccessGenericAlertDialogModel -> {
            when (val title = model.title) {
                is String -> title
                is Int -> context.getString(title)
                else -> context.getString(resourcesR.string.general_warning)
            }
        }
        is FailureGenericAlertDialogModel -> {
            when (val title = model.title) {
                is String -> title
                is Int -> context.getString(title)
                else -> context.getString(resourcesR.string.general_warning)
            }
        }
    }
}

internal fun GenericAlertDialogModel?.getMessage(context: Context): String {
    val model = this ?: return ""
    return when (model) {
        is SuccessGenericAlertDialogModel -> {
            when (val message = model.message) {
                is String -> message
                is Int -> context.getString(message)
                else -> context.getString(resourcesR.string.general_any_error_message)
            }
        }
        is FailureGenericAlertDialogModel -> {
            when (val message = model.message) {
                is String -> message
                is Int -> context.getString(message)
                else -> context.getString(resourcesR.string.general_any_error_message)
            }
        }
    }
}

internal fun GenericAlertDialogModel?.getButtonContent(context: Context): String {
    val model = this ?: return ""
    return when (model) {
        is SuccessGenericAlertDialogModel -> {
            when (val buttonContent = model.buttonContent) {
                is String -> buttonContent
                is Int -> context.getString(buttonContent)
                else -> context.getString(resourcesR.string.button_ok_button)
            }
        }
        is FailureGenericAlertDialogModel -> {
            when (val buttonContent = model.buttonContent) {
                is String -> buttonContent
                is Int -> context.getString(buttonContent)
                else -> context.getString(resourcesR.string.button_ok_button)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAlertDialogModel?.GenericAlert(
    onDismissRequest: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    shouldDismissOnBackPress: Boolean = true,
    sheetState: SheetState = rememberStandardBottomSheetState(
        confirmValueChange = { newState ->
            if (!shouldDismissOnBackPress) {
                newState != SheetValue.Hidden
            } else {
                true
            }
        },
        skipHiddenState = false,
    ),
) {
    val context: Context = LocalContext.current

    if (this is SuccessGenericAlertDialogModel) {
        CustomBottomSheet.GenericSuccessAlert(
            modifier = modifier,
            title = this.getTitle(context),
            message = this.getMessage(context),
            buttonContent = this.getButtonContent(context),
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            onActionClick = onActionClick,
            shouldDismissOnBackPress = shouldDismissOnBackPress,
        )
    } else if (this is FailureGenericAlertDialogModel) {
        CustomBottomSheet.GenericFailureAlert(
            modifier = modifier,
            title = this.getTitle(context),
            message = this.getMessage(context),
            buttonContent = this.getButtonContent(context),
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            onActionClick = onActionClick,
            shouldDismissOnBackPress = shouldDismissOnBackPress,
        )
    }
}

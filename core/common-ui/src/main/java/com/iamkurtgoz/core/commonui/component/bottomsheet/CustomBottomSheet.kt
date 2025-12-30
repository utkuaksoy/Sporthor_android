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
package com.iamkurtgoz.core.commonui.component.bottomsheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.SecureFlagPolicy
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import kotlinx.coroutines.launch

object CustomBottomSheet {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenericSuccessAlert(
        title: String,
        message: String,
        buttonContent: String,
        onDismissRequest: () -> Unit,
        onActionClick: () -> Unit,
        modifier: Modifier = Modifier,
        containerColor: Color = AppTheme.colors.generalColors.backgroundPrimary,
        contentColor: Color = AppTheme.colors.generalColors.textPrimary,
        shape: RoundedCornerShape = RoundedCornerShape(
            topStart = AppTheme.dimens.dp16,
            topEnd = AppTheme.dimens.dp16,
            bottomStart = AppTheme.dimens.dp0,
            bottomEnd = AppTheme.dimens.dp0,
        ),
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
    ) = GenericAlert(
        icon = resourcesR.drawable.img_generic_alert_success,
        title = title,
        message = message,
        buttonContent = buttonContent,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onActionClick = onActionClick,
        containerColor = containerColor,
        contentColor = contentColor,
        sheetState = sheetState,
        shape = shape,
        shouldDismissOnBackPress = shouldDismissOnBackPress,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenericFailureAlert(
        title: String,
        message: String,
        buttonContent: String,
        onDismissRequest: () -> Unit,
        onActionClick: () -> Unit,
        modifier: Modifier = Modifier,
        containerColor: Color = AppTheme.colors.generalColors.backgroundPrimary,
        contentColor: Color = AppTheme.colors.generalColors.textPrimary,
        shape: RoundedCornerShape = RoundedCornerShape(
            topStart = AppTheme.dimens.dp16,
            topEnd = AppTheme.dimens.dp16,
            bottomStart = AppTheme.dimens.dp0,
            bottomEnd = AppTheme.dimens.dp0,
        ),
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
    ) = GenericAlert(
        icon = resourcesR.drawable.img_generic_alert_failure,
        title = title,
        message = message,
        buttonContent = buttonContent,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onActionClick = onActionClick,
        containerColor = containerColor,
        contentColor = contentColor,
        sheetState = sheetState,
        shape = shape,
        shouldDismissOnBackPress = shouldDismissOnBackPress,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenericWarningAlert(
        title: String,
        message: String,
        buttonContent: String,
        onDismissRequest: () -> Unit,
        onActionClick: () -> Unit,
        modifier: Modifier = Modifier,
        containerColor: Color = AppTheme.colors.generalColors.backgroundPrimary,
        contentColor: Color = AppTheme.colors.generalColors.textPrimary,
        shape: RoundedCornerShape = RoundedCornerShape(
            topStart = AppTheme.dimens.dp16,
            topEnd = AppTheme.dimens.dp16,
            bottomStart = AppTheme.dimens.dp0,
            bottomEnd = AppTheme.dimens.dp0,
        ),
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
    ) = GenericAlert(
        icon = resourcesR.drawable.img_generic_alert_warning,
        title = title,
        message = message,
        buttonContent = buttonContent,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onActionClick = onActionClick,
        containerColor = containerColor,
        contentColor = contentColor,
        sheetState = sheetState,
        shape = shape,
        shouldDismissOnBackPress = shouldDismissOnBackPress,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenericAlert(
        @DrawableRes icon: Int,
        title: String,
        message: String,
        buttonContent: String,
        onDismissRequest: () -> Unit,
        onActionClick: () -> Unit,
        modifier: Modifier = Modifier,
        containerColor: Color = AppTheme.colors.generalColors.backgroundPrimary,
        contentColor: Color = AppTheme.colors.generalColors.textPrimary,
        shape: RoundedCornerShape = RoundedCornerShape(
            topStart = AppTheme.dimens.dp16,
            topEnd = AppTheme.dimens.dp16,
            bottomStart = AppTheme.dimens.dp0,
            bottomEnd = AppTheme.dimens.dp0,
        ),
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
    ) = CustomBottomSheetImpl(
        icon = icon,
        title = title,
        message = message,
        buttonContent = buttonContent,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onActionClick = onActionClick,
        containerColor = containerColor,
        contentColor = contentColor,
        sheetState = sheetState,
        shape = shape,
        shouldDismissOnBackPress = shouldDismissOnBackPress,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomBottomSheetImpl(
    @DrawableRes icon: Int,
    title: String,
    message: String,
    buttonContent: String,
    onDismissRequest: () -> Unit,
    onActionClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    shape: RoundedCornerShape,
    sheetState: SheetState,
    shouldDismissOnBackPress: Boolean,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = shouldDismissOnBackPress,
            securePolicy = SecureFlagPolicy.Inherit,
        ),
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(AppTheme.dimens.dp48),
                    )
                }

                item {
                    Text(
                        text = title,
                        style = AppTheme.typography.heading06,
                        color = AppTheme.colors.generalColors.textPrimary,
                        modifier = Modifier
                            .padding(top = AppTheme.spacing.spacingSmallest)
                            .padding(horizontal = AppTheme.spacing.spacingHuge),
                        textAlign = TextAlign.Center,
                    )
                }

                item {
                    Text(
                        text = message,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.generalColors.textSecondary,
                        modifier = Modifier
                            .padding(top = AppTheme.spacing.spacingSmall)
                            .padding(horizontal = AppTheme.spacing.spacingHuge),
                        textAlign = TextAlign.Center,
                    )
                }

                item {
                    AppButton.PrimaryLarge(
                        text = buttonContent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.spacing.spacingLarge)
                            .padding(horizontal = AppTheme.spacing.spacingHuge),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismissRequest.invoke()
                                onActionClick.invoke()
                            }
                        },
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            CustomBottomSheet.GenericAlert(
                icon = resourcesR.drawable.img_generic_alert_success,
                title = stringResource(resourcesR.string.alert_label_reset_password_mail_sent_title),
                message = stringResource(resourcesR.string.alert_label_reset_password_mail_sent_sub_title),
                buttonContent = stringResource(resourcesR.string.button_ok_button),
                sheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded,
                ),
                onDismissRequest = {},
                onActionClick = {},
            )
        }
    }
}

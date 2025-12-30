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
package com.iamkurtgoz.core.commonui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAddEventTypeDialog(
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    addNewEventClick: () -> Unit,
    selectSavedEventsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { true },
        )

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = AppTheme.colors.generalColors.backgroundPrimary,
            scrimColor = AppTheme.colors.generalColors.backgroundPrimary.copy(
                alpha = AppDefaults.COMPOSE_COLORS_THREE_QUARTER_ALPHA,
            ),
            modifier = modifier,
        ) {
            SelectAddEventTypeDialogContent(
                addNewEventClick = addNewEventClick,
                selectSavedEventsClick = selectSavedEventsClick,
            )
        }
    }
}

@Composable
private fun SelectAddEventTypeDialogContent(
    modifier: Modifier = Modifier,
    addNewEventClick: () -> Unit,
    selectSavedEventsClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.spacingMedium)
            .padding(bottom = AppTheme.spacing.spacingMedium),
    ) {
        AppButton.OutlineLarge(
            text = "Yeni Takvim Etkinliği Oluştur", // TODO: Localize
            onClick = addNewEventClick,
            leftIcon = resourcesR.drawable.img_clock_plus,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.spacingMedium),
        )

        AppButton.OutlineLarge(
            text = "Kayıtlı Şablonlardan Seç", // TODO: Localize
            onClick = selectSavedEventsClick,
            leftIcon = resourcesR.drawable.img_library_plus,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.spacingMedium),
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun PreviewSelectAddEventTypeDialogContent() {
    AppTheme {
        AppThemeSurface {
            Box(Modifier.fillMaxSize()) {
                SelectAddEventTypeDialogContent(
                    addNewEventClick = {},
                    selectSavedEventsClick = {},
                )
            }
        }
    }
}

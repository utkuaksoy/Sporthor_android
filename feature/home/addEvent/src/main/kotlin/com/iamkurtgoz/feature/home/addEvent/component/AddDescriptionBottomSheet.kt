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
package com.iamkurtgoz.feature.home.addEvent.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.feature.home.addEvent.AddEventScreenContract
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddDescriptionBottomSheet(
    showSheet: Boolean,
    state: AddEventScreenContract.State,
    setEvent: (AddEventScreenContract.Event) -> Unit,
    onDismissRequest: () -> Unit,
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
            AddDescriptionBottomSheetContent(
                state = state,
                setEvent = setEvent,
            )
        }
    }
}

@Composable
private fun AddDescriptionBottomSheetContent(
    state: AddEventScreenContract.State,
    setEvent: (AddEventScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                text = "Kaydet", // TODO: Localize
                onClick = {
                    setEvent.invoke(AddEventScreenContract.Event.DismissDialogs)
                },
                enabled = state.textDescription.isNotEmpty,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(bottom = AppTheme.spacing.spacingHuge),
            )
        },
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(bottom = AppTheme.spacing.spacingMedium),
        ) {
            Text(
                text = "Açıklama", // TODO: Localize
                style = AppTheme.typography.heading04,
                modifier = modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.spacingSmall))

            AppTextField.Primary(
                placeholder = "Açıklama", // TODO: Localize
                value = state.textDescription.value,
                onValueChange = {
                    setEvent.invoke(AddEventScreenContract.Event.SetDescription(it))
                },
                singleLine = false,
                maxLines = 15,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun PreviewAddTaskTypeBottomSheetContent() {
    AppTheme {
        AppThemeSurface {
            Box(Modifier.fillMaxSize()) {
                AddDescriptionBottomSheetContent(
                    state = AddEventScreenContract.State(
                        isLoading = true,
                        appBuildConfigStatePack = AppBuildConfigStatePack(),
                        appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                        route = HomeScreenAddEventRoute(
                            model = HomeScreenAddEventScreenNavigationModel(
                                selectedDate = null,
                            ),
                        ),
                        eventStartDate = LocalDate.now(),
                        eventStartTime = LocalTime.now(),
                        eventEndDate = LocalDate.now(),
                        eventEndTime = LocalTime.now().plusHours(2),
                    ),
                    setEvent = {},
                )
            }
        }
    }
}

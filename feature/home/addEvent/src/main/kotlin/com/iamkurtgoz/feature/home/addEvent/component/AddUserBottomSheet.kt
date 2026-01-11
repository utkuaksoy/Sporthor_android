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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.feature.home.addEvent.AddEventScreenContract
import com.iamkurtgoz.feature.home.addEvent.domain.model.GetTrainingGroupUserUIModelTeam
import com.iamkurtgoz.feature.home.addEvent.domain.model.mockTrainingGroupUserUIModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddUserBottomSheet(
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
            AddUserBottomSheetContent(
                state = state,
                setEvent = setEvent,
            )
        }
    }
}

@Composable
private fun AddUserBottomSheetContent(
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
                enabled = state.selectedGetTrainingGroupUserUIModelTeam != null || state.selectedGetTrainingGroupUserList.isNotEmpty(),
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
                .padding(padding)
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(bottom = AppTheme.spacing.spacingMedium)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Kişi/Topluluk Ekle", // TODO: Localize
                style = AppTheme.typography.heading04,
                modifier = modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.spacingSmall))

            AppTextField.SearchField(
                placeholder = "Kişi/Topluluk Ara", // TODO: Localize
                value = state.textSearchUser.value,
                onValueChange = {
                    setEvent.invoke(AddEventScreenContract.Event.SetSearchUser(it))
                },
            )

            Text(
                text = "Topluluklar", // TODO: Localize
                style = AppTheme.typography.subtitleLarge,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingMedium),
                textAlign = TextAlign.Start,
            )

            state.getTrainingGroupUserUIModel?.groups?.filterNotNull()?.forEach { item ->
                key(item) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.spacing.spacingSmallest),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        UserImageView(
                            data = item.team?.detail ?: item.groupName.getUserNameFirstChar(),
                            modifier = Modifier
                                .size(56.dp),
                        )

                        Text(
                            text = item.groupName ?: "-", // TODO: Localize
                            style = AppTheme.typography.subtitleLarge,
                            modifier = modifier
                                .weight(1f)
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .padding(start = AppTheme.spacing.spacingMedium),
                            textAlign = TextAlign.Start,
                        )

                        if (item.groupId == state.selectedGetTrainingGroupUserUIModelTeam?.value) {
                            AppButton.PrimarySmall(
                                text = "İptal", // TODO: Localize
                                onClick = {
                                    setEvent.invoke(AddEventScreenContract.Event.SetGetTrainingGroupUserUIModelTeam(null))
                                },
                                modifier = Modifier,
                            )
                        } else {
                            AppButton.SecondarySmall(
                                text = "Gönder", // TODO: Localize
                                onClick = {
                                    setEvent.invoke(AddEventScreenContract.Event.SetGetTrainingGroupUserUIModelTeam(value = GetTrainingGroupUserUIModelTeam(item.groupImage,item.groupName,item.groupId)))
                                },
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }

            Text(
                text = "Kişiler", // TODO: Localize
                style = AppTheme.typography.subtitleLarge,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingMedium),
                textAlign = TextAlign.Start,
            )

            state.getTrainingGroupUserUIModel?.allUsers?.filter {
                if (state.textSearchUser.value.isEmpty()) {
                    true
                } else {
                    it.isMatch(state.textSearchUser.value)
                }
            }?.forEach { item ->
                key(item) {
                    UserRow(
                        modifier = Modifier,
                        userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                        isHeaderUser = true,
                        title = item.name,
                        subTitle = arrayOf(),
                        trailingContent = {
                            AppRadioButton.Secondary(
                                selected = state.selectedGetTrainingGroupUserList.any { it.id == item.id },
                                onClick = {
                                    setEvent(AddEventScreenContract.Event.SetSelectedGetTrainingGroupUser(item))
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(AddEventScreenContract.Event.SetSelectedGetTrainingGroupUser(item))
                        },
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun PreviewAddUserBottomSheetContent() {
    AppTheme {
        AppThemeSurface {
            Box(Modifier.fillMaxSize()) {
                AddUserBottomSheetContent(
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
                        getTrainingGroupUserUIModel = mockTrainingGroupUserUIModel,
                        selectedGetTrainingGroupUserUIModelTeam = mockTrainingGroupUserUIModel.groups?.firstOrNull()?.team,
                    ),
                    setEvent = {},
                )
            }
        }
    }
}

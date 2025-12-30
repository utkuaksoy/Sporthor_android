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
package com.iamkurtgoz.feature.home.selectTrainingGroup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSelectTrainingGroupScreenRoute
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel

@Composable
internal fun SelectTrainingGroupScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToEditTrainingGroupScreen: (HomeScreenEditTrainingGroupScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
    viewModel: SelectTrainingGroupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SelectTrainingGroupScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SelectTrainingGroupScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SelectTrainingGroupScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SelectTrainingGroupScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SelectTrainingGroupScreenContract.SideEffect.NavigateToEditTrainingGroupScreen -> navigateToEditTrainingGroupScreen(event.model, state.route.fromTrainingGroup)
        }
    }

    SelectTrainingGroupScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectTrainingGroupScreenScaffold(
    state: SelectTrainingGroupScreenContract.State,
    setEvent: (SelectTrainingGroupScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(SelectTrainingGroupScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Antrenman Grubu Seç", // TODO: Localize
                    )
                },
                rightContent = {
                    TextButton(
                        content = {
                            Text(
                                text = if (state.isDeleteMode) "İptal" else "Sil",
                                color = Color.Black,
                            )
                        },
                        onClick = {
                            setEvent.invoke(SelectTrainingGroupScreenContract.Event.ToggleDeleteMode)
                        },
                    )
                },
            )
        },
        bottomBar = {
            if (state.isDeleteMode) {
                AppButton.PrimaryLarge(
                    text = "Seçili Antrenman Grubunu Sil",
                    enabled = state.selectedTrainingGroup != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    onClick = {
                        setEvent.invoke(SelectTrainingGroupScreenContract.Event.ShowDeleteTrainingGroupDialog)
                    },
                )
            }
        },
    ) { padding ->
        SelectTrainingGroupScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SelectTrainingGroupScreenContract.Event.DismissDialogs)
        }

        state.deleteTrainingGroupDialogModel?.Alert(
            onDismissRequest = {
                setEvent.invoke(SelectTrainingGroupScreenContract.Event.DismissDialogs)
            },
            onConfirmClick = {
                setEvent.invoke(SelectTrainingGroupScreenContract.Event.DismissDialogs)
                setEvent.invoke(SelectTrainingGroupScreenContract.Event.DeleteTrainingGroup)
            },
        )

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectTrainingGroupScreenScaffold(
                state = SelectTrainingGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSelectTrainingGroupScreenRoute(),
                ),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.editTrainingGroup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenEditTrainingGroupScreenRoute
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.feature.home.editTrainingGroup.component.SelectSeasonDialog

@Composable
internal fun EditTrainingGroupScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSuccessAddTrainingGroupScreen: (HomeScreenSuccessAddTrainingGroupScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
    viewModel: EditTrainingGroupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("EditTrainingGroupScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(EditTrainingGroupScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is EditTrainingGroupScreenContract.SideEffect.NavigateUp -> navigateUp()
            is EditTrainingGroupScreenContract.SideEffect.PopBackStack -> popBackStack()
            is EditTrainingGroupScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is EditTrainingGroupScreenContract.SideEffect.NavigateToSuccessAddTrainingGroupScreen -> navigateToSuccessAddTrainingGroupScreen(event.model, state.route.fromTrainingGroup)
        }
    }

    EditTrainingGroupScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun EditTrainingGroupScreenScaffold(
    state: EditTrainingGroupScreenContract.State,
    setEvent: (EditTrainingGroupScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingMedium),
            ) {
                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    text = "Devam Et", // TODO: Localize
                    onClick = {
                        setEvent.invoke(EditTrainingGroupScreenContract.Event.EditTrainingGroup)
                    },
                )
            }
        },
    ) { padding ->
        EditTrainingGroupScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(EditTrainingGroupScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        SelectSeasonDialog(
            showSheet = state.showSelectSeasonDialog,
            seasons = state.seasonList,
            onDismissRequest = {
                setEvent.invoke(EditTrainingGroupScreenContract.Event.DismissDialogs)
            },
            onSeasonSelected = {
                setEvent.invoke(EditTrainingGroupScreenContract.Event.SetSelectedSeason(it))
                setEvent.invoke(EditTrainingGroupScreenContract.Event.DismissDialogs)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            EditTrainingGroupScreenScaffold(
                state = EditTrainingGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenEditTrainingGroupScreenRoute(
                        model = HomeScreenEditTrainingGroupScreenNavigationModel(
                            teamId = null,
                            teamName = null,
                            teamLogo = null,
                            season = null,
                            id = null,
                            groupName = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

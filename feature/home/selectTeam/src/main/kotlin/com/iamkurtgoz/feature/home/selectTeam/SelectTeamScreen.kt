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
package com.iamkurtgoz.feature.home.selectTeam

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
import androidx.compose.ui.res.stringResource
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
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.dataStore.CustomUserRole

@Composable
internal fun SelectTeamScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToCreateTeam: (fromGenerateClub: Boolean) -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel, fromTrainingGroup: Boolean) -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel, fromGenerateClub: Boolean) -> Unit,
    viewModel: SelectTeamViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("UserTeamsScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SelectTeamScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SelectTeamScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SelectTeamScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SelectTeamScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is SelectTeamScreenContract.SideEffect.CreateTeam -> navigateToCreateTeam(state.route.fromGenerateClub)
            is SelectTeamScreenContract.SideEffect.NavigateToTrainingScreen -> navigateToTrainingScreen(event.model, state.route.fromTrainingGroup)
            is SelectTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument -> navigateToHomeScreenSendClubAuthDocumentScreen(event.model, event.fromGenerateClub)
        }
    }

    SelectTeamScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SelectTeamScaffold(
    state: SelectTeamScreenContract.State,
    setEvent: (SelectTeamScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding()),
            ) {
                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    enabled = state.selectedTeamsList.isNotEmpty(),
                    text = stringResource(resourcesR.string.button_continue_button),
                    onClick = {
                        setEvent.invoke(SelectTeamScreenContract.Event.SaveUserTeams)
                    },
                )

                if (state.route.fromGenerateClub) {
                    AppButton.SecondaryLarge(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.spacingHuge)
                            .padding(top = AppTheme.dimens.dp12),
                        text = "Kulüp Oluştur", // TODO: Localize
                        onClick = {
                            setEvent.invoke(SelectTeamScreenContract.Event.CreateTeam)
                        },
                    )
                } else if (state.customUserRole == CustomUserRole.CLUB_OFFICIAL_AND_TRAINER || state.customUserRole == CustomUserRole.CLUB_OFFICIAL) {
                    AppButton.SecondaryLarge(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.spacingHuge)
                            .padding(top = AppTheme.dimens.dp12),
                        text = "Kulüp Oluştur", // TODO: Localize
                        onClick = {
                            setEvent.invoke(SelectTeamScreenContract.Event.CreateTeam)
                        },
                    )
                } else if (state.customUserRole == CustomUserRole.TRAINER || state.route.isEdit) {
                    AppButton.OutlineLarge(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.spacingHuge)
                            .padding(top = AppTheme.dimens.dp12),
                        text = "Bu Adımı Atla", // TODO: Localize
                        onClick = {
                            setEvent.invoke(SelectTeamScreenContract.Event.NavigateToHome)
                        },
                    )
                }
            }
        },
    ) { padding ->
        SelectTeamScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SelectTeamScreenContract.Event.DismissDialogs)
        }

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
            SelectTeamScaffold(
                state = SelectTeamScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    customUserRole = CustomUserRole.TRAINER,
                    route = HomeScreenSelectTeamRoute(
                        isEdit = false,
                        fromGenerateClub = false,
                        fromTrainingGroup = false,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

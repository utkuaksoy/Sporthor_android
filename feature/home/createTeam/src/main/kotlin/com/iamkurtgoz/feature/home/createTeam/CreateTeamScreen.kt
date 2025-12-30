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
package com.iamkurtgoz.feature.home.createTeam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.iamkurtgoz.core.commonui.component.photoPicker.PhotoPicker
import com.iamkurtgoz.core.commonui.component.successClubCreateBottomSheet.SuccessClubCreateBottomSheet
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenCreateTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.domain.eventbus.impl.CreateClubEventBus

@Composable
internal fun CreateTeamScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToSelectAddress: () -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel, fromGenerateClub: Boolean) -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToCreateTeamSelectBranchScreen: () -> Unit,
    viewModel: CreateTeamViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("CreateTeamScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CreateTeamScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.createTeamEventBus.observeEventBus {
        viewModel.setEvent(CreateTeamScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CreateTeamScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CreateTeamScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CreateTeamScreenContract.SideEffect.NavigateToSelectAddress -> navigateToSelectAddress()
            is CreateTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument -> navigateToHomeScreenSendClubAuthDocumentScreen(event.model, state.route.fromGenerateClub)
            is CreateTeamScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is CreateTeamScreenContract.SideEffect.NavigateToTrainingScreen -> navigateToTrainingScreen(event.model)
            is CreateTeamScreenContract.SideEffect.NavigateToCreateTeamSelectBranchScreen -> navigateToCreateTeamSelectBranchScreen()
        }
    }

    AppTheme.appEventBus.createClubEventBus.observeEventBus { event ->
        when (event) {
            is CreateClubEventBus.Event.SelectedAddressChanged -> {
                val event = CreateTeamScreenContract.Event.SelectedAddressChanged(
                    title = event.title,
                    address = event.address,
                    city = event.city,
                    country = event.country,
                )
                viewModel.setEvent(event)
            }
        }
    }

    CreateTeamScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun CreateTeamScreenScaffold(
    state: CreateTeamScreenContract.State,
    setEvent: (CreateTeamScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding()),
                text = "Kulübü Oluştur", // TODO: Localize
                onClick = {
                    setEvent.invoke(CreateTeamScreenContract.Event.CreateClub)
                },
            )
        },
    ) { padding ->
        CreateTeamScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(CreateTeamScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        PhotoPicker(
            isShow = state.showPhotoPicker,
            onSelectedFileCallback = {
                setEvent.invoke(CreateTeamScreenContract.Event.SetSelectedImage(it))
                setEvent.invoke(CreateTeamScreenContract.Event.DismissDialogs)
            },
            onDismissRequest = {
                setEvent.invoke(CreateTeamScreenContract.Event.DismissDialogs)
            },
        )

        SuccessClubCreateBottomSheet(
            isShow = state.createdClubModel != null,
            imageData = state.selectedImage,
            clubName = state.textClubName.value,
            onSendDocumentClick = {
                setEvent.invoke(CreateTeamScreenContract.Event.NavigateToSendClubAuthDocument)
            },
            onSkipThisPartClick = {
                if (state.route.fromGenerateClub) {
                    setEvent.invoke(CreateTeamScreenContract.Event.NavigateToHome)
                } else {
                    when (state.customUserRole) {
                        CustomUserRole.TRAINER, CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> {
                            setEvent.invoke(CreateTeamScreenContract.Event.NavigateToTrainingScreen)
                        }
                        CustomUserRole.CLUB_OFFICIAL -> {
                            setEvent.invoke(CreateTeamScreenContract.Event.NavigateToHome)
                        }
                        else -> {
                            setEvent.invoke(CreateTeamScreenContract.Event.NavigateToHome)
                        }
                    }
                }
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CreateTeamScreenScaffold(
                state = CreateTeamScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenCreateTeamRoute(
                        fromGenerateClub = false,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.editTeam

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
import com.iamkurtgoz.core.navigation.HomeScreenEditTeamRoute
import com.iamkurtgoz.core.navigation.model.home.editTeam.HomeScreenEditTeamScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.dataStore.CustomUserRole

@Composable
internal fun EditTeamScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToSelectAddress: () -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel) -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToEditTeamSelectBranchScreen: () -> Unit,
    viewModel: EditTeamViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("EditTeamScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(EditTeamScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.editTeamEventBus.observeEventBus {
        viewModel.setEvent(EditTeamScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is EditTeamScreenContract.SideEffect.NavigateUp -> navigateUp()
            is EditTeamScreenContract.SideEffect.PopBackStack -> popBackStack()
            is EditTeamScreenContract.SideEffect.NavigateToSelectAddress -> navigateToSelectAddress()
            is EditTeamScreenContract.SideEffect.NavigateToSendClubAuthDocument -> navigateToHomeScreenSendClubAuthDocumentScreen(event.model)
            is EditTeamScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is EditTeamScreenContract.SideEffect.NavigateToTrainingScreen -> navigateToTrainingScreen(event.model)
            is EditTeamScreenContract.SideEffect.NavigateToEditTeamSelectBranchScreen -> navigateToEditTeamSelectBranchScreen()
        }
    }

    EditTeamScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun EditTeamScreenScaffold(
    state: EditTeamScreenContract.State,
    setEvent: (EditTeamScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingMedium),
                text = "Kulübü Düzenle", // TODO: Localize
                onClick = {
                    setEvent.invoke(EditTeamScreenContract.Event.EditClub)
                },
            )
        },
    ) { padding ->
        EditTeamScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(EditTeamScreenContract.Event.DismissDialogs)
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
                setEvent.invoke(EditTeamScreenContract.Event.SetSelectedImage(it))
                setEvent.invoke(EditTeamScreenContract.Event.DismissDialogs)
            },
            onDismissRequest = {
                setEvent.invoke(EditTeamScreenContract.Event.DismissDialogs)
            },
        )

        SuccessClubCreateBottomSheet(
            isShow = state.editedClubModel != null,
            imageData = state.selectedImage ?: state.originalImageUrl,
            clubName = state.textClubName.value,
            isEdit = true,
            onSendDocumentClick = {
                setEvent.invoke(EditTeamScreenContract.Event.NavigateToSendClubAuthDocument)
            },
            onSkipThisPartClick = {
                when (state.customUserRole) {
                    CustomUserRole.TRAINER, CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> {
                        setEvent.invoke(EditTeamScreenContract.Event.NavigateToTrainingScreen)
                    }
                    CustomUserRole.CLUB_OFFICIAL -> {
                        setEvent.invoke(EditTeamScreenContract.Event.NavigateToHome)
                    }
                    else -> {
                        setEvent.invoke(EditTeamScreenContract.Event.NavigateToHome)
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
            EditTeamScreenScaffold(
                state = EditTeamScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenEditTeamRoute(
                        model = HomeScreenEditTeamScreenNavigationModel(
                            address = null,
                            city = null,
                            clubId = null,
                            clubName = null,
                            confirmationStatus = null,
                            county = null,
                            foundationYear = null,
                            logo = null,
                            branchName = null,
                            value = null,
                            val2 = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

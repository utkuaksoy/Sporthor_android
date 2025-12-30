package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsUpdateCoachScreenRoute

@Composable
internal fun UpdateCoachScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: UpdateCoachViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("UpdateCoachScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(UpdateCoachScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is UpdateCoachScreenContract.SideEffect.NavigateUp -> navigateUp()
            is UpdateCoachScreenContract.SideEffect.PopBackStack -> popBackStack()
            is UpdateCoachScreenContract.SideEffect.NavigateToHome -> navigateToHome()
        }
    }

    UpdateCoachScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateCoachScreenScaffold(
    state: UpdateCoachScreenContract.State,
    setEvent: (UpdateCoachScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(UpdateCoachScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Grup Üyelerini Ekle", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            AppButton.PrimaryLarge(
                text = "Grup Üyelerini Davet Et", // TODO: Localize
                onClick = {
                    setEvent.invoke(UpdateCoachScreenContract.Event.UpdateCoach)
                },
                enabled = state.selectedUserList.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingHuge),
            )
        },
    ) { padding ->
        UpdateCoachScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(UpdateCoachScreenContract.Event.DismissDialogs)
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
            UpdateCoachScreenScaffold(
                state = UpdateCoachScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListTrainingGroupsUpdateCoachScreenRoute.Route(
                        clubId = "687997d99f0b5e1be60d78c0",
                        trainingGroupId = "687eab38777dd7173c66fe4b",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

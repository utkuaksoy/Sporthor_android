package com.iamkurtgoz.feature.home.coachList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListScreenRoute

@Composable
internal fun CoachListScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToTrainingGroups: (clubId: String?) -> Unit,
    viewModel: CoachListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("CoachListScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CoachListScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CoachListScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CoachListScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CoachListScreenContract.SideEffect.NavigateToTrainingGroups -> navigateToTrainingGroups(event.clubId)
        }
    }

    CoachListScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoachListScreenScaffold(
    state: CoachListScreenContract.State,
    setEvent: (CoachListScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(CoachListScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Antrenörlerim",
                    )
                },
            )
        },
    ) { padding ->
        CoachListScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(CoachListScreenContract.Event.DismissDialogs)
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
            CoachListScreenScaffold(
                state = CoachListScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListScreenRoute.Route,
                ),
                setEvent = { },
            )
        }
    }
}

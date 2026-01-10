package com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers

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

@Composable
internal fun BlockedUsersScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: BlockedUsersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("BlockedUsersScreen")

    LaunchedEffect(Unit) {
        viewModel.setEvent(BlockedUsersScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is BlockedUsersScreenContract.SideEffect.NavigateUp -> navigateUp()
            is BlockedUsersScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    BlockedUsersScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockedUsersScreenScaffold(
    state: BlockedUsersScreenContract.State,
    setEvent: (BlockedUsersScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon {
                        setEvent.invoke(BlockedUsersScreenContract.Event.NavigateUp)
                    }
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Engellenen Kullanıcılar",
                    )
                },
            )
        },
    ) { padding ->
        BlockedUsersScreenContent(
            modifier = Modifier.padding(padding),
            state = state,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(BlockedUsersScreenContract.Event.DismissDialogs)
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
            BlockedUsersScreenScaffold(
                state = BlockedUsersScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppTheme.appBuildConfigStatePack,
                    appRemoteConfigStatePack = AppTheme.appRemoteConfigStatePack,
                ),
                setEvent = { },
            )
        }
    }
}

package com.iamkurtgoz.feature.home.profile.settings.accountSettings

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

@Composable
internal fun AccountSettingsScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToBlockedUsers: () -> Unit,
    viewModel: AccountSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("AccountSettingsScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(AccountSettingsScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is AccountSettingsScreenContract.SideEffect.NavigateUp -> navigateUp()
            is AccountSettingsScreenContract.SideEffect.PopBackStack -> popBackStack()
            is AccountSettingsScreenContract.SideEffect.NavigateToBlockedUsers -> navigateToBlockedUsers()
        }
    }

    AccountSettingsScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountSettingsScreenScaffold(
    state: AccountSettingsScreenContract.State,
    setEvent: (AccountSettingsScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon {
                        setEvent.invoke(AccountSettingsScreenContract.Event.NavigateUp)
                    }
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Hesap Ayarları", // TODO: Localize
                    )
                },
            )
        },
    ) { padding ->
        AccountSettingsScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
            isPrivate = false,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(AccountSettingsScreenContract.Event.DismissDialogs)
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
            AccountSettingsScreenScaffold(
                state = AccountSettingsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

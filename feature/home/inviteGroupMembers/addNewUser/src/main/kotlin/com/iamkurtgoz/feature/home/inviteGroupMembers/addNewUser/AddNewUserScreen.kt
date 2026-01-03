package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberAddNewUserRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel

@Composable
internal fun AddNewUserScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: AddNewUserViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("AddNewUserScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(AddNewUserScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is AddNewUserScreenContract.SideEffect.NavigateUp -> navigateUp()
            is AddNewUserScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    AddNewUserScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun AddNewUserScreenScaffold(
    state: AddNewUserScreenContract.State,
    setEvent: (AddNewUserScreenContract.Event) -> Unit,
) {
    AppThemeScaffold { padding ->
        AddNewUserScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(AddNewUserScreenContract.Event.DismissDialogs)
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
            AddNewUserScreenScaffold(
                state = AddNewUserScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenInviteGroupMemberAddNewUserRoute(
                        model = HomeScreenAddNewUserScreenNavigationModel(
                            clubId = null,
                            clubName = null,
                            clubLogo = null,
                            groupId = null,
                            groupName = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

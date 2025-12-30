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
package com.iamkurtgoz.feature.home.successAddTrainingGroup

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
import com.iamkurtgoz.core.navigation.HomeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel

@Composable
internal fun SuccessAddTrainingGroupScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToInviteGroupMembersScreen: (HomeScreenInviteGroupMemberScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
    viewModel: SuccessAddTrainingGroupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SuccessAddTrainingGroupScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SuccessAddTrainingGroupScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SuccessAddTrainingGroupScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SuccessAddTrainingGroupScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SuccessAddTrainingGroupScreenContract.SideEffect.NavigateToInviteGroupMembersScreen -> navigateToInviteGroupMembersScreen(event.model, state.route.fromTrainingGroup)
        }
    }

    SuccessAddTrainingGroupScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SuccessAddTrainingGroupScreenScaffold(
    state: SuccessAddTrainingGroupScreenContract.State,
    setEvent: (SuccessAddTrainingGroupScreenContract.Event) -> Unit,
) {
    AppThemeScaffold { padding ->
        SuccessAddTrainingGroupScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SuccessAddTrainingGroupScreenContract.Event.DismissDialogs)
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
            SuccessAddTrainingGroupScreenScaffold(
                state = SuccessAddTrainingGroupScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSuccessAddTrainingGroupRoute(
                        model = HomeScreenSuccessAddTrainingGroupScreenNavigationModel(
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

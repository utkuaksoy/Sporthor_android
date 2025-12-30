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
package com.iamkurtgoz.feature.home.profile.userRelation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
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
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenProfileUserRelationRoute
import com.iamkurtgoz.feature.home.profile.userRelation.component.UserRelationSegments

@Composable
internal fun UserRelationScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: UserRelationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("UserRelationScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(UserRelationScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.profileUserRelationEventBus.observeEventBus {
        viewModel.setEvent(UserRelationScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is UserRelationScreenContract.SideEffect.NavigateUp -> navigateUp()
            is UserRelationScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    UserRelationScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserRelationScreenScaffold(
    state: UserRelationScreenContract.State,
    setEvent: (UserRelationScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            Column {
                AppToolbar.Toolbar(
                    leftContent = {
                        AppToolbarFields.NavigateIcon {
                            setEvent.invoke(UserRelationScreenContract.Event.NavigateUp)
                        }
                    },
                    centerContent = {
                        AppToolbarFields.Title(
                            text = state.navigationRoute.userName,
                        )
                    },
                )

                UserRelationSegments(
                    state = state,
                    setEvent = setEvent,
                    followerCount = state.navigationRoute.userRelationFollowerCount.toString(),
                    followingCount = state.navigationRoute.userRelationFollowingCount.toString(),
                )
            }
        },
    ) { padding ->
        UserRelationScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(UserRelationScreenContract.Event.DismissDialogs)
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
            UserRelationScreenScaffold(
                state = UserRelationScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigationRoute = HomeScreenProfileUserRelationRoute(
                        userRelationFollowerCount = 100,
                        userRelationFollowingCount = 100,
                        userId = "123",
                        userName = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

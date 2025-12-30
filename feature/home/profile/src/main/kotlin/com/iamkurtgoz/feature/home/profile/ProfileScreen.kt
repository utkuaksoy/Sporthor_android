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
package com.iamkurtgoz.feature.home.profile

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
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.feature.home.profile.component.ProfileTopBar

@Composable
internal fun ProfileScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToUserRelation: (userRelationFollowingCount: Int?, userRelationFollowerCount: Int?, userName: String, userId: String) -> Unit,
    navigateToMessagingScreen: (isGroup: Boolean, title: String, channelId: String, userId: String) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToPostDetail: (userId: String?, index: Int?) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("ProfileScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(ProfileScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.profileEventBus.observeEventBus {
        viewModel.setEvent(ProfileScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is ProfileScreenContract.SideEffect.NavigateUp -> navigateUp()
            is ProfileScreenContract.SideEffect.PopBackStack -> popBackStack()
            is ProfileScreenContract.SideEffect.NavigateToEditProfile -> navigateToEditProfile()
            is ProfileScreenContract.SideEffect.NavigateToUserRelation -> navigateToUserRelation(event.userRelationFollowingCount, event.userRelationFollowerCount, event.userName, event.userId)
            is ProfileScreenContract.SideEffect.NavigateToChatMessaging -> navigateToMessagingScreen(event.isGroup, event.title, event.channelId, event.userId)
            is ProfileScreenContract.SideEffect.NavigateToSettings -> navigateToSetting()
            is ProfileScreenContract.SideEffect.NavigateToPostDetail -> navigateToPostDetail(event.userId, event.index)
        }
    }

    ProfileScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun ProfileScreenScaffold(
    state: ProfileScreenContract.State,
    setEvent: (ProfileScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            ProfileTopBar(
                modifier = Modifier
                    .padding(top = AppTheme.configuration.getSafeContentPaddingValues().calculateTopPadding()),
                state = state,
                setEvent = setEvent,
            )
        },
    ) { contentPadding ->
        ProfileScreenContent(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .padding(bottom = AppTheme.appHomeSafeAreaPadding.calculateBottomPadding()),
            contentPadding = contentPadding,
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(ProfileScreenContract.Event.DismissDialogs)
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
            ProfileScreenScaffold(
                state = ProfileScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenProfileRoute(
                        userId = null,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

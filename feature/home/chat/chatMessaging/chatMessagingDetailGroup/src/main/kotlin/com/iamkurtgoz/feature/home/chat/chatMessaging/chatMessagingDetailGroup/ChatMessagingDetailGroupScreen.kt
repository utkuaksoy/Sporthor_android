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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup

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
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingDetailGroupRoute

@Composable
internal fun ChatMessagingDetailGroupScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToAddUser: (groupId: String?) -> Unit,
    navigateToUpdateGroup: (groupId: String?) -> Unit,
    navigateToAttachments: (userId: String?, groupId: String?) -> Unit,
    viewModel: ChatMessagingDetailGroupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("ChatMessagingDetailGroupScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(ChatMessagingDetailGroupScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.chatDetailUserEventBus.observeEventBus {
        viewModel.setEvent(ChatMessagingDetailGroupScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is ChatMessagingDetailGroupScreenContract.SideEffect.NavigateUp -> navigateUp()
            is ChatMessagingDetailGroupScreenContract.SideEffect.PopBackStack -> popBackStack()
            is ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToAddUser -> navigateToAddUser(event.groupId)
            is ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToUpdateGroup -> navigateToUpdateGroup(event.groupId)
            is ChatMessagingDetailGroupScreenContract.SideEffect.NavigateToAttachments -> navigateToAttachments(event.userId, event.groupId)
        }
    }

    ChatMessagingDetailGroupScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun ChatMessagingDetailGroupScreenScaffold(
    state: ChatMessagingDetailGroupScreenContract.State,
    setEvent: (ChatMessagingDetailGroupScreenContract.Event) -> Unit,
) {
    AppThemeScaffold { padding ->
        ChatMessagingDetailGroupScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.DismissDialogs)
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
            ChatMessagingDetailGroupScreenScaffold(
                state = ChatMessagingDetailGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingDetailGroupRoute(
                        userId = "",
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

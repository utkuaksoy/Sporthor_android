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
package com.iamkurtgoz.feature.home.chat.newChat.newGroupChat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.iamkurtgoz.core.commonui.component.photoPicker.PhotoPicker
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

@Composable
internal fun NewGroupChatScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToMessagingScreen: (isGroup: Boolean, title: String, channelId: String, userId: String) -> Unit,
    viewModel: NewGroupChatViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("GroupChatScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(NewGroupChatScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is NewGroupChatScreenContract.SideEffect.NavigateUp -> navigateUp()
            is NewGroupChatScreenContract.SideEffect.PopBackStack -> popBackStack()
            is NewGroupChatScreenContract.SideEffect.NavigateToMessagingScreen -> navigateToMessagingScreen(event.isGroup, event.title, event.channelId, event.userId)
        }
    }

    NewGroupChatScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewGroupChatScreenScaffold(
    state: NewGroupChatScreenContract.State,
    setEvent: (NewGroupChatScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(NewGroupChatScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Yeni Grup Sohbeti", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            if (state.selectedUserList.isNotEmpty()) {
                AppButton.PrimaryLarge(
                    text = "Grubu Oluştur", // TODO: Localize
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium)
                        .imePadding()
                        .navigationBarsPadding(),
                    onClick = {
                        setEvent.invoke(NewGroupChatScreenContract.Event.GenerateGroupChat)
                    },
                )
            }
        },
    ) { padding ->
        NewGroupChatScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(NewGroupChatScreenContract.Event.DismissDialogs)
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
                setEvent.invoke(NewGroupChatScreenContract.Event.SetSelectedImage(it))
                setEvent.invoke(NewGroupChatScreenContract.Event.DismissDialogs)
            },
            onDismissRequest = {
                setEvent.invoke(NewGroupChatScreenContract.Event.DismissDialogs)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            NewGroupChatScreenScaffold(
                state = NewGroupChatScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

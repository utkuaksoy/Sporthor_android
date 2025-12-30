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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.iamkurtgoz.core.commonui.component.photoPicker.PhotoPicker
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.UpdateGroupScreenRoute

@Composable
internal fun UpdateGroupScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: UpdateGroupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("UpdateGroupScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(UpdateGroupScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is UpdateGroupScreenContract.SideEffect.NavigateUp -> navigateUp()
            is UpdateGroupScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    UpdateGroupScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun UpdateGroupScreenScaffold(
    state: UpdateGroupScreenContract.State,
    setEvent: (UpdateGroupScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                text = "Kaydet",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding()),
                onClick = {
                    setEvent.invoke(
                        UpdateGroupScreenContract.Event.UpdateChatGroup(
                            groupImage = state.groupImage,
                            groupName = state.groupName,
                        ),
                    )
                },
            )
        },
    ) { padding ->
        UpdateGroupScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(UpdateGroupScreenContract.Event.DismissDialogs)
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
                setEvent.invoke(UpdateGroupScreenContract.Event.SetSelectedImage(it))
                setEvent.invoke(UpdateGroupScreenContract.Event.DismissDialogs)
            },
            onDismissRequest = {
                setEvent.invoke(UpdateGroupScreenContract.Event.DismissDialogs)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            UpdateGroupScreenScaffold(
                state = UpdateGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = UpdateGroupScreenRoute(
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

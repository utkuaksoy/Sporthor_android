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
package com.iamkurtgoz.feature.home.profile.profileEdit

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
internal fun ProfileEditScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToEditProfileSelectBranch: () -> Unit,
    navigateToSelectUserRole: () -> Unit,
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    TrackedScreen("EditScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(ProfileEditScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.profileEditEventBus.observeEventBus {
        viewModel.setEvent(ProfileEditScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is ProfileEditScreenContract.SideEffect.NavigateUp -> navigateUp()
            is ProfileEditScreenContract.SideEffect.PopBackStack -> popBackStack()
            is ProfileEditScreenContract.SideEffect.NavigateToProfileEditSelectBranch -> navigateToEditProfileSelectBranch()
            is ProfileEditScreenContract.SideEffect.NavigateToSelectUserRole -> navigateToSelectUserRole()
            is ProfileEditScreenContract.SideEffect.ShowSuccessToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ProfileEditScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileEditScreenScaffold(
    state: ProfileEditScreenContract.State,
    setEvent: (ProfileEditScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(ProfileEditScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Profili Düzenle", // TODO: Localize
                    )
                },
                rightContent = {
                    TextButton(
                        content = {
                            Text(
                                text = "Rol Güncelle",
                                color = AppTheme.colors.generalColors.textPrimary,
                            )
                        },
                        onClick = {
                            setEvent.invoke(ProfileEditScreenContract.Event.NavigateToSelectUserRole)
                        },
                    )
                },
            )
        },
    ) { padding ->
        if (!state.isLoading) {
            ProfileEditScreenContent(
                modifier = Modifier
                    .padding(padding),
                state = state,
                setEvent = setEvent,
            )
        }

        state.alertDialogModel?.Alert {
            setEvent.invoke(ProfileEditScreenContract.Event.DismissDialogs)
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
                setEvent.invoke(ProfileEditScreenContract.Event.SetSelectedImage(it))
                setEvent.invoke(ProfileEditScreenContract.Event.DismissDialogs)
            },
            onDismissRequest = {
                setEvent.invoke(ProfileEditScreenContract.Event.DismissDialogs)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ProfileEditScreenScaffold(
                state = ProfileEditScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

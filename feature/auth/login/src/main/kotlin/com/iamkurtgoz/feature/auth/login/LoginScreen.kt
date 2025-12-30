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
package com.iamkurtgoz.feature.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.commonui.state.keyboardVisibility
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.extension.ifFalse
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R

@Composable
internal fun LoginScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val localKeyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = LocalFocusManager.current

    TrackedScreen("LoginScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(LoginScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is LoginScreenContract.SideEffect.NavigateUp -> navigateUp()
            is LoginScreenContract.SideEffect.PopBackStack -> popBackStack()
            is LoginScreenContract.SideEffect.NavigateToRegister -> navigateToRegister()
            is LoginScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is LoginScreenContract.SideEffect.NavigateToForgotPassword -> navigateToForgotPassword()
            is LoginScreenContract.SideEffect.HideKeyboard -> localKeyboardController?.hide()
            is LoginScreenContract.SideEffect.ClearTextFieldFocus -> focusRequester.clearFocus(force = true)
        }
    }

    LoginScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun LoginScreenScaffold(
    state: LoginScreenContract.State,
    setEvent: (LoginScreenContract.Event) -> Unit,
) {
    val isKeyboardShow by keyboardVisibility()

    AppThemeScaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppTheme.spacing.spacingHuge)
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.loginscreen_label_dont_have_an_account),
                        style = AppTheme.typography.bodyMediumCompact,
                        color = AppTheme.colors.generalColors.textSecondary,
                    )

                    Box(
                        modifier = Modifier
                            .clip(shape = AppTheme.shapes.radiusCircle)
                            .clickable {
                                setEvent.invoke(LoginScreenContract.Event.NavigateToRegister)
                            },
                    ) {
                        Text(
                            text = stringResource(R.string.loginscreen_label_register_now),
                            modifier = Modifier
                                .padding(horizontal = AppTheme.spacing.spacingSmall),
                            style = AppTheme.typography.labelMedium,
                            color = AppTheme.colors.generalColors.textPrimary,
                        )
                    }
                }

                AppButton.PrimaryLarge(
                    text = stringResource(R.string.button_login_button),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge)
                        .padding(bottom = AppTheme.spacing.spacingMedium)
                        .ifTrue(isKeyboardShow) {
                            this.imePadding()
                        }
                        .ifFalse(isKeyboardShow) {
                            this.padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                        },
                    enabled = state.buttonActive,
                    onClick = {
                        setEvent.invoke(LoginScreenContract.Event.Login)
                    },
                )
            }
        },
    ) { padding ->
        LoginScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(LoginScreenContract.Event.DismissDialogs)
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
            LoginScreenScaffold(
                state = LoginScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

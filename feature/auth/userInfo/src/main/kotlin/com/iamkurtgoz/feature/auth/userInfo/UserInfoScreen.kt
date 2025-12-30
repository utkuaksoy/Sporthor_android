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
package com.iamkurtgoz.feature.auth.userInfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.iamkurtgoz.core.navigation.AuthUserInfoScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userInfo.fakeAuthUserInfoScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.userName.AuthUserNameScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun UserInfoScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToUserName: (AuthUserNameScreenNavigateModel) -> Unit,
    viewModel: UserInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("UserInfoScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(UserInfoScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is UserInfoScreenContract.SideEffect.NavigateUp -> navigateUp()
            is UserInfoScreenContract.SideEffect.PopBackStack -> popBackStack()
            is UserInfoScreenContract.SideEffect.NavigateToUserName -> navigateToUserName(event.model)
        }
    }

    UserInfoScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun UserInfoScreenScaffold(
    state: UserInfoScreenContract.State,
    setEvent: (UserInfoScreenContract.Event) -> Unit,
) {
    val isKeyboardShow by keyboardVisibility()

    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                text = stringResource(resourcesR.string.button_continue_button),
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
                    setEvent.invoke(UserInfoScreenContract.Event.SaveUserInfo)
                },
            )
        },
    ) { padding ->
        UserInfoScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(UserInfoScreenContract.Event.DismissDialogs)
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
            UserInfoScreenScaffold(
                state = UserInfoScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = AuthUserInfoScreenRoute(
                        model = fakeAuthUserInfoScreenNavigateModel,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

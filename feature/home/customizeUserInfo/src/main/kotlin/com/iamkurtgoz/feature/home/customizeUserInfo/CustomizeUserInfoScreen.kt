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
package com.iamkurtgoz.feature.home.customizeUserInfo

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.types.CustomizePageType

@Composable
internal fun CustomizeUserInfoScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToOnboarding: () -> Unit,
    viewModel: CustomizeUserInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("CustomizeUserInfoScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CustomizeUserInfoScreenContract.Event.Initialize)
    }

    BackHandler(enabled = state.currentPageIndex != CustomizePageType.Welcome) {
        viewModel.setEvent(CustomizeUserInfoScreenContract.Event.PreviousPage)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CustomizeUserInfoScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CustomizeUserInfoScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CustomizeUserInfoScreenContract.SideEffect.NavigateToOnboarding -> navigateToOnboarding()
        }
    }

    CustomizeUserInfoScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun CustomizeUserInfoScreenScaffold(
    state: CustomizeUserInfoScreenContract.State,
    setEvent: (CustomizeUserInfoScreenContract.Event) -> Unit,
) {
    val context: Context = LocalContext.current
    val title: String = remember(state.currentPageIndex) {
        when (state.currentPageIndex) {
            CustomizePageType.Welcome -> context.getString(resourcesR.string.button_lets_start_now)
            CustomizePageType.UserInfo -> context.getString(resourcesR.string.button_finish)
            else -> context.getString(resourcesR.string.button_continue_button)
        }
    }

    AppThemeScaffold(
        bottomBar = {
            AppButton.PrimaryLarge(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingMedium),
                enabled = !state.buttonDisabled,
                onClick = {
                    setEvent.invoke(CustomizeUserInfoScreenContract.Event.NextPage)
                },
            )
        },
    ) { padding ->
        CustomizeUserInfoScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(CustomizeUserInfoScreenContract.Event.DismissDialogs)
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
            CustomizeUserInfoScreenScaffold(
                state = CustomizeUserInfoScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

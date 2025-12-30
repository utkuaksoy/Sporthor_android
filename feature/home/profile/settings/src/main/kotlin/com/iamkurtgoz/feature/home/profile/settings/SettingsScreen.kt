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
package com.iamkurtgoz.feature.home.profile.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SettingsScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAboutUs: () -> Unit,
    navigateToAccountSettings: () -> Unit,
    navigateToWebView: (routeType: HomeScreenWebViewScreenNavigateModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SettingsScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SettingsScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SettingsScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SettingsScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SettingsScreenContract.SideEffect.NavigateToAboutUs -> navigateToAboutUs()
            is SettingsScreenContract.SideEffect.NavigateToAccountSettings -> navigateToAccountSettings()
            is SettingsScreenContract.SideEffect.NavigateToWebView -> navigateToWebView(event.routeType)
        }
    }

    SettingsScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenScaffold(
    state: SettingsScreenContract.State,
    setEvent: (SettingsScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(SettingsScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Ayarlar", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(bottom = AppTheme.spacing.spacingHuge),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_sporthor_logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .width((AppTheme.configuration.getScreenWidth() * AppDefaults.DECIMAL_0_3).dp),
                )

                Text(
                    text = "Sporthor © 2025. Tüm hakları saklıdır", // TODO: Localize
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.generalColors.textDisabled,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                )
            }
        },
    ) { padding ->
        SettingsScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SettingsScreenContract.Event.DismissDialogs)
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
            SettingsScreenScaffold(
                state = SettingsScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

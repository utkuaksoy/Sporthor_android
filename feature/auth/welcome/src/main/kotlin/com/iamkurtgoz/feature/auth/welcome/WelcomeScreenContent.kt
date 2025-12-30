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
package com.iamkurtgoz.feature.auth.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourceR
import com.iamkurtgoz.feature.auth.welcome.component.WelcomeScreenBackgroundView

@Composable
internal fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    setEvent: (WelcomeScreenContract.Event) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = stringResource(resourceR.string.welcomescreen_label_welcome_app_title),
                color = AppTheme.colors.generalColors.textWhite,
                style = AppTheme.typography.heading03,
            )

            Text(
                text = stringResource(resourceR.string.welcomescreen_label_welcome_app_sub_title),
                color = AppTheme.colors.generalColors.textWhiteSecondary,
                style = AppTheme.typography.bodyLargeCompact,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                .padding(bottom = AppTheme.spacing.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            AppButton.PrimaryLarge(
                text = stringResource(resourceR.string.button_register_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                onClick = {
                    setEvent.invoke(WelcomeScreenContract.Event.NavigateToRegister)
                },
            )

            AppButton.SecondaryWhiteLarge(
                text = stringResource(resourceR.string.button_login_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                onClick = {
                    setEvent.invoke(WelcomeScreenContract.Event.NavigateToLogin)
                },
            )

            TextButton(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall),
                content = {
                    Text(
                        text = stringResource(resourceR.string.button_discover_application),
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.generalColors.textWhite,
                    )
                },
                onClick = {
                    setEvent.invoke(WelcomeScreenContract.Event.NavigateToHome)
                },
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            WelcomeScreenBackgroundView()

            WelcomeScreenContent(
                setEvent = { },
            )
        }
    }
}

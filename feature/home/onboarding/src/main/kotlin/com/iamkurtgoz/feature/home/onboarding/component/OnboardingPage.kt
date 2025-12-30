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
package com.iamkurtgoz.feature.home.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.onboarding.OnboardingScreenContract
import com.iamkurtgoz.feature.home.onboarding.fake.MockOnBoarding

@Composable
internal fun OnboardingPage(
    state: OnboardingScreenContract.State,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        AppAsyncImageLoader.Load(
            data = state.pageList.getOrNull(index)?.image,
            contentDescription = "img_onboarding_welcome",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(AppDefaults.ASPECT_RATIO_SQUARE),
        )

        Text(
            text = state.pageList.getOrNull(index)?.title ?: "",
            style = AppTheme.typography.heading04,
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingHuge),
        )

        Text(
            text = state.pageList.getOrNull(index)?.description ?: "",
            style = AppTheme.typography.bodyLargeCompact,
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingSmall)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            OnboardingPage(
                state = OnboardingScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    pageList = MockOnBoarding.list,
                ),
                index = AppDefaults.ZERO,
            )
        }
    }
}

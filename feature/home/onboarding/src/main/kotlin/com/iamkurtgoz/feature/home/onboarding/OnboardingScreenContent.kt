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
package com.iamkurtgoz.feature.home.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.onboarding.component.OnboardingPage
import com.iamkurtgoz.feature.home.onboarding.fake.MockOnBoarding

@Composable
internal fun OnboardingScreenContent(
    state: OnboardingScreenContract.State,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxSize(),
        state = pagerState,
        pageSpacing = AppTheme.spacing.spacingMedium,
    ) { index ->
        OnboardingPage(
            state = state,
            index = index,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            OnboardingScreenContent(
                state = OnboardingScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    pageList = MockOnBoarding.list,
                ),
                pagerState = rememberPagerState(
                    pageCount = {
                        MockOnBoarding.list.size
                    },
                ),
            )
        }
    }
}

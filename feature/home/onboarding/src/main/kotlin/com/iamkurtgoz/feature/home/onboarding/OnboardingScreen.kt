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

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
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
import com.iamkurtgoz.feature.home.onboarding.fake.MockOnBoarding

@Composable
internal fun OnboardingScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToUserTeams: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("OnboardingScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(OnboardingScreenContract.Event.Initialize)
    }

    BackHandler(enabled = state.currentPageIndex != AppDefaults.ZERO) {
        viewModel.setEvent(OnboardingScreenContract.Event.PreviousPage)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is OnboardingScreenContract.SideEffect.NavigateUp -> navigateUp()
            is OnboardingScreenContract.SideEffect.PopBackStack -> popBackStack()
            is OnboardingScreenContract.SideEffect.NavigateToUserTeams -> navigateToUserTeams()
            is OnboardingScreenContract.SideEffect.NavigateToHome -> navigateToHome()
        }
    }

    OnboardingScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun OnboardingScreenScaffold(
    state: OnboardingScreenContract.State,
    setEvent: (OnboardingScreenContract.Event) -> Unit,
) {
    val context: Context = LocalContext.current
    val pagerState = rememberPagerState(
        pageCount = {
            state.pageList.size
        },
    )

    LaunchedEffect(state.currentPageIndex) {
        pagerState.animateScrollToPage(state.currentPageIndex)
    }

    AppThemeScaffold(
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge)
                        .padding(bottom = AppTheme.spacing.spacingLarge),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    repeat(state.pageList.size) { index ->
                        val isSelected = remember(pagerState.currentPage) { pagerState.currentPage == index }
                        val color = when (isSelected) {
                            true -> AppTheme.colors.generalColors.foregroundBlack
                            false -> AppTheme.colors.generalColors.foregroundBlack.copy(
                                alpha = AppDefaults.COMPOSE_COLORS_TWO_TENTH_ALPHA,
                            )
                        }
                        val width: Dp = when (isSelected) {
                            true -> AppTheme.dimens.dp20
                            false -> AppTheme.dimens.dp8
                        }
                        val shape = when (isSelected) {
                            true -> AppTheme.shapes.radiusPentaExtraLarge
                            false -> AppTheme.shapes.radiusCircle
                        }

                        Box(
                            modifier = Modifier
                                .padding(
                                    start = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingNone else AppTheme.spacing.spacingSmall,
                                )
                                .clip(shape)
                                .background(color)
                                .width(width)
                                .height(AppTheme.dimens.dp8),

                        )
                    }
                }

                AppButton.SecondaryLarge(
                    text = context.getString(resourcesR.string.button_continue_button),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge)
                        .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                        .padding(bottom = AppTheme.spacing.spacingMedium),
                    onClick = {
                        setEvent.invoke(OnboardingScreenContract.Event.NextPage)
                    },
                )
            }
        },
    ) { padding ->
        OnboardingScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            pagerState = pagerState,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(OnboardingScreenContract.Event.DismissDialogs)
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
            OnboardingScreenScaffold(
                state = OnboardingScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    pageList = MockOnBoarding.list,
                ),
                setEvent = { },
            )
        }
    }
}

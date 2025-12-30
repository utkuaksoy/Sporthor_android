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
package com.iamkurtgoz.feature.home.share.complete

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.CustomMediaType

@Composable
internal fun CompleteScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    popBackStackToDashboard: () -> Unit,
    navigateToSelectAddressScreen: () -> Unit,
    viewModel: CompleteViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("CompleteScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CompleteScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CompleteScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CompleteScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CompleteScreenContract.SideEffect.PopBackStackToDashboard -> popBackStackToDashboard()
            is CompleteScreenContract.SideEffect.NavigateToSelectAddressScreen -> navigateToSelectAddressScreen()
        }
    }

    CompleteScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun CompleteScreenScaffold(
    state: CompleteScreenContract.State,
    setEvent: (CompleteScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(AppTheme.colors.generalColors.foregroundPrimary)
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(resourcesR.drawable.img_share_background),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )

        AppThemeScaffold(
            containerColor = AppTheme.colors.generalColors.transparent,
        ) { padding ->
            CompleteScreenContent(
                modifier = Modifier
                    .padding(padding),
                state = state,
                setEvent = setEvent,
            )

            state.alertDialogModel?.Alert {
                setEvent.invoke(CompleteScreenContract.Event.DismissDialogs)
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
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CompleteScreenScaffold(
                state = CompleteScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenShareCompleteRoute(
                        routeType = HomeScreenShareCompleteScreenNavigateModel(
                            shareType = HomeScreenShareCompleteShareTypeScreenNavigateModel.CreatePost,
                            selectedMediaList = listOf(
                                HomeScreenShareCompleteScreenNavigateModelMediaItem(
                                    customMediaType = CustomMediaType.IMAGE,
                                    uri = Uri.EMPTY,
                                ),
                            ),
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

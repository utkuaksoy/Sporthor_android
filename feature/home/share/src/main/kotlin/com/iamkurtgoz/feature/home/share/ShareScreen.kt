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
package com.iamkurtgoz.feature.home.share

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.share.domain.model.LocalMediaUIModel
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun ShareScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToCameraXScreen: () -> Unit,
    navigateToShareCompleteScreen: (routeType: HomeScreenShareCompleteScreenNavigateModel) -> Unit,
    viewModel: ShareViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val localMediaPagingFlow = viewModel.localMediaPagingFlow.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyGridState()

    TrackedScreen("ShareScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(ShareScreenContract.Event.Initialize)
    }

    LaunchedEffect(localMediaPagingFlow.loadState) {
        viewModel.setEvent(ShareScreenContract.Event.SetPagingLoadState(localMediaPagingFlow.loadState))
    }

    AppTheme.appEventBus.shareScreenEventBus.observeEventBus {
        viewModel.setEvent(ShareScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is ShareScreenContract.SideEffect.NavigateUp -> navigateUp()
            is ShareScreenContract.SideEffect.PopBackStack -> popBackStack()
            is ShareScreenContract.SideEffect.ScrollToTop -> {
                lazyGridState.scrollToItem(AppDefaults.ZERO)
            }
            is ShareScreenContract.SideEffect.NavigateToCameraXScreen -> navigateToCameraXScreen()
            is ShareScreenContract.SideEffect.NavigateToShareCompleteScreen -> navigateToShareCompleteScreen(event.routeType)
        }
    }

    ShareScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
        localMediaPagingFlow = localMediaPagingFlow,
        lazyGridState = lazyGridState,
    )
}

@Composable
private fun ShareScreenScaffold(
    state: ShareScreenContract.State,
    setEvent: (ShareScreenContract.Event) -> Unit,
    localMediaPagingFlow: LazyPagingItems<LocalMediaUIModel>,
    lazyGridState: LazyGridState,
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
            ShareScreenContent(
                modifier = Modifier
                    .padding(padding),
                state = state,
                setEvent = setEvent,
                localMediaPagingFlow = localMediaPagingFlow,
                lazyGridState = lazyGridState,
            )

            state.alertDialogModel?.Alert {
                setEvent.invoke(ShareScreenContract.Event.DismissDialogs)
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
            ShareScreenScaffold(
                state = ShareScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenShareRoute(
                        routeType = HomeScreenShareRouteScreenNavigateModel.CreatePost,
                    ),
                ),
                setEvent = { },
                localMediaPagingFlow = flowOf(PagingData.from(emptyList<LocalMediaUIModel>())).collectAsLazyPagingItems(),
                lazyGridState = rememberLazyGridState(),
            )
        }
    }
}

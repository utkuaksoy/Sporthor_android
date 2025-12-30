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
package com.iamkurtgoz.feature.home.profile.postDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.iamkurtgoz.core.navigation.HomeScreenPostDetailRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.feature.home.profile.postDetail.component.comment.CommentDialog

@Composable
internal fun PostDetailScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: PostDetailViewModel = hiltViewModel(),
    navigateToMediaViewer: (routeType: HomeScreenMediaViewerScreenNavigateModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("PostDetailScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(PostDetailScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is PostDetailScreenContract.SideEffect.NavigateUp -> navigateUp()
            is PostDetailScreenContract.SideEffect.PopBackStack -> popBackStack()
            is PostDetailScreenContract.SideEffect.NavigateToMediaViewer -> navigateToMediaViewer(event.routeType)
        }
    }

    PostDetailScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun PostDetailScreenScaffold(
    state: PostDetailScreenContract.State,
    setEvent: (PostDetailScreenContract.Event) -> Unit,
) {
    AppThemeScaffold { padding ->
        PostDetailScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
            contentPadding = PaddingValues(),
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(PostDetailScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        AnimatedVisibility(
            visible = state.commentDialogShowPostId != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            CommentDialog(
                postId = state.commentDialogShowPostId,
                onDismissRequest = {
                    setEvent.invoke(PostDetailScreenContract.Event.DismissDialogs)
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
            PostDetailScreenScaffold(
                state = PostDetailScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenPostDetailRoute(
                        userId = null,
                        index = null,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

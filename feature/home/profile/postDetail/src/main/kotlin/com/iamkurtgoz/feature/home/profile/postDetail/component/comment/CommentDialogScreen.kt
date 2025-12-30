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
package com.iamkurtgoz.feature.home.profile.postDetail.component.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.profile.postDetail.component.comment.component.CommentDialogBottomBar

@Composable
internal fun CommentDialogScreen(
    postId: String?,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { },
    viewModel: CommentDialogViewModel = hiltViewModel(key = postId),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = postId) {
        viewModel.setEvent(CommentDialogScreenContract.Event.SetPostId(postId = postId))
    }

    DisposableEffect(postId) {
        onDispose {
            viewModel.setEvent(CommentDialogScreenContract.Event.SetPostId(postId = null))
        }
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CommentDialogScreenContract.SideEffect.DismissDialog -> onDismissRequest()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.configuration.getScreenHeightDp() * AppDefaults.SCALE_0_6),
    ) {
        CommentDialogContent(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL),
            state = state,
            setEvent = viewModel::setEvent,
        )

        CommentDialogBottomBar(
            state = state,
            setEvent = viewModel::setEvent,
        )
    }

    AnimatedVisibility(
        visible = state.isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        AppLoadingDialog()
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CommentDialogScreen(
                postId = null,
            )
        }
    }
}

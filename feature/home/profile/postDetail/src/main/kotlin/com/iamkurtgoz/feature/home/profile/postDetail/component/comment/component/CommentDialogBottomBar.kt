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
package com.iamkurtgoz.feature.home.profile.postDetail.component.comment.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.profile.postDetail.component.comment.CommentDialogScreenContract
import kotlinx.coroutines.launch

@Composable
internal fun CommentDialogBottomBar(
    state: CommentDialogScreenContract.State,
    setEvent: (CommentDialogScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentPreferenceState by AppTheme.appPreferences.currentPreferenceState.collectAsStateWithLifecycle(initialValue = null)
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .height(AppTheme.dimens.dp64),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserImageView(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp48),
            data = currentPreferenceState?.profilePhoto,
        )

        AppTextField.MessageField(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusTarget()
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusRequester(focusRequester = focusRequester)
                .padding(start = AppTheme.spacing.spacingSmall)
                .padding(end = AppTheme.spacing.spacingMedium),
            value = state.textComment.value,
            onValueChange = {
                setEvent(CommentDialogScreenContract.Event.SetComment(it))
            },
            trailingIcon = if (state.textComment.isEmpty) resourcesR.drawable.img_chat_message_send_disabled else resourcesR.drawable.img_chat_message_send_enabled,
            trailingIconClick = {
                setEvent(CommentDialogScreenContract.Event.SendComment)
            },
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            CommentDialogBottomBar(
                state = CommentDialogScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

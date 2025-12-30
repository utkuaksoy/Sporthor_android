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
package com.iamkurtgoz.feature.home.dashboard.component.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.dashboard.component.comment.component.CommentDialogCommentUserRow
import com.iamkurtgoz.feature.home.dashboard.component.comment.component.CommentDialogNoComment
import com.iamkurtgoz.feature.home.dashboard.domain.model.CommentUIModel

@Composable
internal fun CommentDialogContent(
    state: CommentDialogScreenContract.State,
    setEvent: (CommentDialogScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            Text(
                text = "Yorumlar", // TODO: Localize
                style = AppTheme.typography.heading06,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setEvent.invoke(CommentDialogScreenContract.Event.SendComment)
                    },
                textAlign = TextAlign.Center,
            )
        }

        itemsIndexed(
            items = state.comments,
            key = { index: Int, item: CommentUIModel ->
                "$index${item.id}${item.text}$item"
            },
            itemContent = { index: Int, item: CommentUIModel ->
                CommentDialogCommentUserRow(
                    userHeaderData = null,
                    userName = null,
                    comment = item.text,
                )
            },
        )

        if (!state.isLoading && state.postId != null && state.comments.isEmpty()) {
            item {
                CommentDialogNoComment(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingLarge),
                )
            }
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            CommentDialogContent(
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

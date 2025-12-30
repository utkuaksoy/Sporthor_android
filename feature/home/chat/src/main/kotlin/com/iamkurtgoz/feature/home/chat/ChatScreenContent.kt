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
package com.iamkurtgoz.feature.home.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.feature.home.chat.component.ChatRow
import com.iamkurtgoz.feature.home.chat.component.EmptyChatState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatScreenContent(
    state: ChatScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ChatScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        InfiniteList(
            itemList = if (state.textSearch.isNotEmpty) state.chatFilteredList else state.chatList,
            loadMore = {
                setEvent.invoke(ChatScreenContract.Event.FetchChatList(FetchParam.NEXT_PAGE))
            },
            refresh = {
                setEvent.invoke(ChatScreenContract.Event.FetchChatList(FetchParam.RELOAD))
            },
            contentPadding = PaddingValues(
                bottom = AppTheme.appHomeSafeAreaPadding.calculateBottomPadding(),
            ),
            isRefreshing = state.paginationReloading,
            paginationLoading = state.paginationLoading,
            rowContent = { _, item ->
                ChatRow(
                    modifier = Modifier,
                    item = item,
                    setEvent = setEvent,
                )
            },
        )

        if (state.chatList.isEmpty()) {
            EmptyChatState(
                modifier = Modifier
                    .padding(top = AppTheme.dimens.dp64)
                    .clickable {
                        setEvent.invoke(ChatScreenContract.Event.Initialize)
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
            ChatScreenContent(
                state = ChatScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.chat.chatMessaging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.extensions.DateFormat
import com.iamkurtgoz.core.common.extensions.toString
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingRoute
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.feature.home.chat.chatMessaging.component.ChatRowIncoming
import com.iamkurtgoz.feature.home.chat.chatMessaging.component.ChatRowOutgoing
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatListItem
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemFromUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.withDateHeaders
import kotlinx.collections.immutable.toPersistentList
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatMessagingScreenContent(
    state: ChatMessagingScreenContract.State,
    lazyListState: LazyListState,
    setEvent: (ChatMessagingScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentPreferenceState by AppTheme.appPreferences.currentPreferenceState.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val otherUserId = state.route.userId
    val currentUserId = currentPreferenceState?.userId
        ?: state.users.firstOrNull { it.isCurrentUser == true }?.id
    val currentUserName = state.users.firstOrNull { it.id == currentUserId }?.name
        ?: state.users.firstOrNull { it.isCurrentUser == true }?.name
    val otherUserName = state.users.firstOrNull { it.id == otherUserId }?.name ?: state.route.title

    InfiniteList(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.generalColors.backgroundWeak100),
        listState = lazyListState,
        itemList = state.groupedMessages,
        loadMore = {
            setEvent.invoke(ChatMessagingScreenContract.Event.FetchChatMessagingList(FetchParam.NEXT_PAGE))
        },
        refresh = {
            setEvent.invoke(ChatMessagingScreenContract.Event.FetchChatMessagingList(FetchParam.RELOAD))
        },
        contentPadding = PaddingValues(
            bottom = AppTheme.appHomeSafeAreaPadding.calculateBottomPadding(),
        ),
        isRefreshing = state.paginationReloading,
        paginationLoading = state.paginationLoading,
        reverseLayout = true,
        rowContent = { _, item ->
            if (item is ChatListItem.DateHeader) {
                Text(
                    text = item.dateLabel,
                    style = AppTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = AppTheme.dimens.sp10,
                        color = AppTheme.colors.generalColors.contentSoft600,
                    ),
                    modifier = Modifier
                        .padding(vertical = AppTheme.spacing.spacingMedium)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            } else if (item is ChatListItem.MessageItem) {
                val fromId = item.message.from?.id
                val fromName = item.message.from?.name
                val isOutgoing = if (state.route.isGroup) {
                    currentUserId != null && fromId == currentUserId
                } else {
                    when {
                        currentUserId != null && !fromId.isNullOrBlank() -> fromId == currentUserId
                        currentUserId != null && !currentUserName.isNullOrBlank() && !fromName.isNullOrBlank() -> {
                            fromName == currentUserName
                        }
                        currentUserId == null && otherUserId != null && !fromId.isNullOrBlank() -> fromId != otherUserId
                        else -> false
                    }
                }
                val timeText = item.message.shortDate?.takeIf { it.isNotBlank() }
                    ?: item.message.sendDate?.toString(format = DateFormat.TIME)
                if (isOutgoing) {
                ChatRowOutgoing(
                    isParentMessageRow = item.isParentMessageRow,
                    imageData = item.message.from?.image,
                    userName = currentUserName ?: item.message.from?.name,
                    message = item.message.content,
                    time = timeText,
                    messageType = item.message.messageType,
                    extension = item.message.fileExtension,
                    setEvent = setEvent,
                    modifier = Modifier
                        .padding(end = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingSmallest),
                )
                } else {
                ChatRowIncoming(
                    isParentMessageRow = item.isParentMessageRow,
                    imageData = item.message.from?.image,
                    userName = item.message.from?.name,
                    message = item.message.content,
                    time = timeText,
                    messageType = item.message.messageType,
                    extension = item.message.fileExtension,
                    setEvent = setEvent,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingSmallest),
                )
                }
            }
        },
    )
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val list = listOf(
        ChatMessageItemUIModel(
            content = "Hello",
            fileExtension = "",
            from = ChatMessageItemFromUIModel(
                id = UUID.randomUUID().toString(),
                image = "",
                name = "Hello",
            ),
            id = UUID.randomUUID().toString(),
            messageType = SignalRMessageType.TEXT,
            sendDate = LocalDateTime.now(),
            shortDate = "2 ay once",
        ),
        ChatMessageItemUIModel(
            content = "World",
            fileExtension = "",
            from = ChatMessageItemFromUIModel(
                id = UUID.randomUUID().toString(),
                image = "",
                name = "World",
            ),
            id = UUID.randomUUID().toString(),
            messageType = SignalRMessageType.TEXT,
            sendDate = LocalDateTime.now(),
            shortDate = "2 ay once",
        ),
    )
    AppTheme {
        AppThemeSurface {
            ChatMessagingScreenContent(
                state = ChatMessagingScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingRoute(
                        isGroup = false,
                        title = "",
                        channelId = "",
                        userId = "",
                    ),
                    messages = list.toPersistentList(),
                    groupedMessages = list.withDateHeaders().toPersistentList(),
                ),
                setEvent = { },
                lazyListState = rememberLazyListState(),
            )
        }
    }
}

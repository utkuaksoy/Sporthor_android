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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingDetailGroupRoute
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.component.ChatMessagingDetailGroupBottomRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.component.ChatMessagingDetailGroupTopRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.component.chatMessagingDetailGroupMembersRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.UserRelationUIItemModel

@Composable
internal fun ChatMessagingDetailGroupScreenContent(
    state: ChatMessagingDetailGroupScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ChatMessagingDetailGroupScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        state.isLoading
        item {
            ChatMessagingDetailGroupTopRow(
                imageUrlData = state.groupDetail?.groupImageUrl,
                setEvent = setEvent,
                groupId = state.route.groupId,
                groupName = state.groupDetail?.groupName,
                memberCount = state.groupDetail?.members?.size,
            )
        }

        chatMessagingDetailGroupMembersRow(
            users = state.groupDetail?.members
                ?.map { member ->
                    UserRelationUIItemModel(
                        id = member?.id,
                        name = member?.name,
                        username = member?.username,
                        summary = member?.summary,
                        imageUrl = member?.imageUrl,
                        isFollow = member?.isFollow,
                        isCurrentUser = member?.isCurrentUser,
                    )
                }.orEmpty(),
            setEvent = setEvent,
            isFollow = state.groupDetail?.members
                ?.firstOrNull { it?.id == state.route.userId }
                ?.isFollow ?: false,
            groupId = state.route.groupId,
        )

        item {
            ChatMessagingDetailGroupBottomRow(
                attachmentsCount = state.groupDetail?.mediaCount,
                setEvent = setEvent,
                groupId = state.route.groupId,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ChatMessagingDetailGroupScreenContent(
                state = ChatMessagingDetailGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingDetailGroupRoute(
                        userId = "",
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

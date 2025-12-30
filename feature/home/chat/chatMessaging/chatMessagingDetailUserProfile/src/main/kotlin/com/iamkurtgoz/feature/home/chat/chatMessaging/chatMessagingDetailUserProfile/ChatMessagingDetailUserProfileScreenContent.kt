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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingDetailUserProfileRoute
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.component.ChatMessagingDetailUserProfileSettingsRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.component.ChatMessagingDetailUserProfileTopRow

@Composable
internal fun ChatMessagingDetailUserProfileScreenContent(
    state: ChatMessagingDetailUserProfileScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ChatMessagingDetailUserProfileScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        ChatMessagingDetailUserProfileTopRow(
            imageUrlData = state.chatUserProfile?.profilePhoto,
            setEvent = setEvent,
            followerCount = state.chatUserProfile?.followersCount,
            followingCount = state.chatUserProfile?.followingCount,
            postCount = state.chatUserProfile?.postCount,
            userName = state.chatUserProfile?.userName,
            isFollow = state.chatUserProfile?.isFollow == true,
            userId = state.route.userId,
        )

        ChatMessagingDetailUserProfileSettingsRow(
            modifier = Modifier
                .clickable {
                    setEvent.invoke(ChatMessagingDetailUserProfileScreenContract.Event.NavigateToAttachments(userId = state.route.userId, groupId = state.route.groupId))
                },
            mediaCount = state.chatUserProfile?.mediaCount,
        )
        // ChatMessagingDetailUserProfileCreateGroupRow()
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ChatMessagingDetailUserProfileScreenContent(
                state = ChatMessagingDetailUserProfileScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingDetailUserProfileRoute(
                        userId = "",
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.chat.newChat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.chat.newChat.component.NewChatRowButton
import com.iamkurtgoz.feature.home.chat.newChat.component.NewChatSearchField
import com.iamkurtgoz.feature.home.chat.newChat.domain.type.NewChatRowType

@Composable
internal fun NewChatScreenContent(
    state: NewChatScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (NewChatScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            NewChatSearchField(
                state = state,
                setEvent = setEvent,
            )
        }

        item {
            NewChatRowButton(
                type = NewChatRowType.CREATE_GROUP_CHAT,
                setEvent = setEvent,
            )
        }

        /*
        item {
            NewChatRowButton(
                type = NewChatRowType.CREATE_COMMUNITY,
                setEvent = setEvent,
            )
        }

        item {
            NewChatRowButton(
                type = NewChatRowType.CONNECT_CONTACTS,
                setEvent = setEvent,
            )
        }
         */

        item {
            Text(
                text = "Kişiler", // TODO: Localize
                style = AppTheme.typography.subtitleSmall,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .fillMaxSize(),
            )
        }

        itemsIndexed(
            items = if (state.textSearch.isNotEmpty) state.myFriendsFilteredList else state.myFriendsList,
            key = { index, item ->
                "$index-${item.id}-${item.name}-${item.summary}-${item.imageUrl}-${item.username}"
            },
            itemContent = { index, item ->
                UserRow(
                    modifier = Modifier
                        .padding(top = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingMedium else AppTheme.spacing.spacingNone),
                    contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                    userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                    isHeaderUser = true,
                    title = item.name,
                    subTitle = arrayOf(),
                    onClickAction = {
                        setEvent(NewChatScreenContract.Event.GenerateChatGroup(item))
                    },
                )
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            NewChatScreenContent(
                state = NewChatScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    textSearch = AppTextFieldValue(
                        value = "Text",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

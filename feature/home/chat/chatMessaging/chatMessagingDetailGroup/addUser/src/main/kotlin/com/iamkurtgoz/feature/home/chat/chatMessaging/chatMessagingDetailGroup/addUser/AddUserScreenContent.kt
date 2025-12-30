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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser

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
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.AddUserScreenRoute
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun AddUserScreenContent(
    state: AddUserScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AddUserScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            AppTextField.SearchField(
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(top = AppTheme.spacing.spacingMedium),
                value = state.textSearch.value,
                placeholder = "Kişi ara", // TODO: Localize
                onValueChange = {
                    setEvent.invoke(AddUserScreenContract.Event.SetTextSearch(it))
                },
                leadingIcon = resourcesR.drawable.img_search,
            )
        }

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
                    trailingContent = {
                        AppRadioButton.Secondary(
                            selected = state.selectedUserList.any { it.id == item.id },
                            onClick = {
                                setEvent(AddUserScreenContract.Event.ChangeSelectedUserState(item))
                            },
                        )
                    },
                    onClickAction = {
                        setEvent(AddUserScreenContract.Event.ChangeSelectedUserState(item))
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
            AddUserScreenContent(
                state = AddUserScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = AddUserScreenRoute(
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

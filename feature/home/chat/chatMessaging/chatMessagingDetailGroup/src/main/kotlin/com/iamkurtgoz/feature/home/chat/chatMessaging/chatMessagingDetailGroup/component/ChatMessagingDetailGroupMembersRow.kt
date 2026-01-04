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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.ChatMessagingDetailGroupScreenContract
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.MockUserRelationModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain.model.UserRelationUIItemModel

internal fun LazyListScope.chatMessagingDetailGroupMembersRow(
    users: List<UserRelationUIItemModel>?,
    onAddClick: () -> Unit = {},
    setEvent: (ChatMessagingDetailGroupScreenContract.Event) -> Unit,
    isFollow: Boolean,
    groupId: String?,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(vertical = AppTheme.spacing.spacingHuge),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Grup Üyeleri", // TODO: Localize
                style = AppTheme.typography.subtitleSmall,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { 
                    setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.NavigateToAddUser(groupId))
                },
            ) {
                Icon(
                    modifier = Modifier.padding(end = AppTheme.spacing.spacingSmall),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Kişi Ekle", // TODO: Localize
                    tint = Color.Black,
                )
                Text(
                    text = "Kişi Ekle", // TODO: Localize
                    style = AppTheme.typography.subtitleSmall,
                )
            }
        }
    }

    itemsIndexed(
        items = users ?: emptyList(),
        key = { index, item -> "$index - $item" },
    ) { _, item ->
        UserRow(
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.spacingMedium,
            ),
            userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
            isHeaderUser = true,
            title = item.name,
            subTitle = item.summary?.let { arrayOf(it) },
            trailingContent = {
                if (item.isCurrentUser != true) {
                    if (item.isFollow == true) {
                        AppButton.OutlineMedium(
                            text = "Takiptesin", // TODO: Localize
                            onClick = {
                                setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.OnClickActionButton(isFollow = item.isFollow == true, userId = item.id))
                            },
                            modifier = Modifier
                                .weight(AppDefaults.WEIGHT_FULL),
                        )
                    } else {
                        AppButton.SecondaryMedium(
                            text = "Takip Et", // TODO: Localize
                            onClick = {
                                setEvent.invoke(ChatMessagingDetailGroupScreenContract.Event.OnClickActionButton(isFollow = item.isFollow == true, userId = item.id))
                            },
                            modifier = Modifier
                                .weight(AppDefaults.WEIGHT_FULL),
                        )
                    }
                }
            },
            onClickAction = {},
        )
    }

    item {
        HorizontalDivider(
            modifier = Modifier.padding(
                top = AppTheme.spacing.spacingHuge,
            ),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            LazyColumn {
                chatMessagingDetailGroupMembersRow(
                    users = MockUserRelationModel.list,
                    isFollow = true,
                    groupId = "1",
                    setEvent = {},
                )
            }
        }
    }
}

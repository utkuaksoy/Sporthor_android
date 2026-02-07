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
package com.iamkurtgoz.feature.home.profile.userRelation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.feature.home.profile.userRelation.UserRelationScreenContract
import com.iamkurtgoz.feature.home.profile.userRelation.domain.model.MockUserRelationModel
import com.iamkurtgoz.feature.home.profile.userRelation.domain.model.UserRelationUIItemModel

@Composable
internal fun UserRelationFriends(
    users: List<UserRelationUIItemModel>?,
    modifier: Modifier = Modifier,
    setEvent: (UserRelationScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.spacingMedium, vertical = AppTheme.spacing.spacingSmall),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(
            items = users ?: listOf(),
            key = { index, item ->
                "$index - $item"
            },
        ) { _, item ->
            UserRow(
                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                isHeaderUser = true,
                title = item.name,
                subTitle = item.summary?.let { arrayOf(it) },
                modifier = Modifier,
                trailingContent = {
                    if (item.isFollowRequest == true) {
                        AppButton.OutlineSmall(
                            text = "İstek gönderildi", // TODO: Localize
                            onClick = { },
                            enabled = false,
                            modifier = Modifier,
                        )
                    } else if (item.isFollow == true) {
                        AppButton.OutlineSmall(
                            text = "Takiptesin", // TODO: Localize
                            onClick = {
                                setEvent(UserRelationScreenContract.Event.ChangeFollowStatus(UserActionFollowType.UnFollow, item.id))
                            },
                            modifier = Modifier,
                        )
                    } else {
                        AppButton.SecondarySmall(
                            text = "Takip Et", // TODO: Localize
                            onClick = {
                                setEvent(UserRelationScreenContract.Event.ChangeFollowStatus(UserActionFollowType.Follow, item.id))
                            },
                            modifier = Modifier,
                        )
                    }
                },
                onClickAction = { },
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            UserRelationFriends(
                users = MockUserRelationModel.list,
                setEvent = {},
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.chat.newChat.newGroupChat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.domain.model.MyFriendsFriendItemUIModel
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun NewGroupChatScreenContent(
    state: NewGroupChatScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (NewGroupChatScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimens.dp12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingMedium)
                            .background(
                                color = AppTheme.colors.generalColors.backgroundSoft200,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle)
                            .size(AppTheme.dimens.dp48)
                            .clickable {
                                setEvent.invoke(NewGroupChatScreenContract.Event.SetShowStatePhotoPicker(true))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (state.selectedImage == null) {
                            Image(
                                painter = painterResource(resourcesR.drawable.img_add_image_plus),
                                contentDescription = "image add",
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp24),
                            )
                        } else {
                            AppAsyncImageLoader.Load(
                                data = state.selectedImage,
                                contentDescription = "image add",
                                modifier = Modifier
                                    .clip(AppTheme.shapes.radiusCircle)
                                    .size(AppTheme.dimens.dp48),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (state.textGroupName.isEmpty) {
                            Text(
                                text = "Grup adını giriniz", // TODO: Localize
                                style = AppTheme.typography.subtitleLarge,
                                color = AppTheme.colors.generalColors.contentSoft600,
                            )
                        }

                        BasicTextField(
                            value = state.textGroupName.value,
                            onValueChange = {
                                setEvent.invoke(NewGroupChatScreenContract.Event.SetTextGroupName(it))
                            },
                            textStyle = AppTheme.typography.subtitleLarge.copy(
                                color = AppTheme.colors.generalColors.textPrimary,
                            ),
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                    thickness = AppTheme.dimens.dp1,
                )
            }
        }

        item {
            AppTextField.SearchField(
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(top = AppTheme.spacing.spacingMedium),
                value = state.textSearch.value,
                placeholder = "Kişi ara", // TODO: Localize
                onValueChange = {
                    setEvent.invoke(NewGroupChatScreenContract.Event.SetTextSearch(it))
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
                                setEvent(NewGroupChatScreenContract.Event.ChangeSelectedUserState(item))
                            },
                        )
                    },
                    onClickAction = {
                        setEvent(NewGroupChatScreenContract.Event.ChangeSelectedUserState(item))
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
            NewGroupChatScreenContent(
                state = NewGroupChatScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    myFriendsList = persistentListOf(
                        MyFriendsFriendItemUIModel(
                            id = "1",
                            imageUrl = null,
                            isCurrentUser = false,
                            isFollow = false,
                            name = "Celil Kırca",
                            summary = null,
                            username = "celilkirca",
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

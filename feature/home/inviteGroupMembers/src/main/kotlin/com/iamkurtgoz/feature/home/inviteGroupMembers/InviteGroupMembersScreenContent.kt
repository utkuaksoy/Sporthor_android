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
package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.iamkurtgoz.core.commonui.component.SelectedClubSection
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.MockSocialSearchUIModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun InviteGroupMembersScreenContent(
    state: InviteGroupMembersScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            SelectedClubSection(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                clubName = state.route.model.clubName ?: "-",
                clubLogo = state.route.model.clubLogo ?: "",
                subTitle = state.route.model.groupName,
                onChangeClick = {
                },
                showChangeButton = false,
                showTitle = false,
            )
        }

        item {
            AppTextField.SearchField(
                modifier = Modifier
                    .padding(
                        PaddingValues(
                            horizontal = AppTheme.dimens.dp16,
                            vertical = AppTheme.dimens.dp8,
                        ),
                    ),
                placeholder = "Kişi ara", // TODO: Localize
                value = state.textSearch.value,
                trailingIcon = if (state.textSearch.value.isNotEmpty()) resourcesR.drawable.img_close_circle else null,
                trailingIconClick = {
                    setEvent.invoke(InviteGroupMembersScreenContract.Event.SetSearchText(""))
                },
                leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                onValueChange = {
                    setEvent.invoke(InviteGroupMembersScreenContract.Event.SetSearchText(it))
                },
            )
        }

        if (state.textSearch.value.isNotEmpty()) {
            itemsIndexed(
                items = state.searchResultList?.searchList?.toPersistentList() ?: persistentListOf(),
                key = { index, item -> "${item?.id}-$index" },
            ) { index, item ->
                UserRow(
                    modifier = Modifier
                        .padding(top = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingMedium else AppTheme.spacing.spacingNone),
                    contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                    userHeaderData = item?.image ?: item?.name.getUserNameFirstChar(),
                    isHeaderUser = true,
                    title = item?.name,
                    subTitle = arrayOf(),
                    trailingContent = {
                        AppRadioButton.Secondary(
                            selected = state.selectedUserIdList.any { it == item?.id },
                            onClick = {
                                setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                            },
                        )
                    },
                    onClickAction = {
                        setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                    },
                )
            }
        } else {
            if (state.route.fromTrainingGroup) {
                itemsIndexed(
                    items = state.selectedUserList.filterNotNull(),
                    key = { index, item -> "${item.id}-$index" },
                ) { index, item ->
                    UserRow(
                        modifier = Modifier
                            .padding(top = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingMedium else AppTheme.spacing.spacingNone),
                        contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                        userHeaderData = item.image ?: item.name.getUserNameFirstChar(),
                        isHeaderUser = true,
                        title = item.name,
                        subTitle = arrayOf(),
                        trailingContent = {
                            AppRadioButton.Secondary(
                                selected = state.selectedUserIdList.any { it == item.id },
                                onClick = {
                                    setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                        },
                    )
                }
            } else {
                if (state.followingList?.users?.isNotEmpty() == true) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.spacing.spacingMedium),
                        ) {
                            Text(
                                text = "Takip Ettiklerim", // TODO: Localize
                                modifier = Modifier
                                    .weight(AppDefaults.WEIGHT_FULL)
                                    .padding(top = AppTheme.spacing.spacingHuge),
                                style = AppTheme.typography.labelMedium,
                            )

                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                itemsIndexed(
                    items = state.followingList?.users?.toPersistentList() ?: persistentListOf(),
                    key = { index, item -> "${item.id}-$index" },
                ) { index, item ->
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
                                selected = state.selectedUserIdList.any { it == item.id },
                                onClick = {
                                    setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(item))
                        },
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            InviteGroupMembersScreenContent(
                state = InviteGroupMembersScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenInviteGroupMemberRoute(
                        model = HomeScreenInviteGroupMemberScreenNavigationModel(
                            clubId = null,
                            clubName = "Eczacıbaşı Spor Kulübü",
                            clubLogo = null,
                            groupId = null,
                            groupName = "Yıldız Kız - Eczacıbaşı C",
                        ),
                    ),
                    // textSearch = AppTextFieldValue(value = "asdad"),
                    searchResultList = MockSocialSearchUIModel.list,
                ),
                setEvent = { },
            )
        }
    }
}

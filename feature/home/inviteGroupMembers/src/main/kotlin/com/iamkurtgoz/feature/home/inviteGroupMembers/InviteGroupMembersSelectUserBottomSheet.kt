package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.InviteGroupMembersTab
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.iamkurtgoz.core.resources.R as resourcesR

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
internal fun InviteGroupMembersSelectUserBottomSheet(
    state: InviteGroupMembersScreenContract.State,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
    onInviteClick: () -> Unit,
) {
    val following = state.followingList
    val isPlayersTab = state.selectedTab == InviteGroupMembersTab.PLAYERS

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.generalColors.backgroundPrimary)
            .padding(
                horizontal = AppTheme.spacing.spacingHuge,
                vertical = AppTheme.spacing.spacingMedium,
            ),
    ) {
        Text(
            text = if (isPlayersTab) "Sporcu Ekle" else "Antrenör Ekle",
            style = AppTheme.typography.subtitleLarge,
            color = AppTheme.colors.generalColors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.spacing.spacingSmall),
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = AppTheme.spacing.spacingSmall),
            color = AppTheme.colors.generalColors.backgroundWeak100,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {

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
                            .padding(
                                top = if (index == AppDefaults.ZERO)
                                    AppTheme.spacing.spacingMedium
                                else
                                    AppTheme.spacing.spacingNone,
                            ),
                        contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                        userHeaderData = item?.image ?: item?.name.getUserNameFirstChar(),
                        isHeaderUser = true,
                        title = item?.name,
                        subTitle = arrayOf(),
                        trailingContent = {
                            AppRadioButton.Secondary(
                                selected = state.selectedUserIdList.any { it == item?.id },
                                onClick = {
                                    setEvent(
                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                            item,
                                            tab = state.selectedTab,
                                        ),
                                    )
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(
                                InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                    item,
                                    tab = state.selectedTab,
                                ),
                            )
                        },
                    )
                }
            } else {
                val users = following?.users?.toPersistentList() ?: persistentListOf()
                val coaches = following?.coaches?.toPersistentList() ?: persistentListOf()

                if (state.route.fromTrainingGroup) {
                    if (isPlayersTab) {
                        if (users.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                                ) {
                                    Text(
                                        text = "Oyuncular", // TODO: Localize
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
                            items = users,
                            key = { index, item -> "${item.id}-$index" },
                        ) { index, item ->
                            UserRow(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == AppDefaults.ZERO)
                                            AppTheme.spacing.spacingMedium
                                        else
                                            AppTheme.spacing.spacingNone,
                                    ),
                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                isHeaderUser = true,
                                title = item.name,
                                subTitle = arrayOf(),
                                trailingContent = {
                                    AppRadioButton.Secondary(
                                        selected = state.selectedUserIdList.any { it == item.id },
                                        onClick = {
                                            setEvent(
                                                InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                    item,
                                                    tab = state.selectedTab,
                                                ),
                                            )
                                        },
                                    )
                                },
                                onClickAction = {
                                    setEvent(
                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                            item,
                                            tab = state.selectedTab,
                                        ),
                                    )
                                },
                            )
                        }
                    } else {
                        if (coaches.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                                ) {
                                    Text(
                                        text = "Antrenörler", // TODO: Localize
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
                            items = coaches,
                            key = { index, item -> "${item.id}-$index" },
                        ) { index, item ->
                            UserRow(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == AppDefaults.ZERO)
                                            AppTheme.spacing.spacingMedium
                                        else
                                            AppTheme.spacing.spacingNone,
                                    ),
                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                isHeaderUser = true,
                                title = item.name,
                                subTitle = arrayOf(),
                                trailingContent = {
                                    AppRadioButton.Secondary(
                                        selected = state.selectedUserIdList.any { it == item.id },
                                        onClick = {
                                            setEvent(
                                                InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                    item,
                                                    tab = state.selectedTab,
                                                ),
                                            )
                                        },
                                    )
                                },
                                onClickAction = {
                                    setEvent(
                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                            item,
                                            tab = state.selectedTab,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                } else {
                    if (isPlayersTab) {
                        if (users.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                                ) {
                                    Text(
                                        text = "Takip Ettiğim Oyuncular", // TODO: Localize
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
                            items = users,
                            key = { index, item -> "${item.id}-$index" },
                        ) { index, item ->
                            UserRow(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == AppDefaults.ZERO)
                                            AppTheme.spacing.spacingMedium
                                        else
                                            AppTheme.spacing.spacingNone,
                                    ),
                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                isHeaderUser = true,
                                title = item.name,
                                subTitle = arrayOf(),
                                trailingContent = {
                                    AppRadioButton.Secondary(
                                        selected = state.selectedUserIdList.any { it == item.id },
                                        onClick = {
                                            setEvent(
                                                InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                    item,
                                                    tab = state.selectedTab,
                                                ),
                                            )
                                        },
                                    )
                                },
                                onClickAction = {
                                    setEvent(
                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                            item,
                                            tab = state.selectedTab,
                                        ),
                                    )
                                },
                            )
                        }
                    } else {
                        if (coaches.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                                ) {
                                    Text(
                                        text = "Takip Ettiğim Antrenörler", // TODO: Localize
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
                            items = coaches,
                            key = { index, item -> "${item.id}-$index" },
                        ) { index, item ->
                            UserRow(
                                modifier = Modifier
                                    .padding(
                                        top = if (index == AppDefaults.ZERO)
                                            AppTheme.spacing.spacingMedium
                                        else
                                            AppTheme.spacing.spacingNone,
                                    ),
                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                isHeaderUser = true,
                                title = item.name,
                                subTitle = arrayOf(),
                                trailingContent = {
                                    AppRadioButton.Secondary(
                                        selected = state.selectedUserIdList.any { it == item.id },
                                        onClick = {
                                            setEvent(
                                                InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                    item,
                                                    tab = state.selectedTab,
                                                ),
                                            )
                                        },
                                    )
                                },
                                onClickAction = {
                                    setEvent(
                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                            item,
                                            tab = state.selectedTab,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }

        AppButton.PrimaryLarge(
            text = if (isPlayersTab) "Antrenman Grubuna Sporcu Ekle" else "Antrenman Grubuna Antrenör Ekle",
            onClick = onInviteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.spacing.spacingMedium),
        )
    }
}

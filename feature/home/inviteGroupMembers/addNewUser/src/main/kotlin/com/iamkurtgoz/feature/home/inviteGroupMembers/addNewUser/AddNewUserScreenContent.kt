package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberAddNewUserRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun AddNewUserScreenContent(
    state: AddNewUserScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AddNewUserScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
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
                    setEvent.invoke(AddNewUserScreenContract.Event.SetSearchText(""))
                },
                leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                onValueChange = {
                    setEvent.invoke(AddNewUserScreenContract.Event.SetSearchText(it))
                },
            )
        }

        val relation = state.followingList

        if (relation != null) {
            val users = relation.users?.toPersistentList() ?: persistentListOf()
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
                    onClickAction = {
                        /*setEvent(
                            AddNewUserScreenContract.Event
                                .ChangeSelectedUserState(item),
                        )*/
                    },
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AddNewUserScreenContent(
                state = AddNewUserScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenInviteGroupMemberAddNewUserRoute(
                        model = HomeScreenAddNewUserScreenNavigationModel(
                            isEdit = true,
                            clubId = null,
                            clubName = "Sporthor Voleybol Kulübü",
                            clubLogo = null,
                            groupId = null,
                            groupName = "Başlangıç - Miniminik Erkek",
                        ),
                    ),
                    // textSearch = AppTextFieldValue(value = "asdad"),
                ),
                setEvent = { },
            )
        }
    }
}

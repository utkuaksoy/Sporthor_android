package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach

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
import com.iamkurtgoz.core.commonui.component.SelectedClubSection
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsUpdateCoachScreenRoute
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelMock
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun UpdateCoachScreenContent(
    state: UpdateCoachScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (UpdateCoachScreenContract.Event) -> Unit,
) {
    if (state.response != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
        ) {
            state.response.clubs?.firstOrNull {
                it?.id == state.route.clubId
            }?.let { club ->
                club.trainingGroups?.firstOrNull {
                    it?.id == state.route.trainingGroupId
                }?.let { trainingGroup ->
                    item {
                        SelectedClubSection(
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .padding(horizontal = AppTheme.spacing.spacingHuge),
                            clubName = club.name ?: "-",
                            clubLogo = club.logo ?: "",
                            subTitle = trainingGroup.name ?: "",
                            onChangeClick = {
                            },
                            showChangeButton = false,
                            showTitle = false,
                        )
                    }
                }
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
                        setEvent.invoke(UpdateCoachScreenContract.Event.SetSearchText(""))
                    },
                    leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                    onValueChange = {
                        setEvent.invoke(UpdateCoachScreenContract.Event.SetSearchText(it))
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
                                selected = state.selectedUserList.any { it?.id == item?.id },
                                onClick = {
                                    setEvent(UpdateCoachScreenContract.Event.ChangeSelectedUserState(item))
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(UpdateCoachScreenContract.Event.ChangeSelectedUserState(item))
                        },
                    )
                }
            } else {
                itemsIndexed(
                    items = state.selectedUserList.filterNotNull().toPersistentList(),
                    key = { index, item -> "${item.id}-$index" },
                ) { index, coach ->
                    UserRow(
                        modifier = Modifier,
                        contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                        userHeaderData = coach.image ?: coach.name.getUserNameFirstChar(),
                        isHeaderUser = true,
                        title = coach.name,
                        subTitle = arrayOf(),
                        trailingContent = {
                            AppRadioButton.Secondary(
                                selected = state.selectedUserList.any { it?.id == coach.id },
                                onClick = {
                                    setEvent(UpdateCoachScreenContract.Event.ChangeSelectedUserState(coach))
                                },
                            )
                        },
                        onClickAction = {
                            setEvent(UpdateCoachScreenContract.Event.ChangeSelectedUserState(coach))
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
            UpdateCoachScreenContent(
                state = UpdateCoachScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListTrainingGroupsUpdateCoachScreenRoute.Route(
                        clubId = "687997d99f0b5e1be60d78c0",
                        trainingGroupId = "687eab38777dd7173c66fe4b",
                    ),
                    response = GetClubsAndDetailsDomainModelMock.mock(),
                ),
                setEvent = { },
            )
        }
    }
}

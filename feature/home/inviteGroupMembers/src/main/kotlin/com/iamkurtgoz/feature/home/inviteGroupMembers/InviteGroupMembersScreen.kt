package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.InviteGroupMembersTab
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.MockSocialSearchUIModel

@Composable
internal fun InviteGroupMembersScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSelectGroup: () -> Unit,
    navigateToAddNewUserScreen: (HomeScreenAddNewUserScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
    viewModel: InviteGroupMembersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val (showSheet, setShowSheet) = remember { mutableStateOf(false) }

    TrackedScreen("InviteGroupMembersScreen")

    LaunchedEffect(Unit) {
        viewModel.setEvent(InviteGroupMembersScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is InviteGroupMembersScreenContract.SideEffect.NavigateUp -> navigateUp()
            is InviteGroupMembersScreenContract.SideEffect.PopBackStack -> popBackStack()
            is InviteGroupMembersScreenContract.SideEffect.NavigateToHome -> navigateToHome()
            is InviteGroupMembersScreenContract.SideEffect.CloseBottomSheet -> setShowSheet(false)
            is InviteGroupMembersScreenContract.SideEffect.NavigateToAddAddNewUserScreen ->
                navigateToAddNewUserScreen(event.model, state.route.fromTrainingGroup)
            InviteGroupMembersScreenContract.SideEffect.OpenBottomSheet -> setShowSheet(true)
            InviteGroupMembersScreenContract.SideEffect.CloseBottomSheet -> setShowSheet(false)
            InviteGroupMembersScreenContract.SideEffect.NavigateToSelectGroup -> navigateToSelectGroup()
        }
    }

    InviteGroupMembersScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
        showSheet = showSheet,
        onChangeSheetState = setShowSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InviteGroupMembersScreenScaffold(
    state: InviteGroupMembersScreenContract.State,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
    showSheet: Boolean,
    onChangeSheetState: (Boolean) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(InviteGroupMembersScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = "Anasayfa", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(
                        bottom = AppTheme.configuration
                            .getSafeContentPaddingValues()
                            .calculateBottomPadding(),
                    )
                    .padding(bottom = AppTheme.spacing.spacingHuge),
            ) {
                if (state.route.model.isEdit) {
                    AppButton.OutlineLarge(
                        text = if (state.selectedTab == InviteGroupMembersTab.PLAYERS) {
                            "Yeni Oyuncu Ekle"
                        } else {
                            "Yeni Antrenör Ekle"
                        },
                        onClick = { onChangeSheetState(true) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

                    AppButton.PrimaryLarge(
                        text = "Grup Üyelerini Davet Et",
                        onClick = {
                            setEvent(
                                InviteGroupMembersScreenContract.Event.InviteClubMembersFromScreen,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    AppButton.PrimaryLarge(
                        text = "Anasayfa",
                        onClick = {
                            setEvent(
                                InviteGroupMembersScreenContract.Event.InviteClubMembersFromScreen,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) { padding ->
        InviteGroupMembersScreenContent(
            modifier = Modifier.padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(InviteGroupMembersScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { onChangeSheetState(false) },
            sheetState = sheetState,
            containerColor = AppTheme.colors.generalColors.backgroundPrimary,
        ) {
            InviteGroupMembersSelectUserBottomSheet(
                state = state,
                setEvent = setEvent,
                onInviteClick = {
                    if (!state.route.model.isEdit) {
                        setEvent(InviteGroupMembersScreenContract.Event.NextCreateFlowStep)
                    } else {
                        setEvent(
                            InviteGroupMembersScreenContract.Event
                                .InviteClubMembersFromBottomSheet,
                        )
                    }
                },
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            InviteGroupMembersScreenScaffold(
                state = InviteGroupMembersScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenInviteGroupMemberRoute(
                        model = HomeScreenInviteGroupMemberScreenNavigationModel(
                            clubId = null,
                            clubName = null,
                            clubLogo = null,
                            groupId = null,
                            groupName = null,
                        ),
                    ),
                    // textSearch = AppTextFieldValue(value = "asdad"),
                    searchResultList = MockSocialSearchUIModel.list,
                ),
                setEvent = { },
                showSheet = true,
                onChangeSheetState = { },
            )
        }
    }
}

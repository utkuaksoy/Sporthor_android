package com.iamkurtgoz.feature.home.coachList.trainingGroups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
// 1. GEREKLİ IMPORT'LARI EKLE
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsScreenRoute
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelMock
// 2. GÜVENLİ YÖNLENDİRME FONKSİYONUNU İÇERİ AKTAR
import com.iamkurtgoz.feature.home.inviteGroupMembers.navigation.navigateToInviteGroupMembersScreen

@Composable
internal fun TrainingGroupsScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToUpdateCoachScreen: (clubId: String?, trainingGroupId: String?) -> Unit,
    navigateToInviteGroupMembersScreen: (model: HomeScreenInviteGroupMemberScreenNavigationModel) -> Unit,
    viewModel: TrainingGroupsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("TrainingGroupsScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(TrainingGroupsScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is TrainingGroupsScreenContract.SideEffect.NavigateUp -> navigateUp()
            is TrainingGroupsScreenContract.SideEffect.PopBackStack -> popBackStack()
            is TrainingGroupsScreenContract.SideEffect.NavigateToUpdateCoachScreen -> navigateToUpdateCoachScreen(event.clubId, event.trainingGroupId)
            // 4. YENİ SIDE EFFECT'İ YAKALA VE NAVİGASYONU TETİKLE
            is TrainingGroupsScreenContract.SideEffect.NavigateToInviteGroupMembersScreen -> {
                navigateToInviteGroupMembersScreen(event.model)
            }
        }
    }

    TrainingGroupsScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrainingGroupsScreenScaffold(
    state: TrainingGroupsScreenContract.State,
    setEvent: (TrainingGroupsScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(TrainingGroupsScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = state.response?.clubs?.firstOrNull {
                            it?.id == state.route.clubId
                        }?.name ?: "",
                    )
                },
                rightContent = {
                    TextButton(
                        content = {
                            Text(
                                text = if (state.isDeleteMode) "İptal" else "Sil",
                                color = Color.Black,
                            )
                        },
                        onClick = {
                            setEvent.invoke(TrainingGroupsScreenContract.Event.ToggleDeleteMode)
                        },
                    )
                },
            )
        },
        bottomBar = {
            if (state.isDeleteMode) {
                AppButton.PrimaryLarge(
                    text = "Seçili Antrenörü Sil",
                    enabled = state.selectedUserList.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp),
                    onClick = {
                        setEvent.invoke(TrainingGroupsScreenContract.Event.ShowDeleteCoachDialog)
                    },
                )
            }
        },
    ) { padding ->
        TrainingGroupsScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(TrainingGroupsScreenContract.Event.DismissDialogs)
        }

        state.deleteCoachDialogModel?.Alert(
            onDismissRequest = {
                setEvent.invoke(TrainingGroupsScreenContract.Event.DismissDialogs)
            },
            onConfirmClick = {
                setEvent.invoke(TrainingGroupsScreenContract.Event.DismissDialogs)
                setEvent.invoke(TrainingGroupsScreenContract.Event.DeleteCoach)
            },
        )

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            TrainingGroupsScreenScaffold(
                state = TrainingGroupsScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListTrainingGroupsScreenRoute.Route(
                        clubId = "687997d99f0b5e1be60d78c0",
                    ),
                    response = GetClubsAndDetailsDomainModelMock.mock(),
                ),
                setEvent = { },
            )
        }
    }
}

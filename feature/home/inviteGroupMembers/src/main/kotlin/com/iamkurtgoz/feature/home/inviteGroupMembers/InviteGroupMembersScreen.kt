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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.MockSocialSearchUIModel

@Composable
internal fun InviteGroupMembersScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: InviteGroupMembersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("InviteGroupMembersScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(InviteGroupMembersScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is InviteGroupMembersScreenContract.SideEffect.NavigateUp -> navigateUp()
            is InviteGroupMembersScreenContract.SideEffect.PopBackStack -> popBackStack()
            is InviteGroupMembersScreenContract.SideEffect.NavigateToHome -> navigateToHome()
        }
    }

    InviteGroupMembersScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InviteGroupMembersScreenScaffold(
    state: InviteGroupMembersScreenContract.State,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
) {
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
                        text = "Grup Üyelerini Ekle", // TODO: Localize
                    )
                },
            )
        },
        bottomBar = {
            AppButton.PrimaryLarge(
                text = "Grup Üyelerini Davet Et", // TODO: Localize
                onClick = {
                    setEvent.invoke(InviteGroupMembersScreenContract.Event.InviteClubMembers)
                },
                enabled = state.selectedUserList.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingHuge),
            )
        },
    ) { padding ->
        InviteGroupMembersScreenContent(
            modifier = Modifier
                .padding(padding),
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
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            InviteGroupMembersScreenScaffold(
                state = InviteGroupMembersScreenContract.State(
                    isLoading = true,
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
            )
        }
    }
}

package com.iamkurtgoz.feature.home.editTeam.selectBranch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
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
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesItemUIModel
import com.iamkurtgoz.feature.home.editTeam.selectBranch.domain.model.BranchesUIModel
import java.util.UUID

@Composable
internal fun SelectBranchScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SelectBranchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SelectBranchScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SelectBranchScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SelectBranchScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SelectBranchScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    SelectBranchScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SelectBranchScreenScaffold(
    state: SelectBranchScreenContract.State,
    setEvent: (SelectBranchScreenContract.Event) -> Unit,
) {
    AppThemeScaffold { padding ->
        SelectBranchScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SelectBranchScreenContract.Event.DismissDialogs)
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
            SelectBranchScreenScaffold(
                state = SelectBranchScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    branchesList = BranchesUIModel(
                        branches = listOf(
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                            BranchesItemUIModel(
                                branchId = UUID.randomUUID().toString(),
                                branchImage = UUID.randomUUID().toString(),
                                branchTitle = UUID.randomUUID().toString(),
                                isSelected = false,
                            ),
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

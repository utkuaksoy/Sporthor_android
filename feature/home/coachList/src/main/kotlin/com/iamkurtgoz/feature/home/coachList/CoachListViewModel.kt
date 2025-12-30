package com.iamkurtgoz.feature.home.coachList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListScreenRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.coachList.domain.useCase.GetClubsAndDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CoachListViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getClubsAndDetailsUseCase: GetClubsAndDetailsUseCase,
) : CoreViewModel<CoachListScreenContract.State, CoachListScreenContract.SideEffect, CoachListScreenContract.Event>(
    initialState = CoachListScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = HomeCoachListScreenRoute.toRoute(savedStateHandle),
    ),
) {
    override fun setEvent(event: CoachListScreenContract.Event) {
        when (event) {
            is CoachListScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CoachListScreenContract.Event.NavigateUp -> setSideEffect(CoachListScreenContract.SideEffect.NavigateUp)
            is CoachListScreenContract.Event.PopBackStack -> setSideEffect(CoachListScreenContract.SideEffect.PopBackStack)
            is CoachListScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CoachListScreenContract.Event.NavigateToTrainingGroups -> setSideEffect(CoachListScreenContract.SideEffect.NavigateToTrainingGroups(event.clubId))
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        fetchClubsAndDetails()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun fetchClubsAndDetails() {
        getClubsAndDetailsUseCase
            .invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        response = response,
                    )
                }
            }
    }
}

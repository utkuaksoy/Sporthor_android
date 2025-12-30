package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsUpdateCoachScreenRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateCoachRequest
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelCoach
import com.iamkurtgoz.domain.model.response.SocialDomainItemModel
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase.GetClubsAndDetailsUseCase
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase.SearchUseCase
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.domain.useCase.UpdateCoachUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UpdateCoachViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getClubsAndDetailsUseCase: GetClubsAndDetailsUseCase,
    private val searchUseCase: SearchUseCase,
    private val updateCoachUseCase: UpdateCoachUseCase,
) : CoreViewModel<UpdateCoachScreenContract.State, UpdateCoachScreenContract.SideEffect, UpdateCoachScreenContract.Event>(
    initialState = UpdateCoachScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = HomeCoachListTrainingGroupsUpdateCoachScreenRoute.toRoute(savedStateHandle),
    ),
) {
    override fun setEvent(event: UpdateCoachScreenContract.Event) {
        when (event) {
            is UpdateCoachScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is UpdateCoachScreenContract.Event.NavigateUp -> setSideEffect(UpdateCoachScreenContract.SideEffect.NavigateUp)
            is UpdateCoachScreenContract.Event.PopBackStack -> setSideEffect(UpdateCoachScreenContract.SideEffect.PopBackStack)
            is UpdateCoachScreenContract.Event.DismissDialogs -> dismissDialogs()
            is UpdateCoachScreenContract.Event.SetSearchText -> setSearchText(event.text)
            is UpdateCoachScreenContract.Event.ChangeSelectedUserState -> changeSelectedUserState(event.item)
            is UpdateCoachScreenContract.Event.UpdateCoach -> updateCoach()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        fetchClubsAndDetails()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .debounce(UpdateCoachScreenContract.Static.SEARCH_DEBOUNCE)
            .map { it.textSearch.value }
            .filter { it.length >= UpdateCoachScreenContract.Static.MIN_SEARCH_VALUE_LENGTH }
            .onEach(::getSearch)
            .launchIn(viewModelScope)
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
                val defaultCoach = response.clubs?.firstOrNull {
                    it?.id == viewState.route.clubId
                }?.trainingGroups?.firstOrNull {
                    it?.id == viewState.route.trainingGroupId
                }?.coach
                changeSelectedUserState(defaultCoach)
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        response = response,
                    )
                }
            }
    }

    private fun setSearchText(text: String) {
        val textFieldValue = viewState.textSearch.copy(
            value = text,
            isError = false,
        )
        updateState { state ->
            state.copy(
                textSearch = textFieldValue,
            )
        }
    }

    private var searchJob: Job? = null
    private fun getSearch(query: String) {
        if (searchJob != null) {
            searchJob?.cancel()
            searchJob = null
        }

        searchJob = searchUseCase.invoke(query)
            .requester
            .onLoading {
            }
            .onError {
                updateState { state ->
                    state.copy(
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        searchResultList = it,
                    )
                }
            }
    }

    private fun changeSelectedUserState(item: Any?) {
        val selectedUserList = viewState.selectedUserList.toMutableList()
        val newItem = castAndGetCoach(item)
        val isAlreadySelected = selectedUserList.any { it?.id == newItem?.id }
        selectedUserList.clear()
        if (!isAlreadySelected) {
            selectedUserList.add(newItem)
        }
        updateState { state ->
            state.copy(
                selectedUserList = selectedUserList.toPersistentList(),
            )
        }
    }

    private fun castAndGetCoach(item: Any?): SocialDomainItemModel? {
        if (item is SocialDomainItemModel) {
            return item
        } else if (item is GetClubsAndDetailsDomainModelCoach) {
            return SocialDomainItemModel(
                id = item.id,
                image = item.imageUrl,
                name = item.name,
                attribute = item.summary,
                type = "",
            )
        }
        return null
    }

    private fun updateCoach() {
        val request = UpdateCoachRequest(
            trainingGroupId = viewState.route.trainingGroupId,
            coachId = viewState.selectedUserList.firstOrNull()?.id,
        )
        updateCoachUseCase
            .invoke(request)
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
                setSideEffect(UpdateCoachScreenContract.SideEffect.NavigateToHome)
            }
    }
}

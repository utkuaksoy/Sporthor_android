package com.iamkurtgoz.feature.home.coachList.trainingGroups

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModelUser
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsScreenRoute
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.base.AnyAlertDialogModel
import com.iamkurtgoz.domain.model.request.DeleteCoachRequest
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelCoache
import com.iamkurtgoz.feature.home.coachList.trainingGroups.domain.useCase.DeleteCoachUseCase
import com.iamkurtgoz.feature.home.coachList.trainingGroups.domain.useCase.GetClubsAndDetailsUseCase
import com.iamkurtgoz.feature.home.selectTrainingGroup.domain.useCase.GetTrainingGroupUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainingGroupsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val getClubsAndDetailsUseCase: GetClubsAndDetailsUseCase,
    private val deleteCoachUseCase: DeleteCoachUseCase,
    private val getTrainingGroupUserUseCase: GetTrainingGroupUserUseCase,
) : CoreViewModel<TrainingGroupsScreenContract.State, TrainingGroupsScreenContract.SideEffect, TrainingGroupsScreenContract.Event>(
    initialState = TrainingGroupsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = HomeCoachListTrainingGroupsScreenRoute.toRoute(savedStateHandle),
    ),
) {
    override fun setEvent(event: TrainingGroupsScreenContract.Event) {
        when (event) {
            is TrainingGroupsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is TrainingGroupsScreenContract.Event.NavigateUp -> setSideEffect(TrainingGroupsScreenContract.SideEffect.NavigateUp)
            is TrainingGroupsScreenContract.Event.PopBackStack -> setSideEffect(TrainingGroupsScreenContract.SideEffect.PopBackStack)
            is TrainingGroupsScreenContract.Event.DismissDialogs -> dismissDialogs()
            is TrainingGroupsScreenContract.Event.ToggleDeleteMode -> toggleDeleteMode()
            is TrainingGroupsScreenContract.Event.SetSelectedUserList -> setSelectedGetTrainingGroupUser(event.value)
            is TrainingGroupsScreenContract.Event.ShowDeleteCoachDialog -> showDeleteCoachDialog()
            is TrainingGroupsScreenContract.Event.DeleteCoach -> deleteCoach()
            is TrainingGroupsScreenContract.Event.NavigateToUpdateCoachScreen -> setSideEffect(TrainingGroupsScreenContract.SideEffect.NavigateToUpdateCoachScreen(event.clubId, event.trainingGroupId))
            is TrainingGroupsScreenContract.Event.OnTrainingGroupClicked -> {
                fetchTrainingGroupUsers(event.groupId)
            }
        }
    }

    private fun initialize() = viewModelScope.launch {
        fetchClubsAndDetails()
    }

    private fun dismissDialogs() {
        updateState {
            it.copy(
                alertDialogModel = null,
                deleteCoachDialogModel = null,
            )
        }
    }

    private fun fetchClubsAndDetails() {
        getClubsAndDetailsUseCase
            .invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(isLoading = true)
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

    private fun toggleDeleteMode() {
        updateState { state ->
            state.copy(isDeleteMode = !state.isDeleteMode)
        }
    }

    private fun setSelectedGetTrainingGroupUser(value: GetClubsAndDetailsDomainModelCoache) {
        val list = viewState.selectedUserList.toMutableList()
        list.clear()
        list.add(value)

        updateState { state ->
            state.copy(selectedUserList = list)
        }
    }

    private fun showDeleteCoachDialog() {
        updateState { state ->
            state.copy(
                deleteCoachDialogModel = AnyAlertDialogModel(
                    title = "",
                    message = "${viewState.selectedUserList.firstOrNull()?.name ?: ""} isimli antrenörü silmek istediğinize emin misiniz?",
                    confirmButton = "Evet",
                    dismissButton = "Hayır",
                ),
            )
        }
    }

    private fun deleteCoach() {
        val coachId = viewState.selectedUserList.firstOrNull()?.id ?: ""
        val trainingGroupId = viewState.response?.clubs?.firstOrNull {
            it?.id == viewState.route.clubId
        }?.trainingGroups?.firstOrNull {
            it?.coach?.id == coachId
        }?.id

        val requestBody = DeleteCoachRequest(
            coachId = coachId,
            trainingGroupId = trainingGroupId,
        )

        deleteCoachUseCase
            .invoke(requestBody)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(isLoading = true)
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
            .callWithSuccess {
                fetchClubsAndDetails()
            }
    }

    private fun fetchTrainingGroupUsers(groupId: String) {
        val currentClub = viewState.response?.clubs?.firstOrNull { it?.id == viewState.route.clubId }
        val currentGroup = currentClub?.trainingGroups?.firstOrNull { it?.id == groupId }

        getTrainingGroupUserUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(isLoading = true)
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
                val matchingGroup = response.groups?.firstOrNull { it?.groupId == groupId }

                val usersInGroup = matchingGroup?.users
                    ?.filterNotNull()
                    ?.map { user ->
                        HomeScreenInviteGroupMemberScreenNavigationModelUser(
                            id = user.id,
                            name = user.name,
                            username = user.username,
                            summary = user.summary,
                            imageUrl = user.imageUrl,
                            isFollow = user.isFollow ?: false,
                            isCurrentUser = user.isCurrentUser ?: false,
                        )
                    }
                    ?: emptyList()

                val mappedCoaches = matchingGroup?.coaches
                    ?.filterNotNull()
                    ?.map { coach ->
                        HomeScreenInviteGroupMemberScreenNavigationModelUser(
                            id = coach.id,
                            name = coach.name,
                            username = coach.username,
                            summary = coach.summary,
                            imageUrl = coach.imageUrl,
                            isFollow = coach.isFollow ?: false,
                            isCurrentUser = coach.isCurrentUser ?: false,
                        )
                    }
                    ?: emptyList()

                val fallbackCoach = currentGroup?.coach?.let { coach ->
                    HomeScreenInviteGroupMemberScreenNavigationModelUser(
                        id = coach.id,
                        name = coach.name,
                        username = coach.username,
                        summary = coach.summary,
                        imageUrl = coach.imageUrl,
                        isFollow = coach.isFollow ?: false,
                        isCurrentUser = coach.isCurrentUser ?: false,
                    )
                }

                val coachesInGroup = if (mappedCoaches.isEmpty() && fallbackCoach != null) {
                    listOf(fallbackCoach)
                } else {
                    mappedCoaches
                }

                val model = HomeScreenInviteGroupMemberScreenNavigationModel(
                    isEdit = true,
                    users = usersInGroup,
                    coaches = coachesInGroup,
                    clubId = viewState.route.clubId ?: "",
                    clubName = currentClub?.name ?: "",
                    clubLogo = currentClub?.logo,
                    groupId = groupId,
                    groupName = matchingGroup?.groupName ?: currentGroup?.name ?: "",
                )

                updateState { state ->
                    state.copy(isLoading = false)
                }
                setSideEffect(TrainingGroupsScreenContract.SideEffect.NavigateToInviteGroupMembersScreen(model))
            }
    }
}

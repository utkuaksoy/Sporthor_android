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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.toHomeScreenInviteGroupMemberRouteTypeMap
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest
import com.iamkurtgoz.domain.model.request.CoachesList
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.CoachRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.InviteGroupMembersTab
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.SocialSearchUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.types.UserRelationUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.AddTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.GetUserRelationUseCaseParams
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.SearchUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.useCase.SearchUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class InviteGroupMembersViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val userRelationUseCase: GetUserRelationUseCase,
    private val searchUseCase: SearchUseCase,
    private val addTrainingGroupUserUseCase: AddTrainingGroupUserUseCase,
) : CoreViewModel<InviteGroupMembersScreenContract.State, InviteGroupMembersScreenContract.SideEffect, InviteGroupMembersScreenContract.Event>(
    initialState = InviteGroupMembersScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenInviteGroupMemberRouteTypeMap(),
    ),
) {
    override fun setEvent(event: InviteGroupMembersScreenContract.Event) {
        when (event) {
            is InviteGroupMembersScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is InviteGroupMembersScreenContract.Event.NavigateUp -> setSideEffect(InviteGroupMembersScreenContract.SideEffect.NavigateUp)
            is InviteGroupMembersScreenContract.Event.PopBackStack -> setSideEffect(InviteGroupMembersScreenContract.SideEffect.PopBackStack)
            is InviteGroupMembersScreenContract.Event.DismissDialogs -> dismissDialogs()
            is InviteGroupMembersScreenContract.Event.ChangeSelectedUserState -> changeSelectedUserState(event.item, event.tab)
            is InviteGroupMembersScreenContract.Event.OnChangeTab -> onChangeTab(event.tab)
            is InviteGroupMembersScreenContract.Event.SetSearchText -> setSearchText(event.text)
            is InviteGroupMembersScreenContract.Event.InviteClubMembersFromScreen ->
                inviteClubMembers(isFromBottomSheet = false)

            is InviteGroupMembersScreenContract.Event.InviteClubMembersFromBottomSheet ->
                inviteClubMembers(isFromBottomSheet = true)
            is InviteGroupMembersScreenContract.Event.OpenCreateFlowSheet ->
                openCreateFlowSheet(event.type)
            is InviteGroupMembersScreenContract.Event.NextCreateFlowStep ->
                nextCreateFlowStep()
            is InviteGroupMembersScreenContract.Event.OpenCoachRoleSelection -> openCoachRoleSelection(event.coach)
            is InviteGroupMembersScreenContract.Event.DismissCoachRoleSelection -> dismissCoachRoleSelection()
            is InviteGroupMembersScreenContract.Event.SetCoachRole -> setCoachRole(event.role)
            is InviteGroupMembersScreenContract.Event.ConfirmCoachRoleSelection -> confirmCoachRoleSelection()
            is InviteGroupMembersScreenContract.Event.NavigateToSelectGroup ->
                setSideEffect(InviteGroupMembersScreenContract.SideEffect.NavigateToSelectGroup)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()

        // hem users hem coaches boşsa yeni akış
        if (viewState.route.model.users.isEmpty() &&
            viewState.route.model.coaches.isEmpty()
        ) {
            appPreferences.currentPreferenceState.firstOrNull()?.userId?.let { userId ->
                // Default to PLAYERS tab on first load
                val initialSearchType = 0 // 0 = sporcular
                getFollowing(userId, initialSearchType)
            }
        } else {
            val initialSelectedModels = mutableListOf<SocialSearchUIItemModel>()
            val initialSelectedIds = mutableListOf<String>()

            // Eski oyuncular
            viewState.route.model.users.forEach { ui ->
                initialSelectedModels += SocialSearchUIItemModel(
                    id = ui.id,
                    image = ui.imageUrl,
                    name = ui.name,
                    attribute = ui.summary,
                    type = SocialSearchUIItemType.User,
                    role = InviteGroupMembersTab.PLAYERS,
                )
                ui.id?.let { initialSelectedIds += it }
            }

            // Eski antrenörler
            viewState.route.model.coaches.forEach { coach ->
                initialSelectedModels += SocialSearchUIItemModel(
                    id = coach.id,
                    image = coach.imageUrl,
                    name = coach.name,
                    attribute = coach.summary,
                    type = SocialSearchUIItemType.User,
                    role = InviteGroupMembersTab.STAFF,
                )
                coach.id?.let { initialSelectedIds += it }
            }

            updateState { state ->
                state.copy(
                    followingList = UserRelationUIModel(
                        users = viewState.route.model.users.map {
                            UserRelationUIItemModel(
                                id = it.id,
                                name = it.name,
                                username = it.username,
                                summary = it.summary,
                                imageUrl = it.imageUrl,
                                isFollow = it.isFollow,
                                isCurrentUser = it.isCurrentUser,
                            )
                        },
                        coaches = viewState.route.model.coaches.map {
                            CoachRelationUIItemModel(
                                id = it.id,
                                name = it.name,
                                username = it.username,
                                summary = it.summary,
                                imageUrl = it.imageUrl,
                                isFollow = it.isFollow,
                                isCurrentUser = it.isCurrentUser,
                            )
                        },
                    ),
                    selectedUserList = initialSelectedModels.toPersistentList(),
                    selectedUserIdList = initialSelectedIds.toPersistentList(),
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun onChangeTab(tab: InviteGroupMembersTab) {
        updateState { state ->
            state.copy(
                selectedTab = tab,
                textSearch = state.textSearch.copy(
                    value = "",
                    isError = false,
                ),
                searchResultList = null,
            )
        }

        // Refresh the following list with the new role filter
        if (viewState.route.model.users.isEmpty() && viewState.route.model.coaches.isEmpty()) {
            viewModelScope.launch {
                appPreferences.currentPreferenceState.firstOrNull()?.userId?.let { userId ->
                    val searchType = if (tab == InviteGroupMembersTab.PLAYERS) 0 else 1
                    getFollowing(userId, searchType)
                }
            }
        }
    }

    private fun changeSelectedUserState(
        item: Any?,
        tab: InviteGroupMembersTab,
        forceUpdate: Boolean = false,
    ) {
        val selectedUserList = viewState.selectedUserList.toMutableList()
        val selectedUserIdList = viewState.selectedUserIdList.toMutableList()

        val castItem: SocialSearchUIItemModel? = when (item) {
            is SocialSearchUIItemModel -> item.copy(role = tab)

            is UserRelationUIItemModel -> SocialSearchUIItemModel(
                id = item.id,
                image = item.imageUrl,
                name = item.name,
                attribute = item.summary,
                type = SocialSearchUIItemType.User,
                role = InviteGroupMembersTab.PLAYERS, // bunlar oyuncu
            )

            is CoachRelationUIItemModel -> SocialSearchUIItemModel(
                id = item.id,
                image = item.imageUrl,
                name = item.name,
                attribute = item.summary,
                type = SocialSearchUIItemType.User,
                role = InviteGroupMembersTab.STAFF, // bunlar antrenör
            )

            else -> null
        }

        castItem ?: return

        val exists = selectedUserIdList.any { it == castItem.id }

        if (exists && !forceUpdate) {
            // seçim kaldır
            selectedUserIdList.removeAll { it == castItem.id }
            selectedUserList.removeAll { it?.id == castItem.id }
        } else {
            // yeni seçim veya güncelleme
            if (forceUpdate) {
                // Remove existing to update with new data
                selectedUserList.removeAll { it?.id == castItem.id }
            }
            if (!selectedUserIdList.contains(castItem.id)) {
                castItem.id?.let { selectedUserIdList.add(it) }
            }
            if (!selectedUserList.any { it?.id == castItem.id }) {
                selectedUserList.add(castItem)
            }
        }

        updateState { state ->
            state.copy(
                selectedUserList = selectedUserList.toPersistentList(),
                selectedUserIdList = selectedUserIdList.toPersistentList(),
            )
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

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .debounce(InviteGroupMembersScreenContract.Static.SEARCH_DEBOUNCE)
            .map { it.textSearch.value }
            .filter { it.length >= InviteGroupMembersScreenContract.Static.MIN_SEARCH_VALUE_LENGTH }
            .onEach(::getSearch)
            .launchIn(viewModelScope)
    }

    private var searchJob: Job? = null
    private fun getSearch(query: String) {
        if (searchJob != null) {
            searchJob?.cancel()
            searchJob = null
        }

        val searchType = if (viewState.selectedTab == InviteGroupMembersTab.PLAYERS) {
            0
        } else {
            1
        }

        searchJob = searchUseCase.invoke(SearchUseCaseParams(query, searchType))
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

    private fun getFollowing(userId: String, searchType: Int) {
        val params = GetUserRelationUseCaseParams(
            userId = userId,
            type = UserRelationUIItemType.FOLLOWING,
            searchType = searchType,
        )
        userRelationUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        followingList = it,
                    )
                }
            }
    }

    private fun inviteClubMembers(isFromBottomSheet: Boolean) {
        // 1) Seçili oyuncu ID'leri
        val playerIds: List<String> = viewState.selectedUserList
            .filterNotNull()
            .filter { it.role == InviteGroupMembersTab.PLAYERS }
            .mapNotNull { it.id }

        // 2) Seçili antrenörler → CoachesList'e map edilecek
        val coachItems: List<CoachesList> = viewState.selectedUserList
            .filterNotNull()
            .filter { it.role == InviteGroupMembersTab.STAFF }
            .mapNotNull { coachUi ->
                val coachId = coachUi.id ?: return@mapNotNull null

                CoachesList(
                    valId = coachId,
                    // attribute genelde summary/rol bilgisini taşıyorsa
                    // JSON'daki "val2": "Fizyoterapist,Yönetici" gibi
                    val2 = coachUi.attribute,
                    name = coachUi.name,
                )
            }

        if (playerIds.isEmpty() && coachItems.isEmpty()) {
            return
        }

        val params = AddTrainingGroupUserRequest(
            groupId = viewState.route.model.groupId,
            users = playerIds,
            coaches = coachItems,
        )

        addTrainingGroupUserUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { it.copy(isLoading = false) }

                if (isFromBottomSheet) {
                    setSideEffect(InviteGroupMembersScreenContract.SideEffect.CloseBottomSheet)
                } else {
                    setSideEffect(InviteGroupMembersScreenContract.SideEffect.NavigateToHome)
                }
            }
    }

    private fun buildCoachesList(
        coaches: List<SocialSearchUIItemModel>,
    ): CoachesList? {
        if (coaches.isEmpty()) return null

        val first = coaches.getOrNull(0)
        val second = coaches.getOrNull(1)

        return CoachesList(
            valId = first?.id,
            val2 = second?.id,
            name = when {
                coaches.size == 1 -> first?.name
                else -> listOfNotNull(first?.name, second?.name).joinToString(", ")
            },
        )
    }

    private fun openCreateFlowSheet(type: InviteGroupMembersTab) {
        if (viewState.route.model.isEdit) return

        updateState { state ->
            state.copy(
                selectedTab = type,
                firstCreateSheet = state.firstCreateSheet ?: type,
                isInCreateFlowSecondStep = false,
            )
        }
        setSideEffect(InviteGroupMembersScreenContract.SideEffect.OpenBottomSheet)
    }

    private fun nextCreateFlowStep() {
        if (viewState.route.model.isEdit) {
            setSideEffect(InviteGroupMembersScreenContract.SideEffect.CloseBottomSheet)
            return
        }

        val first = viewState.firstCreateSheet ?: viewState.selectedTab

        if (!viewState.isInCreateFlowSecondStep) {
            val next = when (first) {
                InviteGroupMembersTab.PLAYERS -> InviteGroupMembersTab.STAFF
                InviteGroupMembersTab.STAFF -> InviteGroupMembersTab.PLAYERS
            }

            updateState { state ->
                state.copy(
                    selectedTab = next,
                    isInCreateFlowSecondStep = true,
                    // BURASI ÖNEMLİ: tab değişirken search alanını temizle
                    textSearch = state.textSearch.copy(
                        value = "",
                        isError = false,
                    ),
                    searchResultList = null,
                )
            }
        } else {
            setSideEffect(InviteGroupMembersScreenContract.SideEffect.CloseBottomSheet)
        }
    }

    private fun openCoachRoleSelection(coach: Any) {
        val currentRole = when (coach) {
            is CoachRelationUIItemModel -> coach.summary
            is SocialSearchUIItemModel -> coach.attribute
            else -> null
        }
        updateState { state ->
            state.copy(
                showCoachRoleSelectionBottomSheet = true,
                selectedCoachForRoleSelection = coach,
                selectedCoachRole = currentRole,
            )
        }
    }

    private fun dismissCoachRoleSelection() {
        updateState { state ->
            state.copy(
                showCoachRoleSelectionBottomSheet = false,
                selectedCoachForRoleSelection = null,
                selectedCoachRole = null,
            )
        }
    }

    private fun setCoachRole(role: String) {
        updateState { state ->
            state.copy(
                selectedCoachRole = role,
            )
        }
    }

    private fun confirmCoachRoleSelection() {
        val coach = viewState.selectedCoachForRoleSelection
        val role = viewState.selectedCoachRole ?: ""

        val updatedCoach = when (coach) {
            is CoachRelationUIItemModel -> coach.copy(summary = role)
            is SocialSearchUIItemModel -> coach.copy(attribute = role)
            else -> null
        }

        updatedCoach?.let {
            changeSelectedUserState(it, InviteGroupMembersTab.STAFF, forceUpdate = true)
        }
        dismissCoachRoleSelection()
    }
}

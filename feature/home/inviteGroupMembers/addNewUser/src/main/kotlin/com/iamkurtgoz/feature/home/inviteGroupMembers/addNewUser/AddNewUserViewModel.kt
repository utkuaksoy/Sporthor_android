package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.toHomeScreenAddNewUserRouteTypeMap
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.CoachRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIItemType
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.UserRelationUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddNewUserViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
) : CoreViewModel<AddNewUserScreenContract.State, AddNewUserScreenContract.SideEffect, AddNewUserScreenContract.Event>(
    initialState = AddNewUserScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toHomeScreenAddNewUserRouteTypeMap(),
    ),
) {
    override fun setEvent(event: AddNewUserScreenContract.Event) {
        when (event) {
            is AddNewUserScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AddNewUserScreenContract.Event.NavigateUp -> setSideEffect(AddNewUserScreenContract.SideEffect.NavigateUp)
            is AddNewUserScreenContract.Event.PopBackStack -> setSideEffect(AddNewUserScreenContract.SideEffect.PopBackStack)
            is AddNewUserScreenContract.Event.DismissDialogs -> dismissDialogs()
            is AddNewUserScreenContract.Event.ChangeSelectedUserState -> TODO()
            AddNewUserScreenContract.Event.InviteClubMembers -> TODO()
            is AddNewUserScreenContract.Event.SetSearchText -> TODO()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        if (viewState.route.model.users.isEmpty()) {
            appPreferences.currentPreferenceState.firstOrNull()?.userId?.let {
                //getFollowing(it)
            }
        } else {
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
                    selectedUserList = viewState.route.model.users.map {
                        SocialSearchUIItemModel(
                            id = it.id,
                            image = it.imageUrl,
                            name = it.name,
                            attribute = it.summary,
                            type = SocialSearchUIItemType.User,
                        )
                    },
                    selectedUserIdList = viewState.route.model.users.mapNotNull {
                        it.id
                    },
                )
            }
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

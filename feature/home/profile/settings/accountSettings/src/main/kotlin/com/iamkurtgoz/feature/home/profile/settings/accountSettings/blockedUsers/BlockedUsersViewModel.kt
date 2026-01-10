package com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.RemoveBlockUserRequest
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.GetBlockedUsersUseCase
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.RemoveBlockedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BlockedUsersViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val removeBlockedUserUseCase: RemoveBlockedUserUseCase,
) : CoreViewModel<BlockedUsersScreenContract.State, BlockedUsersScreenContract.SideEffect, BlockedUsersScreenContract.Event>(
    initialState = BlockedUsersScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: BlockedUsersScreenContract.Event) {
        when (event) {
            is BlockedUsersScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is BlockedUsersScreenContract.Event.NavigateUp -> setSideEffect(BlockedUsersScreenContract.SideEffect.NavigateUp)
            is BlockedUsersScreenContract.Event.PopBackStack -> setSideEffect(BlockedUsersScreenContract.SideEffect.PopBackStack)
            is BlockedUsersScreenContract.Event.DismissDialogs -> dismissDialogs()
            is BlockedUsersScreenContract.Event.RemoveBlockedUser -> removeBlockedUser(event.userId)
        }
    }

    private fun initialize() = viewModelScope.launch {
        getBlockedUsersUseCase.invoke()
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
            .callWithSuccess { response ->
                val users = response.users?.map {
                    BlockedUserUiModel(
                        id = it.id.orEmpty(),
                        name = it.name.orEmpty(),
                        username = it.username,
                        imageUrl = it.imageUrl,
                    )
                }.orEmpty()

                updateState { state ->
                    state.copy(
                        isLoading = false,
                        blockedUsers = users,
                    )
                }
            }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun removeBlockedUser(userId: String) {
        if (state.value.removingUserIds.contains(userId)) return

        updateState { state ->
            state.copy(removingUserIds = state.removingUserIds + userId)
        }

        removeBlockedUserUseCase.invoke(RemoveBlockUserRequest(userId))
            .requester
            .onError {
                updateState { state ->
                    state.copy(
                        removingUserIds = state.removingUserIds - userId,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        blockedUsers = state.blockedUsers.filterNot { it.id == userId },
                        removingUserIds = state.removingUserIds - userId,
                    )
                }
            }
    }
}

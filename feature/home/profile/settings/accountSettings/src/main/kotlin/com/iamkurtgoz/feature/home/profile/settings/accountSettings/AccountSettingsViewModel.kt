package com.iamkurtgoz.feature.home.profile.settings.accountSettings

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateProfilePublicPrivateRequest
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.DeleteAccountUseCase
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.UpdateProfilePublicPrivateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AccountSettingsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val updateProfilePublicPrivateUseCase: UpdateProfilePublicPrivateUseCase,
) : CoreViewModel<AccountSettingsScreenContract.State, AccountSettingsScreenContract.SideEffect, AccountSettingsScreenContract.Event>(
    initialState = AccountSettingsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: AccountSettingsScreenContract.Event) {
        when (event) {
            is AccountSettingsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AccountSettingsScreenContract.Event.NavigateUp -> setSideEffect(AccountSettingsScreenContract.SideEffect.NavigateUp)
            is AccountSettingsScreenContract.Event.PopBackStack -> setSideEffect(AccountSettingsScreenContract.SideEffect.PopBackStack)
            is AccountSettingsScreenContract.Event.DismissDialogs -> dismissDialogs()
            is AccountSettingsScreenContract.Event.DeleteAccount -> deleteAccount()
            is AccountSettingsScreenContract.Event.UpdateProfilePublicPrivate -> updateProfilePublicPrivate(event.isPublic)
            is AccountSettingsScreenContract.Event.NavigateToBlockedUsers ->
                setSideEffect(AccountSettingsScreenContract.SideEffect.NavigateToBlockedUsers)

        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun deleteAccount() {
        deleteAccountUseCase.invoke()
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
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }

    private fun updateProfilePublicPrivate(isPublic: Boolean) {
        
        val request = UpdateProfilePublicPrivateRequest(isPublic)
        updateProfilePublicPrivateUseCase.invoke(request)
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
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }
}

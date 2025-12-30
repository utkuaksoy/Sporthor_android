package com.iamkurtgoz.feature.home.profile.settings.aboutUs

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AboutUsViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
) : CoreViewModel<AboutUsScreenContract.State, AboutUsScreenContract.SideEffect, AboutUsScreenContract.Event>(
    initialState = AboutUsScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: AboutUsScreenContract.Event) {
        when (event) {
            is AboutUsScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is AboutUsScreenContract.Event.NavigateUp -> setSideEffect(AboutUsScreenContract.SideEffect.NavigateUp)
            is AboutUsScreenContract.Event.PopBackStack -> setSideEffect(AboutUsScreenContract.SideEffect.PopBackStack)
            is AboutUsScreenContract.Event.DismissDialogs -> dismissDialogs()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }
}

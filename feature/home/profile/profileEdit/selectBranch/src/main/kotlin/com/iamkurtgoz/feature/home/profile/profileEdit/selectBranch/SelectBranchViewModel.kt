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
package com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch

import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.response.BranchInfoRowDomainModel
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.domain.model.BranchesItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.domain.useCase.BranchesAttributeUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.domain.useCase.BranchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectBranchViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val branchesUseCase: BranchesUseCase,
    private val branchesAttributeUseCase: BranchesAttributeUseCase,
    private val appEventBus: AppEventBus,
) : CoreViewModel<SelectBranchScreenContract.State, SelectBranchScreenContract.SideEffect, SelectBranchScreenContract.Event>(
    initialState = SelectBranchScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: SelectBranchScreenContract.Event) {
        when (event) {
            is SelectBranchScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectBranchScreenContract.Event.NavigateUp -> setSideEffect(SelectBranchScreenContract.SideEffect.NavigateUp)
            is SelectBranchScreenContract.Event.PopBackStack -> setSideEffect(SelectBranchScreenContract.SideEffect.PopBackStack)
            is SelectBranchScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectBranchScreenContract.Event.SetSelectedBranch -> setSelectedBranch(event.branch)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getBranches()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun getBranches() {
        branchesUseCase.invoke()
            .requester
            .onLoading { }
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
                        branchesList = it,
                    )
                }
            }
    }

    private fun setSelectedBranch(branch: BranchesItemUIModel?) {
        updateState { state ->
            state.copy(
                selectedBranch = branch,
            )
        }
        getBranchAttribute(
            branchId = branch?.branchId,
        )
    }

    private fun getBranchAttribute(branchId: String?) {
        branchesAttributeUseCase.invoke(branchId)
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
                        branchesAttributeList = it,
                    )
                }
                viewModelScope.launch {
                    appEventBus.updateSelectedBranch(
                        branchImage = viewState.selectedBranch?.branchImage,
                        branchTitle = viewState.selectedBranch?.branchTitle,
                        branchId = viewState.selectedBranch?.branchId,
                        branchAttribute = BranchesAttributeItemDomainModel(
                            branchId = it.branchId,
                            branchInfoRow = it.branchInfoRow?.map {
                                BranchInfoRowDomainModel(
                                    title = it.title,
                                    placeholder = it.placeholder,
                                    text = it.text,
                                    parameterName = it.parameterName,
                                    isRequired = it.isRequired,
                                    type = it.type?.type,
                                )
                            },
                        ),
                    )
                    setSideEffect(SelectBranchScreenContract.SideEffect.PopBackStack)
                }
            }
    }
}

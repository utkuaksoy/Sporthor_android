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
import com.iamkurtgoz.domain.eventbus.impl.ProfileEditEventBus
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
            is SelectBranchScreenContract.Event.ToggleSelectedBranch -> toggleSelectedBranch(event.branch)
            is SelectBranchScreenContract.Event.OnClickAdd -> onClickAdd()
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
                val apiSelectedIds = it.branches
                    ?.filter { branch -> branch.isSelected == true }
                    ?.mapNotNull { branch -> branch.branchId }
                    ?.toSet()
                    ?: emptySet()
                val selectedIds = (apiSelectedIds + appEventBus.profileEditSelectedBranchIds)
                updateState { state ->
                    state.copy(
                        branchesList = it,
                        selectedBranchIds = selectedIds,
                    )
                }
            }
    }

    private fun toggleSelectedBranch(branch: BranchesItemUIModel) {
        val branchId = branch.branchId ?: return
        val selectedIds = viewState.selectedBranchIds.toMutableSet()
        val isSelected = selectedIds.contains(branchId)

        if (isSelected) {
            selectedIds.remove(branchId)
            updateState { state ->
                state.copy(
                    selectedBranchIds = selectedIds,
                )
            }
            appEventBus.profileEditSelectedBranchIds = selectedIds
            viewModelScope.launch {
                appEventBus.profileEditEventBus.send(
                    ProfileEditEventBus.Event.RemoveSelectedBranch(
                        branchId = branchId,
                    ),
                )
            }
        } else {
            selectedIds.add(branchId)
            updateState { state ->
                state.copy(
                    selectedBranchIds = selectedIds,
                )
            }
            appEventBus.profileEditSelectedBranchIds = selectedIds
            getBranchAttribute(
                branchId = branchId,
                branch = branch,
            )
        }
    }

    private fun onClickAdd() {
        setSideEffect(SelectBranchScreenContract.SideEffect.PopBackStack)
    }

    private fun getBranchAttribute(branchId: String?, branch: BranchesItemUIModel) {
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
                        branchImage = branch.branchImage,
                        branchTitle = branch.branchTitle,
                        branchId = branch.branchId,
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
                }
            }
    }
}

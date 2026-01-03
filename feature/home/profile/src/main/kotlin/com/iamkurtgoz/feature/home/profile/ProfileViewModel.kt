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
package com.iamkurtgoz.feature.home.profile

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.controller.UserActionController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.impl.ProfileEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataSegmentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillUIModel
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileActionButtonType
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileComponents
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileDetailComponents
import com.iamkurtgoz.feature.home.profile.domain.types.toProfileActionButtonType
import com.iamkurtgoz.feature.home.profile.domain.useCase.GetProfileDetailUseCase
import com.iamkurtgoz.feature.home.profile.domain.useCase.GetProfileUseCase
import com.iamkurtgoz.feature.home.profile.domain.useCase.UserPostsUseCase
import com.iamkurtgoz.feature.home.profile.domain.useCase.UserPostsUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val getProfileDetailUseCase: GetProfileDetailUseCase,
    private val userActionController: UserActionController,
    private val userPostsUseCase: UserPostsUseCase,
) : CoreViewModel<ProfileScreenContract.State, ProfileScreenContract.SideEffect, ProfileScreenContract.Event>(
    initialState = ProfileScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        navigateRoute = savedStateHandle.toRoute(),
    ),
) {
    override fun setEvent(event: ProfileScreenContract.Event) {
        when (event) {
            is ProfileScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ProfileScreenContract.Event.NavigateUp -> setSideEffect(ProfileScreenContract.SideEffect.NavigateUp)
            is ProfileScreenContract.Event.PopBackStack -> setSideEffect(ProfileScreenContract.SideEffect.PopBackStack)
            is ProfileScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ProfileScreenContract.Event.SetSelectedSegmentState -> setSelectedSegmentState(segmentData = event.segmentData)
            is ProfileScreenContract.Event.SetSelectedSkillState -> setSelectedSkillState(skillData = event.skillData)
            is ProfileScreenContract.Event.OnClickActionButton -> onClickActionButton(actionButtonType = event.actionButtonType, userId = event.userId)
            is ProfileScreenContract.Event.OnUserRelation -> onClickUserRelation(userRelationFollowingCount = event.userRelationFollowingCount, userRelationFollowerCount = event.userRelationFollowerCount, userId = event.userId)
            is ProfileScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(status = event.eventBusState)
            is ProfileScreenContract.Event.UserPosts -> getUserPosts()
            is ProfileScreenContract.Event.NavigateToSettings -> setSideEffect(ProfileScreenContract.SideEffect.NavigateToSettings)
            is ProfileScreenContract.Event.NavigateToPostDetail -> {
                viewModelScope.launch {
                    setSideEffect(ProfileScreenContract.SideEffect.NavigateToPostDetail(getUserId(), event.index))
                }
            }
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getProfile()
        getUserPosts()
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private suspend fun getUserId(): String? {
        return viewState.navigateRoute.userId ?: appPreferences.currentPreferenceState.firstOrNull()?.userId
    }

    private fun getProfile() = viewModelScope.launch {
        getProfileUseCase.invoke(getUserId())
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
            .callWithSuccess { response ->
                val selectedSegmentIndex = response.components?.find { it.type == ProfileComponents.Segments.type }?.data?.selectedSegmentIndex ?: AppDefaults.ZERO
                updateState { state ->
                    state.copy(
                        profileModel = response,
                        selectedSegmentState = response.components?.find { it.type == ProfileComponents.Segments.type }?.data?.segments?.getOrNull(selectedSegmentIndex),
                    )
                }
                getProfileDetail()
            }
    }

    private fun getProfileDetail() = viewModelScope.launch {
        getProfileDetailUseCase.invoke(getUserId())
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
            .callWithSuccess { response ->
                val selectedSkillState = response.components?.filterNotNull()?.find { it.type == ProfileDetailComponents.FeaturedSkills.type }?.data?.skills?.firstOrNull { it?.isSelected == true }
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        profileDetailModel = response,
                        selectedSkillState = selectedSkillState,
                    )
                }
            }
    }

    private fun getUserPosts() = viewModelScope.launch {
        if (viewState.paginationInitialing || viewState.paginationLoading || viewState.paginationReloading) {
            return@launch
        }

        val params = UserPostsUseCaseParams(
            userId = getUserId(),
            page = viewState.paginationPage,
            pageSize = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        )
        userPostsUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy()
                }
            }
            .onError {
                updateState {
                    it.copy()
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        userPostsList = it,
                    )
                }
            }
    }

    private fun setSelectedSegmentState(segmentData: ProfileComponentDataSegmentUIModel?) {
        updateState { state ->
            state.copy(
                selectedSegmentState = segmentData,
            )
        }
    }

    private fun setSelectedSkillState(skillData: ProfileDetailComponentDataSkillUIModel?) {
        updateState { state ->
            state.copy(
                selectedSkillState = skillData,
            )
        }
    }

    private fun onClickActionButton(actionButtonType: ProfileActionButtonType, userId: String) {
        Timber.d(userId)
        if (actionButtonType == ProfileActionButtonType.EditProfile) {
            setSideEffect(ProfileScreenContract.SideEffect.NavigateToEditProfile)
        } else if (actionButtonType == ProfileActionButtonType.Follow || actionButtonType == ProfileActionButtonType.Following) {
            val followType: UserActionFollowType = when (actionButtonType) {
                ProfileActionButtonType.Follow -> UserActionFollowType.Follow
                ProfileActionButtonType.Following -> UserActionFollowType.UnFollow
                else -> UserActionFollowType.Follow
            }

            userActionController.changeFollowStatus(
                scope = viewModelScope,
                targetUserId = userId,
                followType = followType,
                onErrorAction = {
                    updateState { state ->
                        state.copy(
                            alertDialogModel = it.toAlertDialog,
                        )
                    }
                },
            )

            changeFollowStatus(actionButtonType)
        } else if (actionButtonType == ProfileActionButtonType.Message) {
            val effect = ProfileScreenContract.SideEffect.NavigateToChatMessaging(
                isGroup = false,
                title = viewState.profileModel?.info?.name ?: "",
                channelId = userId,
                userId = userId,
            )
            setSideEffect(effect)
        }
    }

    private fun onClickUserRelation(userRelationFollowingCount: Int?, userRelationFollowerCount: Int?, userId: String) {
        val effect = ProfileScreenContract.SideEffect.NavigateToUserRelation(
            userRelationFollowingCount = userRelationFollowingCount,
            userRelationFollowerCount = userRelationFollowerCount,
            userName = viewState.profileModel?.info?.username ?: "",
            userId = userId,
        )
        setSideEffect(effect)
    }

    private fun changeFollowStatus(actionButtonType: ProfileActionButtonType) {
        val profileModel = viewState.profileModel
        var profileActionButtons = profileModel?.components?.firstOrNull { it.type == ProfileScreenContract.Static.ACTION_BUTTON_COMPONENTS_TYPE }

        val newButtons: MutableList<String> = mutableListOf()
        if (actionButtonType == ProfileActionButtonType.Follow) {
            profileActionButtons?.data?.buttons?.map { it.toProfileActionButtonType() }?.fastForEach {
                if (it == ProfileActionButtonType.Follow) {
                    newButtons.add(ProfileActionButtonType.Following.type)
                    newButtons.add(ProfileActionButtonType.Message.type)
                } else {
                    newButtons.add(it.type)
                }
            }
        } else if (actionButtonType == ProfileActionButtonType.Following) {
            profileActionButtons?.data?.buttons?.map { it.toProfileActionButtonType() }?.fastForEach {
                when (it) {
                    ProfileActionButtonType.Following -> {
                        newButtons.add(ProfileActionButtonType.Follow.type)
                    }
                    ProfileActionButtonType.Message -> {}
                    else -> {
                        newButtons.add(it.type)
                    }
                }
            }
        }

        profileActionButtons = profileActionButtons?.copy(
            data = profileActionButtons.data?.copy(
                buttons = newButtons,
            ),
        )

        updateState { state ->
            state.copy(
                profileModel = profileModel?.copy(
                    components = profileModel.components?.mapNotNull {
                        if (it.type == ProfileScreenContract.Static.ACTION_BUTTON_COMPONENTS_TYPE) {
                            profileActionButtons
                        } else {
                            it
                        }
                    },
                ),
            )
        }
    }

    private fun updateEventBusStatus(status: ProfileEventBus.Event) {
        when (status) {
            is ProfileEventBus.Event.UpdateFollowingStatus -> {
                val userId = status.targetUserId
                if (userId == viewState.profileModel?.info?.id) {
                    changeFollowStatus(
                        actionButtonType = when (status.followType) {
                            UserActionFollowType.Follow -> ProfileActionButtonType.Follow
                            UserActionFollowType.UnFollow -> ProfileActionButtonType.Following
                        },
                    )
                }
            }
        }
    }
}

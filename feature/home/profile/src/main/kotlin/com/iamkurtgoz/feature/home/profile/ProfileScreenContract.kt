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

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.ProfileEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileComponentDataSegmentUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailComponentDataSkillUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileDetailUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.ProfileUIModel
import com.iamkurtgoz.feature.home.profile.domain.model.UserPostsUIModel
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileActionButtonType

internal class ProfileScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val navigateRoute: HomeScreenProfileRoute,
        val profileModel: ProfileUIModel? = null,
        val profileDetailModel: ProfileDetailUIModel? = null,
        val selectedSegmentState: ProfileComponentDataSegmentUIModel? = null,
        val selectedSkillState: ProfileDetailComponentDataSkillUIModel? = null,
        var paginationPage: Int = AppDefaults.LIST_PARAM_PAGE,
        var paginationItemsPerPage: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        var paginationInitialing: Boolean = false,
        var paginationLoading: Boolean = false,
        var paginationReloading: Boolean = false,
        var paginationHasNext: Boolean = true,
        val userPostsList: UserPostsUIModel? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToEditProfile : SideEffect()
        data class NavigateToUserRelation(val userRelationFollowingCount: Int?, val userRelationFollowerCount: Int?, val userName: String, val userId: String) : SideEffect()
        data class NavigateToChatMessaging(val isGroup: Boolean, val title: String, val channelId: String, val userId: String) : SideEffect()
        data object NavigateToSettings : SideEffect()
        data class NavigateToPostDetail(val userId: String?, val index: Int?) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class SetSelectedSegmentState(val segmentData: ProfileComponentDataSegmentUIModel?) : Event()
        data class SetSelectedSkillState(val skillData: ProfileDetailComponentDataSkillUIModel?) : Event()
        data class OnClickActionButton(val actionButtonType: ProfileActionButtonType, val userId: String) : Event()
        data class OnUserRelation(val userRelationFollowingCount: Int?, val userRelationFollowerCount: Int?, val userId: String) : Event()
        data class UpdateEventBusStatus(val eventBusState: ProfileEventBus.Event) : Event()
        data class UserPosts(val fetchParam: FetchParam) : Event()
        data object NavigateToSettings : Event()
        data class NavigateToPostDetail(val userId: String?, val index: Int?) : Event()
    }

    object Static {
        const val ACTION_BUTTON_COMPONENTS_TYPE = "ProfileActionButtons"
    }
}

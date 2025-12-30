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
package com.iamkurtgoz.feature.home.profile.postDetail

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.HomeScreenPostDetailRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.feature.home.profile.postDetail.domain.model.UserPostsItemUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class PostDetailScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenPostDetailRoute,
        val userPostFeedList: ImmutableList<UserPostsItemUIModel> = persistentListOf(),
        var paginationPage: Int = AppDefaults.LIST_PARAM_PAGE,
        var paginationItemsPerPage: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        var paginationInitialing: Boolean = false,
        var paginationLoading: Boolean = false,
        var paginationReloading: Boolean = false,
        var paginationHasNext: Boolean = true,
        val userId: String? = null,
        val playingVideoUrl: String? = null,
        val commentDialogShowPostId: String? = null,
        val showReportDialogForPostId: String? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class DashboardFeed(val fetchParam: FetchParam) : Event()
        data class SetPlayingVideoUrl(val playingVideoUrl: String?) : Event()
        data class SetLikeStatus(val postId: String?, val actionType: UserActionPostLikeType) : Event()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : Event()
        data class SetCommentDialogShowPostId(val commentDialogShowPostId: String?) : Event()
        data class ShowReportDialog(val postId: String?) : Event()
        data class SetHide(val postId: String?) : Event()
    }

    object Static
}

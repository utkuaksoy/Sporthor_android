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
package com.iamkurtgoz.feature.home.dashboard

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.eventbus.impl.DashboardEventBus
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.feature.home.dashboard.domain.model.DashboardPostUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModelItem
import com.iamkurtgoz.feature.home.dashboard.domain.model.StoryFeedUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class DashboardScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val userId: String? = null,
        var paginationPage: Int = AppDefaults.LIST_PARAM_PAGE,
        var paginationItemsPerPage: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
        var paginationInitialing: Boolean = false,
        var paginationLoading: Boolean = false,
        var paginationReloading: Boolean = false,
        var paginationHasNext: Boolean = true,
        val storyFeedList: StoryFeedUIModel? = null,
        val menuList: MenuUIModel? = null,
        val dashboardFeedList: ImmutableList<DashboardPostUIModel> = persistentListOf(),
        val commentDialogShowPostId: String? = null,
        val playingVideoUrl: String? = null,
        val showReportDialogForPostId: String? = null,
        val reportText: String = "",
        val menuTitle: String = "Hızlı Menü",
        val isSingleMainMenu: Boolean = false,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data class NavigateToStory(val userId: String) : SideEffect()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : SideEffect()
        data class NavigateToShare(val routeType: HomeScreenShareRouteScreenNavigateModel) : SideEffect()
        data class NavigateToWebView(val routeType: HomeScreenWebViewScreenNavigateModel) : SideEffect()
        data object NavigateToCalendar : SideEffect()
        data object NavigateToNotifications : SideEffect()
        data object NavigateToCreateTeamScreen : SideEffect()
        data object NavigateToSelectSportClubScreen : SideEffect()
        data class NavigateToSelectTeamScreen(val fromGenerateClub: Boolean, val fromTrainingGroup: Boolean) : SideEffect()
        data class NavigateToSelectTrainingGroupScreen(val fromTrainingGroup: Boolean) : SideEffect()
        data object NavigateToCoachListScreen : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data class DashboardFeed(val fetchParam: FetchParam) : Event()
        data class ClickedStoryItem(val userId: String? = null) : Event()
        data class UpdateEventBusStatus(val eventBusState: DashboardEventBus.Event) : Event()
        data class SetLikeStatus(val postId: String?, val actionType: UserActionPostLikeType) : Event()
        data class NavigateToMediaViewer(val routeType: HomeScreenMediaViewerScreenNavigateModel) : Event()
        data class NavigateToShare(val routeType: HomeScreenShareRouteScreenNavigateModel) : Event()
        data class NavigateToWebView(val routeType: HomeScreenWebViewScreenNavigateModel) : Event()
        data object SetAskedNotificationPermission : Event()
        data class SetCommentDialogShowPostId(val commentDialogShowPostId: String?) : Event()
        data class SetComplain(val postId: String?) : Event()
        data class SetHide(val postId: String?) : Event()
        data class SetDelete(val postId: String?) : Event()
        data class ReportPost(val postId: String?, val reason: String?) : Event()
        data class SetPlayingVideoUrl(val playingVideoUrl: String?) : Event()
        data class ShowReportDialog(val postId: String?) : Event()
        data class UpdateReportText(val text: String?) : Event()
        data object DismissReportDialog : Event()
        data object GetMenu : Event()
        data class OnMainMenuClick(val mainMenuItem: MenuUIModelItem) : Event()
        data object NavigateToCalendar : Event()
        data object NavigateToNotifications : Event()
        data object NavigateToCreateTeamScreen : Event()
        data object NavigateToSelectSportClubScreen : Event()
        data class NavigateToSelectTeamScreen(val fromGenerateClub: Boolean, val fromTrainingGroup: Boolean) : Event()
        data class NavigateToSelectTrainingGroupScreen(val fromTrainingGroup: Boolean) : Event()
        data object NavigateToCoachListScreen : Event()
    }

    object Static
}

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
package com.iamkurtgoz.core.navigation

import android.os.Parcelable
import androidx.annotation.Keep
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editEvent.HomeScreenEditEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editTeam.HomeScreenEditTeamScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.editPhoto.HomeScreenShareEditPhotoScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class HomeScreenRoute(
    val isNewRegisteredUser: Boolean,
)

@Keep
@Serializable
data object HomeScreenDashboardRoute

@Keep
@Serializable
data class HomeScreenDashboardWebviewRoute(
    val routeType: HomeScreenWebViewScreenNavigateModel,
)

@Keep
@Serializable
data object HomeScreenSearchRoute

@Keep
@Serializable
data class HomeScreenShareRoute(
    val routeType: HomeScreenShareRouteScreenNavigateModel,
)

@Keep
@Serializable
data object HomeScreenChatRoute

@Keep
@Serializable
data class HomeScreenProfileRoute(
    val userId: String?,
)

@Keep
@Serializable
data class PostDetailScreenProfileRoute(
    val userId: String?,
    val index: Int?,
)

@Keep
@Serializable
data object HomeScreenCustomizeUserInfoRoute

@Keep
@Serializable
data object HomeScreenOnboardingRoute

@Keep
@Serializable
data class HomeScreenSelectTeamRoute(
    val isEdit: Boolean,
    val fromGenerateClub: Boolean,
    val fromTrainingGroup: Boolean,
)

@Keep
@Serializable
data class HomeScreenCreateTeamRoute(
    val fromGenerateClub: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenEditTeamRoute(
    val model: HomeScreenEditTeamScreenNavigationModel,
)

@Keep
@Serializable
data object HomeScreenNewChatRoute

@Keep
@Serializable
data object HomeScreenNewGroupChatRoute

@Keep
@Serializable
data class HomeScreenChatMessagingRoute(
    val isGroup: Boolean,
    val title: String,
    val channelId: String,
    val userId: String,
)

@Keep
@Serializable
data class HomeScreenChatMessagingDetailGroupRoute(
    val userId: String?,
    val groupId: String?,
)

@Keep
@Serializable
data class AddUserScreenRoute(
    val groupId: String?,
)

@Keep
@Serializable
data class UpdateGroupScreenRoute(
    val groupId: String?,
)

@Keep
@Serializable
data class HomeScreenChatMessagingDetailUserProfileRoute(
    val userId: String?,
    val groupId: String?,
)

@Keep
@Serializable
data class HomeScreenChatMessagingDetailAttachmentsRoute(
    val userId: String?,
    val groupId: String?,
)

@Keep
@Serializable
data object HomeScreenProfileEditRoute

@Keep
@Serializable
data class HomeScreenPostDetailRoute(
    val userId: String?,
    val index: Int?,
)

@Keep
@Serializable
data object HomeScreenProfileSettingsRoute

@Keep
@Serializable
data object HomeScreenProfileSettingsAboutUsRoute

@Keep
@Serializable
data object HomeScreenAccountSettingsScreenRoute

@Keep
@Serializable
data object HomeScreenProfileEditSelectBranchRoute

@Keep
@Serializable
data object HomeScreenProfileEditSelectUserRoleRoute

@Keep
@Serializable
data class HomeScreenProfileUserRelationRoute(
    val userRelationFollowingCount: Int?,
    val userRelationFollowerCount: Int?,
    val userName: String,
    val userId: String,
)

@Keep
@Serializable
data class HomeScreenMediaViewerRoute(
    val routeType: HomeScreenMediaViewerScreenNavigateModel,
)

@Keep
@Serializable
data class HomeScreenStoryViewerRoute(
    val userId: String,
)

@Keep
@Serializable
data object HomeScreenCameraxRoute

@Keep
@Serializable
@Parcelize
data class HomeScreenShareCompleteRoute(
    val routeType: HomeScreenShareCompleteScreenNavigateModel,
) : Parcelable

@Keep
@Serializable
data class HomeScreenShareEditPhotoRoute(
    val routeType: HomeScreenShareEditPhotoScreenNavigateModel,
)

@Keep
@Serializable
data class HomeScreenSelectAddressRoute(
    val isMapActive: Boolean,
    val isBlackBackground: Boolean,
)

@Keep
@Serializable
data class HomeScreenSendClubAuthDocumentRoute(
    val model: HomeScreenSendClubAuthDocumentScreenNavigateModel,
    val fromGenerateClub: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenSuccessDocumentUploadRoute(
    val model: HomeScreenSuccessDocumentUploadScreenNavigateModel,
)

@Keep
@Serializable
data class HomeScreenTrainingRoute(
    val model: HomeScreenTrainingScreenNavigateModel,
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenInviteGroupMemberRoute(
    val model: HomeScreenInviteGroupMemberScreenNavigationModel,
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenInviteGroupMemberAddNewUserRoute(
    val model: HomeScreenAddNewUserScreenNavigationModel,
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenSuccessAddTrainingGroupRoute(
    val model: HomeScreenSuccessAddTrainingGroupScreenNavigationModel,
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data object HomeScreenCalendarRoute

@Keep
@Serializable
data class HomeScreenCalendarDetailRoute(
    val model: HomeScreenCalendarDetailScreenNavigationModel,
)

@Keep
@Serializable
data class HomeScreenAddEventRoute(
    val model: HomeScreenAddEventScreenNavigationModel,
)

@Keep
@Serializable
data class HomeScreenEditEventRoute(
    val model: HomeScreenEditEventScreenNavigationModel,
)

@Keep
@Serializable
data object HomeScreenSelectEventDraftsRoute

@Keep
@Serializable
data object HomeScreenNotificationsRoute

@Keep
@Serializable
data object HomeScreenSelectSportClubScreenRoute

@Keep
@Serializable
data class HomeScreenSelectTrainingGroupScreenRoute(
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data class HomeScreenEditTrainingGroupScreenRoute(
    val model: HomeScreenEditTrainingGroupScreenNavigationModel,
    val fromTrainingGroup: Boolean = false,
)

@Keep
@Serializable
data object HomeScreenEditTeamSelectBranchRoute

@Keep
@Serializable
data object HomeScreenCreateTeamSelectBranchRoute

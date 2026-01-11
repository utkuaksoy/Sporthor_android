package com.iamkurtgoz.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatRoute
import com.iamkurtgoz.core.navigation.HomeScreenDashboardRoute
import com.iamkurtgoz.core.navigation.HomeScreenOnboardingRoute
import com.iamkurtgoz.core.navigation.HomeScreenRoute
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.core.navigation.ext.navigateAndClearBackStack
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.core.navigation.route.HomeNavRoute
import com.iamkurtgoz.feature.home.addEvent.navigation.addEventScreenNavigation
import com.iamkurtgoz.feature.home.addEvent.navigation.navigateToAddEventScreen
import com.iamkurtgoz.feature.home.calendar.navigation.calendarScreenNavigation
import com.iamkurtgoz.feature.home.calendar.navigation.navigateToCalendarScreen
import com.iamkurtgoz.feature.home.calendarDetail.navigation.calendarDetailScreenNavigation
import com.iamkurtgoz.feature.home.calendarDetail.navigation.navigateToCalendarDetailScreen
import com.iamkurtgoz.feature.home.camerax.navigation.cameraxScreenNavigation
import com.iamkurtgoz.feature.home.camerax.navigation.navigateToCameraxScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.navigation.attachmentsScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.navigation.navigateToAttachmentsScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.navigation.addUserScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.navigation.navigateToAddUserScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.navigation.chatMessagingDetailGroupScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.navigation.navigateToChatMessagingDetailGroupScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.navigation.navigateToUpdateGroupScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.navigation.updateGroupScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.navigation.chatMessagingDetailUserProfileScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.navigation.navigateToChatMessagingDetailUserProfileScreen
import com.iamkurtgoz.feature.home.chat.chatMessaging.navigation.chatMessagingScreenNavigation
import com.iamkurtgoz.feature.home.chat.chatMessaging.navigation.navigateToChatMessagingScreen
import com.iamkurtgoz.feature.home.chat.navigation.chatScreenNavigation
import com.iamkurtgoz.feature.home.chat.newChat.navigation.navigateToNewChatScreen
import com.iamkurtgoz.feature.home.chat.newChat.navigation.newChatScreenNavigation
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.navigation.navigateToNewGroupChatScreen
import com.iamkurtgoz.feature.home.chat.newChat.newGroupChat.navigation.newGroupChatScreenNavigation
import com.iamkurtgoz.feature.home.coachList.navigation.coachListScreenNavigation
import com.iamkurtgoz.feature.home.coachList.navigation.navigateToCoachListScreen
import com.iamkurtgoz.feature.home.coachList.trainingGroups.navigation.navigateToTrainingGroupsScreen
import com.iamkurtgoz.feature.home.coachList.trainingGroups.navigation.trainingGroupsScreenNavigation
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.navigation.navigateToUpdateCoachScreen
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.navigation.updateCoachScreenNavigation
import com.iamkurtgoz.feature.home.createTeam.navigation.createTeamScreenNavigation
import com.iamkurtgoz.feature.home.createTeam.navigation.navigateToCreateTeamScreen
import com.iamkurtgoz.feature.home.createTeam.selectBranch.navigation.navigateToCreateTeamSelectBranchScreen
import com.iamkurtgoz.feature.home.createTeam.selectBranch.navigation.selectCreateTeamBranchScreenNavigation
import com.iamkurtgoz.feature.home.customizeUserInfo.navigation.customizeUserInfoScreenNavigation
import com.iamkurtgoz.feature.home.dashboard.navigation.dashboardScreenNavigation
import com.iamkurtgoz.feature.home.dashboard.navigation.navigateToDashboardScreen
import com.iamkurtgoz.feature.home.dashboard.webview.navigation.navigateToWebviewScreen
import com.iamkurtgoz.feature.home.dashboard.webview.navigation.webviewScreenNavigation
import com.iamkurtgoz.feature.home.editEvent.navigation.editEventScreenNavigation
import com.iamkurtgoz.feature.home.editEvent.navigation.navigateToEditEventScreen
import com.iamkurtgoz.feature.home.editTeam.navigation.editTeamScreenNavigation
import com.iamkurtgoz.feature.home.editTeam.navigation.navigateToEditTeamScreen
import com.iamkurtgoz.feature.home.editTeam.selectBranch.navigation.navigateToEditTeamSelectBranchScreen
import com.iamkurtgoz.feature.home.editTeam.selectBranch.navigation.selectEditTeamBranchScreenNavigation
import com.iamkurtgoz.feature.home.editTrainingGroup.navigation.editTrainingGroupScreenNavigation
import com.iamkurtgoz.feature.home.editTrainingGroup.navigation.navigateToEditTrainingGroupScreen
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.navigation.addNewUserScreenNavigation
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.navigation.navigateToAddNewUserScreen
import com.iamkurtgoz.feature.home.inviteGroupMembers.navigation.inviteGroupMembersScreenNavigation
import com.iamkurtgoz.feature.home.inviteGroupMembers.navigation.navigateToInviteGroupMembersScreen
import com.iamkurtgoz.feature.home.mediaViewer.navigation.mediaViewerScreenNavigation
import com.iamkurtgoz.feature.home.mediaViewer.navigation.navigateToMediaViewerScreen
import com.iamkurtgoz.feature.home.notifications.navigation.navigateToNotificationsScreen
import com.iamkurtgoz.feature.home.notifications.navigation.notificationsScreenNavigation
import com.iamkurtgoz.feature.home.onboarding.navigation.onboardingScreenNavigation
import com.iamkurtgoz.feature.home.profile.navigation.navigateToProfileScreen
import com.iamkurtgoz.feature.home.profile.navigation.profileScreenNavigation
import com.iamkurtgoz.feature.home.profile.postDetail.navigation.navigateToPostDetailScreen
import com.iamkurtgoz.feature.home.profile.postDetail.navigation.postDetailScreenNavigation
import com.iamkurtgoz.feature.home.profile.profileEdit.navigation.navigateToProfileEditScreen
import com.iamkurtgoz.feature.home.profile.profileEdit.navigation.profileEditScreenNavigation
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.navigation.navigateToProfileEditSelectBranchScreen
import com.iamkurtgoz.feature.home.profile.profileEdit.selectBranch.navigation.profileEditSelectBranchScreenNavigation
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.navigation.navigateToProfileEditSelectUserRoleScreen
import com.iamkurtgoz.feature.home.profile.profileEdit.selectUserRole.navigation.profileEditSelectUserRoleScreenNavigation
import com.iamkurtgoz.feature.home.profile.settings.aboutUs.navigation.navigateToAboutUsScreen
import com.iamkurtgoz.feature.home.profile.settings.aboutUs.navigation.profileSettingAboutUsScreenNavigation
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.navigation.accountSettingsScreenNavigation
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers.navigation.blockedUsersScreenNavigation
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers.navigation.navigateToBlockedUsersScreen
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.navigation.navigateToAccountSettingsScreen
import com.iamkurtgoz.feature.home.profile.settings.navigation.navigateToProfileSettingScreen
import com.iamkurtgoz.feature.home.profile.settings.navigation.profileSettingScreenNavigation
import com.iamkurtgoz.feature.home.profile.userRelation.navigation.navigateToProfileUserRelationScreen
import com.iamkurtgoz.feature.home.profile.userRelation.navigation.profileUserRelationScreenNavigation
import com.iamkurtgoz.feature.home.search.navigation.searchScreenNavigation
import com.iamkurtgoz.feature.home.selectAddress.navigation.navigateToSelectAddressScreen
import com.iamkurtgoz.feature.home.selectAddress.navigation.selectAddressScreenNavigation
import com.iamkurtgoz.feature.home.selectEventDrafts.navigation.navigateToSelectEventDraftsScreen
import com.iamkurtgoz.feature.home.selectEventDrafts.navigation.selectEventDraftsScreenNavigation
import com.iamkurtgoz.feature.home.selectSportClub.navigation.navigateToSelectSportClubScreen
import com.iamkurtgoz.feature.home.selectSportClub.navigation.selectSportClubScreenNavigation
import com.iamkurtgoz.feature.home.selectTeam.navigation.navigateToSelectTeamScreen
import com.iamkurtgoz.feature.home.selectTeam.navigation.selectTeamScreenNavigation
import com.iamkurtgoz.feature.home.selectTrainingGroup.navigation.navigateToSelectTrainingGroupScreen
import com.iamkurtgoz.feature.home.selectTrainingGroup.navigation.selectTrainingGroupScreenNavigation
import com.iamkurtgoz.feature.home.sendClubAuthDocument.navigation.navigateToSendClubAuthDocumentScreen
import com.iamkurtgoz.feature.home.sendClubAuthDocument.navigation.sendClubAuthDocumentScreenNavigation
import com.iamkurtgoz.feature.home.share.complete.navigation.navigateToShareCompleteScreen
import com.iamkurtgoz.feature.home.share.complete.navigation.shareCompleteScreenNavigation
import com.iamkurtgoz.feature.home.share.navigation.navigateToShareScreen
import com.iamkurtgoz.feature.home.share.navigation.shareScreenNavigation
import com.iamkurtgoz.feature.home.storyViewer.navigation.navigateToStoryViewerScreen
import com.iamkurtgoz.feature.home.storyViewer.navigation.storyViewerScreenNavigation
import com.iamkurtgoz.feature.home.successAddTrainingGroup.navigation.navigateToSuccessAddTrainingGroupScreen
import com.iamkurtgoz.feature.home.successAddTrainingGroup.navigation.successAddTrainingGroupScreenNavigation
import com.iamkurtgoz.feature.home.successDocumentUploadScreen.navigation.navigateToSuccessDocumentUploadScreenScreen
import com.iamkurtgoz.feature.home.successDocumentUploadScreen.navigation.successDocumentUploadScreenScreenNavigation
import com.iamkurtgoz.feature.home.trainingScreen.navigation.navigateToTrainingScreenScreen
import com.iamkurtgoz.feature.home.trainingScreen.navigation.trainingScreenScreenNavigation

@Suppress("LongMethod")
@Composable
internal fun HomeScreenContent(
    state: HomeScreenContract.State,
    homeNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = homeNavController,
        route = HomeNavRoute::class,
        startDestination = state.startDestination,
    ) {
        dashboardScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToStory = { userId ->
                homeNavController.navigateToStoryViewerScreen(
                    userId = userId,
                )
            },
            navigateToMediaViewer = { routeType: HomeScreenMediaViewerScreenNavigateModel ->
                homeNavController.navigateToMediaViewerScreen(
                    routeType = routeType,
                )
            },
            navigateToShare = { routeType: HomeScreenShareRouteScreenNavigateModel ->
                homeNavController.navigateToShareScreen(
                    routeType = routeType,
                )
            },
            navigateToWebView = { routeType: HomeScreenWebViewScreenNavigateModel ->
                homeNavController.navigateToWebviewScreen(
                    routeType = routeType,
                )
            },
            navigateToCalendar = homeNavController::navigateToCalendarScreen,
            navigateToNotifications = homeNavController::navigateToNotificationsScreen,
            navigateToCreateTeamScreen = homeNavController::navigateToCreateTeamScreen,
            navigateToSelectSportClubScreen = homeNavController::navigateToSelectSportClubScreen,
            navigateToSelectTeamScreen = { fromGenerateClub: Boolean, fromTrainingGroup: Boolean ->
                homeNavController.navigateToSelectTeamScreen(
                    isEdit = !fromGenerateClub && !fromTrainingGroup,
                    fromGenerateClub = fromGenerateClub,
                    fromTrainingGroup = fromTrainingGroup,
                )
            },
            navigateToSelectTrainingGroupScreen = homeNavController::navigateToSelectTrainingGroupScreen,
            navigateToCoachListScreen = homeNavController::navigateToCoachListScreen,
        )

        searchScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToProfile = {
                homeNavController.navigateToProfileScreen(
                    userId = it,
                )
            },
            navigateToTeam = {},
        )

        postDetailScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToMediaViewer = homeNavController::navigateToMediaViewerScreen,
        )

        shareScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToCameraXScreen = homeNavController::navigateToCameraxScreen,
            navigateToShareCompleteScreen = homeNavController::navigateToShareCompleteScreen,
        )

        chatScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToNewChatScreen = homeNavController::navigateToNewChatScreen,
            navigateToMessagingScreen = { isGroup, title, channelId, userId ->
                homeNavController.navigateToChatMessagingScreen(
                    isGroup = isGroup,
                    title = title,
                    channelId = channelId,
                    userId = userId,
                )
            },
        )

        profileScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToEditProfile = homeNavController::navigateToProfileEditScreen,
            navigateToUserRelation = { userRelationFollowing, userRelationFollower, userName, userId ->
                homeNavController.navigateToProfileUserRelationScreen(
                    userRelationFollowingCount = userRelationFollowing,
                    userRelationFollowerCount = userRelationFollower,
                    userName = userName,
                    userId = userId,
                )
            },
            navigateToMessagingScreen = homeNavController::navigateToChatMessagingScreen,
            navigateToSetting = homeNavController::navigateToProfileSettingScreen,
            navigateToPostDetail = { userId, index ->
                homeNavController.navigateToPostDetailScreen(
                    userId = userId,
                    index = index,
                )
            },
        )

        // Sub
        customizeUserInfoScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToOnboarding = {
                homeNavController.navigateAndClearBackStack(HomeScreenOnboardingRoute)
            },
        )

        onboardingScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToUserTeams = {
                homeNavController.navigateAndClearBackStack(
                    HomeScreenSelectTeamRoute(
                        isEdit = false,
                        fromGenerateClub = false,
                        fromTrainingGroup = false,
                    ),
                )
            },
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
        )

        newChatScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToMessagingScreen = { isGroup, title, channelId, userId ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToChatMessagingScreen(
                    isGroup = isGroup,
                    title = title,
                    channelId = channelId,
                    userId = userId,
                    navOptions = option.build(),
                )
            },
            navigateToNewGroupChat = homeNavController::navigateToNewGroupChatScreen,
        )

        newGroupChatScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToMessagingScreen = { isGroup, title, channelId, userId ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = HomeScreenChatRoute,
                    inclusive = false,
                )

                homeNavController.navigateToChatMessagingScreen(
                    isGroup = isGroup,
                    title = title,
                    channelId = channelId,
                    userId = userId,
                    navOptions = option.build(),
                )
            },
        )

        chatMessagingScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToMediaViewer = { routeType: HomeScreenMediaViewerScreenNavigateModel ->
                homeNavController.navigateToMediaViewerScreen(
                    routeType = routeType,
                )
            },
            navigateToChatMessagingDetailUser = { userId, groupId ->
                homeNavController.navigateToChatMessagingDetailUserProfileScreen(
                    userId = userId,
                    groupId = groupId,
                )
            },
            navigateToChatMessagingDetailGroup = { userId, groupId ->
                homeNavController.navigateToChatMessagingDetailGroupScreen(
                    userId = userId,
                    groupId = groupId,
                )
            },
        )

        chatMessagingDetailUserProfileScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToAttachments = { userId, groupId ->
                homeNavController.navigateToAttachmentsScreen(
                    userId = userId,
                    groupId = groupId,
                )
            },
        )

        chatMessagingDetailGroupScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToAddUser = { groupId ->
                homeNavController.navigateToAddUserScreen(
                    groupId = groupId,
                )
            },
            navigateToAttachments = { userId, groupId ->
                homeNavController.navigateToAttachmentsScreen(
                    userId = userId,
                    groupId = groupId,
                )
            },
            navigateToUpdateGroup = { groupId ->
                homeNavController.navigateToUpdateGroupScreen(
                    groupId = groupId,
                )
            },
        )

        updateGroupScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        addUserScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        profileEditScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToEditProfileSelectBranch = homeNavController::navigateToProfileEditSelectBranchScreen,
            navigateToSelectUserRole = homeNavController::navigateToProfileEditSelectUserRoleScreen,
        )

        profileSettingScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToAboutUs = homeNavController::navigateToAboutUsScreen,
            navigateToAccountSettings = homeNavController::navigateToAccountSettingsScreen,
            navigateToWebView = homeNavController::navigateToWebviewScreen,
        )

        profileSettingAboutUsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        accountSettingsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToBlockedUsers = homeNavController::navigateToBlockedUsersScreen,
        )

        blockedUsersScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        profileEditSelectBranchScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        profileEditSelectUserRoleScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        profileUserRelationScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        mediaViewerScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        storyViewerScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        cameraxScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        shareCompleteScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            popBackStackToDashboard = {
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = HomeScreenDashboardRoute,
                    inclusive = false,
                )
                option.setLaunchSingleTop(true)

                homeNavController.navigateToDashboardScreen(
                    navOptions = option.build(),
                )
            },
            navigateToSelectAddressScreen = {
                homeNavController.navigateToSelectAddressScreen(
                    isMapActive = false,
                    isBlackBackground = true,
                )
            },
        )

        selectTeamScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToCreateTeam = { fromGenerateClub: Boolean ->
                homeNavController.navigateToCreateTeamScreen(fromGenerateClub = fromGenerateClub)
            },
            navigateToTrainingScreen = { model, fromTrainingGroup: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToTrainingScreenScreen(
                    model = model,
                    fromTrainingGroup = fromTrainingGroup,
                    navOptions = option.build(),
                )
            },
            navigateToHomeScreenSendClubAuthDocumentScreen = { model, fromGenerateClub: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToSendClubAuthDocumentScreen(
                    model = model,
                    fromGenerateClub = fromGenerateClub,
                    navOptions = option.build(),
                )
            },
        )

        createTeamScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToSelectAddress = {
                homeNavController.navigateToSelectAddressScreen(false)
            },
            navigateToHomeScreenSendClubAuthDocumentScreen = { model, fromGenerateClub: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToSendClubAuthDocumentScreen(
                    model = model,
                    fromGenerateClub = fromGenerateClub,
                    navOptions = option.build(),
                )
            },
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToTrainingScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToTrainingScreenScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToCreateTeamSelectBranchScreen = homeNavController::navigateToCreateTeamSelectBranchScreen,
        )

        editTeamScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToSelectAddress = {
                homeNavController.navigateToSelectAddressScreen(false)
            },
            navigateToHomeScreenSendClubAuthDocumentScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = HomeScreenSelectTeamRoute::class,
                    inclusive = false,
                )

                homeNavController.navigateToSendClubAuthDocumentScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToTrainingScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = HomeScreenSelectTeamRoute::class,
                    inclusive = false,
                )

                homeNavController.navigateToTrainingScreenScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToEditTeamSelectBranchScreen = homeNavController::navigateToEditTeamSelectBranchScreen,
        )

        selectAddressScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        sendClubAuthDocumentScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToTrainingScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToTrainingScreenScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToSuccessDocumentUploadScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToSuccessDocumentUploadScreenScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToWebView = { routeType: HomeScreenWebViewScreenNavigateModel ->
                homeNavController.navigateToWebviewScreen(
                    routeType = routeType,
                )
            },
        )

        successDocumentUploadScreenScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToTrainingScreen = { model ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToTrainingScreenScreen(
                    model = model,
                    navOptions = option.build(),
                )
            },
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
        )

        trainingScreenScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToSuccessAddTrainingGroupScreen = { model, fromTrainingGroup: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToSuccessAddTrainingGroupScreen(
                    model = model,
                    fromTrainingGroup = fromTrainingGroup,
                    navOptions = option.build(),
                )
            },
        )

        successAddTrainingGroupScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToInviteGroupMembersScreen = { model, fromTrainingGroup: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToInviteGroupMembersScreen(
                    model = model,
                    fromTrainingGroup = fromTrainingGroup,
                    navOptions = option.build(),
                )
            },
        )

        inviteGroupMembersScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToSelectGroup = homeNavController::navigateToSelectTrainingGroupScreen,
            navigateToAddNewUserScreen = { model, fromTrainingGroup: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = false,
                )

                homeNavController.navigateToAddNewUserScreen(
                    model = model,
                    fromTrainingGroup = fromTrainingGroup,
                    navOptions = option.build(),
                )
            }
        )

        addNewUserScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        webviewScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        attachmentsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        calendarScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToCalendarDetail = homeNavController::navigateToCalendarDetailScreen,
            navigateToAddEvent = homeNavController::navigateToAddEventScreen,
            navigateToSelectEventDrafts = homeNavController::navigateToSelectEventDraftsScreen,
        )

        calendarDetailScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToAddEvent = homeNavController::navigateToAddEventScreen,
            navigateToSelectEventDrafts = homeNavController::navigateToSelectEventDraftsScreen,
            navigateToEditEventScreen = homeNavController::navigateToEditEventScreen,
        )

        addEventScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToSelectAddress = {
                homeNavController.navigateToSelectAddressScreen(true)
            },
        )

        editEventScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToSelectAddress = {
                homeNavController.navigateToSelectAddressScreen(true)
            },
        )

        selectEventDraftsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToAddEvent = homeNavController::navigateToAddEventScreen,
        )

        notificationsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        selectSportClubScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToEditTeamScreen = homeNavController::navigateToEditTeamScreen,
        )

        selectTrainingGroupScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToEditTrainingGroupScreen = { model, fromTrainingGroup ->
                homeNavController.navigateToEditTrainingGroupScreen(model, fromTrainingGroup)
            },
        )

        editTrainingGroupScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
            navigateToSuccessAddTrainingGroupScreen = { model, fromTrainingGroup: Boolean ->
                val option = NavOptions.Builder()
                option.setPopUpTo(
                    route = homeNavController.currentDestination?.route,
                    inclusive = true,
                )

                homeNavController.navigateToSuccessAddTrainingGroupScreen(
                    model = model,
                    fromTrainingGroup = fromTrainingGroup,
                    navOptions = option.build(),
                )
            },
        )

        coachListScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToTrainingGroups = homeNavController::navigateToTrainingGroupsScreen,
        )

        trainingGroupsScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToUpdateCoachScreen = homeNavController::navigateToUpdateCoachScreen,
            navigateToInviteGroupMembersScreen = { model ->
                homeNavController.navigateToInviteGroupMembersScreen(model = model)
            }
        )

        updateCoachScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
            navigateToHome = {
                homeNavController.navigateAndClearBackStack(HomeScreenDashboardRoute)
            },
        )

        selectEditTeamBranchScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )

        selectCreateTeamBranchScreenNavigation(
            navigateUp = homeNavController::navigateUp,
            popBackStack = homeNavController::popBackStack,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            HomeScreenContent(
                state = HomeScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    startDestination = HomeScreenDashboardRoute,
                    navigateRoute = HomeScreenRoute(
                        isNewRegisteredUser = false,
                    ),
                ),
                homeNavController = rememberNavController(),
            )
        }
    }
}

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
package com.iamkurtgoz.feature.home.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenDashboardRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.feature.home.dashboard.DashboardScreen

fun NavGraphBuilder.dashboardScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToStory: (String) -> Unit,
    navigateToMediaViewer: (routeType: HomeScreenMediaViewerScreenNavigateModel) -> Unit,
    navigateToShare: (routeType: HomeScreenShareRouteScreenNavigateModel) -> Unit,
    navigateToWebView: (routeType: HomeScreenWebViewScreenNavigateModel) -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToCreateTeamScreen: () -> Unit,
    navigateToSelectSportClubScreen: () -> Unit,
    navigateToSelectTeamScreen: (fromGenerateClub: Boolean, fromTrainingGroup: Boolean) -> Unit,
    navigateToSelectTrainingGroupScreen: (fromTrainingGroup: Boolean) -> Unit,
    navigateToCoachListScreen: () -> Unit,
    navigateToPaymentListScreen: () -> Unit,
) {
    composable<HomeScreenDashboardRoute> {
        DashboardScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToStory = navigateToStory,
            navigateToMediaViewer = navigateToMediaViewer,
            navigateToShare = navigateToShare,
            navigateToWebView = navigateToWebView,
            navigateToCalendar = navigateToCalendar,
            navigateToNotifications = navigateToNotifications,
            navigateToCreateTeamScreen = navigateToCreateTeamScreen,
            navigateToSelectSportClubScreen = navigateToSelectSportClubScreen,
            navigateToSelectTeamScreen = navigateToSelectTeamScreen,
            navigateToSelectTrainingGroupScreen = navigateToSelectTrainingGroupScreen,
            navigateToCoachListScreen = navigateToCoachListScreen,
            navigateToPaymentListScreen = navigateToPaymentListScreen,
        )
    }
}

fun NavController.navigateToDashboardScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenDashboardRoute
    this.navigate(route, navOptions)
}

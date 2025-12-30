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
package com.iamkurtgoz.feature.home.sendClubAuthDocument.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenSendClubAuthDocumentRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.homeScreenSendClubAuthDocumentRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.feature.home.sendClubAuthDocument.SendClubAuthDocumentScreen

fun NavGraphBuilder.sendClubAuthDocumentScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToSuccessDocumentUploadScreen: (HomeScreenSuccessDocumentUploadScreenNavigateModel) -> Unit,
    navigateToWebView: (routeType: HomeScreenWebViewScreenNavigateModel) -> Unit,
) {
    composable<HomeScreenSendClubAuthDocumentRoute>(
        typeMap = homeScreenSendClubAuthDocumentRouteTypeMap,
    ) {
        SendClubAuthDocumentScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToHome = navigateToHome,
            navigateToTrainingScreen = navigateToTrainingScreen,
            navigateToSuccessDocumentUploadScreen = navigateToSuccessDocumentUploadScreen,
            navigateToWebView = navigateToWebView,
        )
    }
}

fun NavController.navigateToSendClubAuthDocumentScreen(
    model: HomeScreenSendClubAuthDocumentScreenNavigateModel,
    fromGenerateClub: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenSendClubAuthDocumentRoute(
        model = model,
        fromGenerateClub = fromGenerateClub,
    )
    this.navigate(route, navOptions)
}

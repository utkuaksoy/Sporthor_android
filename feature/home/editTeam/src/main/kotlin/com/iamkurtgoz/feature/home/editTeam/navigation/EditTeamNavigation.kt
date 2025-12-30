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
package com.iamkurtgoz.feature.home.editTeam.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenEditTeamRoute
import com.iamkurtgoz.core.navigation.model.home.editTeam.HomeScreenEditTeamScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editTeam.homeScreenEditTeamRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.feature.home.editTeam.EditTeamScreen

fun NavGraphBuilder.editTeamScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToSelectAddress: () -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel) -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToEditTeamSelectBranchScreen: () -> Unit,
) {
    composable<HomeScreenEditTeamRoute>(
        typeMap = homeScreenEditTeamRouteTypeMap,
    ) {
        EditTeamScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToSelectAddress = navigateToSelectAddress,
            navigateToHomeScreenSendClubAuthDocumentScreen = navigateToHomeScreenSendClubAuthDocumentScreen,
            navigateToHome = navigateToHome,
            navigateToTrainingScreen = navigateToTrainingScreen,
            navigateToEditTeamSelectBranchScreen = navigateToEditTeamSelectBranchScreen,
        )
    }
}

fun NavController.navigateToEditTeamScreen(model: HomeScreenEditTeamScreenNavigationModel, navOptions: NavOptions? = null) {
    val route = HomeScreenEditTeamRoute(
        model = model,
    )
    this.navigate(route, navOptions)
}

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
package com.iamkurtgoz.feature.home.createTeam.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenCreateTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.feature.home.createTeam.CreateTeamScreen

fun NavGraphBuilder.createTeamScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToSelectAddress: () -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel, fromGenerateClub: Boolean) -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToHome: () -> Unit,
    navigateToCreateTeamSelectBranchScreen: () -> Unit,
) {
    composable<HomeScreenCreateTeamRoute> {
        CreateTeamScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToSelectAddress = navigateToSelectAddress,
            navigateToHomeScreenSendClubAuthDocumentScreen = navigateToHomeScreenSendClubAuthDocumentScreen,
            navigateToHome = navigateToHome,
            navigateToTrainingScreen = navigateToTrainingScreen,
            navigateToCreateTeamSelectBranchScreen = navigateToCreateTeamSelectBranchScreen,
        )
    }
}

fun NavController.navigateToCreateTeamScreen(fromGenerateClub: Boolean = false, navOptions: NavOptions? = null) {
    val route = HomeScreenCreateTeamRoute(
        fromGenerateClub = fromGenerateClub,
    )
    this.navigate(route, navOptions)
}

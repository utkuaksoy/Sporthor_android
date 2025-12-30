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
package com.iamkurtgoz.feature.home.selectTeam.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenSelectTeamRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.feature.home.selectTeam.SelectTeamScreen

fun NavGraphBuilder.selectTeamScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToCreateTeam: (fromGenerateClub: Boolean) -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel, fromTrainingGroup: Boolean) -> Unit,
    navigateToHomeScreenSendClubAuthDocumentScreen: (HomeScreenSendClubAuthDocumentScreenNavigateModel, fromGenerateClub: Boolean) -> Unit,
) {
    composable<HomeScreenSelectTeamRoute> {
        SelectTeamScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToHome = navigateToHome,
            navigateToCreateTeam = navigateToCreateTeam,
            navigateToTrainingScreen = navigateToTrainingScreen,
            navigateToHomeScreenSendClubAuthDocumentScreen = navigateToHomeScreenSendClubAuthDocumentScreen,
        )
    }
}

fun NavController.navigateToSelectTeamScreen(
    isEdit: Boolean = false,
    fromGenerateClub: Boolean = false,
    fromTrainingGroup: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenSelectTeamRoute(
        isEdit = isEdit,
        fromGenerateClub = fromGenerateClub,
        fromTrainingGroup = fromTrainingGroup,
    )
    this.navigate(route, navOptions)
}

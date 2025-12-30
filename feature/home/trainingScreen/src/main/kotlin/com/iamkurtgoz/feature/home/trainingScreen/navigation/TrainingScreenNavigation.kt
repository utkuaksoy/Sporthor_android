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
package com.iamkurtgoz.feature.home.trainingScreen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenTrainingRoute
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.homeScreenTrainingRouteTypeMap
import com.iamkurtgoz.feature.home.trainingScreen.TrainingScreenScreen

fun NavGraphBuilder.trainingScreenScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSuccessAddTrainingGroupScreen: (HomeScreenSuccessAddTrainingGroupScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
) {
    composable<HomeScreenTrainingRoute>(
        typeMap = homeScreenTrainingRouteTypeMap,
    ) {
        TrainingScreenScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToHome = navigateToHome,
            navigateToSuccessAddTrainingGroupScreen = navigateToSuccessAddTrainingGroupScreen,
        )
    }
}

fun NavController.navigateToTrainingScreenScreen(
    model: HomeScreenTrainingScreenNavigateModel,
    fromTrainingGroup: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenTrainingRoute(
        model = model,
        fromTrainingGroup = fromTrainingGroup,
    )
    this.navigate(route, navOptions)
}

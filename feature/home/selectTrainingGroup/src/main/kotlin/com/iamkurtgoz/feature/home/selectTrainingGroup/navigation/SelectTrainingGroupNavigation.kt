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
package com.iamkurtgoz.feature.home.selectTrainingGroup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenSelectTrainingGroupScreenRoute
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.feature.home.selectTrainingGroup.SelectTrainingGroupScreen

fun NavGraphBuilder.selectTrainingGroupScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToEditTrainingGroupScreen: (HomeScreenEditTrainingGroupScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
) {
    composable<HomeScreenSelectTrainingGroupScreenRoute> {
        SelectTrainingGroupScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToEditTrainingGroupScreen = navigateToEditTrainingGroupScreen,
        )
    }
}

fun NavController.navigateToSelectTrainingGroupScreen(fromTrainingGroup: Boolean = false, navOptions: NavOptions? = null) {
    val route = HomeScreenSelectTrainingGroupScreenRoute(
        fromTrainingGroup = fromTrainingGroup
    )
    this.navigate(route, navOptions)
}

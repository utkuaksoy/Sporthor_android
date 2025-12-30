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
package com.iamkurtgoz.feature.home.successDocumentUploadScreen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenSuccessDocumentUploadRoute
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.homeScreenSuccessDocumentUploadRouteTypeMap
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.feature.home.successDocumentUploadScreen.SuccessDocumentUploadScreenScreen

fun NavGraphBuilder.successDocumentUploadScreenScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<HomeScreenSuccessDocumentUploadRoute>(
        typeMap = homeScreenSuccessDocumentUploadRouteTypeMap,
    ) {
        SuccessDocumentUploadScreenScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToTrainingScreen = navigateToTrainingScreen,
            navigateToHome = navigateToHome,
        )
    }
}

fun NavController.navigateToSuccessDocumentUploadScreenScreen(
    model: HomeScreenSuccessDocumentUploadScreenNavigateModel,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenSuccessDocumentUploadRoute(
        model = model,
    )
    this.navigate(route, navOptions)
}

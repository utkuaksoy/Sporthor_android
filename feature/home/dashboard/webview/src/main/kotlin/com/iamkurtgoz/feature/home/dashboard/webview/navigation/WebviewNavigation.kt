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
package com.iamkurtgoz.feature.home.dashboard.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenDashboardWebviewRoute
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.homeScreenWebViewRouteTypeMap
import com.iamkurtgoz.feature.home.dashboard.webview.WebviewScreen

fun NavGraphBuilder.webviewScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenDashboardWebviewRoute>(
        typeMap = homeScreenWebViewRouteTypeMap,
    ) {
        WebviewScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToWebviewScreen(routeType: HomeScreenWebViewScreenNavigateModel, navOptions: NavOptions? = null) {
    val route = HomeScreenDashboardWebviewRoute(
        routeType = routeType,
    )
    this.navigate(route, navOptions)
}

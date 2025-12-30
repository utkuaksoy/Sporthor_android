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
package com.iamkurtgoz.feature.auth.userInfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.AuthUserInfoScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userInfo.AuthUserInfoScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.auth.userInfo.authUserInfoScreenRouteTypeMap
import com.iamkurtgoz.core.navigation.model.auth.userName.AuthUserNameScreenNavigateModel
import com.iamkurtgoz.feature.auth.userInfo.UserInfoScreen

fun NavGraphBuilder.userInfoScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToUserName: (AuthUserNameScreenNavigateModel) -> Unit,
) {
    composable<AuthUserInfoScreenRoute>(typeMap = authUserInfoScreenRouteTypeMap) {
        UserInfoScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToUserName = navigateToUserName,
        )
    }
}

fun NavController.navigateToUserInfoScreen(model: AuthUserInfoScreenNavigateModel, navOptions: NavOptions? = null) {
    val route = AuthUserInfoScreenRoute(
        model = model,
    )
    this.navigate(route, navOptions)
}

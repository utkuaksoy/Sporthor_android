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
package com.iamkurtgoz.feature.home.successAddTrainingGroup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.homeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.feature.home.successAddTrainingGroup.SuccessAddTrainingGroupScreen

fun NavGraphBuilder.successAddTrainingGroupScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToInviteGroupMembersScreen: (HomeScreenInviteGroupMemberScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
) {
    composable<HomeScreenSuccessAddTrainingGroupRoute>(
        typeMap = homeScreenSuccessAddTrainingGroupRoute,
    ) {
        SuccessAddTrainingGroupScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToInviteGroupMembersScreen = navigateToInviteGroupMembersScreen,
        )
    }
}

fun NavController.navigateToSuccessAddTrainingGroupScreen(
    model: HomeScreenSuccessAddTrainingGroupScreenNavigationModel,
    fromTrainingGroup: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenSuccessAddTrainingGroupRoute(
        model = model,
        fromTrainingGroup = fromTrainingGroup,
    )
    this.navigate(route, navOptions)
}
